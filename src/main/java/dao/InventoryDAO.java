package dao;

import db.DatabaseConnection;
import model.InventoryItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public boolean addOrUpdateStock(String drugName, int quantityToAdd, int reorderLevel) {
        InventoryItem existing = getByName(drugName);
        try (Connection conn = DatabaseConnection.connect()) {
            if (existing == null) {
                String sql = "INSERT INTO inventory (drug_name, quantity, reorder_level) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, drugName);
                    pstmt.setInt(2, quantityToAdd);
                    pstmt.setInt(3, reorderLevel);
                    pstmt.executeUpdate();
                }
            } else {
                String sql = "UPDATE inventory SET quantity = quantity + ?, reorder_level = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, quantityToAdd);
                    pstmt.setInt(2, reorderLevel);
                    pstmt.setInt(3, existing.getId());
                    pstmt.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public InventoryItem getByName(String drugName) {
        String sql = "SELECT * FROM inventory WHERE drug_name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, drugName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InventoryItem> getAllStock() {
        List<InventoryItem> results = new ArrayList<>();
        String sql = "SELECT * FROM inventory ORDER BY drug_name";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean deductStock(String drugName, int amount) {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE drug_name = ? AND quantity >= ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setString(2, drugName);
            pstmt.setInt(3, amount);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private InventoryItem mapRow(ResultSet rs) throws SQLException {
        return new InventoryItem(rs.getInt("id"), rs.getString("drug_name"),
                rs.getInt("quantity"), rs.getInt("reorder_level"));
    }
}
