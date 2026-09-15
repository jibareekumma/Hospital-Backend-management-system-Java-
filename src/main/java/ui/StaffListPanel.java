package ui;

import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private StaffDAO staffDAO;

    public StaffListPanel(MainFrame mainFrame) {
        this.staffDAO = new StaffDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(12);
        JButton searchButton = new JButton("Find");
        JButton refreshButton = new JButton("View All");
        JComboBox<String> departmentFilter = new JComboBox<>(new String[]{"All Departments", "Cardiology", "Pediatrics", "Pharmacy", "Laboratory", "General"});
        JButton registerButton = new JButton("Register Staff");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(departmentFilter);
        topPanel.add(registerButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Role", "Department", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewProfileButton = new JButton("View Profile");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton accountButton = new JButton("Manage Account");

        bottomPanel.add(viewProfileButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(accountButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAllStaff();

        refreshButton.addActionListener(e -> loadAllStaff());

        searchButton.addActionListener(e -> {
            String term = searchField.getText().trim();
            populateTable(term.isEmpty() ? staffDAO.getAllStaff() : staffDAO.findStaffByName(term));
        });

        departmentFilter.addActionListener(e -> {
            String selected = (String) departmentFilter.getSelectedItem();
            if ("All Departments".equals(selected)) {
                loadAllStaff();
            } else {
                populateTable(staffDAO.getStaffByDepartment(selected));
            }
        });

        registerButton.addActionListener(e -> mainFrame.showStaffForm(null));

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        viewProfileButton.addActionListener(e -> {
            int id = getSelectedStaffId();
            if (id != -1) mainFrame.showStaffProfile(id);
        });

        updateButton.addActionListener(e -> {
            int id = getSelectedStaffId();
            if (id != -1) mainFrame.showStaffForm(staffDAO.getStaffById(id));
        });

        deleteButton.addActionListener(e -> {
            int id = getSelectedStaffId();
            if (id != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this staff member?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    staffDAO.deleteStaff(id);
                    loadAllStaff();
                }
            }
        });

        accountButton.addActionListener(e -> {
            int id = getSelectedStaffId();
            if (id != -1) mainFrame.showStaffAccount(id);
        });
    }

    private int getSelectedStaffId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a staff member first.");
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    public void loadAllStaff() {
        populateTable(staffDAO.getAllStaff());
    }

    private void populateTable(List<Staff> staffList) {
        tableModel.setRowCount(0);
        for (Staff s : staffList) {
            tableModel.addRow(new Object[]{s.getId(), s.getFullName(), s.getRole(), s.getDepartment(), s.getStatus()});
        }
    }
}