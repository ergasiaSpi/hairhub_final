package com.hairhub.Admin.Salon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SalonDao {
  public void addSalon(Salon salon) {
    String query = "INSERT INTO Salons (name, address, phone_number, email, rating)VALUES( ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)){
        stmt.setString(1, salon.getName());
        stmt.setString(2, salon.getAddress());
        stmt.setString(4, salon.getPhoneNumber());
        stmt.setString(5, salon.getEmail());
        stmt.setDouble(6, salon.getRating());
        stmt.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
   }
   public List<Salon> getAllSalons() {
      List<Salon> salons = new ArrayList<>();
      String query = "SELECT * FROM Salons";
      try (Connection conn = DatabaseConnection.getConnection();
           PreparedStatement stmt = conn.prepareStatement(query);
           ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Salon salon = new Salon();
            salon.setSalonId(rs.getInt("salon_id"));
            salon.setName(rs.getString("name"));
            salon.setAddress(rs.getString("address"));
            salon.setPhoneNumber(rs.getString("phone_number"));
            salon.setEmail(rs.getString("email"));
            salon.setRating(rs.getDouble("rating"));
            salons.add(salon);
        }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return salons;
   }
  }
