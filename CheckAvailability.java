import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CheckAvailability {
    private Connection connection;

    //Σύνδεση με ΒΔ
    public CheckAvailability(String dbPath) throws SQLException{
        connection = DriverManager.getConnection(dbPath);
    }

    //  Μέθοδος για εύρεση διαθέσιμων ωρών
    //* @param stylistName Όνομα του στυλίστα
    //* @param appointmentDate Ημερομηνία για το ραντεβού (μορφή YYYY-MM-DD)
    //* @param serviceType Ο τύπος της υπηρεσίας
    //* @return Λίστα διαθέσιμων ωρών
    //* @throws SQLException Αν υπάρχει πρόβλημα με τη βάση δεδομένων


    public List<String> FindTime(String stylistName, String appointmentDate, String serviceType) throws SQLException {
        // Λήψη των booked time slots 
        List<TimeSlot> bookedSlots = getBookedTimeSlots(stylistName, appointmentDate);

        // Λήψη του shift του στυλίστα
        TimeSlot stylistShift = getStylistShift(stylistName);

        // Λήψη διάρκειας υπηρεσίας
        LocalTime serviceDuration = getServiceDuration(serviceType);

        // Υπολογισμός διαθέσιμων slots
        List<String> optimalSlots = calculateOptimalSlots(stylistShift, bookedSlots, serviceDuration);

        return optimalSlots;
    }

    //Μέθοδος για εύρεση του shift του στυλίστα
    private TimeSlot getStylistShift(String stylistName) throws SQLException {
        String shiftQuery = "SELECT shift_start, shift_end FROM Stylists WHERE stylist_name = ?";
        PreparedStatement shiftStmt = connection.prepareStatement(shiftQuery);
        shiftStmt.setString(1, stylistName);

        ResultSet shiftResult = shiftStmt.executeQuery();
        if (!shiftResult.next()) {
            throw new SQLException("Stylist not found");
        }

        LocalTime shiftStart = LocalTime.parse(shiftResult.getString("shift_start"));
        LocalTime shiftEnd = LocalTime.parse(shiftResult.getString("shift_end"));

        return new TimeSlot(shiftStart, shiftEnd);
    }


//Μέθοδος για εύρεση διάρκειας υπηρεσίας
    private LocalTime getServiceDuration(String serviceType) throws SQLException {
        String serviceQuery = "SELECT duration FROM Services WHERE service_type = ?";
        try (PreparedStatement serviceStmt = connection.prepareStatement(serviceQuery)) {
            serviceStmt.setString(1, serviceType);
    
            ResultSet serviceResult = serviceStmt.executeQuery();
            if (!serviceResult.next()) {
                throw new SQLException("Service type not found");
            }
    
            return serviceResult.getTime("duration").toLocalTime();
        }

    }

    //λήψη των booked time slots
     
    private List<TimeSlot> getBookedTimeSlots(String stylistName, String appointmentDate) throws SQLException {
        String appointmentsQuery = "SELECT time_start, time_end FROM AvailabilitybyStylist " +
                                   "WHERE stylist_id = (SELECT stylist_id FROM Stylists WHERE stylist_name = ?) " +
                                   "AND appoint_date = ?";
        PreparedStatement appointmentsStmt = connection.prepareStatement(appointmentsQuery);
        appointmentsStmt.setString(1, stylistName);
        appointmentsStmt.setString(2, appointmentDate);

        ResultSet appointmentsResult = appointmentsStmt.executeQuery();

        List<TimeSlot> bookedSlots = new ArrayList<>();
        while (appointmentsResult.next()) {
            LocalTime bookedStart = LocalTime.parse(appointmentsResult.getString("time_start"));
            LocalTime bookedEnd = LocalTime.parse(appointmentsResult.getString("time_end"));
            bookedSlots.add(new TimeSlot(bookedStart, bookedEnd));
        }

        return bookedSlots;
    }

    //Μέθοδος για υπολογισμό διαθέσιμων slots
     
    private List<String> calculateOptimalSlots(TimeSlot shift, List<TimeSlot> bookedSlots, LocalTime serviceDuration) {
        List<String> optimalSlots = new ArrayList<>();
        LocalTime currentTime = shift.getStart();

        int serviceDurationMinutes = serviceDuration.toSecondOfDay() / 60;

        // Βοηθητική μέθοδος backtracking
        backtrack(currentTime, shift.getEnd(), serviceDurationMinutes, bookedSlots, new ArrayList<>(), optimalSlots);

        return optimalSlots;
    }

    private void backtrack(LocalTime currentTime, LocalTime shiftEnd, int serviceDuration, List<TimeSlot> bookedSlots,
                           List<LocalTime> currentCombination, List<String> optimalSlots) {
        // Αν έχουμε περάσει το τέλος της βάρδιας, σταματάμε
        if (currentTime.plusMinutes(serviceDuration).isAfter(shiftEnd)) {
            // Υπολογισμός της "ποιότητας" του συνδυασμού
            if (currentCombination.size() > optimalSlots.size()) {
                optimalSlots.clear();
                for (LocalTime time : currentCombination) {
                    optimalSlots.add(time.toString());
                }
            }
            return;
        }

        // Δημιουργία υποψήφιου slot
        TimeSlot candidateSlot = new TimeSlot(currentTime, currentTime.plusMinutes(serviceDuration));
        boolean isAvailable = true;

        for (TimeSlot bookedSlot : bookedSlots) {
            if (candidateSlot.overlaps(bookedSlot)) {
                isAvailable = false;
                break;
            }
        }

        // Αν το slot είναι διαθέσιμο, προσθέτουμε στη λίστα
        if (isAvailable) {
            currentCombination.add(currentTime);

            // Προχωράμε στο επόμενο slot
            backtrack(currentTime.plusMinutes(serviceDuration), shiftEnd, serviceDuration, bookedSlots, currentCombination, optimalSlots);

            // Backtrack: αφαιρούμε το τελευταίο slot
            currentCombination.remove(currentCombination.size() - 1);
        }

        // Προχωράμε στο επόμενο slot ανεξάρτητα από τη διαθεσιμότητα
        backtrack(currentTime.plusMinutes(serviceDuration), shiftEnd, serviceDuration, bookedSlots, currentCombination, optimalSlots);
    }  

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private static class TimeSlot {
        private LocalTime start;
        private LocalTime end;

        public TimeSlot(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalTime getStart() {
            return start;
        }
    
        public LocalTime getEnd() {
            return end;
        }

        public boolean overlaps(TimeSlot other) {
            return !(end.isBefore(other.start) || start.isAfter(other.end));
        }
    }
}
    
