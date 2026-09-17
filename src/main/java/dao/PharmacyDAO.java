package dao;

import db.DatabaseConnection;
import model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDAO {

    private static final String BASE_SELECT =
            "SELECT pr.id, pr.patient_id, p.first_name || ' ' || p.last_name AS patient_name, " +
            "pr.doctor_name, pr.medication, pr.dosage, pr.status, pr.date_prescribed " +
            "FROM prescriptions pr LEFT JOIN patients p ON pr.patient_id = p.id ";

    public boolean createPrescription(int patientId, String doctorName, String medication,
                                       String dosage, String datePrescribed) {
        String sql = "INSERT INTO prescriptions (patient_id, doctor_name, medication, dosage, status, date_prescribed) " +
                "VALUES (?, ?, ?, ?, 'Pending', ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setString(2, doctorName);
            pstmt.setString(3, medication);
            pstmt.setString(4, dosage);
            pstmt.setString(5, datePrescribed);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prescription> getAllPrescriptions() {
        return runQuery(BASE_SELECT + "ORDER BY pr.id DESC");
    }

    public List<Prescription> getPrescriptionsByPatient(int patientId) {
        List<Prescription> results = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE pr.patient_id = ? ORDER BY pr.id DESC";
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

    public Prescription getPrescriptionById(int id) {
        String sql = BASE_SELECT + "WHERE pr.id = ?";
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

    public boolean markDispensed(int id) {
        String sql = "UPDATE prescriptions SET status = 'Dispensed' WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Prescription> runQuery(String sql) {
        List<Prescription> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Prescription mapRow(ResultSet rs) throws SQLException {
        return new Prescription(
                rs.getInt("id"), rs.getInt("patient_id"), rs.getString("patient_name"),
                rs.getString("doctor_name"), rs.getString("medication"), rs.getString("dosage"),
                rs.getString("status"), rs.getString("date_prescribed")
        );
    }
}
