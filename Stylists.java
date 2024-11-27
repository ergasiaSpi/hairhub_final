import java.sql.*;

public class StylistDatabaseConnection {
        
        String DB_URL = "jdbc:mysql://localhost:3306/";
        String USER = "root";
        String PASSWORD = "";


public void addStylist(int stylistId, String stylistName, int salonId, String specializations, String shiftStart, String shiftEnd) {
    String INSERT_SQL = "INSERT INTO Stylists (stylist_id, stylist_name, salon_id, specializations, shift_start, shift_end) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

      
        preparedStatement.setInt(1, stylistId);
        preparedStatement.setString(2, stylistName);
        preparedStatement.setInt(3, salonId);
        preparedStatement.setString(4, specializations);
        preparedStatement.setTime(5, Time.valueOf(shiftStart));
        preparedStatement.setTime(6, Time.valueOf(shiftEnd));

      
        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Stylist added successfully!");
        } else {
            System.out.println("Failed to add the stylist.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void showStylists() {
    String QUERY = "SELECT * FROM Stylists";

    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(QUERY)) {

      
        System.out.println("Stylist ID | Stylist Name      | Salon ID | Specializations       | Shift Start | Shift End");
        System.out.println("-----------------------------------------------------------------------------------------");

        
        while (resultSet.next()) {
            int stylistId = resultSet.getInt("stylist_id");
            String stylistName = resultSet.getString("stylist_name");
            int salonId = resultSet.getInt("salon_id");
            String specializations = resultSet.getString("specializations");
            Time shiftStart = resultSet.getTime("shift_start");
            Time shiftEnd = resultSet.getTime("shift_end");

            
            System.out.printf("%-10d | %-17s | %-8d | %-21s | %-11s | %-8s%n",
                    stylistId, stylistName, salonId, 
                    specializations == null ? "N/A" : specializations, 
                    shiftStart, shiftEnd);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

}
}
