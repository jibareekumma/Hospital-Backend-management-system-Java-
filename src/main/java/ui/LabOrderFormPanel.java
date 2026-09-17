package ui;

import dao.LabDAO;
import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LabOrderFormPanel extends JPanel {

    public LabOrderFormPanel(MainFrame mainFrame, int patientId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Order Lab Test");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        StaffDAO staffDAO = new StaffDAO();
        LabDAO labDAO = new LabDAO();

        JComboBox<String> doctorBox = new JComboBox<>();
        List<Staff> doctors = staffDAO.getStaffByRole("Doctor");
        for (Staff d : doctors) doctorBox.addItem(d.getFullName());

        JTextField testNameField = new JTextField(15);
        JTextField dateField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        add(doctorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Test Name:"), gbc);
        gbc.gridx = 1;
        add(testNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Date Ordered (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        JButton saveButton = new JButton("Order Test");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 4;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            if (doctorBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a doctor first.");
                return;
            }
            if (testNameField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Test name and date are required.");
                return;
            }

            boolean success = labDAO.orderTest(patientId, (String) doctorBox.getSelectedItem(),
                    testNameField.getText().trim(), dateField.getText().trim());
            JOptionPane.showMessageDialog(this, success ? "Test ordered." : "Failed to order test.");
            mainFrame.showLabList();
        });

        cancelButton.addActionListener(e -> mainFrame.showLabList());
    }
}
