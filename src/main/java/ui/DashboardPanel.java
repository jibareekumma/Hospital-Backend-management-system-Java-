package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(MainFrame mainFrame, User user) {
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

        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton patientsButton = new JButton("Manage Patients");
        menuPanel.add(patientsButton);

        add(menuPanel, BorderLayout.CENTER);

        patientsButton.addActionListener(e -> mainFrame.showPatientList());

        JButton staffButton = new JButton("Manage Staff");
        menuPanel.add(staffButton);
        staffButton.addActionListener(e -> mainFrame.showStaffList());

        JButton appointmentsButton = new JButton("Manage Appointments");
        menuPanel.add(appointmentsButton);
        appointmentsButton.addActionListener(e -> mainFrame.showAppointmentList());

        JButton admissionsButton = new JButton("Admission & Bedding");
        menuPanel.add(admissionsButton);
        admissionsButton.addActionListener(e -> mainFrame.showAdmissionList());

        JButton clinicalButton = new JButton("Clinical Management");
menuPanel.add(clinicalButton);
clinicalButton.addActionListener(e -> mainFrame.showClinicalList());


JButton labButton = new JButton("Laboratory Services");
menuPanel.add(labButton);
labButton.addActionListener(e -> mainFrame.showLabList());

JButton pharmacyButton = new JButton("Pharmacy Services");
menuPanel.add(pharmacyButton);
pharmacyButton.addActionListener(e -> mainFrame.showPrescriptionList());
    }
}
