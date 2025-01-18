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

        // Σύνδεση με τη βάση δεδομένων
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:hairhub.db")) {

            // Επαναλαμβάνουμε την εισαγωγή μέχρι να είναι έγκυρο το salonId
            while (!validSalonId) {
                System.out.print("Please enter a salon ID: ");
                String input = scanner.nextLine();
                
                try {
                    // Προσπαθούμε να μετατρέψουμε την είσοδο σε ακέραιο
                    salonId = Integer.parseInt(input);
                    
                    // Ελέγχουμε αν το salon_id υπάρχει στον πίνακα Salons
                    if (isValidSalonId(connection, salonId)) {
                        validSalonId = true;  // Αν είναι έγκυρο, βγαίνουμε από τον βρόχο
                    } else {
                        System.out.println("Invalid salon ID! No salon found with ID " + salonId);
                    }
                    
                } catch (NumberFormatException e) {
                    // Αν η είσοδος δεν είναι έγκυρη αριθμητικά
                    System.out.println("Invalid input! Please enter a valid integer.");
                }
            }

        // Αν το ID είναι έγκυρο, επιστρέφουμε το ID του salon
        return salonId;

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
        return -1;  // Επιστρέφει -1 σε περίπτωση σφάλματος στη βάση δεδομένων
    }
    } 
    
        // Μέθοδος για να ελέγξουμε αν το salon_id υπάρχει στον πίνακα Salons
    public static boolean isValidSalonId(Connection connection, int salonId) {
        String query = "SELECT COUNT(*) FROM Salons WHERE salon_id = ?";  // SQL query για να μετρήσουμε τις εγγραφές

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, salonId);  // Ορίζουμε το πραγματικό salon_id που εισήγαγε ο χρήστης
            ResultSet resultSet = stmt.executeQuery();  // Εκτελούμε το query

            // Αν το COUNT(*) είναι μεγαλύτερο από 0, τότε υπάρχει salon με αυτό το ID
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

        // Σύνδεση με τη βάση δεδομένων
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:hairhub.db")) {

            // Επαναλαμβάνουμε την εισαγωγή μέχρι να είναι έγκυρο το stylistId
            while (!validStylistId) {
                System.out.print("Please enter a stylist ID: ");
                String input = scanner.nextLine();
                
                try {
                    // Προσπαθούμε να μετατρέψουμε την είσοδο σε ακέραιο
                    stylistId = Integer.parseInt(input);
                    
                    // Ελέγχουμε αν το stylist_id υπάρχει στον πίνακα Stylists
                    if (isValidStylistId(connection, stylistId)) {
                        validStylistId = true;  // Αν είναι έγκυρο, βγαίνουμε από τον βρόχο
                    } else {
                        System.out.println("Invalid stylist ID! No stylist found with ID " + stylistId);
                    }
                    
                } catch (NumberFormatException e) {
                    // Αν η είσοδος δεν είναι έγκυρη αριθμητικά
                    System.out.println("Invalid input! Please enter a valid integer.");
                }
            }

            // Αν το ID είναι έγκυρο, επιστρέφουμε το ID του stylist
            return stylistId;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return -1;  // Επιστρέφει -1 σε περίπτωση σφάλματος στη βάση δεδομένων
        }
    }

    // Μέθοδος για να ελέγξουμε αν το stylist_id υπάρχει στον πίνακα Stylists
    public static boolean isValidStylistId(Connection connection, int stylistId) {
        String query = "SELECT COUNT(*) FROM Stylists WHERE stylist_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stylistId);
            ResultSet resultSet = stmt.executeQuery();

            // Αν το COUNT(*) είναι μεγαλύτερο από 0, τότε υπάρχει stylist με αυτό το ID
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

                // Ελέγχουμε αν η υπηρεσία με το επιλεγμένο ID υπάρχει
                if (serviceExists(connection, chosenServiceId)) {
                    System.out.println("Service " + chosenServiceId + " chosen successfully.");
                } else {
                    System.out.println("Invalid Service ID. Please try again.");
                    chosenServiceId = -1;  // Επαναφορά για νέα επιλογή
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer Service ID.");
                scanner.next();  // Καθαρίζουμε την είσοδο του scanner
            }
        }
        return chosenServiceId;  // Επιστρέφουμε το ID της επιλεγμένης υπηρεσίας
    }

    // Μέθοδος για να ελέγξουμε αν η υπηρεσία υπάρχει στη βάση δεδομένων
    private static boolean serviceExists(Connection connection, int serviceId) {
        String query = "SELECT COUNT(*) FROM Services WHERE service_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.getInt(1) > 0;  // Επιστρέφουμε true αν υπάρχει τουλάχιστον μια υπηρεσία με αυτό το ID
        } catch (SQLException e) {
            System.out.println("Error checking service existence: " + e.getMessage());
            return false;
        }
    }

    public static LocalDate ChooseDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate validDate = null;
        boolean valid = false;

        // Ημερομηνία περιοχής από την τρέχουσα ημερομηνία μέχρι 24/7/2025
        LocalDate startDate = LocalDate.now();  // Η τρέχουσα ημερομηνία
        LocalDate endDate = LocalDate.of(2025, 7, 24);

        while (!valid) {
            System.out.print("Please enter a date (dd/MM/yyyy): ");
            String inputDate = scanner.nextLine();
            
            // Προσπαθούμε να κάνουμε parse την ημερομηνία
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                validDate = LocalDate.parse(inputDate, formatter);
                
                // Ελέγχουμε αν η ημερομηνία είναι εντός του επιτρεπόμενου εύρους
                if (!validDate.isBefore(startDate) && !validDate.isAfter(endDate)) {
                    valid = true;  // Η ημερομηνία είναι έγκυρη
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
            
            // Αν δεν υπάρχουν διαθέσιμες ώρες
            if (availableTimeSlots.isEmpty()) {
                System.out.println("No available time slots for the selected stylist, date, and service.");
                return null;  // Επιστροφή null αν δεν υπάρχουν διαθέσιμες ώρες
            }
    
            // Εμφάνιση διαθέσιμων ωρών
            System.out.println("Available time slots:");
            for (int i = 0; i < availableTimeSlots.size(); i++) {
                System.out.println((i + 1) + ". " + availableTimeSlots.get(i));
            }
    
            // Επιλογή ώρας από τον χρήστη
            System.out.println("Choose a time slot by number:");
            int timeSlotChoice = -1;
            
            // Έλεγχος εγκυρότητας εισόδου για την επιλογή του χρήστη
            while (timeSlotChoice < 1 || timeSlotChoice > availableTimeSlots.size()) {
                if (scanner.hasNextInt()) {
                    timeSlotChoice = scanner.nextInt();
                    if (timeSlotChoice < 1 || timeSlotChoice > availableTimeSlots.size()) {
                        System.out.println("Invalid choice. Please select a valid time slot number.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Καθαρίζει την μη έγκυρη είσοδο
                }
            }
    
            // Ανάλυση του επιλεγμένου χρόνου
            String TimeStartstr = availableTimeSlots.get(timeSlotChoice - 1);
            LocalTime TimeStart = LocalTime.parse(TimeStartstr);
            String TimeEndstr = availableTimeSlots.get(timeSlotChoice);
            LocalTime TimeEnd = LocalTime.parse(TimeEndstr);

            return new TimeSlot(TimeStart, TimeEnd);

            


           
    
        } catch (SQLException e) {
            // Χειρισμός εξαιρέσεων που σχετίζονται με την βάση δεδομένων
            System.out.println("Database error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            // Χειρισμός εξαιρέσεων κατά την ανάλυση ημερομηνίας/ώρας
            System.out.println("Invalid time format: " + e.getMessage());
        } catch (Exception e) {
            // Γενικός χειρισμός για άλλες εξαιρέσεις
            System.out.println("An error occurred: " + e.getMessage());
        }
    
        return null; // Επιστροφή null σε περίπτωση σφάλματος
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
                // Βάζουμε την παράμετρο user_id πριν την εκτέλεση του query
                stmt.setInt(1, user_id);
                stmt.setInt(2, salon_id);
                stmt.setInt(3, stylist_id);
                stmt.setInt(4, service_id);
    
                try (ResultSet resultSet = stmt.executeQuery()) {
                    // Ελέγχουμε αν υπάρχει εγγραφή στο ResultSet
                    if (resultSet.next()) {
                        // Εμφάνιση λεπτομερειών ραντεβού
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
        // SQL query για να πάρουμε το service_type από τον πίνακα Services
        String query = "SELECT duration FROM Services WHERE service_id = ?";
        
        // Προετοιμασία της δήλωσης για το query
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Ορισμός του service_id στο prepared statement
            pstmt.setInt(1, service_id);
    
            // Εκτέλεση του query και λήψη των αποτελεσμάτων
            ResultSet rs = pstmt.executeQuery();
    
            // Αν το αποτέλεσμα δεν είναι κενό (δηλαδή βρέθηκε το service_id στη βάση)
            if (rs.next()) {
                return rs.getTime("duration").toLocalTime();
            } else {
                // Αν δεν βρεθεί το service_id στη βάση, ρίχνουμε εξαίρεση
                throw new SQLException("Service duration not found with the given ID");
            }
        }
    }
    public static LocalTime parseTimeWithSeconds(String timeString) {
        // Έλεγχος αν το String είναι στη μορφή HH:mm (χωρίς δευτερόλεπτα)
        if (timeString.length() == 5) { // Μορφή HH:mm
            timeString += ":00"; // Προσθήκη των δευτερολέπτων
        }
        
        // Χρησιμοποίηση του LocalTime.parse με το σωστό formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timeString, formatter);
    }


}
        