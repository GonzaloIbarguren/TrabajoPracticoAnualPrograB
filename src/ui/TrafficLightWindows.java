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

                int boxWidth = (int) (getWidth()*0.2);
                int boxHeight = (int) (getHeight()*0.15);
                int margin = 20;
                int boxX = (int) (getWidth()*0.7);
                int boxY = (int) (getHeight()*0.05);

                g.setColor(Color.BLACK);
                g.fillRect(boxX,boxY,boxWidth,boxHeight);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial",Font.BOLD,Math.max(12,boxHeight/5)));
                g.drawString("Street 1: "+controller.getLightMain().getStreet(),boxX+10,boxY+20+boxHeight/25);
                g.drawString("Street 1: "+controller.getLightSecundary().getStreet(),boxX+10,boxY+(2*boxHeight/3));

            }
        };
        timer = new Timer(500, e -> panel.repaint());
        timer.start();
        add(panel);
        setVisible(true);
    }

}