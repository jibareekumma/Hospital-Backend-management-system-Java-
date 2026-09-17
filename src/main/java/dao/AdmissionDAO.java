package dao;

import db.DatabaseConnection;
import model.Admission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdmissionDAO {

    private static final String BASE_SELECT =
            "SELECT ad.id, ad.patient_id, p.first_name || ' ' || p.last_name AS patient_name, " +
            "ad.bed_id, b.bed_number, b.ward, ad.admission_date, ad.discharge_date, ad.status " +
            "FROM admissions ad " +
            "LEFT JOIN patients p ON ad.patient_id = p.id " +
            "LEFT JOIN beds b ON ad.bed_id = b.id ";

    public boolean admitPatient(int patientId, int bedId, String admissionDate) {
        String sql = "INSERT INTO admissions (patient_id, bed_id, admission_date, status) VALUES (?, ?, ?, 'Admitted')";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, bedId);
            pstmt.setString(3, admissionDate);
            pstmt.executeUpdate();

            new BedDAO().setBedStatus(bedId, "Occupied");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dischargePatient(int admissionId, String dischargeDate) {
        Admission admission = getAdmissionById(admissionId);
        if (admission == null) return false;

        String sql = "UPDATE admissions SET status = 'Discharged', discharge_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dischargeDate);
            pstmt.setInt(2, admissionId);
            pstmt.executeUpdate();

            new BedDAO().setBedStatus(admission.getBedId(), "Available");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Admission> getAllAdmissions() {
        return runQuery(BASE_SELECT + "ORDER BY ad.id DESC");
    }

    public List<Admission> getActiveAdmissions() {
        return runQuery(BASE_SELECT + "WHERE ad.status = 'Admitted' ORDER BY ad.id DESC");
    }

    public Admission getAdmissionById(int id) {
        String sql = BASE_SELECT + "WHERE ad.id = ?";
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

    private List<Admission> runQuery(String sql) {
        List<Admission> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Admission mapRow(ResultSet rs) throws SQLException {
        return new Admission(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getInt("bed_id"),
                rs.getString("bed_number"),
                rs.getString("ward"),
                rs.getString("admission_date"),
                rs.getString("discharge_date"),
                rs.getString("status")
        );
    }
}