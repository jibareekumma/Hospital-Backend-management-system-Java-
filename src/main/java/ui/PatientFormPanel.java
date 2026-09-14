package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import java.awt.*;

public class PatientFormPanel extends JPanel {

    public PatientFormPanel(MainFrame mainFrame, Patient existingPatient) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        boolean isEdit = existingPatient != null;

        JLabel titleLabel = new JLabel(isEdit ? "Update Patient" : "Register Patient");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField dobField = new JTextField(15);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField bloodGroupField = new JTextField(15);

        String[] labels = {"First Name:", "Last Name:", "Date of Birth (YYYY-MM-DD):", "Gender:", "Phone:", "Email:", "Address:", "Blood Group:"};
        JComponent[] fields = {firstNameField, lastNameField, dobField, genderBox, phoneField, emailField, addressField, bloodGroupField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            add(fields[i], gbc);
        }

        if (isEdit) {
            firstNameField.setText(existingPatient.getFirstName());
            lastNameField.setText(existingPatient.getLastName());
            dobField.setText(existingPatient.getDateOfBirth());
            genderBox.setSelectedItem(existingPatient.getGender());
            phoneField.setText(existingPatient.getPhone());
            emailField.setText(existingPatient.getEmail());
            addressField.setText(existingPatient.getAddress());
            bloodGroupField.setText(existingPatient.getBloodGroup());
        }

        JButton saveButton = new JButton(isEdit ? "Update" : "Register");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = labels.length + 1; gbc.gridwidth = 1;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        PatientDAO patientDAO = new PatientDAO();

        saveButton.addActionListener(e -> {
            if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First and last name are required.");
                return;
            }

            Patient patient = new Patient(
                    isEdit ? existingPatient.getId() : 0,
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    dobField.getText().trim(),
                    (String) genderBox.getSelectedItem(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    addressField.getText().trim(),
                    bloodGroupField.getText().trim()
            );

            boolean success = isEdit ? patientDAO.updatePatient(patient) : patientDAO.registerPatient(patient);

            if (success) {
                JOptionPane.showMessageDialog(this, isEdit ? "Patient updated." : "Patient registered.");
                mainFrame.showPatientList();
            } else {
                JOptionPane.showMessageDialog(this, "Something went wrong.");
            }
        });

        cancelButton.addActionListener(e -> mainFrame.showPatientList());
    }
}