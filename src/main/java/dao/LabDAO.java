package dao;

import db.DatabaseConnection;
import model.LabTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabDAO {

    private static final String BASE_SELECT =
            "SELECT l.id, l.patient_id, p.first_name || ' ' || p.last_name AS patient_name, " +
            "l.doctor_name, l.test_name, l.status, l.result, l.date_ordered, l.date_completed " +
            "FROM lab_tests l LEFT JOIN patients p ON l.patient_id = p.id ";

    public boolean orderTest(int patientId, String doctorName, String testName, String dateOrdered) {
        String sql = "INSERT INTO lab_tests (patient_id, doctor_name, test_name, status, date_ordered) " +
                "VALUES (?, ?, ?, 'Pending', ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setString(2, doctorName);
            pstmt.setString(3, testName);
            pstmt.setString(4, dateOrdered);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean recordResult(int testId, String result, String dateCompleted) {
        String sql = "UPDATE lab_tests SET result = ?, status = 'Completed', date_completed = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, result);
            pstmt.setString(2, dateCompleted);
            pstmt.setInt(3, testId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LabTest> getAllTests() {
        return runQuery(BASE_SELECT + "ORDER BY l.id DESC");
    }

    public List<LabTest> getTestsByPatient(int patientId) {
        List<LabTest> results = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE l.patient_id = ? ORDER BY l.id DESC";
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

    public List<LabTest> getPendingTests() {
        return runQuery(BASE_SELECT + "WHERE l.status = 'Pending' ORDER BY l.id DESC");
    }

    public LabTest getTestById(int id) {
        String sql = BASE_SELECT + "WHERE l.id = ?";
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

    private List<LabTest> runQuery(String sql) {
        List<LabTest> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private LabTest mapRow(ResultSet rs) throws SQLException {
        return new LabTest(
                rs.getInt("id"), rs.getInt("patient_id"), rs.getString("patient_name"),
                rs.getString("doctor_name"), rs.getString("test_name"), rs.getString("status"),
                rs.getString("result"), rs.getString("date_ordered"), rs.getString("date_completed")
        );
    }
}
