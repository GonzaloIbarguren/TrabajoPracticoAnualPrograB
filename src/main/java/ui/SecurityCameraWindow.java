package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SecurityCameraWindow extends JFrame {

    private Image accidentImage;
    private static String ACCIDENT_IMAGE_PATH;

    public SecurityCameraWindow(String cameraId) {
        ArrayList<String> listaImagenes = new ArrayList<String>();
        listaImagenes.add("/sec_cam_1.jpg");
        listaImagenes.add("/sec_cam_2.jpg");
        listaImagenes.add("/sec_cam_3.jpg");
        ACCIDENT_IMAGE_PATH = listaImagenes.get(new Random().nextInt(listaImagenes.size()));
        try {
            accidentImage = new ImageIcon(Objects.requireNonNull(
                    SecurityCameraWindow.class.getResource(ACCIDENT_IMAGE_PATH)
            )).getImage();
        } catch (NullPointerException e) {
            System.err.println("Error al cargar la imagen: " + ACCIDENT_IMAGE_PATH +
                    ". Usando fondo de error.");
            accidentImage = null;
        }

        setTitle("Cámara de Seguridad - ID: " + cameraId + " | VISTA EN VIVO");
        setSize(700, 500);
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
                    g.drawString("¡IMAGEN DE ACCIDENTE NO ENCONTRADA!",
                            getWidth() / 10, getHeight() / 2);
                    g.setFont(new Font("Arial", Font.PLAIN, getWidth() / 40));
                    g.drawString("Verifique la ruta: " + ACCIDENT_IMAGE_PATH,
                            getWidth() / 10, getHeight() / 2 + getWidth() / 25);
                }
            }
        };

        JButton notifyButton = new JButton("Notificar Emergencia (Accidente confirmado)");
        notifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        notifyButton.setBackground(Color.RED.darker());
        notifyButton.setForeground(Color.WHITE);
        notifyButton.setFocusPainted(false);

        notifyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Alerta de Accidente enviada a Policía y Servicios Médicos.\nCámara " + cameraId + " confirmada.",
                    "Notificación Enviada",
                    JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.add(notifyButton);

        add(viewerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

}