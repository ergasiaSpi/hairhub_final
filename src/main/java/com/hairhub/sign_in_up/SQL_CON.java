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
}
