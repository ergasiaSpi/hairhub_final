package com.hairhub.sign_in_up;

import java.sql.*;

public class SQL_CON {

    public static void SQL_INSERT(String Username, String Password, String email, String phone, String postal_code, String role) {
        
        String Query = "INSERT INTO Users (username, user_password, email, phone, postal_code, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Σύνδεση με SQLite και εκτέλεση του INSERT query
        try (Connection myCon = DriverManager.getConnection("jdbc:sqlite:hairhub.db");  // Σύνδεση με SQLite
             PreparedStatement pstmt = myCon.prepareStatement(Query);) {
            
            pstmt.setString(1, Username);
            pstmt.setString(2, Password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, postal_code);
            pstmt.setString(6, role);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    public static boolean SQL_SELECT(String Username, String Password) {

        String Query = "SELECT * FROM Users WHERE username = ? AND user_password = ?";
        
        // Σύνδεση με SQLite και εκτέλεση του SELECT query
        try (Connection myCon = DriverManager.getConnection("jdbc:sqlite:hairhub.db");  // Σύνδεση με SQLite
             PreparedStatement pstmt = myCon.prepareStatement(Query);) {
            
            pstmt.setString(1, Username);
            pstmt.setString(2, Password);
            ResultSet result = pstmt.executeQuery();
            
            if (result.next()) {  // Αν υπάρχει τουλάχιστον μια εγγραφή
                System.out.println("Welcome: " + Username);
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    public static void View_Users() {
        String query = "SELECT * FROM Users";
    
        try (Connection myCon = DriverManager.getConnection("jdbc:sqlite:hairhub.db");
             Statement stmt = myCon.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
             
            System.out.println("User Data:");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-10s %-15s %-15s %-15s %-10s %-15s%n", "ID", "Username", "Password", "Email", "Phone", "Role");
            System.out.println("-------------------------------------------------");
    
            while (result.next()) {
                int userId = result.getInt("user_id");
                String username = result.getString("username");
                String userPassword = result.getString("user_password");
                String email = result.getString("email");
                String phone = result.getString("phone");
                String role = result.getString("role");
    
                System.out.printf("%-10d %-15s %-15s %-15s %-10s %-15s%n", userId, username, userPassword, email, phone, role);
            }
    
        } catch (SQLException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
    }

    // Μέθοδος για εμφάνιση των κομμωτηρίων
    public static void showSalons(Connection connection) {
        String query = "SELECT salon_id, name, address, phone_number, email FROM Salons";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("List of Salons:");
            System.out.println("--------------------------------------------------");

            // Έλεγχος αν υπάρχουν αποτελέσματα
            if (!rs.isBeforeFirst()) {
                System.out.println("No salons found.");
            } else {
                // Εμφάνιση των αποτελεσμάτων
                while (rs.next()) {
                    int salonId = rs.getInt("salon_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phone_number");
                    String email = rs.getString("email");
                    

                    System.out.printf("Salon ID: %d\n", salonId);
                    System.out.printf("Name: %s\n", name);
                    System.out.printf("Address: %s\n", address);
                    System.out.printf("Phone Number: %s\n", phoneNumber);
                    System.out.printf("Email: %s\n", email);
                    System.out.println("--------------------------------------------------");
                }
            }
         

        } catch (SQLException e) {
            System.out.println("Error while retrieving salons: " + e.getMessage());
        }
    }

    public static void showStylists(Connection connection, int salonId) {
        // SQL query για να πάρουμε τους stylists για το συγκεκριμένο salon
        String query = "SELECT stylist_id, stylist_name, specializations, shift_start, shift_end " +
                       "FROM Stylists WHERE salon_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, salonId);  // Ορίζουμε το salon_id στην prepared statement
            ResultSet resultSet = stmt.executeQuery();  // Εκτελούμε το query

            // Αν δεν υπάρχουν stylists για το συγκεκριμένο salon
            if (!resultSet.next()) {
                System.out.println("No stylists found for this salon.");
                return;
            }

            // Εμφανίζουμε τους stylists
            System.out.println("Stylists for salon with ID " + salonId + ":");
            do {
                int stylistId = resultSet.getInt("stylist_id");
                String stylistName = resultSet.getString("stylist_name");
                String specializations = resultSet.getString("specializations");
                Time shiftStart = resultSet.getTime("shift_start");
                Time shiftEnd = resultSet.getTime("shift_end");

                // Εμφανίζουμε τις πληροφορίες του κάθε stylist
                System.out.println("Stylist ID: " + stylistId);
                System.out.println("Name: " + stylistName);
                System.out.println("Specializations: " + (specializations != null ? specializations : "Not specified"));
                System.out.println("Shift Start: " + shiftStart);
                System.out.println("Shift End: " + shiftEnd);
                System.out.println("------------------------");
            } while (resultSet.next());  // Συνεχίζουμε μέχρι να τελειώσουν όλοι οι stylists

        } catch (SQLException e) {
            System.out.println("Error fetching stylists: " + e.getMessage());
        }
    }

    // Μέθοδος για να εμφανίζει όλες τις διαθέσιμες υπηρεσίες
    public static void showServices(Connection connection) {
        // SQL query για να πάρουμε όλες τις υπηρεσίες από τον πίνακα Services
        String query = "SELECT service_id, service, price, duration FROM Services";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();  // Εκτελούμε το query

            // Αν δεν υπάρχουν υπηρεσίες
            if (!resultSet.next()) {
                System.out.println("No services available.");
                return;
            }

            // Εμφανίζουμε τις υπηρεσίες
            System.out.println("Available services:");
            do {
                int serviceId = resultSet.getInt("service_id");
                String serviceName = resultSet.getString("service");
                double price = resultSet.getDouble("price");
                Time duration = resultSet.getTime("duration");

                // Εμφανίζουμε τις πληροφορίες για κάθε υπηρεσία
                System.out.println("Service ID: " + serviceId);
                System.out.println("Service Name: " + serviceName);
                System.out.println("Price: " + price + " EUR");
                System.out.println("Duration: " + duration);
                System.out.println("------------------------");
            } while (resultSet.next());  // Συνεχίζουμε μέχρι να τελειώσουν όλες οι υπηρεσίες

        } catch (SQLException e) {
            System.out.println("Error fetching services: " + e.getMessage());
        }
    }




    public static void viewAppointmentsByAdmin(int adminId) {
        String query = """
            SELECT 
                a.appointment_id,
                u.username AS customer_name,
                s.name AS salon_name,
                st.stylist_name,
                sr.service,
                a.date,
                a.time_start,
                a.status
            FROM 
                Appointments a
            INNER JOIN Users u ON a.user_id = u.user_id
            INNER JOIN Salons s ON a.salon_id = s.salon_id
            LEFT JOIN Stylists st ON a.stylist_id = st.stylist_id
            INNER JOIN Services sr ON a.service_id = sr.service_id
            WHERE 
                s.admin_id = ?
            ORDER BY 
                a.date, a.time_start;
            """;
    
        try (Connection myCon = DriverManager.getConnection("jdbc:sqlite:hairhub.db");
             PreparedStatement pstmt = myCon.prepareStatement(query)) {
             
            pstmt.setInt(1, adminId);
    
            try (ResultSet result = pstmt.executeQuery()) {
                System.out.println("Appointments for Admin ID: " + adminId);
                System.out.println("---------------------------------------------------------");
                System.out.printf("%-10s %-15s %-15s %-15s %-15s %-10s %-10s%n", 
                                  "ID", "Customer", "Salon", "Stylist", "Service", "Date", "Time");
                System.out.println("---------------------------------------------------------");
    
                while (result.next()) {
                    int appointmentId = result.getInt("appointment_id");
                    String customerName = result.getString("customer_name");
                    String salonName = result.getString("salon_name");
                    String stylistName = result.getString("stylist_name");
                    String service = result.getString("service");
                    String date = result.getString("date");
                    String time = result.getString("time_start");
                    String status = result.getString("status");
    
                    System.out.printf("%-10d %-15s %-15s %-15s %-15s %-10s %-10s%n", 
                                      appointmentId, customerName, salonName, stylistName, service, date, time);
                }
            }
    
        } catch (SQLException e) {
            System.out.println("Error retrieving appointments: " + e.getMessage());
        }
    }
    
    
}
