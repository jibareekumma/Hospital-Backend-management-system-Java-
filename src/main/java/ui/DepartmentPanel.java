package ui;

import dao.DepartmentDAO;
import model.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DepartmentPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private DepartmentDAO departmentDAO;

    public DepartmentPanel(MainFrame mainFrame) {
        this.departmentDAO = new DepartmentDAO();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(12);
        JTextField descriptionField = new JTextField(20);
        JButton addButton = new JButton("Add Department");
        JButton backButton = new JButton("Back to Dashboard");

        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(new JLabel("Description:"));
        topPanel.add(descriptionField);
        topPanel.add(addButton);
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Description"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Delete Selected");
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAll();

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department name is required.");
                return;
            }
            departmentDAO.addDepartment(name, descriptionField.getText().trim());
            nameField.setText("");
            descriptionField.setText("");
            loadAll();
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a department first.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            departmentDAO.deleteDepartment(id);
            loadAll();
        });

        backButton.addActionListener(e -> mainFrame.showDashboard(mainFrame.getCurrentUser()));
    }

    private void loadAll() {
        tableModel.setRowCount(0);
        List<Department> departments = departmentDAO.getAllDepartments();
        for (Department d : departments) {
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getDescription()});
        }
    }
}
