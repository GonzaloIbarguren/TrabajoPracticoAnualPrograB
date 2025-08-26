package ui;

import javax.swing.*;
import java.awt.*;

public class LightCircle extends JPanel {
    private Color color = Color.BLACK;

    public void setColor(Color color){
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(),getHeight())-10;
        g.setColor(color);
        g.fillOval((getWidth()-size)/2,(getHeight()-size)/2,size,size);
        g.setColor(Color.BLACK);
        g.drawOval((getWidth()-size)/2,(getHeight()-size)/2,size,size);

    }
}
