package ui;

import Model.*;

import javax.swing.*;
import java.awt.*;
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

        // 🧠 Crear botones
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

        // ➕ Agregar paneles
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        btnDeviceReport.addActionListener(e-> ShowDeviceReport(deviceList));
        //btnFinesReport.addActionListener();
        //btnLicenseReport.addActionListener();
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

}
