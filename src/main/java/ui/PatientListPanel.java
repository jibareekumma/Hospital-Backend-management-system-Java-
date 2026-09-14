package ui;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientListPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private PatientDAO patientDAO;
    private MainFrame mainFrame;

    public PatientListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.patientDAO = new PatientDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Find");
        JButton refreshButton = new JButton("View All");
        JButton registerButton = new JButton("Register Patient");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Search (name or ID):"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(registerButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Gender", "Phone"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewProfileButton = new JButton("View Profile");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        bottomPanel.add(viewProfileButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAllPatients();

        refreshButton.addActionListener(e -> loadAllPatients());

        searchButton.addActionListener(e -> {
            String term = searchField.getText().trim();
            if (term.isEmpty()) {
                loadAllPatients();
            } else {
                populateTable(patientDAO.findPatients(term));
            }
        });

        registerButton.addActionListener(e -> mainFrame.showPatientForm(null));

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));

        viewProfileButton.addActionListener(e -> {
            int id = getSelectedPatientId();
            if (id != -1) mainFrame.showPatientProfile(id);
        });

        updateButton.addActionListener(e -> {
            int id = getSelectedPatientId();
            if (id != -1) {
                Patient patient = patientDAO.getPatientById(id);
                mainFrame.showPatientForm(patient);
            }
        });

        deleteButton.addActionListener(e -> {
            int id = getSelectedPatientId();
            if (id != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this patient?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    patientDAO.deletePatient(id);
                    loadAllPatients();
                }
            }
        });
    }

    private int getSelectedPatientId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    public void loadAllPatients() {
        populateTable(patientDAO.getAllPatients());
    }

    private void populateTable(List<Patient> patients) {
        tableModel.setRowCount(0);
        for (Patient p : patients) {
            tableModel.addRow(new Object[]{p.getId(), p.getFirstName(), p.getLastName(), p.getGender(), p.getPhone()});
        }
    }
}