package ui;

import dao.AdmissionDAO;
import model.Admission;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BedAdmissionListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private AdmissionDAO admissionDAO;

    public BedAdmissionListPanel(MainFrame mainFrame) {
        this.admissionDAO = new AdmissionDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton admitButton = new JButton("Admit Patient");
        JButton manageBedsButton = new JButton("Manage Beds");
        JButton showAllButton = new JButton("View All History");
        JButton showActiveButton = new JButton("View Active Admissions Only");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(admitButton);
        topPanel.add(manageBedsButton);
        topPanel.add(showAllButton);
        topPanel.add(showActiveButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Bed", "Ward", "Admitted", "Discharged", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField dischargeDateField = new JTextField(12);
        JButton dischargeButton = new JButton("Discharge Selected");
        bottomPanel.add(new JLabel("Discharge Date (YYYY-MM-DD):"));
        bottomPanel.add(dischargeDateField);
        bottomPanel.add(dischargeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAll();

        admitButton.addActionListener(e -> mainFrame.showAdmissionForm());
        manageBedsButton.addActionListener(e -> mainFrame.showBedPanel());
        showAllButton.addActionListener(e -> loadAll());
        showActiveButton.addActionListener(e -> populateTable(admissionDAO.getActiveAdmissions()));
        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        dischargeButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an admission first.");
                return;
            }
            String date = dischargeDateField.getText().trim();
            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a discharge date.");
                return;
            }
            int admissionId = (int) tableModel.getValueAt(row, 0);
            admissionDAO.dischargePatient(admissionId, date);
            JOptionPane.showMessageDialog(this, "Patient discharged. Bed is now available.");
            loadAll();
        });
    }

    private void loadAll() {
        populateTable(admissionDAO.getAllAdmissions());
    }

    private void populateTable(List<Admission> admissions) {
        tableModel.setRowCount(0);
        for (Admission a : admissions) {
            tableModel.addRow(new Object[]{a.getId(), a.getPatientName(), a.getBedNumber(), a.getWard(),
                    a.getAdmissionDate(), a.getDischargeDate(), a.getStatus()});
        }
    }
}