package ui;

import dao.AppointmentDAO;
import dao.PatientDAO;
import dao.StaffDAO;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Hospital Administration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        PatientDAO patientDAO = new PatientDAO();
        StaffDAO staffDAO = new StaffDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        int totalPatients = patientDAO.getAllPatients().size();
        int totalStaff = staffDAO.getAllStaff().size();
        int totalAppointments = appointmentDAO.getAllAppointments().size();
        long activeAppointments = appointmentDAO.getAppointmentsByStatus("Pending").size();

        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 10, 15));
        statsPanel.add(makeStatLabel("Total Patients: " + totalPatients));
        statsPanel.add(makeStatLabel("Total Staff: " + totalStaff));
        statsPanel.add(makeStatLabel("Total Appointments: " + totalAppointments));
        statsPanel.add(makeStatLabel("Pending Appointments: " + activeAppointments));
        statsPanel.add(makeStatLabel("Total Revenue: Not available (Billing module not yet implemented)"));

        add(statsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton departmentsButton = new JButton("Manage Departments");
        JButton backButton = new JButton("Back to Dashboard");
        bottomPanel.add(departmentsButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        departmentsButton.addActionListener(e -> mainFrame.showDepartmentPanel());
        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }

    private JLabel makeStatLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }
}
