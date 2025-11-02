package ui;

import Model.Orientation;
import Model.TrafficLightController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class TrafficLightWindows extends JFrame {
    private TrafficLightController controller;
    private Timer timer;
    private Image backgroundImage;

    public TrafficLightWindows(TrafficLightController controller) {
        this.controller = controller;
        backgroundImage = new ImageIcon(Objects.requireNonNull(TrafficLightWindows.class.getResource("/street.png"))).getImage();


        setTitle("Traffic Light info");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                int size = 40;
                int xMain, xSec, yMain, ySec;
                if (controller.getLightMain().getOrientation() == Orientation.SOUTH || controller.getLightMain().getOrientation() == Orientation.NORTH || controller.getLightMain().getOrientation() == Orientation.NORTH_SOUTH) {
                    xMain = (int) (getWidth() * 0.487) - size / 2;
                    yMain = (int) (getHeight() * 0.3) - size / 2;
                    xSec = (int) (getWidth() * 0.65) - size / 2;
                    ySec = (int) (getHeight() * 0.503) - size / 2;

                } else {
                    xSec = (int) (getWidth() * 0.487) - size / 2;
                    ySec = (int) (getHeight() * 0.3) - size / 2;
                    xMain = (int) (getWidth() * 0.65) - size / 2;
                    yMain = (int) (getHeight() * 0.503) - size / 2;

                }


                g.setColor(controller.getLightMain().getState());
                g.fillOval(xMain, yMain, size, size);
                g.setColor(controller.getLightSecundary().getState());
                g.fillOval(xSec, ySec, size, size);


                g.setColor(Color.WHITE);
                int fontSize = getWidth() / 40;
                int textX = (int) (getWidth() * 0.72);
                int textY1 = (int) (getHeight() * 0.1);
                int textY2 = (int) (getHeight() * 0.17);
                g.setFont(new Font("Arial", Font.BOLD, fontSize));
                g.drawString(controller.getLightMain().getStreet(), textX, textY1);
                g.drawString(controller.getLightSecundary().getStreet(), textX, textY2);

                int arrowSize = fontSize;
                Graphics2D g2 = (Graphics2D) g;
                drawArrow(g2, textX + fontSize * 6, textY1 - fontSize / 2, arrowSize, controller.getLightMain().getOrientation());
                drawArrow(g2, textX + fontSize * 6, textY2 - fontSize / 2, arrowSize, controller.getLightSecundary().getOrientation());

            }
            private void drawArrow(Graphics2D g2, int x, int y, int size, Orientation dir) {
                Polygon arrow = new Polygon();

                switch (dir) {
                    case NORTH:
                        g2.drawLine(x, y + size, x, y);
                        arrow.addPoint(x, y);
                        arrow.addPoint(x - size / 3, y + size / 2);
                        arrow.addPoint(x + size / 3, y + size / 2);
                        break;

                    case SOUTH:
                        g2.drawLine(x, y, x, y + size);
                        arrow.addPoint(x, y + size);
                        arrow.addPoint(x - size / 3, y + size / 2);
                        arrow.addPoint(x + size / 3, y + size / 2);
                        break;

                    case EAST:
                        g2.drawLine(x, y, x + size, y);
                        arrow.addPoint(x + size, y);
                        arrow.addPoint(x + size / 2, y - size / 3);
                        arrow.addPoint(x + size / 2, y + size / 3);
                        break;

                    case WEST:
                        g2.drawLine(x + size, y, x, y);
                        arrow.addPoint(x, y);
                        arrow.addPoint(x + size / 2, y - size / 3);
                        arrow.addPoint(x + size / 2, y + size / 3);
                        break;
                }

                g2.fillPolygon(arrow);
            }
        };

        timer = new Timer(500, e -> panel.repaint());
        timer.start();
        add(panel);
        setVisible(true);
    }

}