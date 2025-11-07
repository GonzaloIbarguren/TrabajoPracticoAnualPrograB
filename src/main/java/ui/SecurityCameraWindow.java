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
        setSize(700, 500); // Un buen tamaño para visualizar el "accidente"
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel viewerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (accidentImage != null) {
                    // Dibuja la imagen, escalándola para que se ajuste al tamaño del panel
                    g.drawImage(accidentImage, 0, 0, getWidth(), getHeight(), this);

                    // Opcional: Agregar un texto de "grabando"
                    g.setColor(Color.RED);
                    g.fillOval(getWidth() - 30, 10, 20, 20);
                    g.setFont(new Font("Arial", Font.BOLD, 14));
                    g.drawString("LIVE", getWidth() - 70, 25);

                } else {
                    // Si la imagen falla, muestra un mensaje de error grande
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

        // --- 4. Botón de Notificación (el operador debe confirmar la alerta) ---
        JButton notifyButton = new JButton("Notificar Emergencia (Accidente confirmado)");
        notifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        notifyButton.setBackground(Color.RED.darker());
        notifyButton.setForeground(Color.WHITE);
        notifyButton.setFocusPainted(false);

        // Acción del botón: Simula la notificación
        notifyButton.addActionListener(e -> {
            // Aquí deberías llamar al método notifyEmergencyServices de tu modelo.
            // Por ejemplo: centralController.notify(cameraId, "ACCIDENTE DE TRÁFICO", "POLICE", "EMS");
            JOptionPane.showMessageDialog(this,
                    "Alerta de Accidente enviada a Policía y Servicios Médicos.\nCámara " + cameraId + " confirmada.",
                    "Notificación Enviada",
                    JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cierra la ventana después de notificar
        });

        // --- 5. Ensamblaje Final ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.add(notifyButton);

        add(viewerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

  //   Si necesitas que la ventana solo se abra con un ID:
     public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new SecurityCameraWindow("C-42"));
     }
}