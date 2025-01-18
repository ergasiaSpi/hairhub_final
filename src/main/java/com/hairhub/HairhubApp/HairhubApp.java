package com.hairhub.HairhubApp;

import com.hairhub.sign_in_up.UserSessionManager;
import com.hairhub.Admin.*;
import com.hairhub.sign_in_up.SQL_CON;
import com.hairhub.sign_in_up.UserInput;
import com.hairhub.Admin.Admin_insert;
import com.hairhub.BookAnAppointment.AppointmentScheduler;
import java.time.LocalDate;

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

            System.out.println("Welcome to HairHub! Please choose an option:");
            
            
                System.out.println("1. Sign In");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        signInProcess(connection, scanner);
                        break;

                    case 2:
                        signUpProcess(connection, scanner);
                        break;

                    case 3:
                        System.out.println("Exiting application. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void signInProcess(Connection connection, Scanner scanner) throws SQLException{
        
        System.out.println("Please log in.");
        String username = UserInput.Get_Username(true);
        String password = UserInput.Get_Password();
        SQL_CON.SQL_SELECT(username, password);
        int userId = authenticateUser(connection, username, password);
        if (userId != -1) {
            UserSessionManager.signInUser(userId);

            String userRole = UserSessionManager.getSignedInUserRole();

            System.out.printf("Hello, %s! You are logged in as a %s.%n", username, userRole);
            showRoleBasedMenu(scanner, connection, userId, userRole);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void signUpProcess(Connection connection, Scanner scanner) throws SQLException{
        // Assuming you will add the logic for sign up
        System.out.println("Please sign up.");
        String username = UserInput.Get_Username(true);
        String password = UserInput.Get_Password();
        String phone = UserInput.Get_Phone();
        String email = UserInput.Get_Email();
        String zip_code = UserInput.Get_Postcode();
        String role = UserInput.Get_Role();
        SQL_CON.SQL_INSERT(username, password, email, phone, zip_code, role);

        // Add sign-up logic here, such as inserting new user into the database
        // If successful, then proceed to sign-in process after sign-up

        System.out.println("Sign up successful. Please Sign in. ");
        signInProcess(connection, scanner);
    }

    public static int authenticateUser(Connection connection, String username, String password) {
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

    private static void showRoleBasedMenu(Scanner scanner, Connection connection, int userId, String userRole) throws SQLException {
        boolean exit = false;
        
            if (userRole.equals("admin")) {
                showAdminMenu(scanner, connection, userId);
            } else if (userRole.equals("customer")) {
                showCustomerMenu(scanner, connection, userId);
            }

            
        }
    

    private static void showAdminMenu(Scanner scanner, Connection connection, int userId) throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View all users");
            System.out.println("2. View all appointments");
            System.out.println("3. Add salon");
            System.out.println("4. Add stylist");
            System.out.println("5. Add service");
            System.out.println("6. Delete salon");
            System.out.println("7. Delete stylist");
            System.out.println("8. Sign Out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Viewing all users...");
                    SQL_CON.View_Users();
                    break;

                case 2:
                    System.out.println("Viewing all appointments...");
                    SQL_CON.viewAppointmentsByAdmin(choice);
                    break;

                case 3:
                    Admin_Show.showSalons(connection);
                    Admin_insert.insertIntoSalons(connection, scanner);
                    break;

                case 4:
                    Admin_Show.showStylists(connection);
                    Admin_insert.insertIntoStylists(connection, scanner);
                    break;

                case 5:
                    Admin_Show.showServices(connection);
                    Admin_insert.insertIntoServices(connection, scanner);
                    break;

                case 6:
                    Admin_Show.showSalons(connection);
                    Admin_delete.deleteSalon(connection, scanner);
                    break;

                case 7:
                    Admin_Show.showStylists(connection);
                    Admin_delete.deleteFromStylists(connection, scanner);
                    break;

                case 8:
                    System.out.println("Signing out...");
                    exit = true;
                    UserSessionManager.signOutUser();
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
            System.out.println("3. Sign Out");
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
                    System.out.println("Signing out...");
                    UserSessionManager.signOutUser();
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
            SQL_CON.showSalons(connection);
            int salon_id = AppointmentScheduler.chooseSalon();
            SQL_CON.showStylists(connection, salon_id);
            int stylist_id = AppointmentScheduler.chooseStylist();
            SQL_CON.showServices(connection);
            int service_id = AppointmentScheduler.chooseService(connection);
            LocalDate date = AppointmentScheduler.ChooseDate();

            AppointmentScheduler scheduler = new AppointmentScheduler();
            scheduler.runScheduler(); 
        } catch (SQLException e) {
            System.out.println("Error initializing Appointment Scheduler: " + e.getMessage());
        }
    }

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
