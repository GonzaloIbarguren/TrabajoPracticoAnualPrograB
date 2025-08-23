package Model;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class TrafficLight implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String street;
    private boolean main = false;
    private Color state;

    public TrafficLight() {
    }
    public void nextState(){
        if (state.equals(Color.RED))
            this.state = Color.GREEN;
        else if (state.equals(Color.GREEN))
            this.state = Color.YELLOW;
            else
                this.state = Color.RED;
    }
    public Color getState() {
        return state;
    }

    public void setState(Color state) {
        this.state = state;
    }

    public boolean getMain() {
        return main;
    }

    public void setMain() {
        this.main = true;
        this.state = Color.GREEN;
    }
}


