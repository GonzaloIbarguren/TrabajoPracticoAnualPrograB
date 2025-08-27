package ui;

import Model.TrafficLightController;

import javax.swing.*;
import java.awt.*;

public class IntersectionPanel extends JPanel {
    private TrafficLightController controller;

    public IntersectionPanel (TrafficLightController controller){
        this.controller = controller;
        setPreferredSize(new Dimension(300,300));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,h/3,w,h/3);
        g.fillRect(w/3,0,w/3,h);

        g.setColor(controller.getLightMain().getState());
        g.fillOval(w/3-20,h/3-40,20,20);
        g.setColor(Color.BLACK);
        g.drawOval(w/3-20,h/3-40,20,20);

        g.setColor(controller.getLightSecundary().getState());
        g.fillOval(2*w/3,2*h/3,20,20);
        g.setColor(Color.BLACK);
        g.drawOval(2*w/3,2*h/3,20,20);

    }
}
