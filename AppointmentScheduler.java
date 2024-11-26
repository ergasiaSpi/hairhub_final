import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppointmentScheduler {


    private static final String DB_URL = "jdbc:sqlite:salon.db";

    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Μέθοδος για κλείσιμο ραντεβού
    public boolean bookAppointment(int userId, int salonId, int stylistId, int serviceId, date date, time timeStart) {
        
        // Εύρεση διάρκειας 
        int duration = getServiceDuration(serviceId);
        if (duration == -1) {
            System.out.println("Η υπηρεσία δεν βρέθηκε.");
            return false;
        }

        // Υπολογισμός ώρας λήξης
        String timeEnd = calculateEndTime(timeStart, duration);

        // Έλεγχος διαθεσιμότητας κομμωτή
        if (!isStylistAvailable(stylistId, date, timeStart, timeEnd)) {
            System.out.println("Ο κομμωτής δεν είναι διαθέσιμος για αυτή την ώρα.");
            return false;
        }

        // Εισαγωγή του ραντεβού στον πίνακα Appointments
        String query = "INSERT INTO Appointments (user_id, salon_id, stylist_id, service_id, date, time_start, status) " +
                       "VALUES (" + userId + ", " + salonId + ", " + stylistId + ", " + serviceId + ", '" + date + "', '" + timeStart + "', 'pending')";
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
          
            int rowsInserted = stmt.executeUpdate(query);
            return rowsInserted > 0;
         } catch (SQLException e) {
                e.printStackTrace();
                return false;
         }
    
    }

    // Μέθοδος για εύρεση της διάρκεια μιας υπηρεσίας
    private int getServiceDuration(int serviceId) {
        String query = "SELECT duration FROM Services WHERE service_id = " + serviceId;
        try (Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

       if (rs.next()) {
           return rs.getInt("duration");
       }
   } catch (SQLException e) {
       e.printStackTrace();
   }
        return -1; // Αν η υπηρεσία δεν βρεθεί
    }

    // Υπολογισμός ώρας λήξης
    private String calculateEndTime(time timeStart, int duration) {
        String[] parts = timeStart.split(":");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]) + duration;

        hours += minutes / 60;
        minutes %= 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    // Main
    public static void main(String[] args) {
        AppointmentScheduler scheduler = new AppointmentScheduler();

        boolean success = scheduler.bookAppointment(1, 1, 2, 3, "2024-12-01", "10:00");

        if (success) {
            System.out.println("Το ραντεβού κλείστηκε επιτυχώς!");
        } else {
            System.out.println("Η κράτηση απέτυχε.");
        }
    }
}


    

       
   
    
