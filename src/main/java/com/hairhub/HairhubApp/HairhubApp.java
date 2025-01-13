package com.hairhub.HairhubApp;

import com.hairhub.sign_in_up.UserSessionManager;
import com.hairhub.sign_in_up.UserInput;
import com.hairhub.BookAnAppointment.AppointmentScheduler;

import java.sql.*;
import java.util.Scanner;

public class HairhubApp {
    private static final String DB_URL = "jdbc:sqlite:hairhub.db"; 

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            }

            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to HairHub! Please log in.");
            String username = UserInput.Get_Username();
            String password = UserInput.Get_Password();

            // Authenticate the user
            int userId = authenticateUser(connection, username, password);
            if (userId != -1) {
                UserSessionManager.signInUser(userId);
                String userRole = UserSessionManager.getSignedInUserRole();

                System.out.printf("Hello, %s! You are logged in as a %s.%n", username, userRole);

                boolean exit = false;
                while (!exit) {
                    if (userRole.equals("admin")) {
                        showAdminMenu(scanner, connection, userId);
                    } else if (userRole.equals("customer")) {
                        showCustomerMenu(scanner, connection, userId);
                    }
                    
                    exit = true; 
                }

                UserSessionManager.signOutUser();
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private static int authenticateUser(Connection connection, String username, String password) {
        String query = "SELECT user_id, role FROM Users WHERE username = ? AND user_password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role");
                UserSessionManager.setUserRole(role); 
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        return -1; 
    }

    private static void showAdminMenu(Scanner scanner, Connection connection, int userId) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View all users");
            System.out.println("2. View all appointments");
            System.out.println("3. Manage salons");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all users...");
                    
                    break;

                case 2:
                    System.out.println("Viewing all appointments...");
                    break;

                case 3:
                    System.out.println("Redirecting to manage salons...");
                    
                    break;

                case 4:
                    System.out.println("Exiting admin interface.");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void showCustomerMenu(Scanner scanner, Connection connection, int userId) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Book an appointment");
            System.out.println("2. Show my latest appointment");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Redirecting to appointment booking...");
                    bookAppointment(connection, userId); 
                    break;

                case 2:
                    System.out.println("Fetching your latest appointment...");
                    showLatestAppointment(connection, userId); // Fixed typo here
                    break;

                case 3:
                    System.out.println("Exiting application. Goodbye!");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void bookAppointment(Connection connection, int userId) {
        try {
            AppointmentScheduler scheduler = new AppointmentScheduler();
            scheduler.runScheduler(); 

        } catch (SQLException e) {
            System.out.println("Error initializing Appointment Scheduler: " + e.getMessage());
        }
    }

    // Show the latest appointment for the user
    private static void showLatestAppointment(Connection connection, int userId) {
        String query = "SELECT A.appointment_id, A.date, A.time_start, S.name AS salon_name, SR.service " +
                       "FROM Appointments A " +
                       "JOIN Salons S ON A.salon_id = S.salon_id " +
                       "JOIN Services SR ON A.service_id = SR.service_id " +
                       "WHERE A.user_id = ? " +
                       "ORDER BY A.date DESC, A.time_start DESC LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                System.out.printf("Appointment ID: %d%n", resultSet.getInt("appointment_id"));
                System.out.printf("Date: %s%n", resultSet.getString("date"));
                System.out.printf("Time: %s%n", resultSet.getString("time_start"));
                System.out.printf("Salon: %s%n", resultSet.getString("salon_name"));
                System.out.printf("Service: %s%n", resultSet.getString("service"));
            } else {
                System.out.println("No appointments found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching latest appointment: " + e.getMessage());
        }
    }
}