package dao;

import db.DatabaseConnection;
import model.ClinicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClinicalDAO {

    private static final String BASE_SELECT =
            "SELECT m.id, m.patient_id, p.first_name || ' ' || p.last_name AS patient_name, " +
            "m.doctor_name, m.diagnosis, m.treatment, m.notes, m.record_date " +
            "FROM medical_history m LEFT JOIN patients p ON m.patient_id = p.id ";

    public boolean addRecord(int patientId, String doctorName, String diagnosis,
                              String treatment, String notes, String recordDate) {
        String sql = "INSERT INTO medical_history (patient_id, doctor_name, diagnosis, treatment, notes, record_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setString(2, doctorName);
            pstmt.setString(3, diagnosis);
            pstmt.setString(4, treatment);
            pstmt.setString(5, notes);
            pstmt.setString(6, recordDate);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ClinicalRecord> getRecordsByPatient(int patientId) {
        List<ClinicalRecord> results = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE m.patient_id = ? ORDER BY m.id DESC";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ClinicalRecord getRecordById(int id) {
        String sql = BASE_SELECT + "WHERE m.id = ?";
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

    public boolean updateRecord(int id, String diagnosis, String treatment, String notes) {
        String sql = "UPDATE medical_history SET diagnosis = ?, treatment = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diagnosis);
            pstmt.setString(2, treatment);
            pstmt.setString(3, notes);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ClinicalRecord mapRow(ResultSet rs) throws SQLException {
        return new ClinicalRecord(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getString("doctor_name"),
                rs.getString("diagnosis"),
                rs.getString("treatment"),
                rs.getString("notes"),
                rs.getString("record_date")
        );
    }
}