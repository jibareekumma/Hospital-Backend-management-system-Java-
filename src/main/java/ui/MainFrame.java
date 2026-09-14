package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    public MainFrame() {
        setTitle("Hospital Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(new LoginPanel(this), "login");
        container.add(new SignupPanel(this), "signup");

        add(container);

        cardLayout.show(container, "login");
    }

    public void showLogin() {
        cardLayout.show(container, "login");
    }

    public void showSignup() {
        cardLayout.show(container, "signup");
    }

    public void showDashboard(User user) {
        DashboardPanel dashboardPanel = new DashboardPanel(user);
        container.add(dashboardPanel, "dashboard");
        cardLayout.show(container, "dashboard");
    }
}