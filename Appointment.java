import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Appointment {
	
   
    private int appointmentId;
    private int userId;
    private int salonId;
    private int stylistId;
    private int serviceId;
    private Date date;
    private Time timeStart;
    private String status;
    private static final String DB_SERVER = "jdbc:mysql://localhost:3306/hair_hub";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "******";

    
    public Appointment() {
    	
	}
    

    public Appointment(int appointmentId, int userId, int salonId, int stylistId, int serviceId, Date date, Time timeStart, String status) {
    	this.appointmentId = appointmentId;
    	this.userId = userId;
        this.salonId = salonId;
        this.stylistId = stylistId;
        this.serviceId = serviceId;
        this.date = date;
        this.timeStart = timeStart;
        this.status = status;
    }

    
    public Appointment(int userId, int salonId, int stylistId, int serviceId, Date date, Time timeStart, String status) {
    	this.userId = userId;
        this.salonId = salonId;
        this.stylistId = stylistId;
        this.serviceId = serviceId;
        this.date = date;
        this.timeStart = timeStart;
        this.status = status;
    }
    

	
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSalonId() {
        return salonId;
    }

    public void setSalonId(int salonId) {
        this.salonId = salonId;
    }

    public int getStylistId() {
        return stylistId;
    }

    public void setStylistId(int stylistId) {
        this.stylistId = stylistId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
    public Appointment insertNewAppointment(Appointment appointment) throws SQLException {
        
        try {
        	/
        	Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);

        	
        	String sql = "INSERT INTO appointments (user_id, salon_id, stylist_id, service_id, date, time_start, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, appointment.getUserId());
            stmt.setInt(2, appointment.getSalonId());
            stmt.setInt(3, appointment.getStylistId());
            stmt.setInt(4, appointment.getServiceId());
            stmt.setDate(5, appointment.getDate());
            stmt.setTime(6, appointment.getTimeStart());
            stmt.setString(7, appointment.getStatus());
            stmt.executeUpdate();

            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointment.setAppointmentId(rs.getInt(1));
                
                return appointment;
            }
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        
        return null;
    }

    
    public static Appointment searchAppointmentById(int id) throws SQLException {

        try {
        	
        	Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);
        	
        	
            String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            
            if (rs.next()) {
                Appointment appointment = new Appointment();
               
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setUserId(rs.getInt("user_id"));
                appointment.setSalonId(rs.getInt("salon_id"));
                appointment.setStylistId(rs.getInt("stylist_id"));
                appointment.setServiceId(rs.getInt("service_id"));
                appointment.setDate(rs.getDate("date"));
                appointment.setTimeStart(rs.getTime("time_start"));
                appointment.setStatus(rs.getString("status"));
               
                return appointment;
            }
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        return null; 

    public boolean updateAppointment(Appointment appointment) throws SQLException {
        
        try {
        	
        	Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);
        	
        	
        	String sql = "UPDATE appointments SET "
        			+ "user_id = ?, salon_id = ?, stylist_id = ?, service_id = ?, date = ?, time_start = ?, status = ? "
        			+ "WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment.getUserId());
            stmt.setInt(2, appointment.getSalonId());
            stmt.setInt(3, appointment.getStylistId());
            stmt.setInt(4, appointment.getServiceId());
            stmt.setDate(5, appointment.getDate());
            stmt.setTime(6, appointment.getTimeStart());
            stmt.setString(7, appointment.getStatus());
            stmt.setInt(8, appointment.getAppointmentId());
            stmt.executeUpdate();
            
            return true;
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
       
        return false;
    }

  
    public boolean deleteAppointment(Appointment appointment) throws SQLException {
        
        try {
        	
        	Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);
        	
        	
        	String sql = "DELETE FROM appointments WHERE appointment_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment.getAppointmentId());
            stmt.executeUpdate();
            return true;
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        return false;
    }

    public static List<Appointment> listAppointments() throws SQLException {
    	
        List<Appointment> appointments = new ArrayList<>();
        
        try {
        	
        	Connection conn = DriverManager.getConnection(DB_SERVER, DB_USER, DB_PASSWORD);

        	
        	String sql = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setUserId(rs.getInt("user_id"));
                appointment.setSalonId(rs.getInt("salon_id"));
                appointment.setStylistId(rs.getInt("stylist_id"));
                appointment.setServiceId(rs.getInt("service_id"));
                appointment.setDate(rs.getDate("date"));
                appointment.setTimeStart(rs.getTime("time_start"));
                appointment.setStatus(rs.getString("status"));
                appointments.add(appointment);
            }
        } catch(Exception e) {
        	System.out.println(e.getMessage());
        }
      
        return appointments;
    }
    
}
