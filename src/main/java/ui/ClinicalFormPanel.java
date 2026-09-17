package ui;

import dao.ClinicalDAO;
import dao.StaffDAO;
import model.ClinicalRecord;
import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClinicalFormPanel extends JPanel {

    public ClinicalFormPanel(MainFrame mainFrame, int patientId, ClinicalRecord existingRecord) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        boolean isEdit = existingRecord != null;

        JLabel titleLabel = new JLabel(isEdit ? "Update Clinical Record" : "Add Clinical Record");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        StaffDAO staffDAO = new StaffDAO();
        ClinicalDAO clinicalDAO = new ClinicalDAO();

        JComboBox<String> doctorBox = new JComboBox<>();
        List<Staff> doctors = staffDAO.getStaffByRole("Doctor");
        for (Staff d : doctors) doctorBox.addItem(d.getFullName());

        JTextField diagnosisField = new JTextField(20);
        JTextField treatmentField = new JTextField(20);
        JTextArea notesArea = new JTextArea(4, 20);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        JTextField dateField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        add(doctorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Diagnosis:"), gbc);
        gbc.gridx = 1;
        add(diagnosisField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Treatment Plan:"), gbc);
        gbc.gridx = 1;
        add(treatmentField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Notes/Observations:"), gbc);
        gbc.gridx = 1;
        add(notesScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        if (isEdit) {
            doctorBox.setSelectedItem(existingRecord.getDoctorName());
            doctorBox.setEnabled(false);
            diagnosisField.setText(existingRecord.getDiagnosis());
            treatmentField.setText(existingRecord.getTreatment());
            notesArea.setText(existingRecord.getNotes());
            dateField.setText(existingRecord.getRecordDate());
            dateField.setEditable(false);
        }

        JButton saveButton = new JButton(isEdit ? "Save Changes" : "Add Record");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 6;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            String diagnosis = diagnosisField.getText().trim();
            String treatment = treatmentField.getText().trim();
            String notes = notesArea.getText().trim();

            if (diagnosis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Diagnosis is required.");
                return;
            }

            boolean success;
            if (isEdit) {
                success = clinicalDAO.updateRecord(existingRecord.getId(), diagnosis, treatment, notes);
            } else {
                if (doctorBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Register a doctor first.");
                    return;
                }
                if (dateField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Date is required.");
                    return;
                }
                success = clinicalDAO.addRecord(patientId, (String) doctorBox.getSelectedItem(),
                        diagnosis, treatment, notes, dateField.getText().trim());
            }

            JOptionPane.showMessageDialog(this, success ? "Saved." : "Something went wrong.");
            mainFrame.showClinicalList();
        });

        cancelButton.addActionListener(e -> mainFrame.showClinicalList());
    }
}