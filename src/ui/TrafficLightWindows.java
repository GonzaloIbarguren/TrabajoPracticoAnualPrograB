package ui;

import Model.TrafficLightController;

import javax.swing.*;
import java.awt.*;


public class TrafficLightWindows extends JFrame {
    private TrafficLightController controller;
    private Timer timer;
    private IntersectionPanel intersectionPanel;
    public TrafficLightWindows(TrafficLightController controller) {
        this.controller = controller;
        setTitle("Traffic Light info");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        intersectionPanel = new IntersectionPanel(controller);

        JLabel street1Label = new JLabel("Calle 1: " + controller.getLightMain().getStreet());
        JLabel street2Label = new JLabel("Calle 2: " + controller.getLightSecundary().getStreet());




        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
        infoPanel.add(street1Label);
        infoPanel.add(street2Label);
        add(intersectionPanel,BorderLayout.CENTER);
        add(infoPanel,BorderLayout.SOUTH);



        timer = new Timer(500,e -> intersectionPanel.repaint());
        timer.start();

        setVisible(true);
    }

    public void dispose(){
        super.dispose();
        timer.stop();
    }
}