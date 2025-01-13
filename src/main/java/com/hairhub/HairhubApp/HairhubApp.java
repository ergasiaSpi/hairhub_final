package com.hairhub.HairhubApp;

import com.hairhub.sign_in_up.UserSessionManager;
import com.hairhub.BookAnAppointment.AppointmentScheduler;
import com.hairhub.sign_in_up.UserInput;

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

           
            int userId = authenticateUser(connection, username, password);
            if (userId != -1) {
                UserSessionManager.signInUser(userId);
                UserSessionManager.getSignedInUserRole();

                System.out.printf("Hello, %s!%n", username);

                
                boolean exit = false;
                while (!exit) {
                    System.out.println("\nMenu:");
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
                            showLatestAppointment(connection, userId);
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

                UserSessionManager.signOutUser();
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    private static int authenticateUser(Connection connection, String username, String password) {
        String query = "SELECT user_id FROM Users WHERE username = ? AND user_password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        return -1; //authentication fails
    }

    
    

     private static void bookAppointment(Connection connection, int userId) {
        try {
            AppointmentScheduler scheduler = new AppointmentScheduler();
            scheduler.runScheduler();  // This will start the appointment booking process

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