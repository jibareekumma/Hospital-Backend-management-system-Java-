package dao;

import db.DatabaseConnection;
import model.Bed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BedDAO {

    public boolean addBed(String bedNumber, String ward) {
        String sql = "INSERT INTO beds (bed_number, ward, status) VALUES (?, ?, 'Available')";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bedNumber);
            pstmt.setString(2, ward);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bed> getAllBeds() {
        return runQuery("SELECT * FROM beds ORDER BY ward, bed_number");
    }

    public List<Bed> getAvailableBeds() {
        return runQuery("SELECT * FROM beds WHERE status = 'Available' ORDER BY ward, bed_number");
    }

    public Bed getBedById(int id) {
        String sql = "SELECT * FROM beds WHERE id = ?";
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

    public boolean setBedStatus(int id, String status) {
        String sql = "UPDATE beds SET status = ? WHERE id = ?";
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

    private List<Bed> runQuery(String sql) {
        List<Bed> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Bed mapRow(ResultSet rs) throws SQLException {
        return new Bed(rs.getInt("id"), rs.getString("bed_number"), rs.getString("ward"), rs.getString("status"));
    }
}