package ui;

import dao.LabDAO;
import dao.PatientDAO;
import model.LabTest;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LabListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private LabDAO labDAO;
    private JComboBox<String> patientBox;

    public LabListPanel(MainFrame mainFrame) {
        this.labDAO = new LabDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PatientDAO patientDAO = new PatientDAO();
        List<Patient> patients = patientDAO.getAllPatients();

        patientBox = new JComboBox<>();
        for (Patient p : patients) patientBox.addItem(p.getId() + " - " + p.getFullName());

        JButton loadByPatientButton = new JButton("Load Patient History");
        JButton viewAllButton = new JButton("View All Tests");
        JButton viewPendingButton = new JButton("View Pending Tests");
        JButton orderButton = new JButton("Order Lab Test");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Patient:"));
        topPanel.add(patientBox);
        topPanel.add(loadByPatientButton);
        topPanel.add(viewAllButton);
        topPanel.add(viewPendingButton);
        topPanel.add(orderButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Test", "Status", "Result", "Ordered", "Completed"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton recordResultButton = new JButton("Record Result");
        bottomPanel.add(recordResultButton);
        add(bottomPanel, BorderLayout.SOUTH);

        populateTable(labDAO.getAllTests());

        viewAllButton.addActionListener(e -> populateTable(labDAO.getAllTests()));
        viewPendingButton.addActionListener(e -> populateTable(labDAO.getPendingTests()));

        loadByPatientButton.addActionListener(e -> {
            if (patientBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a patient first.");
                return;
            }
            int patientId = Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
            populateTable(labDAO.getTestsByPatient(patientId));
        });

        orderButton.addActionListener(e -> {
            if (patientBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a patient first.");
                return;
            }
            int patientId = Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
            mainFrame.showLabOrderForm(patientId);
        });

        recordResultButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a test first.");
                return;
            }
            int testId = (int) tableModel.getValueAt(row, 0);
            mainFrame.showLabResultForm(testId);
        });

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }

    private void populateTable(List<LabTest> tests) {
        tableModel.setRowCount(0);
        for (LabTest t : tests) {
            tableModel.addRow(new Object[]{t.getId(), t.getPatientName(), t.getDoctorName(), t.getTestName(),
                    t.getStatus(), t.getResult(), t.getDateOrdered(), t.getDateCompleted()});
        }
    }
}
