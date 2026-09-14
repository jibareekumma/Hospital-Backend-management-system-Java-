package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(User user) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JLabel hospitalLabel = new JLabel("Jibare Lab", SwingConstants.CENTER);
        hospitalLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("Logged in as: " + user.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLabel roleLabel = new JLabel("Role: " + user.getRole(), SwingConstants.CENTER);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        topPanel.add(hospitalLabel);
        topPanel.add(usernameLabel);
        topPanel.add(roleLabel);

        add(topPanel, BorderLayout.NORTH);
    }
}