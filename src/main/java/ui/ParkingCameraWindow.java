package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ParkingCameraWindow extends JFrame {

    private Image parkingImage;
    private static String PARKING_IMAGE_PATH;
    private final String cameraId;

    public ParkingCameraWindow(String cameraId) {
        this.cameraId = cameraId;

        switch (cameraId) {
            case "Parking Camera 1":
                PARKING_IMAGE_PATH = "/trafficInfractionImages/parking-infringement1.jpg";
                break;
            case "Parking Camera 2":
                PARKING_IMAGE_PATH = "/trafficInfractionImages/parking-infringement2.jpg";
                break;
            case "Parking Camera 3":
                PARKING_IMAGE_PATH = "/trafficInfractionImages/parking-infringement3.jpeg";
                break;
            case "Parking Camera 4":
                PARKING_IMAGE_PATH = "/trafficInfractionImages/parking-infringement4.jpeg";
                break;
            default:
                System.out.println("Camera ID not recognized (" + getCameraId() + "), using default image.");
                break;
        }

        try {
            parkingImage = new ImageIcon(Objects.requireNonNull(
                    ParkingCameraWindow.class.getResource(PARKING_IMAGE_PATH)
            )).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error while trying to update: " + PARKING_IMAGE_PATH +
                    ". Using error background.");
            parkingImage = null;
        }

        setTitle("Parking camera - ID: " + getCameraId() + " | Live View");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel viewerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (parkingImage != null) {
                    g.drawImage(parkingImage, 0, 0, getWidth(), getHeight(), this);

                    g.setColor(Color.GREEN.darker());
                    g.fillOval(getWidth() - 30, 10, 20, 20);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    g.drawString("PARKING LIVE", getWidth() - 110, 25);

                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("Arial", Font.BOLD, getWidth() / 25));
                    g.drawString("Parking image not found.",
                            getWidth() / 10, getHeight() / 2);
                    g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 40));
                    g.drawString("Check path: " + PARKING_IMAGE_PATH,
                            getWidth() / 10, getHeight() / 2 + getWidth() / 25);
                }
            }
        };

        add(viewerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public String getCameraId() {
        return cameraId;
    }
}