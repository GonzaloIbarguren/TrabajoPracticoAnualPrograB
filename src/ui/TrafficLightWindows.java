package ui;

import Model.TrafficLightController;

import javax.swing.*;
import java.awt.*;


public class TrafficLightWindows extends JFrame {
    private TrafficLightController controller;
    private Timer timer;
    private LightCircle mainCirle,secundaryCircle;
    public TrafficLightWindows(TrafficLightController controller) {
        this.controller = controller;
        setTitle("Traffic Light info");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel street1Label = new JLabel("Calle 1: " + controller.getLightMain().getStreet());
        JLabel street2Label = new JLabel("Calle 2: " + controller.getLightSecundary().getStreet());

        mainCirle = new LightCircle();
        secundaryCircle = new LightCircle();
        mainCirle.setPreferredSize(new Dimension(80,80));
        secundaryCircle.setPreferredSize(new Dimension(80,80));

        JPanel lightPanel = new JPanel(new GridLayout(1,2,20,20));
        lightPanel.add(mainCirle);
        lightPanel.add(secundaryCircle);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
        infoPanel.add(street1Label);
        infoPanel.add(street2Label);
        add(lightPanel,BorderLayout.CENTER);
        add(infoPanel,BorderLayout.SOUTH);



        timer = new Timer(500,e -> updateColors());
        timer.start();

        setVisible(true);
    }
    public void updateColors(){
        mainCirle.setColor(controller.getLightMain().getState());
        secundaryCircle.setColor(controller.getLightSecundary().getState());
    }
    public void dispose(){
        super.dispose();
        timer.stop();
    }
}