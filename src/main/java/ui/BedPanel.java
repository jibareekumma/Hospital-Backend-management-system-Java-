package ui;

import dao.BedDAO;
import model.Bed;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BedPanel extends JPanel {

    private DefaultTableModel tableModel;
    private BedDAO bedDAO;

    public BedPanel(MainFrame mainFrame) {
        this.bedDAO = new BedDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField bedNumberField = new JTextField(8);
        JTextField wardField = new JTextField(10);
        JButton addBedButton = new JButton("Add Bed");
        JButton showAllButton = new JButton("Show All");
        JButton showAvailableButton = new JButton("Show Available Only");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Bed Number:"));
        topPanel.add(bedNumberField);
        topPanel.add(new JLabel("Ward:"));
        topPanel.add(wardField);
        topPanel.add(addBedButton);
        topPanel.add(showAllButton);
        topPanel.add(showAvailableButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Bed Number", "Ward", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadAll();

        addBedButton.addActionListener(e -> {
            String bedNumber = bedNumberField.getText().trim();
            String ward = wardField.getText().trim();
            if (bedNumber.isEmpty() || ward.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bed number and ward are required.");
                return;
            }
            bedDAO.addBed(bedNumber, ward);
            bedNumberField.setText("");
            wardField.setText("");
            loadAll();
        });

        showAllButton.addActionListener(e -> loadAll());
        showAvailableButton.addActionListener(e -> populateTable(bedDAO.getAvailableBeds()));
        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }

    private void loadAll() {
        populateTable(bedDAO.getAllBeds());
    }

    private void populateTable(List<Bed> beds) {
        tableModel.setRowCount(0);
        for (Bed b : beds) {
            tableModel.addRow(new Object[]{b.getId(), b.getBedNumber(), b.getWard(), b.getStatus()});
        }
    }
}