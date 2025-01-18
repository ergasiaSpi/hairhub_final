package com.hairhub.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin_delete {

    public static void deleteFromStylists(Connection connection, Scanner scanner) throws SQLException {
    System.out.println("Enter the stylist_id to delete: ");
    int stylistId = scanner.nextInt();

    String sql = "DELETE FROM Stylists WHERE stylist_id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, stylistId);

        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Stylist with ID " + stylistId + " deleted successfully.");
        } else {
            System.out.println("No stylist found with ID " + stylistId + ".");
        }
    } catch (SQLException e) {
        System.out.println("Error while deleting stylist: " + e.getMessage());
    }
    }

    public static void deleteSalon(Connection connection, Scanner scanner) throws SQLException{
        
        System.out.println("Enter the salon_id to delete: ");
        int salonId = scanner.nextInt();

        String DELETE_SQL = "DELETE FROM Salons WHERE salon_id = ?";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
    
          
            preparedStatement.setInt(1, salonId);
    
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Salon with ID " + salonId + " was deleted successfully!");
            } else {
                System.out.println("No salon found with ID " + salonId + ".");
            }
    
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting the salon:");
            e.printStackTrace();
        }
    }

}
