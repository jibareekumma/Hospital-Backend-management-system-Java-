package ui;

import dao.ClinicalDAO;
import dao.PatientDAO;
import model.ClinicalRecord;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClinicalListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private ClinicalDAO clinicalDAO;
    private JComboBox<String> patientBox;
    private List<Patient> patients;

    public ClinicalListPanel(MainFrame mainFrame) {
        this.clinicalDAO = new ClinicalDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        PatientDAO patientDAO = new PatientDAO();
        patients = patientDAO.getAllPatients();

        patientBox = new JComboBox<>();
        for (Patient p : patients) patientBox.addItem(p.getId() + " - " + p.getFullName());

        JButton loadButton = new JButton("Load Records");
        JButton addButton = new JButton("Add Clinical Record");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Patient:"));
        topPanel.add(patientBox);
        topPanel.add(loadButton);
        topPanel.add(addButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Doctor", "Diagnosis", "Treatment", "Notes", "Date"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateButton = new JButton("Update Selected Record");
        bottomPanel.add(updateButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadRecordsForSelectedPatient());

        addButton.addActionListener(e -> {
            int patientId = getSelectedPatientId();
            if (patientId != -1) mainFrame.showClinicalForm(patientId, null);
        });

        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a record first.");
                return;
            }
            int recordId = (int) tableModel.getValueAt(row, 0);
            ClinicalRecord record = clinicalDAO.getRecordById(recordId);
            mainFrame.showClinicalForm(record.getPatientId(), record);
        });

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        if (!patients.isEmpty()) {
            loadRecordsForSelectedPatient();
        }
    }

    private int getSelectedPatientId() {
        if (patientBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Register a patient first.");
            return -1;
        }
        return Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
    }

    private void loadRecordsForSelectedPatient() {
        int patientId = getSelectedPatientId();
        if (patientId == -1) return;
        populateTable(clinicalDAO.getRecordsByPatient(patientId));
    }

    private void populateTable(List<ClinicalRecord> records) {
        tableModel.setRowCount(0);
        for (ClinicalRecord r : records) {
            tableModel.addRow(new Object[]{r.getId(), r.getDoctorName(), r.getDiagnosis(),
                    r.getTreatment(), r.getNotes(), r.getRecordDate()});
        }
    }
}