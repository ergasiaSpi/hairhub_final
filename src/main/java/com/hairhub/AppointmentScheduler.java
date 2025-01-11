import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AppointmentScheduler {

    private Connection connection;
    private DistanceCalculator distanceCalculator;
    private CheckAvailability checkAvailability;

    // Constructor to initialize the database connection and dependencies
    public AppointmentScheduler(String db) throws SQLException {
        connection = DriverManager.getConnection(db);
        this.distanceCalculator = new DistanceCalculator(connection); // Initialize DistanceCalculator
        this.checkAvailability = new CheckAvailability(connection); // Initialize CheckAvailability
    }

    // Method to book an appointment
    public boolean bookAppointment(int userId, int salonId, int stylistId, int serviceId,
                                   LocalDate date, LocalTime timeStart, LocalTime timeEnd) throws SQLException {
        // Check if the service is available at the salon
        if (!checkAvailability.isServiceAvailable(salonId, getServiceType(serviceId))) {
            System.out.println("Service is not available at this salon.");
            return false;
        }

        // Check if the stylist is available at the selected time
        if (!checkAvailability.isStylistAvailable(stylistId, date, timeStart, timeEnd)) {
            System.out.println("Stylist is not available at this time.");
            return false;
        }

        // Proceed with booking if available
        String query = "INSERT INTO Appointments (user_id, salon_id, stylist_id, service_id, date, time_start, time_end, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, salonId);
            stmt.setInt(3, stylistId);
            stmt.setInt(4, serviceId);
            stmt.setDate(5, java.sql.Date.valueOf(date));
            stmt.setTime(6, java.sql.Time.valueOf(timeStart));
            stmt.setTime(7, java.sql.Time.valueOf(timeEnd));
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // Method to get the service type by service ID
    private String getServiceType(int serviceId) throws SQLException {
        String query = "SELECT service_type FROM Services WHERE service_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("service_type");
            }
        }
        return null;
    }

    // Method to get stylist names for a salon ID
    private List<String> getStylistsBySalonId(int salonId) throws SQLException {
        List<String> stylistNames = new ArrayList<>();
        String query = "SELECT stylist_name FROM Stylists WHERE salon_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, salonId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stylistNames.add(resultSet.getString("stylist_name"));
            }
        }
        return stylistNames;
    }

    // Method to get available services from the salon
    private List<String> getServices() throws SQLException {
        List<String> serviceTypes = new ArrayList<>();
        String query = "SELECT service_type FROM Services";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                serviceTypes.add(resultSet.getString("service_type"));
            }
        }
        return serviceTypes;
    }

    // Method to get user ID (assuming a method to check the current signed-in user)
    private int getSignedInUserId() {
        // Assuming the SignIn class manages authentication
        SignIn signIn = new SignIn();
        if (signIn.isUserSignedIn()) {
            return signIn.getUserId();
        } else {
            throw new IllegalStateException("User is not signed in. Please log in to proceed.");
        }
    }

    // Method to run the appointment scheduler interface
    public void runScheduler() {
        try (Scanner scanner = new Scanner(System.in)) {
            int userId = getSignedInUserId();

            // Prompt for salon and stylist selection
            System.out.println("Enter the salon ID you wish to choose:");
            int salonId = scanner.nextInt();

            List<String> stylists = getStylistsBySalonId(salonId);
            System.out.println("Stylists for salon ID " + salonId + ":");
            for (int i = 0; i < stylists.size(); i++) {
                System.out.println((i + 1) + ". " + stylists.get(i));
            }

            System.out.println("Choose a stylist by number:");
            int stylistChoice = scanner.nextInt();
            if (stylistChoice <= 0 || stylistChoice > stylists.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            String stylistName = stylists.get(stylistChoice - 1);

            // Prompt for service selection
            List<String> services = getServices();
            System.out.println("Available services:");
            for (int i = 0; i < services.size(); i++) {
                System.out.println((i + 1) + ". " + services.get(i));
            }

            System.out.println("Choose a service by number:");
            int serviceChoice = scanner.nextInt();
            if (serviceChoice <= 0 || serviceChoice > services.size()) {
                System.out.println("Invalid service choice.");
                return;
            }
            String serviceType = services.get(serviceChoice - 1);

            // Prompt for appointment date
            System.out.println("Enter appointment date (YYYY-MM-DD):");
            String appointmentDate = scanner.next();

            // Check available time slots using CheckAvailability
            List<String> availableTimeSlots = checkAvailability.findAvailableTimeSlots(stylistChoice, LocalDate.parse(appointmentDate), serviceType);

            if (availableTimeSlots.isEmpty()) {
                System.out.println("No available time slots for the selected stylist, date, and service.");
                return;
            }

            // Display available time slots
            System.out.println("Available time slots:");
            for (int i = 0; i < availableTimeSlots.size(); i++) {
                System.out.println((i + 1) + ". " + availableTimeSlots.get(i));
            }

            // Prompt for time slot selection
            System.out.println("Choose a time slot by number:");
            int timeSlotChoice = scanner.nextInt();
            if (timeSlotChoice <= 0 || timeSlotChoice > availableTimeSlots.size()) {
                System.out.println("Invalid time slot choice.");
                return;
            }

            String chosenTimeSlot = availableTimeSlots.get(timeSlotChoice - 1);
            String[] timeParts = chosenTimeSlot.split("-");
            LocalTime timeStart = LocalTime.parse(timeParts[0]);
            LocalTime timeEnd = LocalTime.parse(timeParts[1]);

            // Book the appointment
            boolean success = bookAppointment(userId, salonId, stylistChoice, serviceChoice, LocalDate.parse(appointmentDate), timeStart, timeEnd);
            if (success) {
                System.out.println("Appointment successfully booked!");
            } else {
                System.out.println("Failed to book the appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
