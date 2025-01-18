package com.hairhub.Admin;

import java.util.Scanner;

import com.hairhub.HairhubApp.HairhubApp;
import com.hairhub.sign_in_up.Constrictions;
import com.hairhub.sign_in_up.UserInput;
import com.hairhub.sign_in_up.UserSessionManager;

import java.sql.*;


public class Admin_insert {
    
    public static void insertIntoSalons(Connection connection, Scanner scanner) throws SQLException {
        
        int adminId = UserSessionManager.getSignedInUserId();
        scanner.nextLine();

        System.out.println("Enter salon name: ");
        String name = scanner.nextLine();  

        while (!Constrictions.Name_Constrictions(name)) {
            System.out.println("Invalid salon name. Please try again: ");
            name = scanner.nextLine();
        }

        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        System.out.println("Enter zipcode: ");
        String zipcode = scanner.nextLine();

        while (!Constrictions.Postcode_Constrictions(zipcode)) {
            System.out.println("Invalid salon zipcode. Please try again: ");
            zipcode = scanner.nextLine();
        }

        System.out.println("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        while (!Constrictions.Phone_Constrictions(phoneNumber)) {
            System.out.println("Invalid salon phonenumber. Please try again: ");
            phoneNumber = scanner.nextLine();
        }

        System.out.println("Enter email: ");
        String email = scanner.nextLine();

        while (!Constrictions.Email_Constrictions(email)) {
            System.out.println("Invalid salon email. Please try again: ");
            email = scanner.nextLine();
        }
        

        String sql = "INSERT INTO Salons (admin_id, name, address, zipcode, phone_number, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, zipcode);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, email);
           
            pstmt.executeUpdate();
            System.out.println("Data inserted into Salons successfully.");

        }
    }

    public static void insertIntoStylists(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Choose a salon to add a stylist: ");
        int salonId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Enter stylist_name: "); 
        String stylistName = scanner.nextLine();

        while (!Constrictions.Name_Constrictions(stylistName)) {
            System.out.println("Invalid stylist name. Please try again: ");
            stylistName = scanner.nextLine();}
        

        System.out.println("Enter specializations: ");
        String specializations = scanner.nextLine();

        System.out.println("Enter shift_start (HH:MM:SS): ");
        String shiftStart = scanner.nextLine();

        while (!Constrictions.isValidTime(stylistName)) {
            System.out.println("Invalid shift start. Shift start must be HH:MM:SS. Please try again: ");
            shiftStart = scanner.nextLine();}

        System.out.println("Enter shift_end (HH:MM:SS): ");
        String shiftEnd = scanner.nextLine();

        while (!Constrictions.isValidTime(shiftEnd)) {
            System.out.println("Invalid shift end. Shift start must be HH:MM:SS. Please try again: ");
            shiftEnd = scanner.nextLine();}

        String sql = "INSERT INTO Stylists (stylist_name, salon_id, specializations, shift_start, shift_end) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stylistName);
            pstmt.setInt(2, salonId);
            pstmt.setString(3, specializations);
            pstmt.setString(4, shiftStart);
            pstmt.setString(5, shiftEnd);
            pstmt.executeUpdate();
            System.out.println("Data inserted into Stylists successfully.");

        }
    }

    public static void insertIntoServices(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter stylist_id: ");
        int stylistId = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.println("Enter service name: ");
        String service = scanner.nextLine();
    
        System.out.println("Enter service type (e.g., haircut, coloring): ");
        String serviceType = scanner.nextLine();
    
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); 
    
        System.out.println("Enter duration (HH:MM:SS): ");
        String duration = scanner.nextLine();

        while (!Constrictions.isValidTime(duration)) {
            System.out.println("Invalid duration. Duration must be HH:MM:SS. Please try again: ");
            duration = scanner.nextLine();}
       
        String sql = "INSERT INTO Services (stylist_id, service, service_type, price, duration) VALUES (?, ?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stylistId);
            pstmt.setString(2, service);
            pstmt.setString(3, serviceType);
            pstmt.setDouble(4, price);
            pstmt.setString(5, duration);
            pstmt.executeUpdate();
            System.out.println("Service added successfully.");
    

        } catch (SQLException e) {
            System.out.println("Error while adding service: " + e.getMessage());
        }
    }

    

}
