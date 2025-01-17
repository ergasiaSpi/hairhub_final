package com.hairhub.BookAnAppointment;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CheckAvailability {
    private Connection connection;

   
    public CheckAvailability(String dbPath) throws SQLException{
        connection = DriverManager.getConnection(dbPath);
    }

  

    public List<String> FindTime(int stylistid, String appointmentDate, int serviceid) throws SQLException {
        
        List<TimeSlot> bookedSlots = getBookedTimeSlots(stylistid, appointmentDate);

      
        TimeSlot stylistShift = getStylistShift(stylistid);

    
        LocalTime serviceDuration = getServiceDuration(serviceid);

      
        List<String> optimalSlots = calculateOptimalSlots(stylistShift, bookedSlots, serviceDuration);

        return optimalSlots;
    }


    private TimeSlot getStylistShift(int stylistid) throws SQLException {
        String shiftQuery = "SELECT shift_start, shift_end FROM Stylists WHERE stylist_id = ?";
        PreparedStatement shiftStmt = connection.prepareStatement(shiftQuery);
        shiftStmt.setInt(1, stylistid);

        ResultSet shiftResult = shiftStmt.executeQuery();
        if (!shiftResult.next()) {
            throw new SQLException("Stylist not found");
        }

        LocalTime shiftStart = LocalTime.parse(shiftResult.getString("shift_start"));
        LocalTime shiftEnd = LocalTime.parse(shiftResult.getString("shift_end"));

        return new TimeSlot(shiftStart, shiftEnd);
    }



    private LocalTime getServiceDuration(int serviceid) throws SQLException {
    String serviceQuery = "SELECT duration FROM Services WHERE service_id = ?";
    try (PreparedStatement serviceStmt = connection.prepareStatement(serviceQuery)) {
        serviceStmt.setInt(1, serviceid);

        ResultSet serviceResult = serviceStmt.executeQuery();
        if (!serviceResult.next()) {
            throw new SQLException("Service type not found");
        }

        
        String durationString = serviceResult.getString("duration");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        
        if (durationString.length() == 5) { 
            durationString += ":00"; 
        }

        return LocalTime.parse(durationString, formatter);  
    }
}


    
     
    private List<TimeSlot> getBookedTimeSlots(int stylistid, String appointmentDate) throws SQLException {
        String appointmentsQuery = "SELECT time_start, time_end FROM AvailabilitybyStylist " +
                                   "WHERE stylist_id = ? " +
                                   "AND appoint_date = ?";
        PreparedStatement appointmentsStmt = connection.prepareStatement(appointmentsQuery);
        appointmentsStmt.setInt(1, stylistid);
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
    

  
     
    private List<String> calculateOptimalSlots(TimeSlot shift, List<TimeSlot> bookedSlots, LocalTime serviceDuration) {
        List<String> optimalSlots = new ArrayList<>();
        LocalTime currentTime = shift.getStart();

        int serviceDurationMinutes = serviceDuration.toSecondOfDay() / 60;

       
        backtrack(currentTime, shift.getEnd(), serviceDurationMinutes, bookedSlots, new ArrayList<>(), optimalSlots);

        return optimalSlots;
    }

    private void backtrack(LocalTime currentTime, LocalTime shiftEnd, int serviceDuration, List<TimeSlot> bookedSlots,
                           List<LocalTime> currentCombination, List<String> optimalSlots) {
       
        if (currentTime.plusMinutes(serviceDuration).isAfter(shiftEnd)) {
          
            if (currentCombination.size() > optimalSlots.size()) {
                optimalSlots.clear();
                for (LocalTime time : currentCombination) {
                    optimalSlots.add(time.toString());
                }
            }
            return;
        }

       
        TimeSlot candidateSlot = new TimeSlot(currentTime, currentTime.plusMinutes(serviceDuration));
        boolean isAvailable = true;

        for (TimeSlot bookedSlot : bookedSlots) {
            if (candidateSlot.overlaps(bookedSlot)) {
                isAvailable = false;
                break;
            }
        }

     
        if (isAvailable) {
            currentCombination.add(currentTime);

          
            backtrack(currentTime.plusMinutes(serviceDuration), shiftEnd, serviceDuration, bookedSlots, currentCombination, optimalSlots);

       
            currentCombination.remove(currentCombination.size() - 1);
        }

      
        backtrack(currentTime.plusMinutes(serviceDuration), shiftEnd, serviceDuration, bookedSlots, currentCombination, optimalSlots);
    }

    

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
   