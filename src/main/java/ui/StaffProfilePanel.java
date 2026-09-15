package ui;

import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import java.awt.*;

public class StaffProfilePanel extends JPanel {

    public StaffProfilePanel(MainFrame mainFrame, int staffId) {
        setLayout(new BorderLayout(10, 10));
        StaffDAO staffDAO = new StaffDAO();
        Staff staff = staffDAO.getStaffById(staffId);

        if (staff == null) {
            add(new JLabel("Staff member not found."), BorderLayout.NORTH);
            return;
        }

        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        detailsPanel.add(new JLabel("Name: " + staff.getFullName()));
        detailsPanel.add(new JLabel("Role: " + staff.getRole()));
        detailsPanel.add(new JLabel("Department: " + staff.getDepartment()));
        detailsPanel.add(new JLabel("Status: " + staff.getStatus()));
        detailsPanel.add(new JLabel("Phone: " + staff.getPhone()));
        detailsPanel.add(new JLabel("Email: " + staff.getEmail()));
        detailsPanel.add(new JLabel("Username: " + staff.getUsername()));

        JButton backButton = new JButton("Back to Staff List");
        backButton.addActionListener(e -> mainFrame.showStaffList());

        add(detailsPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}