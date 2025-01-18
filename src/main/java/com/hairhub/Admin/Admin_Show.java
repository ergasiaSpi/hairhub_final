package com.hairhub.Admin;
import java.sql.*;


public class Admin_Show {

    public static void showSalons(Connection connection) throws SQLException{
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
        }
    }

    public static void showStylists(Connection connection) {
        // SQL query για να πάρουμε τους stylists για το συγκεκριμένο salon
        String query = "SELECT stylist_id, stylist_name, specializations, shift_start, shift_end " +
                       "FROM Stylists";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet resultSet = stmt.executeQuery();  // Εκτελούμε το query

            // Αν δεν υπάρχουν stylists για το συγκεκριμένο salon
            if (!resultSet.next()) {
                System.out.println("No stylists found for this salon.");
                return;
            }

            // Εμφανίζουμε τους stylists
            System.out.println("Stylists for salon with ID");
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
                String duration = resultSet.getString("duration");

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
}