package com.hairhub.BookAnAppointment;
import java.sql.*;

public class DistanceCalculator {

    private Connection connection;

     
    public DistanceCalculator(Connection connection) {
        this.connection = connection;
    }

    
    public double calculateDistance(Location location1, Location location2) {
        final int R = 6371;  
        double latDistance = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double lonDistance = Math.toRadians(location2.getLongitude() - location1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;  
    }

    
    public Location getLocationByZipCode(String zipcode) throws SQLException {
        String query = "SELECT * FROM Location WHERE zipcode = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, zipcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longtitude");
                return new Location(zipcode, latitude, longitude);
            }
        }
        return null;
    }
}
