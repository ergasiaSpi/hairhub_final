package com.Hairhub.sign_in_up;

import java.sql.*;

public class SQL_CON {

    public static void SQL_INSERT(String Username, String Password) {
        
        String Query = ("INSERT INTO SignUp (Username, Password) VALUES (?, ?)");
        //Υλοποιηση της διεπαφης AutoCloseable μεσα στις παρενθεσεις//
         try(Connection myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "789451");
            PreparedStatement pstmt = myCon.prepareStatement(Query);) {
                pstmt.setString(1, Username);
                pstmt.setString(2, Password);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            
            }catch (SQLException e) {
                System.out.println("connection failed: " + e.getMessage());
            }
    
        }

    public static boolean SQL_SELECT(String Username, String Password) {

        String Query = ("SELECT * FROM SignUp WHERE Username = ? AND Password = ?");

         try(Connection myCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "789451");
            PreparedStatement pstmt = myCon.prepareStatement(Query);) {
                pstmt.setString(1, Username);
                pstmt.setString(2, Password);
                ResultSet result = pstmt.executeQuery();
                if (result.next()==true) {
                    System.out.println("Welcome: " + Username);
                    return true;
                } else {
                    System.out.println("Invalid username or password.");
                    return false;
                }
            
            }catch (SQLException e) {
                System.out.println("connection failed: " + e.getMessage());
                return false;
            }
    }
}
