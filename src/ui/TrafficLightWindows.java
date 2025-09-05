package ui;

import Model.TrafficLightController;

import javax.swing.*;
import java.awt.*;


public class TrafficLightWindows extends JFrame {
    private TrafficLightController controller;
    private Timer timer;
    private Image backgroundImage;
    public TrafficLightWindows(TrafficLightController controller) {
        this.controller = controller;
        backgroundImage = new ImageIcon("street.png").getImage();
        setTitle("Traffic Light info");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
                int size = 40;

                int xMain = (int) (getWidth()*0.487)-size/2;
                int yMain = (int) (getHeight()*0.3)-size/2;

                g.setColor(controller.getLightMain().getState());
                g.fillOval(xMain,yMain,size,size);

                int xSec = (int) (getWidth()*0.65)-size/2;
                int ySec = (int) (getHeight()*0.503)-size/2;

                g.setColor(controller.getLightSecundary().getState());
                g.fillOval(xSec,ySec,size,size);
            }
        };
        timer = new Timer(500, e -> panel.repaint());
        timer.start();
        add(panel);
        setVisible(true);
    }

}