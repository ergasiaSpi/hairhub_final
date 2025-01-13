package com.hairhub.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDao {
    private static final String DB_URL = "jdbc:sqlite:/path/to/your/database.db";

    // Method to add a service
    public void addService(Service service) {
        String query = "INSERT INTO Services (id, name, description, price, duration) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, service.getId());
            stmt.setString(2, service.getName());
            stmt.setString(3, service.getDescription());
            stmt.setDouble(4, service.getPrice());
            stmt.setInt(5, service.getDuration());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all services
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM Services";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Service service = new Service(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("duration"));
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    // Method to update a service
    public void updateService(Service service) {
        String query = "UPDATE Services SET name = ?, description = ?, price = ?, duration = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());
            stmt.setInt(4, service.getDuration());
            stmt.setInt(5, service.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a service by ID
    public void deleteService(int serviceId) {
        String query = "DELETE FROM Services WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a service by ID
    public Service getServiceById(int serviceId) {
        String query = "SELECT * FROM Services WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Service(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("duration"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
