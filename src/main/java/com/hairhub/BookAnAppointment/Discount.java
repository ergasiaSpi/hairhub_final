package com.hairhub.BookAnAppointment;

import java.sql.*;
import java.util.*;

public class Discount {
    private static final double DISCOUNT_RATE = 0.2; // 20% έκπτωση

    public static void applyDiscount(int stylistId, String date, Connection connection) {
        try {
            // SQL για καταμέτρηση ραντεβού για τον συγκεκριμένο stylist και ημερομηνία
            String countAppointmentsQuery = """
                SELECT COUNT(*) AS appointment_count
                FROM Appointments
                WHERE stylist_id = ? AND date = ?;
            """;
    
            try (PreparedStatement countStmt = connection.prepareStatement(countAppointmentsQuery)) {
                countStmt.setInt(1, stylistId);
                countStmt.setString(2, date);
                ResultSet rs = countStmt.executeQuery();
    
                boolean applyDiscount = false;
                if (rs.next() && rs.getInt("appointment_count") < 2) {
                    applyDiscount = true;  // Εάν υπάρχουν λιγότερα από 2 ραντεβού, εφαρμόζουμε την έκπτωση
                }
    
                // Ενημέρωση όλων των υπηρεσιών του stylist με την έκπτωση ή χωρίς
                String servicePriceQuery = """
                    SELECT service_id, price
                    FROM Services
                    WHERE stylist_id = ?;
                """;
    
                try (PreparedStatement priceStmt = connection.prepareStatement(servicePriceQuery)) {
                    priceStmt.setInt(1, stylistId);
                    ResultSet priceRs = priceStmt.executeQuery();
    
                    while (priceRs.next()) {
                        double originalPrice = priceRs.getDouble("price");
                        double discountedPrice = applyDiscount ? originalPrice * (1 - DISCOUNT_RATE) : 0;
    
                        // Ενημέρωση της τιμής discount για την υπηρεσία του κομμωτή
                        String updateDiscountedPriceQuery = """
                            UPDATE Services
                            SET discounted_price = ?
                            WHERE service_id = ?;
                        """;
    
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateDiscountedPriceQuery)) {
                            if (applyDiscount) {
                                updateStmt.setDouble(1, discountedPrice);  // Εφαρμόζουμε την έκπτωση
                            } else {
                                updateStmt.setNull(1, java.sql.Types.DOUBLE);  // Εάν δεν ισχύει η έκπτωση, το κάνουμε null
                            }
                            updateStmt.setInt(2, priceRs.getInt("service_id"));
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error applying discount: " + e.getMessage());
        }
    }
    
}
