package ui;

import Model.*;
import dataBase.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportsWindows extends JFrame {
    private final JTextArea reportArea;

    public ReportsWindows(List<Device> deviceList) {
        setTitle("System Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        final JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(900, 600));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(5, 1, 5, 5)); // 5 filas, 1 columna
        leftPanel.setBackground(Color.gray);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnDeviceReport = new JButton("Devices Report");
        JButton btnFinesReport = new JButton("Fines Reports");
        JButton btnLicenseReport = new JButton("License Report");
        JButton btnSecurityReport = new JButton("Security Reports");
        JButton btnEventsReport = new JButton("Events Reports");

        leftPanel.add(btnDeviceReport);
        leftPanel.add(btnFinesReport);
        leftPanel.add(btnLicenseReport);
        leftPanel.add(btnSecurityReport);
        leftPanel.add(btnEventsReport);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Output"));


        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        btnDeviceReport.addActionListener(e-> ShowDeviceReport(deviceList));
        btnFinesReport.addActionListener(e -> showFinesReport());
        btnLicenseReport.addActionListener(e -> {
            String plate = JOptionPane.showInputDialog(
                    this, // ventana padre
                    "Enter the vehicle plate:",
                    "Search Fines",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (plate != null && !plate.trim().isEmpty()) {
                showFinesByPlate(plate.trim().toUpperCase());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "You must enter a valid plate.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
        //btnSecurityReport.addActionListener();
        //btnEventsReport.addActionListener();



        setVisible(true);
    }

    private void ShowDeviceReport(List<Device> deviceList){
        StringBuilder report = new StringBuilder();
        report.append("===== DEVICE CONTROLLERS REPORT =====\n\n");

        List<TrafficLightController> trafficLights = new ArrayList<>();
        List<SecurityCamera> securityCameras = new ArrayList<>();
        List<ParkingCamera> parkingCameras = new ArrayList<>();
        List<Radar> radars = new ArrayList<>();

        for (Device d : deviceList) {
            switch (d.getTypeDevice()) {
                case "trafficLightController" -> trafficLights.add((TrafficLightController) d);
                case "securityCamera" -> securityCameras.add((SecurityCamera) d);
                case "parkingCamera" -> parkingCameras.add((ParkingCamera) d);
                case "radar" -> radars.add((Radar) d);
            }
        }
        report.append("=== TRAFFIC LIGHT CONTROLLERS ===\n\n");
        if (trafficLights.isEmpty()) report.append("No traffic lights found.\n\n");
        for (TrafficLightController tl : trafficLights) {
            report.append("• ID: ").append(tl.getId()).append("\n");
            report.append("  State: ").append(tl.getState()).append("\n");
            report.append("  Street 1: ").append(tl.getLightMain().getStreet()).append("\n");
            report.append("  Street 2: ").append(tl.getLightSecundary().getStreet()).append("\n");
            report.append("  Intermittent Mode: ").append(tl.getIntermittent() ? "YES" : "NO").append("\n\n");
        }
        report.append("\n\n");

        report.append("=== SECURITY CAMERAS ===\n\n");
        if (securityCameras.isEmpty()) report.append("No security cameras found.\n\n");
        for (SecurityCamera cam : securityCameras) {
            report.append("• ID: ").append(cam.getId()).append("\n");
            report.append("  Status: ").append(cam.getState()).append("\n\n");
        }
        report.append("\n\n");

        report.append("=== PARKING CAMERAS ===\n\n");
        if (parkingCameras.isEmpty()) report.append("No parking cameras found.\n\n");
        for (ParkingCamera pcam : parkingCameras) {
            report.append("• ID: ").append(pcam.getId()).append("\n");
            report.append("  Status: ").append(pcam.getState()).append("\n\n");
        }
        report.append("\n\n");

        report.append("=== RADAR DEVICES ===\n\n");
        if (radars.isEmpty()) report.append("No radars found.\n\n");
        for (Radar radar : radars) {
            report.append("• ID: ").append(radar.getId()).append("\n");
            report.append("  Status: ").append(radar.getState()).append("\n\n");
        }

        reportArea.setText(report.toString());
    }
    public void showFinesReport() {
        StringBuilder report = new StringBuilder();

        try (Connection conn = DataBaseConnection.getConnection()) {
            report.append("===== FINES REPORT (GROUPED BY TYPE) =====\n\n");

            PreparedStatement ps = conn.prepareStatement("""
            SELECT infringement_type, fine_number, vehicle_plate, description,
                   infringement_date, amount, points
            FROM fine
            ORDER BY infringement_type, amount DESC
        """);
            ResultSet rs = ps.executeQuery();

            String currentType = null;
            BigDecimal subtotal = BigDecimal.ZERO;
            int count = 0;
            int totalCount = 0;
            BigDecimal totalSum = BigDecimal.ZERO;

            while (rs.next()) {
                String type = rs.getString("infringement_type");

                // Cambio de tipo → mostrar subtotal anterior
                if (currentType == null || !currentType.equals(type)) {
                    if (currentType != null) {
                        report.append(String.format("Subtotal for %s → Count: %d | Sum: $%.2f\n\n",
                                currentType, count, subtotal));
                        totalCount += count;
                        totalSum = totalSum.add(subtotal);
                    }
                    currentType = type;
                    count = 0;
                    subtotal = BigDecimal.ZERO;

                    report.append(">> TYPE: ").append(type).append("\n");
                    report.append("---------------------------------------------\n");
                    report.append(String.format("%-10s | %-10s | %-8s | %-10s | %-6s%n",
                            "Fine #", "Plate", "Amount", "Date", "Points"));
                    report.append("----------------------------------------------------------\n");
                }

                BigDecimal amount = rs.getBigDecimal("amount");
                report.append(String.format("%06d      | %-10s | $%-7.2f | %-10s | %d%n",
                        rs.getInt("fine_number"),
                        rs.getString("vehicle_plate"),
                        amount,
                        rs.getTimestamp("infringement_date").toLocalDateTime().toLocalDate(),
                        rs.getInt("points")));

                subtotal = subtotal.add(amount);
                count++;
            }

            // último subtotal
            if (currentType != null) {
                report.append(String.format("\nSubtotal for %s → Count: %d | Sum: $%.2f\n",
                        currentType, count, subtotal));
                totalCount += count;
                totalSum = totalSum.add(subtotal);
            }

            // Totales globales
            report.append("\n===== GLOBAL SUMMARY =====\n");
            report.append(String.format("Total fines: %d%n", totalCount));
            report.append(String.format("Total amount: $%.2f%n", totalSum.doubleValue()));

        } catch (SQLException e) {
            report.append(" Error loading fines: ").append(e.getMessage());
            e.printStackTrace();
        }

        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setText(report.toString());
        reportArea.setCaretPosition(0);
    }
    public void showFinesByPlate(String plate) {
        StringBuilder report = new StringBuilder();

        try (Connection conn = DataBaseConnection.getConnection()) {
            PreparedStatement checkPlate = conn.prepareStatement("""
            SELECT COUNT(*) FROM automobiles WHERE licenseplate = ?
        """);
            checkPlate.setString(1, plate);
            ResultSet rsCheck = checkPlate.executeQuery();
            rsCheck.next();
            int exists = rsCheck.getInt(1);

            if (exists == 0) {

                reportArea.setText(" Vehicle plate not found in the database: " + plate);
                return;
            }


            PreparedStatement ps = conn.prepareStatement("""
            SELECT fine_number, infringement_type, description,
                   infringement_date, amount, points
            FROM fine
            WHERE vehicle_plate = ?
            ORDER BY infringement_date DESC
        """);
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();

            report.append("===== FINES FOR VEHICLE: ").append(plate).append(" =====\n\n");
            report.append(String.format("%-8s | %-15s | %-10s | %-8s | %-6s%n",
                    "Fine #", "Type", "Date", "Amount", "Points"));
            report.append("--------------------------------------------------------\n");

            BigDecimal total = BigDecimal.ZERO;
            int count = 0;

            while (rs.next()) {
                BigDecimal amount = rs.getBigDecimal("amount");
                report.append(String.format("%06d | %-15s | %-10s | $%-7.2f | %d%n",
                        rs.getInt("fine_number"),
                        rs.getString("infringement_type"),
                        rs.getTimestamp("infringement_date")
                                .toLocalDateTime().toLocalDate(),
                        amount,
                        rs.getInt("points")));
                total = total.add(amount);
                count++;
            }

            if (count == 0) {
                report.append("\nNo fines found for this vehicle.\n");
            } else {
                report.append("\n--------------------------------------------\n");
                report.append(String.format("Total fines: %d | Total amount: $%.2f%n", count, total.doubleValue()));
            }

        } catch (SQLException e) {
            report.append("⚠Error loading fines for plate ").append(plate)
                    .append(": ").append(e.getMessage());
            e.printStackTrace();
        }

        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setText(report.toString());
        reportArea.setCaretPosition(0);
    }



}