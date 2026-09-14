package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private User currentUser;

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
        this.currentUser = user;
        DashboardPanel dashboardPanel = new DashboardPanel(this, user);
        container.add(dashboardPanel, "dashboard");
        cardLayout.show(container, "dashboard");
    }

    public void showPatientList() {
        PatientListPanel patientListPanel = new PatientListPanel(this);
        container.add(patientListPanel, "patientList");
        cardLayout.show(container, "patientList");
    }

    public void showPatientForm(model.Patient patient) {
        PatientFormPanel patientFormPanel = new PatientFormPanel(this, patient);
        container.add(patientFormPanel, "patientForm");
        cardLayout.show(container, "patientForm");
    }

    public void showPatientProfile(int patientId) {
        PatientProfilePanel patientProfilePanel = new PatientProfilePanel(this, patientId);
        container.add(patientProfilePanel, "patientProfile");
        cardLayout.show(container, "patientProfile");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}