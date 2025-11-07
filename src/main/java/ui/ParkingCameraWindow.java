package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ParkingCameraWindow extends JFrame {

    private Image parkingImage;
    private static final String PARKING_IMAGE_PATH = "/park_cam_1.jpg";

    public ParkingCameraWindow(String cameraId) {
        try {
            parkingImage = new ImageIcon(Objects.requireNonNull(
                    ParkingCameraWindow.class.getResource(PARKING_IMAGE_PATH)
            )).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error al cargar la imagen: " + PARKING_IMAGE_PATH +
                    ". Usando fondo de error.");
            parkingImage = null;
        }

        setTitle("Cámara de Estacionamiento - ID: " + cameraId + " | VISTA EN VIVO");
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
                    g.drawString("¡IMAGEN DE PARKING NO ENCONTRADA!",
                            getWidth() / 10, getHeight() / 2);
                    g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 40));
                    g.drawString("Verifique la ruta: " + PARKING_IMAGE_PATH,
                            getWidth() / 10, getHeight() / 2 + getWidth() / 25);
                }
            }
        };

        add(viewerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

}