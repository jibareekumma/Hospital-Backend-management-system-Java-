package ui;

import dao.AdmissionDAO;
import dao.BedDAO;
import dao.PatientDAO;
import model.Bed;
import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BedAdmissionFormPanel extends JPanel {

    public BedAdmissionFormPanel(MainFrame mainFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Admit Patient");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        PatientDAO patientDAO = new PatientDAO();
        BedDAO bedDAO = new BedDAO();
        AdmissionDAO admissionDAO = new AdmissionDAO();

        List<Patient> patients = patientDAO.getAllPatients();
        List<Bed> availableBeds = bedDAO.getAvailableBeds();

        JComboBox<String> patientBox = new JComboBox<>();
        for (Patient p : patients) patientBox.addItem(p.getId() + " - " + p.getFullName());

        JComboBox<String> bedBox = new JComboBox<>();
        for (Bed b : availableBeds) bedBox.addItem(b.getId() + " - Bed " + b.getBedNumber() + " (" + b.getWard() + ")");

        JTextField dateField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        add(patientBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Available Bed/Ward:"), gbc);
        gbc.gridx = 1;
        add(bedBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Admission Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        JButton admitButton = new JButton("Admit");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 4;
        add(admitButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        admitButton.addActionListener(e -> {
            if (patientBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Register a patient first.");
                return;
            }
            if (bedBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No available beds. Add a bed first.");
                return;
            }
            if (dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Admission date is required.");
                return;
            }

            int patientId = Integer.parseInt(((String) patientBox.getSelectedItem()).split(" - ")[0]);
            int bedId = Integer.parseInt(((String) bedBox.getSelectedItem()).split(" - ")[0]);

            boolean success = admissionDAO.admitPatient(patientId, bedId, dateField.getText().trim());
            JOptionPane.showMessageDialog(this, success ? "Patient admitted." : "Admission failed.");
            mainFrame.showAdmissionList();
        });

        cancelButton.addActionListener(e -> mainFrame.showAdmissionList());
    }
}
