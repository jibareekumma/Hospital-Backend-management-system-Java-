package ui;

import dao.InventoryDAO;
import model.InventoryItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {

    private DefaultTableModel tableModel;
    private InventoryDAO inventoryDAO;

    public InventoryPanel(MainFrame mainFrame) {
        this.inventoryDAO = new InventoryDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField drugNameField = new JTextField(12);
        JTextField quantityField = new JTextField(6);
        JTextField reorderField = new JTextField(6);
        JButton addStockButton = new JButton("Add/Update Stock");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Drug Name:"));
        topPanel.add(drugNameField);
        topPanel.add(new JLabel("Quantity to Add:"));
        topPanel.add(quantityField);
        topPanel.add(new JLabel("Reorder Level:"));
        topPanel.add(reorderField);
        topPanel.add(addStockButton);
        topPanel.add(refreshButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Drug", "Quantity", "Reorder Level", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadAll();

        addStockButton.addActionListener(e -> {
            String drugName = drugNameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String reorderText = reorderField.getText().trim();

            if (drugName.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Drug name and quantity are required.");
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityText);
                int reorderLevel = reorderText.isEmpty() ? 10 : Integer.parseInt(reorderText);
                inventoryDAO.addOrUpdateStock(drugName, quantity, reorderLevel);
                drugNameField.setText("");
                quantityField.setText("");
                reorderField.setText("");
                loadAll();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity and reorder level must be numbers.");
            }
        });

        refreshButton.addActionListener(e -> loadAll());
        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }

    private void loadAll() {
        tableModel.setRowCount(0);
        List<InventoryItem> items = inventoryDAO.getAllStock();
        for (InventoryItem i : items) {
            String status = i.isLowStock() ? "LOW STOCK" : "OK";
            tableModel.addRow(new Object[]{i.getId(), i.getDrugName(), i.getQuantity(), i.getReorderLevel(), status});
        }
    }
}
