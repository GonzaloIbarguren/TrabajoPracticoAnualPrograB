package ui;

import dataBase.DataBaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VehicleRegistrationWindow extends JFrame {

    public VehicleRegistrationWindow() {
        setTitle("Vehicle Registration");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("1. Brand", createBrandPanel());
        tabbedPane.addTab("2. Model", createModelPanel());
        tabbedPane.addTab("3. Automobile", createAutomobilePanel());

        add(tabbedPane);
    }

    private JPanel createBrandPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Model Name"}, 0);
        JTable table = new JTable(model);
        loadBrands(model);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Brand");
        JButton btnDel = new JButton("Delete Brand");

        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Brand Name:");
            if (name != null && !name.trim().isEmpty()) {

                try (Connection conn = DataBaseConnection.getConnection();
                     Statement stmt = conn.createStatement()) {

                    String sql = "INSERT INTO vehiclemake (name) VALUES ('" + name.toUpperCase().trim() + "')";
                    stmt.executeUpdate(sql);

                    loadBrands(model);
                    JOptionPane.showMessageDialog(this, "Brand added successfully!");

                } catch (SQLException ex) {
                    if ("23505".equals(ex.getSQLState())) {
                        JOptionPane.showMessageDialog(this,
                                "Error: The Brand '" + name + "' already exists.",
                                "Duplicate Entry",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Database Error: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();

            try (Connection conn = DataBaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("DELETE FROM vehiclemake WHERE id_make = " + id);
                loadBrands(model);

            } catch (SQLException ex) {
                if ("23503".equals(ex.getSQLState())) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete this Brand because Models depend on it.\n" +
                                    "Please delete the associated models first.", "Integrity Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createModelPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Model Name", "Year", "Brand Name"}, 0);
        JTable table = new JTable(model);
        loadModels(model);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Model");
        JButton btnDel = new JButton("Delete Model");

        btnAdd.addActionListener(e -> showAddModelDialog(model));

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = table.getValueAt(row, 0).toString();

                if (!executeSQL("DELETE FROM model WHERE id_model = " + id))
                    JOptionPane.showMessageDialog(this, "Cannot delete: Vehicles depend on this Model.");

                loadModels(model);
            }
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showAddModelDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "New Model", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtName = new JTextField();
        JTextField txtYear = new JTextField();
        JComboBox<String> comboMakes = new JComboBox<>();
        loadBrandsIntoCombo(comboMakes);

        fieldsPanel.add(new JLabel("Model:")); fieldsPanel.add(comboMakes);
        fieldsPanel.add(new JLabel("Model Name:")); fieldsPanel.add(txtName);
        fieldsPanel.add(new JLabel("Year:")); fieldsPanel.add(txtYear);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        bottomPanel.add(btnSave);

        dialog.add(fieldsPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String makeSelection = (String) comboMakes.getSelectedItem();
            if (makeSelection == null) return;
            int idMake = Integer.parseInt(makeSelection.split(" - ")[0]);

            if (txtName.getText().isEmpty() || txtYear.getText().isEmpty()){
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int year;
            try {
                year = Integer.parseInt(txtYear.getText());
                if (year < 1950 || year > 2025) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid year (1950 - 2025)",
                            "Invalid Year", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Year must be a number.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String name = txtName.getText().toUpperCase();

            try (Connection conn = dataBase.DataBaseConnection.getConnection()) {

                String checkSql = "SELECT COUNT(*) FROM model WHERE id_make = ? AND name = ? AND year = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                    pstmt.setInt(1, idMake);
                    pstmt.setString(2, name);
                    pstmt.setInt(3, year);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "The model '" + name + "' (" + year + ") already exists for this brand.",
                                "Duplicate Model", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                String insertSql = "INSERT INTO model (name, year, id_make) VALUES (?, ?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setString(1, name);
                    insertPstmt.setInt(2, year);
                    insertPstmt.setInt(3, idMake);
                    insertPstmt.executeUpdate();
                }

                loadModels(tableModel);
                JOptionPane.showMessageDialog(dialog, "Model added successfully.");
                dialog.dispose();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialog.setVisible(true);
    }

    private JPanel createAutomobilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15,15,15,15));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Plate", "Colour", "Owner", "Model Name"}, 0);
        JTable table = new JTable(model);
        loadAutos(model);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Automobile");
        JButton btnDel = new JButton("Delete Automobile");

        btnAdd.addActionListener(e -> showAddAutoDialog(model));

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = table.getValueAt(row, 0).toString();

                if (!executeSQL("DELETE FROM automobiles WHERE id = " + id))
                    JOptionPane.showMessageDialog(this, "Cannot delete: Fines depend on this Auto.");

                loadAutos(model);
            }
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showAddAutoDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "New Automobile", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtOwner = new JTextField();
        JTextField txtAddress = new JTextField();

        JComboBox<String> comboModels = new JComboBox<>();
        loadModelsIntoCombo(comboModels);

        JTextField txtPlate = new JTextField();

        String[] commonColour = { "White", "Black", "Grey", "Silver", "Red", "Blue", "Green", "Yellow",
                "Brown", "Orange", "Beige"};

        JComboBox<String> comboColour = new JComboBox<>(commonColour);

        fieldsPanel.add(new JLabel("Owner Name:")); fieldsPanel.add(txtOwner);
        fieldsPanel.add(new JLabel("Address:")); fieldsPanel.add(txtAddress);
        fieldsPanel.add(new JLabel("Model:")); fieldsPanel.add(comboModels);
        fieldsPanel.add(new JLabel("Colour:")); fieldsPanel.add(comboColour);
        fieldsPanel.add(new JLabel("License Plate:")); fieldsPanel.add(txtPlate);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        bottomPanel.add(btnSave);

        dialog.add(fieldsPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String modelSelection = (String) comboModels.getSelectedItem();
            if (modelSelection == null) return;
            int idModel = Integer.parseInt(modelSelection.split(" - ")[0]);

            String plateValidFormat = "^([A-Z]{3}\\s\\d{3})|([A-Z]{2}\\s\\d{3}\\s[A-Z]{2})$";
            String plate = txtPlate.getText().toUpperCase();
            String colour = (String) comboColour.getSelectedItem();
            String owner = txtOwner.getText();
            String address = txtAddress.getText();

            if (owner.isEmpty() || address.isEmpty() || colour.isEmpty() || plate.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields must be completed.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!plate.matches(plateValidFormat)){
                JOptionPane.showMessageDialog(dialog, "Invalid License plate format.\nValid formats: (ABC 123) or (AB 123 CD)", "Validation error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO automobiles (licenseplate, color, owner, address, id_model) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DataBaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, plate);
                ps.setString(2, colour);
                ps.setString(3, owner);
                ps.setString(4, address);
                ps.setInt(5, idModel);

                ps.executeUpdate();

                loadAutos(tableModel);
                JOptionPane.showMessageDialog(dialog, "Vehicle registered successfully.");
                dialog.dispose();

            } catch (SQLException ex) {

                if ("23505".equals(ex.getSQLState())) {
                    JOptionPane.showMessageDialog(dialog, "Error: License plate (" + plate + ") is already registered.",
                            "Duplicate License plate",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }

    private void loadBrands(DefaultTableModel model) {
        model.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vehiclemake ORDER BY id_make")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id_make"), rs.getString("name")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadModels(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT m.id_model, m.name, m.year, v.name as make_name " +
                "FROM model m JOIN vehiclemake v ON m.id_make = v.id_make ORDER BY m.id_model";
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_model"),
                        rs.getString("name"),
                        rs.getInt("year"),
                        rs.getString("make_name")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadAutos(DefaultTableModel model) {
        model.setRowCount(0);

        String sql = "SELECT a.id, a.licenseplate, a.color, a.owner, m.name as model_name " +
                "FROM automobiles a JOIN model m ON a.id_model = m.id_model ORDER BY a.id";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("licenseplate"),
                        rs.getString("color"),
                        rs.getString("owner"),
                        rs.getString("model_name")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadBrandsIntoCombo(JComboBox<String> combo) {
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_make, name FROM vehiclemake")) {
            while (rs.next()) {
                combo.addItem(rs.getInt("id_make") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadModelsIntoCombo(JComboBox<String> combo) {
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT m.id_model, m.name, v.name as make FROM model m JOIN vehiclemake v ON m.id_make = v.id_make ORDER BY v.name, m.id_model ASC")) {
            while (rs.next()) {
                combo.addItem(rs.getInt("id_model") + " - " + rs.getString("make") + " " + rs.getString("name"));
            }
        } catch (SQLException e) { e.printStackTrace();}
    }

    private boolean executeSQL(String sql) {
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                JOptionPane.showMessageDialog(this, "Duplicate entry.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            else {
                System.err.println("SQL Error: " + e.getMessage());
            }
            return false;
        }
    }
}