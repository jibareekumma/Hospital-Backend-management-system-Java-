package ui;

import dao.LabDAO;
import model.LabTest;

import javax.swing.*;
import java.awt.*;

public class LabResultFormPanel extends JPanel {

    public LabResultFormPanel(MainFrame mainFrame, int testId) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        LabDAO labDAO = new LabDAO();
        LabTest test = labDAO.getTestById(testId);

        if (test == null) {
            add(new JLabel("Test not found."));
            return;
        }

        JLabel titleLabel = new JLabel("Record Result: " + test.getTestName() + " (" + test.getPatientName() + ")");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JTextArea resultArea = new JTextArea(5, 20);
        JScrollPane resultScroll = new JScrollPane(resultArea);
        JTextField dateField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Result:"), gbc);
        gbc.gridx = 1;
        add(resultScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Date Completed (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        JButton saveButton = new JButton("Save Result");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 3;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            if (resultArea.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Result and date are required.");
                return;
            }
            boolean success = labDAO.recordResult(testId, resultArea.getText().trim(), dateField.getText().trim());
            JOptionPane.showMessageDialog(this, success ? "Result recorded." : "Failed to save.");
            mainFrame.showLabList();
        });

        cancelButton.addActionListener(e -> mainFrame.showLabList());
    }
}
