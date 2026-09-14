package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {

    public SignupPanel(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JTextField usernameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Doctor", "Admin"});

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        add(roleBox, gbc);

        JButton signupButton = new JButton("Sign Up");
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(signupButton, gbc);

        JButton goToLoginButton = new JButton("Already have an account? Login");
        gbc.gridy = 7;
        add(goToLoginButton, gbc);

        UserDAO userDAO = new UserDAO();

        signupButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            if (userDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken.");
                return;
            }

            boolean success = userDAO.registerUser(username, email, password, role);
            if (success) {
                JOptionPane.showMessageDialog(this, "Account created. Please login.");
                mainFrame.showLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed. Try again.");
            }
        });

        goToLoginButton.addActionListener(e -> mainFrame.showLogin());
    }
}