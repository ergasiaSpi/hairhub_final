package com.hairhub.BookAnAppointment.appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.hairhub.BookAnAppointment.location.ZipcodeSessionManager;
import com.hairhub.sign_in_up.UserSessionManager;

public class AppointmentScheduler {

    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:your-database-path.db"; // Update your database path

    // Constructor to initialize the database connection
    public AppointmentScheduler() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);
    }

    // Method to book an appointment
      public boolean bookAppointment(int userId, int salonId, int stylistId, int serviceId, 
                                   LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        String query = "INSERT INTO Appointments (appointment_id, user_id, salon_id, stylist_id, service_id, date, time_start, time_end) " +
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

    // Method to get available services
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

    // Method to get the user's ID (assuming the user is already signed in)
    private int getSignedInUserId() {
    if (UserSessionManager.isUserSignedIn()) {
        return UserSessionManager.getSignedInUserId();
    } else {
        throw new IllegalStateException("User is not signed in. Please log in to proceed.");
    }

}

    // Method to calculate the distance between the user's location and the salon location
    private double calculateDistance(String userZipcode, String salonZipcode) throws SQLException {
        String query = "SELECT latitude, longitude FROM Location WHERE zipcode = ?";
        double userLat = 0.0, userLon = 0.0, salonLat = 0.0, salonLon = 0.0;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Get the user's latitude and longitude
            stmt.setString(1, userZipcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userLat = rs.getDouble("latitude");
                userLon = rs.getDouble("longitude");
            }

            // Get the salon's latitude and longitude
            stmt.setString(1, salonZipcode);
            rs = stmt.executeQuery();
            if (rs.next()) {
                salonLat = rs.getDouble("latitude");
                salonLon = rs.getDouble("longitude");
            }
        }

        // Haversine formula to calculate the distance
        final double R = 6371; // Radius of the Earth in km
        double dLat = Math.toRadians(salonLat - userLat);
        double dLon = Math.toRadians(salonLon - userLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(salonLat)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // returns distance in km
    }

    // Method to run the appointment scheduler
    public void runScheduler() {
        try (Scanner scanner = new Scanner(System.in)) {
            int userId = getSignedInUserId();

            // Get salon selection
            System.out.println("Enter the salon ID you wish to choose:");
            int salonId = scanner.nextInt();

            // Get salon zipcode from database
            String salonZipcode = "";
            String query = "SELECT zipcode FROM Salons WHERE salon_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, salonId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    salonZipcode = rs.getString("zipcode");
                }
            }

            String userZipcode = ZipcodeSessionManager.getUserZipcode();       

            double distance = calculateDistance(userZipcode, salonZipcode);
            System.out.println("Distance from your location to the salon: " + distance + " km");

            // Check if salon is within a desired range (e.g., 10 km)
            if (distance > 10) {
                System.out.println("The salon is too far away from your location. Please choose a closer salon.");
                return;
            }

            // Fetch stylists by salon ID
            List<String> stylists = getStylistsBySalonId(salonId);
            System.out.println("Stylists for salon ID " + salonId + ":");
            for (int i = 0; i < stylists.size(); i++) {
                System.out.println((i + 1) + ". " + stylists.get(i));
            }

            System.out.println("Choose a stylist by number:");
            int stylistChoice = scanner.nextInt();
            String stylistName = stylists.get(stylistChoice - 1);

            // Fetch available services
            List<String> services = getServices();
            System.out.println("Available services:");
            for (int i = 0; i < services.size(); i++) {
                System.out.println((i + 1) + ". " + services.get(i));
            }

            System.out.println("Choose a service by number:");
            int serviceChoice = scanner.nextInt();
            String serviceType = services.get(serviceChoice - 1);

            // Get appointment date
            System.out.println("Enter appointment date (YYYY-MM-DD):");
            String appointmentDate = scanner.next();

            // Use CheckAvailability to find available time slots
            CheckAvailability checkAvailability = new CheckAvailability(DATABASE_URL);
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

            // Select time slot
            System.out.println("Choose a time slot by number:");
            int timeSlotChoice = scanner.nextInt();
            String chosenTimeSlot = availableTimeSlots.get(timeSlotChoice - 1);
            String[] timeParts = chosenTimeSlot.split("-");
            LocalTime timeStart = LocalTime.parse(timeParts[0]);
            LocalTime timeEnd = LocalTime.parse(timeParts[1]);

            // ΝΕΦΕΛΗ ΔΕΣ ΤΙ ΠΑΙΖΕΙ ΕΔΩ 
            boolean success = bookAppointment( userId, salonId, stylistChoice, serviceChoice, 
                                              LocalDate.parse(appointmentDate), timeStart, timeEnd);
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
