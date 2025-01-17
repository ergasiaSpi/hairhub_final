package com.hairhub.BookAnAppointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import com.hairhub.sign_in_up.UserSessionManager;

public class AppointmentScheduler {

    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:your-database-path.db"; // Update your database path

    
    public AppointmentScheduler() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);
    }

   
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

   
    private int getSignedInUserId() {
    if (UserSessionManager.isUserSignedIn()) {
        return UserSessionManager.getSignedInUserId();
    } else {
        throw new IllegalStateException("User is not signed in. Please log in to proceed.");
    }

}

   
    private double calculateDistance(String userZipcode, String salonZipcode) throws SQLException {
        String query = "SELECT latitude, longitude FROM Location WHERE zipcode = ?";
        double userLat = 0.0, userLon = 0.0, salonLat = 0.0, salonLon = 0.0;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, userZipcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userLat = rs.getDouble("latitude");
                userLon = rs.getDouble("longitude");
            }

            
            stmt.setString(1, salonZipcode);
            rs = stmt.executeQuery();
            if (rs.next()) {
                salonLat = rs.getDouble("latitude");
                salonLon = rs.getDouble("longitude");
            }
        }

        
        final double R = 6371; 
        double dLat = Math.toRadians(salonLat - userLat);
        double dLon = Math.toRadians(salonLon - userLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(salonLat)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; 
    }
        
        
    public static int chooseSalon() {
        Scanner scanner = new Scanner(System.in);
        int salonId = -1;
        boolean validSalonId = false;

        
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:hairhub.db")) {

            
            while (!validSalonId) {
                System.out.print("Please enter a salon ID: ");
                String input = scanner.nextLine();
                
                try {
                    
                    salonId = Integer.parseInt(input);
                    
                   
                    if (isValidSalonId(connection, salonId)) {
                        validSalonId = true;  
                    } else {
                        System.out.println("Invalid salon ID! No salon found with ID " + salonId);
                    }
                    
                } catch (NumberFormatException e) {
                    
                    System.out.println("Invalid input! Please enter a valid integer.");
                }
            }


        return salonId;

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
        return -1;  
    }
    } 
    
        
    public static boolean isValidSalonId(Connection connection, int salonId) {
        String query = "SELECT COUNT(*) FROM Salons WHERE salon_id = ?";  

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, salonId);  
            ResultSet resultSet = stmt.executeQuery(); 

           
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error while checking salon ID: " + e.getMessage());
            return false;
        }
    }



    public static int chooseStylist() {
        Scanner scanner = new Scanner(System.in);
        int stylistId = -1;
        boolean validStylistId = false;

       
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:hairhub.db")) {

            
            while (!validStylistId) {
                System.out.print("Please enter a stylist ID: ");
                String input = scanner.nextLine();
                
                try {
                   
                    stylistId = Integer.parseInt(input);
                    
                    
                    if (isValidStylistId(connection, stylistId)) {
                        validStylistId = true;  
                    } else {
                        System.out.println("Invalid stylist ID! No stylist found with ID " + stylistId);
                    }
                    
                } catch (NumberFormatException e) {
                   
                    System.out.println("Invalid input! Please enter a valid integer.");
                }
            }

            
            return stylistId;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return -1;  
        }
    }

   
    public static boolean isValidStylistId(Connection connection, int stylistId) {
        String query = "SELECT COUNT(*) FROM Stylists WHERE stylist_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stylistId);
            ResultSet resultSet = stmt.executeQuery();

            
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error while checking stylist ID: " + e.getMessage());
            return false;
        }
    }

    public static int chooseService(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int chosenServiceId = -1;

        while (chosenServiceId == -1) {
            System.out.print("Please enter the Service ID to choose a service: ");
            if (scanner.hasNextInt()) {
                chosenServiceId = scanner.nextInt();

                
                if (serviceExists(connection, chosenServiceId)) {
                    System.out.println("Service " + chosenServiceId + " chosen successfully.");
                } else {
                    System.out.println("Invalid Service ID. Please try again.");
                    chosenServiceId = -1;  
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer Service ID.");
                scanner.next(); 
            }
        }
        return chosenServiceId;  
    }

  
    private static boolean serviceExists(Connection connection, int serviceId) {
        String query = "SELECT COUNT(*) FROM Services WHERE service_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.getInt(1) > 0;  
        } catch (SQLException e) {
            System.out.println("Error checking service existence: " + e.getMessage());
            return false;
        }
    }

    public static LocalDate ChooseDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate validDate = null;
        boolean valid = false;

        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2025, 7, 24);

        while (!valid) {
            System.out.print("Please enter a date (dd/MM/yyyy): ");
            String inputDate = scanner.nextLine();
            
           
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                validDate = LocalDate.parse(inputDate, formatter);
                
                
                if (!validDate.isBefore(startDate) && !validDate.isAfter(endDate)) {
                    valid = true;  
                } else {
                    System.out.println("The date must be between today (" + startDate + ") and 24/07/2025. Please try again.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
        return validDate;
    }

    public static TimeSlot ChooseTime(int stylist_id, String appoint_date, int serviceid) {
        Scanner scanner = new Scanner(System.in);
        
        
        try {
            
            CheckAvailability checkAvailability = new CheckAvailability(DATABASE_URL);
            List<String> availableTimeSlots = checkAvailability.FindTime(stylist_id, appoint_date, serviceid);
            
            
            if (availableTimeSlots.isEmpty()) {
                System.out.println("No available time slots for the selected stylist, date, and service.");
                return null;  // Επιστροφή null αν δεν υπάρχουν διαθέσιμες ώρες
            }
    
            
            System.out.println("Available time slots:");
            for (int i = 0; i < availableTimeSlots.size(); i++) {
                System.out.println((i + 1) + ". " + availableTimeSlots.get(i));
            }
    
           
            System.out.println("Choose a time slot by number:");
            int timeSlotChoice = -1;
            
           
            while (timeSlotChoice < 1 || timeSlotChoice > availableTimeSlots.size()) {
                if (scanner.hasNextInt()) {
                    timeSlotChoice = scanner.nextInt();
                    if (timeSlotChoice < 1 || timeSlotChoice > availableTimeSlots.size()) {
                        System.out.println("Invalid choice. Please select a valid time slot number.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); 
                }
            }
    
           
            String TimeStartstr = availableTimeSlots.get(timeSlotChoice - 1);
            LocalTime TimeStart = LocalTime.parse(TimeStartstr);
            String TimeEndstr = availableTimeSlots.get(timeSlotChoice);
            LocalTime TimeEnd = LocalTime.parse(TimeEndstr);

            return new TimeSlot(TimeStart, TimeEnd);

            


           
    
        } catch (SQLException e) {
            
            System.out.println("Database error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            
            System.out.println("Invalid time format: " + e.getMessage());
        } catch (Exception e) {
            
            System.out.println("An error occurred: " + e.getMessage());
        }
    
        return null; 
    }
    
    public static void isValidAppointment(boolean successfulBooking, int user_id, int stylist_id, int salon_id, int service_id, Connection connection) {
        String query = "SELECT A.date, A.time_start, S.name AS salon_name, SR.service, ST.stylist_name " +
                       "FROM Appointments A " +
                       "JOIN Salons S ON A.salon_id = S.salon_id " +
                       "JOIN Services SR ON A.service_id = SR.service_id " +
                       "JOIN Stylists ST ON A.stylist_id = ST.stylist_id " +
                       "WHERE A.user_id = ? AND S.salon_id = ? AND ST.stylist_id = ? AND SR.service_id = ?" +
                       "ORDER BY A.date DESC, A.time_start DESC LIMIT 1";
    
        if (successfulBooking) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                
                stmt.setInt(1, user_id);
                stmt.setInt(2, salon_id);
                stmt.setInt(3, stylist_id);
                stmt.setInt(4, service_id);
    
                try (ResultSet resultSet = stmt.executeQuery()) {
                    
                    if (resultSet.next()) {
                       
                        System.out.println("\nAppointment details:");                   
                        System.out.printf("Date: %s%n", resultSet.getString("date"));
                        System.out.printf("Time: %s%n", resultSet.getString("time_start"));
                        System.out.printf("Salon: %s%n", resultSet.getString("salon_name"));
                        System.out.printf("Service: %s%n", resultSet.getString("service"));
                        System.out.printf("Stylist: %s%n", resultSet.getString("stylist_name"));
                    } else {
                        System.out.println("No appointment found for the given user.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error fetching latest appointment: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to book the appointment");
        }
    }
    
    public static LocalTime Get_ServiceDuration (Connection connection, int service_id) throws SQLException {
        
        String query = "SELECT duration FROM Services WHERE service_id = ?";
        
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, service_id);
    
           
            ResultSet rs = pstmt.executeQuery();
    
           
            if (rs.next()) {
                return rs.getTime("duration").toLocalTime();
            } else {
               
                throw new SQLException("Service duration not found with the given ID");
            }
        }
    }
    public static LocalTime parseTimeWithSeconds(String timeString) {
        
        if (timeString.length() == 5) { 
            timeString += ":00"; 
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timeString, formatter);
    }

}  
}
    
   