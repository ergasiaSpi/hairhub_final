import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SalonDao {
  public void addSalon(Salon salon) {
    String query = "INSERT INTO Salons (name, address, location, phone_number, email,)VALUES( ?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
        stmt.setString(1, salon.getName());
        stmt.setString(2, salon.getAddress());
        stmt.setString(3, salon.getLocation());
        stmt.setString(4, salon.getPhoneNumber());
        stmt.setString(5, salon.getEmail());
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
            salon.setLocation(rs.getString("location"));
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
  public List<Salon> findSalonsByLocation(String location) {
     List<Salon> salons = new ArrayList<>();
     String query = "SELECT * FROM Salons WHERE location = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, location);
        ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
           Salon salon = new Salon();
           salon.setSalonId(rs.getInt("salon_id"));
           salon.setName(rs.getString("name"));
           salon.setAddress(rs.getString("address"));
           salon.setLocation(rs.getString("location"));
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
