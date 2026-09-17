package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class UserAccountPanel extends JPanel {

    public UserAccountPanel(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        UserDAO userDAO = new UserDAO();
        User currentUser = mainFrame.getCurrentUser();

        JLabel titleLabel = new JLabel("My Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JTextField usernameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        usernameField.setText(currentUser.getUsername());
        emailField.setText(currentUser.getEmail());

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        JButton saveProfileButton = new JButton("Save Profile");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(saveProfileButton, gbc);
        gbc.gridwidth = 1;

        JLabel passwordTitle = new JLabel("Change Password");
        passwordTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(passwordTitle, gbc);
        gbc.gridwidth = 1;

        JPasswordField currentPasswordField = new JPasswordField(15);
        JPasswordField newPasswordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        add(newPasswordField, gbc);

        JButton changePasswordButton = new JButton("Change Password");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        add(changePasswordButton, gbc);
        gbc.gridwidth = 1;

        JButton backButton = new JButton("Back to Dashboard");
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        add(backButton, gbc);

        saveProfileButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            if (username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and email are required.");
                return;
            }
            boolean success = userDAO.updateProfile(currentUser.getId(), username, email);
            JOptionPane.showMessageDialog(this, success ? "Profile updated." : "Update failed.");
        });

        changePasswordButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both password fields are required.");
                return;
            }
            boolean success = userDAO.changePassword(currentUser.getId(), currentPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Password changed.");
                currentPasswordField.setText("");
                newPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.");
            }
        });

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }
}
