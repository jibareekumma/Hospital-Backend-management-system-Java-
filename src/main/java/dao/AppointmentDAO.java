package dao;

import db.DatabaseConnection;
import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String BASE_SELECT =
            "SELECT a.id, a.patient_id, p.first_name || ' ' || p.last_name AS patient_name, " +
            "a.doctor_id, a.doctor_name, a.appointment_date, a.appointment_time, a.reason, a.status " +
            "FROM appointments a LEFT JOIN patients p ON a.patient_id = p.id ";

    public boolean bookAppointment(int patientId, int doctorId, String doctorName,
                                    String date, String time, String reason) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, doctor_name, appointment_date, appointment_time, reason, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, doctorId);
            pstmt.setString(3, doctorName);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            pstmt.setString(6, reason);
            pstmt.setString(7, "Pending");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAllAppointments() {
        return runQuery(BASE_SELECT + "ORDER BY a.id DESC");
    }

    public List<Appointment> findAppointments(String term) {
        List<Appointment> results = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.first_name LIKE ? OR p.last_name LIKE ? OR a.doctor_name LIKE ? OR a.appointment_date LIKE ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String like = "%" + term + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> results = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.status = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Appointment getAppointmentById(int id) {
        String sql = BASE_SELECT + "WHERE a.id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean rescheduleAppointment(int id, String newDate, String newTime, String newReason) {
        String sql = "UPDATE appointments SET appointment_date = ?, appointment_time = ?, reason = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newDate);
            pstmt.setString(2, newTime);
            pstmt.setString(3, newReason);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelAppointment(int id) {
        return updateStatus(id, "Cancelled");
    }

    private List<Appointment> runQuery(String sql) {
        List<Appointment> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getInt("doctor_id"),
                rs.getString("doctor_name"),
                rs.getString("appointment_date"),
                rs.getString("appointment_time"),
                rs.getString("reason"),
                rs.getString("status")
        );
    }
}