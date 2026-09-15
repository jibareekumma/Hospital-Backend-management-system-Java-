package ui;

import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import java.awt.*;

public class StaffAccountPanel extends JPanel {

    public StaffAccountPanel(MainFrame mainFrame, int staffId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        StaffDAO staffDAO = new StaffDAO();
        Staff staff = staffDAO.getStaffById(staffId);

        if (staff == null) {
            add(new JLabel("Staff member not found."));
            return;
        }

        JLabel titleLabel = new JLabel("Account Management: " + staff.getFullName());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JLabel statusLabel = new JLabel("Current Status: " + staff.getStatus());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        add(statusLabel, gbc);
        gbc.gridwidth = 1;

        JButton toggleStatusButton = new JButton(staff.getStatus().equals("Active") ? "Deactivate" : "Activate");
        gbc.gridx = 0; gbc.gridy = 2;
        add(toggleStatusButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("New Password:"), gbc);
        JPasswordField newPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(newPasswordField, gbc);

        JButton resetPasswordButton = new JButton("Reset Password");
        gbc.gridx = 0; gbc.gridy = 4;
        add(resetPasswordButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Change Role:"), gbc);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Doctor", "Nurse", "Pharmacist", "Laboratory Technician"});
        roleBox.setSelectedItem(staff.getRole());
        gbc.gridx = 1;
        add(roleBox, gbc);

        JButton updateRoleButton = new JButton("Update Role");
        gbc.gridx = 0; gbc.gridy = 6;
        add(updateRoleButton, gbc);

        JButton backButton = new JButton("Back to Staff List");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(backButton, gbc);

        toggleStatusButton.addActionListener(e -> {
            String newStatus = staff.getStatus().equals("Active") ? "Inactive" : "Active";
            staffDAO.setStatus(staffId, newStatus);
            JOptionPane.showMessageDialog(this, "Status changed to " + newStatus);
            mainFrame.showStaffAccount(staffId);
        });

        resetPasswordButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword());
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a new password first.");
                return;
            }
            staffDAO.resetPassword(staffId, newPassword);
            JOptionPane.showMessageDialog(this, "Password reset.");
        });

        updateRoleButton.addActionListener(e -> {
            String newRole = (String) roleBox.getSelectedItem();
            staffDAO.updateRole(staffId, newRole);
            JOptionPane.showMessageDialog(this, "Role updated to " + newRole);
            mainFrame.showStaffAccount(staffId);
        });

        backButton.addActionListener(e -> mainFrame.showStaffList());
    }
}