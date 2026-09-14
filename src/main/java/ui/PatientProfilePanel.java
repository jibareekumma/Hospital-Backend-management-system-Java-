package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientProfilePanel extends JPanel {

    public PatientProfilePanel(MainFrame mainFrame, int patientId) {
        setLayout(new BorderLayout(10, 10));
        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById(patientId);

        if (patient == null) {
            add(new JLabel("Patient not found."), BorderLayout.NORTH);
            return;
        }

        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.add(new JLabel("Name: " + patient.getFullName()));
        detailsPanel.add(new JLabel("Gender: " + patient.getGender()));
        detailsPanel.add(new JLabel("DOB: " + patient.getDateOfBirth()));
        detailsPanel.add(new JLabel("Blood Group: " + patient.getBloodGroup()));
        detailsPanel.add(new JLabel("Phone: " + patient.getPhone()));
        detailsPanel.add(new JLabel("Email: " + patient.getEmail()));
        detailsPanel.add(new JLabel("Address: " + patient.getAddress()));

        JButton backButton = new JButton("Back to Patient List");
        backButton.addActionListener(e -> mainFrame.showPatientList());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(detailsPanel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Appointments", buildTable(
                new String[]{"Doctor", "Date", "Status"},
                patientDAO.getAppointmentsByPatient(patientId)));
        tabs.addTab("Prescriptions", buildTable(
                new String[]{"Medication", "Dosage", "Date Prescribed"},
                patientDAO.getPrescriptionsByPatient(patientId)));
        tabs.addTab("Medical History", buildTable(
                new String[]{"Diagnosis", "Treatment", "Date"},
                patientDAO.getHistoryByPatient(patientId)));

        add(tabs, BorderLayout.CENTER);
    }

    private JScrollPane buildTable(String[] columns, List<String[]> rows) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (String[] row : rows) {
            model.addRow(row);
        }
        return new JScrollPane(new JTable(model));
    }
}