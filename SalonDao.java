import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SalonDao {
    private static final String DB_URL = "your_database_url";

    public void addSalon(int salonId, String name, String address, String phoneNumber, String email) {
        String INSERT_SQL = "INSERT INTO Salons (salon_id, name, address, phone_number, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_SQL)) {

            preparedStatement.setInt(1, salonId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Salon added successfully!");
            } else {
                System.out.println("Failed to add the salon.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Salon> getAllSalons() {
        List<Salon> salons = new ArrayList<>();
        String query = "SELECT salon_id, name, address, phone_number, email FROM Salons";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Salon salon = new Salon();
                salon.setSalonId(rs.getInt("salon_id"));
                salon.setName(rs.getString("name"));
                salon.setAddress(rs.getString("address"));
                salon.setPhoneNumber(rs.getString("phone_number"));
                salon.setEmail(rs.getString("email"));
                salons.add(salon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salons;
    }
 }
