package com.hairhub.BookAnAppointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;


public class TimeSlot {
    private LocalTime start;
    private LocalTime end;
    private Connection connection;

    
    public TimeSlot(String dbPath) throws SQLException {
        connection = DriverManager.getConnection(dbPath);
    }

    
    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    
    public LocalTime getStart() {
        return start;
    }

   
    public LocalTime getEnd() {
        return end;
    }

  
    public boolean overlaps(TimeSlot other) {
        return !(end.isBefore(other.start) || start.isAfter(other.end));
    }


    public static void removeBookedTimeSlot(int stylistId, String appointmentDate, LocalTime timeStart, LocalTime timeEnd, Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM AvailabilitybyStylist " +
                             "WHERE stylist_id = ? AND appoint_date = ? AND time_start = ? AND time_end = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, stylistId);
            deleteStmt.setString(2, appointmentDate);
            deleteStmt.setTime(3, Time.valueOf(timeStart));
            deleteStmt.setTime(4, Time.valueOf(timeEnd));
            deleteStmt.executeUpdate();
            System.out.println("Time slot removed successfully.");
        } catch (SQLException e) {
            System.out.println("Error removing time slot: " + e.getMessage());
            throw e;
        }
    }

    
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }
}

