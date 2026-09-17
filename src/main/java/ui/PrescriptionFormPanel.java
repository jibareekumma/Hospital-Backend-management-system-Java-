package ui;

import dao.PharmacyDAO;
import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PrescriptionFormPanel extends JPanel {

    public PrescriptionFormPanel(MainFrame mainFrame, int patientId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Prescription");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        StaffDAO staffDAO = new StaffDAO();
        PharmacyDAO pharmacyDAO = new PharmacyDAO();

        JComboBox<String> doctorBox = new JComboBox<>();
        List<Staff> doctors = staffDAO.getStaffByRole("Doctor");
        for (Staff d : doctors) doctorBox.addItem(d.getFullName());

        JTextField medicationField = new JTextField(15);
        JTextField dosageField = new JTextField(15);
        JTextField dateField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        add(doctorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Medication:"), gbc);
        gbc.gridx = 1;
        add(medicationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1;
        add(dosageField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        JButton saveButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 5;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            if (doctorBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a doctor first.");
                return;
            }
            if (medicationField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Medication and date are required.");
                return;
            }

            boolean success = pharmacyDAO.createPrescription(patientId, (String) doctorBox.getSelectedItem(),
                    medicationField.getText().trim(), dosageField.getText().trim(), dateField.getText().trim());
            JOptionPane.showMessageDialog(this, success ? "Prescription created." : "Failed to create.");
            mainFrame.showPrescriptionList();
        });

        cancelButton.addActionListener(e -> mainFrame.showPrescriptionList());
    }
}
