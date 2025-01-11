import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentScheduler {

    private Connection connection;

    // Constructor to initialize database connection
    public AppointmentScheduler(String db) throws SQLException {
        connection = DriverManager.getConnection(db);
    }

    // Method to book an appointment
    public boolean bookAppointment(int userId, int salonId, int stylistId, int serviceId, 
                                   LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        String query = "INSERT INTO Appointments (user_id, salon_id, stylist_id, service_id, date, time_start, time_end) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get stylists by salon ID
    private List<String> getStylistsBySalonId(int salonId) throws SQLException {
        List<String> stylistNames = new ArrayList<>();
        String query = "SELECT stylist_name FROM stylists WHERE salon_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, salonId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stylistNames.add(resultSet.getString("stylist_name"));
            }
        }
        return stylistNames;
    }

    // Method to get available services
    private List<String> getServices() throws SQLException {
        List<String> serviceTypes = new ArrayList<>();
        String query = "SELECT service_type FROM services";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                serviceTypes.add(resultSet.getString("service_type"));
            }
        }
        return serviceTypes;
    }

    // Method to check if a user is signed in and retrieve their user ID
    private int getSignedInUserId() {
        SignIn signIn = new SignIn(); // Assuming SignIn handles the authentication session
        if (signIn.isUserSignedIn()) {
            return signIn.getUserId();
        } else {
            throw new IllegalStateException("User is not signed in. Please log in to proceed.");
        }
    }

   
    public void runScheduler() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get user ID from sign-in
            int userId = getSignedInUserId();
    
            // Prompt for salon selection
            System.out.println("Enter the salon ID you wish to choose:");
            int salonId = scanner.nextInt();
    
            // Fetch and display stylists
            List<String> stylists = getStylistsBySalonId(salonId);
            System.out.println("Stylists for salon ID " + salonId + ":");
            for (int i = 0; i < stylists.size(); i++) {
                System.out.println((i + 1) + ". " + stylists.get(i));
            }
    
            // Prompt for stylist selection
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
            CheckAvailability checkAvailability = new CheckAvailability("jdbc:sqlite:your-database-path.db");
            List<String> availableTimeSlots = checkAvailability.FindTime(stylistName, appointmentDate, serviceType);
    
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
    
            // Get stylist ID (assuming stylist IDs are aligned with list order)
            int stylistId = stylistChoice;
    
            // Get service ID (assuming service IDs are aligned with list order)
            int serviceId = serviceChoice;
    
            // Book the appointment
            boolean success = bookAppointment(userId, salonId, stylistId, serviceId, LocalDate.parse(appointmentDate), timeStart, timeEnd);
            if (success) {
                System.out.println("Appointment successfully booked!");
            } else {
                System.out.println("Failed to book the appointment.");
            }
    
            // Close CheckAvailability connection
            checkAvailability.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
