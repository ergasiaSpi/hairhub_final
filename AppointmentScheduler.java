import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AppointmentSchedulerApp {

    private AppointmentScheduler scheduler;
    private SalonDao salonDao;
    private CheckAvailability checkAvailability;
    private Scanner scanner;

    public AppointmentSchedulerApp(String dbPath) throws SQLException {
        this.scheduler = new AppointmentScheduler(dbPath);
        this.salonDao = new SalonDao();
        this.checkAvailability = new CheckAvailability(dbPath);
        this.scanner = new Scanner(System.in);
    }

    public void run() throws SQLException {
        public void run() throws SQLException {
    try {
      
        int userId = getSignedInUserId(); 

        String location = askForLocation();
        List<Salon> salons = salonDao.findSalonsByLocation(location);

        if (salons.isEmpty()) {
            System.out.println("Δε βρέθηκαν salons στην περιοχή " + location + ".");
            return;
        }

        displaySalons(salons);
        int salonId = chooseSalon(salons);

        List<String> stylists = scheduler.getStylistsBySalonId(salonId);

        if (stylists.isEmpty()) {
            System.out.println("Δεν υπάρχουν διαθέσιμοι stylists για το επιλεγμένο salon.");
            return;
        }


            displayStylists(stylists);
            int stylistIndex = chooseStylist(stylists);
            String stylistName = stylists.get(stylistIndex - 1);

            int userId = 1; 
            int serviceId = askForService(); 
            String serviceType = "Haircut"; /
            String appointmentDate = askForDate();

            
            List<String> availableSlots = checkAvailability.FindTime(stylistName, appointmentDate, serviceType);

            if (availableSlots.isEmpty()) {
                System.out.println("Δεν υπάρχουν διαθέσιμα ραντεβού για τον επιλεγμένο stylist την ημερομηνία " + appointmentDate + ".");
                return;
            }

            System.out.println("Διαθέσιμα ραντεβού:");
            for (int i = 0; i < availableSlots.size(); i++) {
                System.out.println((i + 1) + ". " + availableSlots.get(i));
            }

            System.out.println("Διάλεξε έναν αριθμό από τη λίστα:");
            int slotChoice = scanner.nextInt();

            if (slotChoice <= 0 || slotChoice > availableSlots.size()) {
                throw new IllegalArgumentException("Μη έγκυρη επιλογή.");
            }

            String chosenTimeSlot = availableSlots.get(slotChoice - 1);
            String[] timeParts = chosenTimeSlot.split("-");
            String timeStart = timeParts[0].trim();
            String timeEnd = timeParts[1].trim();

            Appointment appointment = new Appointment();
            int lastId = appointment.getLastAppointmentId(); 
            
            boolean success = scheduler.bookAppointment(lastId + 1, userId, salonId, stylistIndex, serviceId, appointmentDate, timeStart, timeEnd);

            if (success) {
                System.out.println("Το ραντεβού κλείστηκε επιτυχώς!");
            } else {
                System.out.println("Η κράτηση απέτυχε.");
            }
        } catch (Exception e) {
            System.err.println("Προέκυψε σφάλμα: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                checkAvailability.close();
            } catch (SQLException e) {
                System.err.println("Σφάλμα κατά το κλείσιμο της σύνδεσης: " + e.getMessage());
            }
            scanner.close();
        }
    }

    private String askForLocation() {
        System.out.println("Ψάχνεις για salon σε ποια περιοχή:");
        return scanner.next();
    }

    public int getLastAppointmentId() throws SQLException {
    int lastAppointmentId = 0;

    String query = "SELECT appointment_id FROM appointments ORDER BY appointment_id DESC LIMIT 1";

    try (Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        if (rs.next()) {
            lastAppointmentId = rs.getInt("appointment_id");
        }
    } catch (SQLException e) {
        System.err.println("Σφάλμα κατά την ανάκτηση του τελευταίου appointment_id: " + e.getMessage());
        throw e;
    }

    return lastAppointmentId;
}


    private void displaySalons(List<Salon> salons) {
        System.out.println("Salons στην περιοχή:");
        for (Salon salon : salons) {
            System.out.println(salon.getSalonId() + " - " + salon.getName() + " - " + salon.getPhoneNumber());
        }
    }

    private int chooseSalon(List<Salon> salons) {
        System.out.println("Δάλεξε το salon που επιθυμείς επιλέγοντας από τα παραπάνω ID:");
        int salonId = scanner.nextInt();

        boolean valid = salons.stream().anyMatch(salon -> salon.getSalonId() == salonId);

        if (!valid) {
            throw new IllegalArgumentException("Μη έγκυρο salon ID.");
        }

        return salonId;
    }

    private void displayStylists(List<String> stylists) {
        System.out.println("Stylists:");
        for (int i = 0; i < stylists.size(); i++) {
            System.out.println((i + 1) + ". " + stylists.get(i));
        }
    }

    private int chooseStylist(List<String> stylists) {
        System.out.println("Διάλεξε έναν αριθμό από τη λίστα:");
        int choice = scanner.nextInt();

        if (choice <= 0 || choice > stylists.size()) {
            throw new IllegalArgumentException("Μη έγκυρη επιλογή.");
        }

        return choice;
    }

    private int askForService() {
        System.out.println("Επίλεξε το ID της υπηρεσίας που επιθυμείς:");
        return scanner.nextInt();
    }

    private String askForDate() {
        System.out.println("Εισήγαγε την ημερομηνία του ραντεβού (yyyy-MM-dd):");
        return scanner.next();
    }
}

       
   
    
