import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppointmentScheduler {

    private Connection connection;

    //Σύνδεση με ΒΔ
    public AppointmentScheduler(String db) throws SQLException {
        connection =  DriverManager.getConnection(db);
    }

    // Μέθοδος για κλείσιμο ραντεβού
    public boolean bookAppointment(int userId, int salonId, int stylistId, int serviceId, date date, time timeStart, time timeEnd) {
        
        // Εισαγωγή του ραντεβού στον πίνακα Appointments
        String query = "INSERT INTO Appointments (user_id, salon_id, stylist_id, service_id, date, time_start, time_end) " +
                       "VALUES (" + userId + ", " + salonId + ", " + stylistId + ", " + serviceId + ", " + date + ", " + timeStart + ", " + timeEnd )";
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
          
            int rowsInserted = stmt.executeUpdate(query);
            return rowsInserted > 0;
         } catch (SQLException e) {
                e.printStackTrace();
                return false;
         }
    
    }
   
    private List<String> getStylistsBySalonId(int salon_Id) {
        List<String> stylistNames = new ArrayList<>();
        String query = "SELECT stylist_name FROM stylists WHERE salon_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query)
            
        preparedStatement.setInt(1, salon_Id);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            stylistNames.add(resultSet.getString("stylist_name"));
        }
        
        return stylistNames;
    }

    private List<String> getServices() {
        List<String> serviceTypes = new ArrayList<>();
        String query = "SELECT service_type FROM services";
        PreparedStatement preparedStatement = connection.prepareStatement(query)) 
    

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            serviceTypes.add(resultSet.getString("service_type"));
        }
        
        return serviceTypes;
    }


    
    // Main
    public static void main(String[] args) {
        AppointmentScheduler scheduler = new AppointmentScheduler();

        // Εισαγωγή περιοχής για εύρεση των διαθέσιμων salons
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ψάχνεις για salon σε ποια περιοχή:");
        String location = scanner.next();
        SalonDao salonDao = new SalonDao();
        List<Salon> salonsList = salonDao.findSalonsByLocation(location);
        
        System.out.println("Salons στην περιοχή " + location + ":");
        for (int i = 0; i < salonsList.size(); i++) {
        	System.out.println(salonsList.get(i).getSalonId() + " - " + salonsList.get(i).getName() + " - " + salonsList.get(i).getPhoneNumber());
        }
        
        //Επιλογή Salon
        scanner = new Scanner(System.in);
        System.out.println("Δάλεξε το salon που επιθυμείς επιλέγοντας από τα παραπάνω id:");
        int salonId = scanner.nextInt();

        // Εύρεση stylists του συγκεκριμένου salon
        List<String> stylists = getStylistsBySalonId(salonId);

        System.out.println("Stylists για το Salon ID " + salonId + ":");
        
        for (int i = 0; i < stylists.size(); i++) {
            System.out.println((i + 1) + ". " + stylists.get(i));
        }

        //Επιλογή stylist
        System.out.println("Διάλεξε έναν αριθμό από τη λίστα:");
        int choice = scanner.nextInt();

         if (choice > 0 && choice <= stylists.size()) {
                
            } else {
                System.out.println("Μη έγκυρη επιλογή.");
            }
        
        scanner.close();
        
        //Κλείσιμο Ραντεβού
        boolean success = scheduler.bookAppointment( userId, salonId, stylistId, serviceId, date, timeStart, timeEnd);

        if (success) {
            System.out.println("Το ραντεβού κλείστηκε επιτυχώς!");
        } else {
            System.out.println("Η κράτηση απέτυχε.");
        }
    }
}


    

       
   
    
