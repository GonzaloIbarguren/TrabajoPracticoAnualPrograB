package Model;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class TrafficLight {
    private Orientation orientation;
    private String street;
    private boolean main;
    private Color state;

    public TrafficLight(String street, String orientation, Boolean main) {
        this.street = street;
        this.orientation = Orientation.valueOf(orientation);
        this.main = main;
        state = Color.YELLOW;
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

    public String getStreet() {
        return street;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setDirection(Orientation direction) {
        this.orientation = direction;
    }
}


