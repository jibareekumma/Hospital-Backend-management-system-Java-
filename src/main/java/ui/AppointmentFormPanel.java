package ui;

import dao.AppointmentDAO;
import dao.PatientDAO;
import dao.StaffDAO;
import model.Appointment;
import model.Patient;
import model.Staff;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AppointmentFormPanel extends JPanel {

    public AppointmentFormPanel(MainFrame mainFrame, Appointment existingAppointment) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        boolean isReschedule = existingAppointment != null;

        JLabel titleLabel = new JLabel(isReschedule ? "Reschedule Appointment" : "Book Appointment");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        PatientDAO patientDAO = new PatientDAO();
        StaffDAO staffDAO = new StaffDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        List<Patient> patients = patientDAO.getAllPatients();
        List<Staff> doctors = staffDAO.getStaffByRole("Doctor");

        JComboBox<String> patientBox = new JComboBox<>();
        for (Patient p : patients) patientBox.addItem(p.getId() + " - " + p.getFullName());

        JComboBox<String> doctorBox = new JComboBox<>();
        for (Staff d : doctors) doctorBox.addItem(d.getId() + " - " + d.getFullName());

        JTextField dateField = new JTextField(15);
        JTextField timeField = new JTextField(15);
        JTextField reasonField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        add(patientBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        add(doctorBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Time (e.g. 10:30 AM):"), gbc);
        gbc.gridx = 1;
        add(timeField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        add(reasonField, gbc);

        if (isReschedule) {
            for (int i = 0; i < patientBox.getItemCount(); i++) {
                if (patientBox.getItemAt(i).startsWith(existingAppointment.getPatientId() + " - ")) {
                    patientBox.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < doctorBox.getItemCount(); i++) {
                if (doctorBox.getItemAt(i).startsWith(existingAppointment.getDoctorId() + " - ")) {
                    doctorBox.setSelectedIndex(i);
                    break;
                }
            }
            patientBox.setEnabled(false);
            doctorBox.setEnabled(false);
            dateField.setText(existingAppointment.getAppointmentDate());
            timeField.setText(existingAppointment.getAppointmentTime());
            reasonField.setText(existingAppointment.getReason());
        }

        JButton saveButton = new JButton(isReschedule ? "Save Changes" : "Book");
        JButton cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 6;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String reason = reasonField.getText().trim();

            if (date.isEmpty() || time.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Date and time are required.");
                return;
            }

            if (isReschedule) {
                boolean success = appointmentDAO.rescheduleAppointment(existingAppointment.getId(), date, time, reason);
                JOptionPane.showMessageDialog(this, success ? "Appointment rescheduled." : "Failed to reschedule.");
            } else {
                if (patientBox.getSelectedItem() == null || doctorBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Register a patient and a doctor first.");
                    return;
                }
                String selectedPatient = (String) patientBox.getSelectedItem();
                String selectedDoctor = (String) doctorBox.getSelectedItem();
                int patientId = Integer.parseInt(selectedPatient.split(" - ")[0]);
                int doctorId = Integer.parseInt(selectedDoctor.split(" - ")[0]);
                String doctorName = selectedDoctor.split(" - ")[1];

                boolean success = appointmentDAO.bookAppointment(patientId, doctorId, doctorName, date, time, reason);
                JOptionPane.showMessageDialog(this, success ? "Appointment booked." : "Booking failed.");
            }

            mainFrame.showAppointmentList();
        });

        cancelButton.addActionListener(e -> mainFrame.showAppointmentList());
    }
}
