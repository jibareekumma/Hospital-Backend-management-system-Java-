package ui;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private AppointmentDAO appointmentDAO;

    public AppointmentListPanel(MainFrame mainFrame) {
        this.appointmentDAO = new AppointmentDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(12);
        JButton searchButton = new JButton("Find");
        JButton refreshButton = new JButton("View All");
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Completed", "Cancelled"});
        JButton bookButton = new JButton("Book Appointment");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Search (patient/doctor/date):"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(statusFilter);
        topPanel.add(bookButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Date", "Time", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton rescheduleButton = new JButton("Reschedule");
        JButton completeButton = new JButton("Mark Completed");
        JButton cancelButton = new JButton("Cancel Appointment");

        bottomPanel.add(rescheduleButton);
        bottomPanel.add(completeButton);
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAll();

        refreshButton.addActionListener(e -> loadAll());

        searchButton.addActionListener(e -> {
            String term = searchField.getText().trim();
            populateTable(term.isEmpty() ? appointmentDAO.getAllAppointments() : appointmentDAO.findAppointments(term));
        });

        statusFilter.addActionListener(e -> {
            String selected = (String) statusFilter.getSelectedItem();
            populateTable("All".equals(selected) ? appointmentDAO.getAllAppointments() : appointmentDAO.getAppointmentsByStatus(selected));
        });

        bookButton.addActionListener(e -> mainFrame.showAppointmentForm(null));

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        rescheduleButton.addActionListener(e -> {
            int id = getSelectedId();
            if (id != -1) mainFrame.showAppointmentForm(appointmentDAO.getAppointmentById(id));
        });

        completeButton.addActionListener(e -> {
            int id = getSelectedId();
            if (id != -1) {
                appointmentDAO.updateStatus(id, "Completed");
                loadAll();
            }
        });

        cancelButton.addActionListener(e -> {
            int id = getSelectedId();
            if (id != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Cancel this appointment?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    appointmentDAO.cancelAppointment(id);
                    loadAll();
                }
            }
        });
    }

    private int getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment first.");
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    public void loadAll() {
        populateTable(appointmentDAO.getAllAppointments());
    }

    private void populateTable(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus()});
        }
    }
}