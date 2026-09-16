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

    public void showStaffList() {
    StaffListPanel staffListPanel = new StaffListPanel(this);
    container.add(staffListPanel, "staffList");
    cardLayout.show(container, "staffList");
}

public void showStaffForm(model.Staff staff) {
    StaffFormPanel staffFormPanel = new StaffFormPanel(this, staff);
    container.add(staffFormPanel, "staffForm");
    cardLayout.show(container, "staffForm");
}

public void showStaffProfile(int staffId) {
    StaffProfilePanel staffProfilePanel = new StaffProfilePanel(this, staffId);
    container.add(staffProfilePanel, "staffProfile");
    cardLayout.show(container, "staffProfile");
}

public void showStaffAccount(int staffId) {
    StaffAccountPanel staffAccountPanel = new StaffAccountPanel(this, staffId);
    container.add(staffAccountPanel, "staffAccount");
    cardLayout.show(container, "staffAccount");
}

public void showAppointmentList() {
    AppointmentListPanel appointmentListPanel = new AppointmentListPanel(this);
    container.add(appointmentListPanel, "appointmentList");
    cardLayout.show(container, "appointmentList");
}

public void showAppointmentForm(model.Appointment appointment) {
    AppointmentFormPanel appointmentFormPanel = new AppointmentFormPanel(this, appointment);
    container.add(appointmentFormPanel, "appointmentForm");
    cardLayout.show(container, "appointmentForm");
}
}