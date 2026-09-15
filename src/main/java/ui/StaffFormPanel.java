package ui;

import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import java.awt.*;

public class StaffFormPanel extends JPanel {

    public StaffFormPanel(MainFrame mainFrame, Staff existingStaff) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        boolean isEdit = existingStaff != null;

        JLabel titleLabel = new JLabel(isEdit ? "Update Staff" : "Register Staff");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Laboratory Technician"});
        JComboBox<String> departmentBox = new JComboBox<>(new String[]{"Cardiology", "Pediatrics", "Pharmacy", "Laboratory", "General"});
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        String[] labels = {"First Name:", "Last Name:", "Role:", "Department:", "Phone:", "Email:", "Username:"};
        JComponent[] fields = {firstNameField, lastNameField, roleBox, departmentBox, phoneField, emailField, usernameField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            add(fields[i], gbc);
        }

        if (!isEdit) {
            gbc.gridx = 0; gbc.gridy = labels.length + 1;
            add(new JLabel("Initial Password:"), gbc);
            gbc.gridx = 1;
            add(passwordField, gbc);
        } else {
            firstNameField.setText(existingStaff.getFirstName());
            lastNameField.setText(existingStaff.getLastName());
            roleBox.setSelectedItem(existingStaff.getRole());
            departmentBox.setSelectedItem(existingStaff.getDepartment());
            phoneField.setText(existingStaff.getPhone());
            emailField.setText(existingStaff.getEmail());
            usernameField.setText(existingStaff.getUsername());
            usernameField.setEditable(false);
        }

        JButton saveButton = new JButton(isEdit ? "Update" : "Register");
        JButton cancelButton = new JButton("Cancel");

        int buttonRow = isEdit ? labels.length + 1 : labels.length + 2;
        gbc.gridx = 0; gbc.gridy = buttonRow;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        StaffDAO staffDAO = new StaffDAO();

        saveButton.addActionListener(e -> {
            if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First and last name are required.");
                return;
            }

            if (isEdit) {
                Staff staff = new Staff(
                        existingStaff.getId(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        (String) roleBox.getSelectedItem(),
                        (String) departmentBox.getSelectedItem(),
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        existingStaff.getUsername(),
                        existingStaff.getStatus()
                );
                boolean success = staffDAO.updateStaff(staff);
                JOptionPane.showMessageDialog(this, success ? "Staff updated." : "Update failed.");
            } else {
                if (usernameField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(this, "Username and password are required for a new staff account.");
                    return;
                }
                Staff staff = new Staff(
                        0,
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        (String) roleBox.getSelectedItem(),
                        (String) departmentBox.getSelectedItem(),
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        usernameField.getText().trim(),
                        "Active"
                );
                boolean success = staffDAO.registerStaff(staff, new String(passwordField.getPassword()));
                JOptionPane.showMessageDialog(this, success ? "Staff registered." : "Registration failed.");
            }

            mainFrame.showStaffList();
        });

        cancelButton.addActionListener(e -> mainFrame.showStaffList());
    }
}