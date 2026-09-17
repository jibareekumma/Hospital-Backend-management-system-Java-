package ui;

import dao.PatientDAO;
import dao.PharmacyDAO;
import model.Patient;
import model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrescriptionListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private PharmacyDAO pharmacyDAO;
    private JComboBox<String> patientBox;

    public PrescriptionListPanel(MainFrame mainFrame) {
        this.pharmacyDAO = new PharmacyDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PatientDAO patientDAO = new PatientDAO();
        List<Patient> patients = patientDAO.getAllPatients();

        patientBox = new JComboBox<>();
        for (Patient p : patients) patientBox.addItem(p.getId() + " - " + p.getFullName());

        JButton loadByPatientButton = new JButton("Load Patient History");
        JButton viewAllButton = new JButton("View All Prescriptions");
        JButton createButton = new JButton("Create Prescription");
        JButton inventoryButton = new JButton("Manage Inventory");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Patient:"));
        topPanel.add(patientBox);
        topPanel.add(loadByPatientButton);
        topPanel.add(viewAllButton);
        topPanel.add(createButton);
        topPanel.add(inventoryButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Medication", "Dosage", "Status", "Date"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton dispenseButton = new JButton("Dispense Selected");
        bottomPanel.add(dispenseButton);
        add(bottomPanel, BorderLayout.SOUTH);

        populateTable(pharmacyDAO.getAllPrescriptions());

        viewAllButton.addActionListener(e -> populateTable(pharmacyDAO.getAllPrescriptions()));

        loadByPatientButton.addActionListener(e -> {
            if (patientBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a patient first.");
                return;
            }
            int patientId = Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
            populateTable(pharmacyDAO.getPrescriptionsByPatient(patientId));
        });

        createButton.addActionListener(e -> {
            if (patientBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a patient first.");
                return;
            }
            int patientId = Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
            mainFrame.showPrescriptionForm(patientId);
        });

        inventoryButton.addActionListener(e -> mainFrame.showInventoryPanel());

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        dispenseButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a prescription first.");
                return;
            }
            int prescriptionId = (int) tableModel.getValueAt(row, 0);
            Prescription prescription = pharmacyDAO.getPrescriptionById(prescriptionId);

            if ("Dispensed".equals(prescription.getStatus())) {
                JOptionPane.showMessageDialog(this, "Already dispensed.");
                return;
            }

            dao.InventoryDAO inventoryDAO = new dao.InventoryDAO();
            boolean stockAvailable = inventoryDAO.deductStock(prescription.getMedication(), 1);

            pharmacyDAO.markDispensed(prescriptionId);

            model.InventoryItem item = inventoryDAO.getByName(prescription.getMedication());
            String message = stockAvailable ? "Medication dispensed." : "Dispensed, but inventory has no matching stock record for this drug.";
            if (item != null && item.isLowStock()) {
                message += "\nLow stock warning: " + item.getDrugName() + " is at " + item.getQuantity() + " units.";
            }
            JOptionPane.showMessageDialog(this, message);

            populateTable(pharmacyDAO.getAllPrescriptions());
        });
    }

    private void populateTable(List<Prescription> prescriptions) {
        tableModel.setRowCount(0);
        for (Prescription p : prescriptions) {
            tableModel.addRow(new Object[]{p.getId(), p.getPatientName(), p.getDoctorName(),
                    p.getMedication(), p.getDosage(), p.getStatus(), p.getDatePrescribed()});
        }
    }
}
