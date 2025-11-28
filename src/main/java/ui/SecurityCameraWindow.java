package ui;

import dataBase.DataBaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SecurityCameraWindow extends JFrame {

    private Image accidentImage;
    private static String ACCIDENT_IMAGE_PATH;
    private final String cameraId;

    public SecurityCameraWindow(String cameraId) {
        this.cameraId = cameraId;

        switch (cameraId) {
            case "Camera 1":
                ACCIDENT_IMAGE_PATH = "/trafficInfractionImages/sec_cam_1.jpg";
                break;
            case "Camera 2":
                ACCIDENT_IMAGE_PATH = "/trafficInfractionImages/sec_cam_2.jpg";
                break;
            case "Camera 3":
                ACCIDENT_IMAGE_PATH = "/trafficInfractionImages/sec_cam_4.jpg";
                break;
            case "Camera 4":
                ACCIDENT_IMAGE_PATH = "/trafficInfractionImages/sec_cam_5.jpg";
                break;
            default:
                System.out.println("Camera ID not recognized (" + getCameraId() + "), using default image.");
                break;
        }

        try {
            accidentImage = new ImageIcon(Objects.requireNonNull(
                    SecurityCameraWindow.class.getResource(ACCIDENT_IMAGE_PATH)
            )).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error while trying to update: " + ACCIDENT_IMAGE_PATH +
                    ". Using error background..");
            accidentImage = null;
        }

        setTitle("Security Camera - ID: " + getCameraId() + " | Live View");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel viewerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (accidentImage != null) {
                    g.drawImage(accidentImage, 0, 0, getWidth(), getHeight(), this);

                    g.setColor(Color.RED);
                    g.fillOval(getWidth() - 30, 10, 20, 20);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    g.drawString("LIVE", getWidth() - 70, 25);

                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, getWidth() / 25));
                    g.drawString("NO SIGNAL",
                            getWidth() / 10, getHeight() / 2);
                    g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 40));
                    g.drawString("Verifique la ruta: " + ACCIDENT_IMAGE_PATH,
                            getWidth() / 10, getHeight() / 2 + getWidth() / 25);
                }
            }
        };

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 10, 10));
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JButton btnPolice = new JButton("Notify Police");
        btnPolice.setBackground(new Color(0, 51, 153));
        btnPolice.setForeground(Color.WHITE);

        btnPolice.addActionListener(e -> {
            registerAlertInDB("Police");
            btnPolice.setEnabled(false);
            btnPolice.setText("POLICE (Alert sent)");
            btnPolice.setBackground(Color.GRAY);
        });

        JButton btnAmbulance = new JButton("Notify Medical Services");

        btnAmbulance.addActionListener(e -> {
            registerAlertInDB("Medical");
            btnAmbulance.setEnabled(false);
            btnAmbulance.setText("AMBULANCE (Alert sent)");
        });

        JButton btnFire = new JButton("Notify Firefighters");
        btnFire.setBackground(new Color(204, 0, 0));
        btnFire.setForeground(Color.WHITE);

        btnFire.addActionListener(e -> {
            registerAlertInDB("Firefighters");
            btnFire.setEnabled(false);
            btnFire.setText("FIREFIGHTERS (Alert sent)");
            btnFire.setBackground(Color.GRAY);
        });

        bottomPanel.add(btnPolice);
        bottomPanel.add(btnAmbulance);
        bottomPanel.add(btnFire);

        add(viewerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void registerAlertInDB(String serviceType) {
        String sql = "INSERT INTO security_alerts (alert_date, service_type, description) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setString(2, serviceType);
            ps.setString(3, "Alert triggered from Camera ID: " + getCameraId());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Alert sent to: " + serviceType,
                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving alert: " + e.getMessage(),
                    "Error in DataBase", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getCameraId() {
        return cameraId;
    }
}