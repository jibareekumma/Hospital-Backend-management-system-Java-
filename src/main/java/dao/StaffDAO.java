package dao;

import dao.UserDAO;
import db.DatabaseConnection;
import model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public boolean registerStaff(Staff staff, String plainPassword) {
        String sql = "INSERT INTO staff (first_name, last_name, role, department, phone, email, username, password, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setString(3, staff.getRole());
            pstmt.setString(4, staff.getDepartment());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getEmail());
            pstmt.setString(7, staff.getUsername());
            pstmt.setString(8, UserDAO.hashPassword(plainPassword));
            pstmt.setString(9, "Active");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Staff> getAllStaff() {
        return runQuery("SELECT * FROM staff ORDER BY id DESC");
    }

    public List<Staff> findStaffByName(String term) {
        List<Staff> results = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE first_name LIKE ? OR last_name LIKE ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + term + "%");
            pstmt.setString(2, "%" + term + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Staff> getStaffByDepartment(String department) {
        List<Staff> results = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE department = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Staff getStaffById(int id) {
        String sql = "SELECT * FROM staff WHERE id = ?";
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

    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET first_name = ?, last_name = ?, role = ?, department = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setString(3, staff.getRole());
            pstmt.setString(4, staff.getDepartment());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getEmail());
            pstmt.setInt(7, staff.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(int id) {
        String sql = "DELETE FROM staff WHERE id = ?";
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

    public boolean setStatus(int id, String status) {
        String sql = "UPDATE staff SET status = ? WHERE id = ?";
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

    public boolean resetPassword(int id, String newPlainPassword) {
        String sql = "UPDATE staff SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, UserDAO.hashPassword(newPlainPassword));
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRole(int id, String newRole) {
        String sql = "UPDATE staff SET role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Staff> runQuery(String sql) {
        List<Staff> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Staff mapRow(ResultSet rs) throws SQLException {
        return new Staff(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("role"),
                rs.getString("department"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("status")
        );
    }

    public List<Staff> getStaffByRole(String role) {
    List<Staff> results = new ArrayList<>();
    String sql = "SELECT * FROM staff WHERE role = ?";
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, role);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) results.add(mapRow(rs));
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return results;
}
}