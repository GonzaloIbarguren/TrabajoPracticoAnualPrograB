package Model;

import org.jxmapviewer.viewer.GeoPosition;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Device  {
    private String id;
    private GeoPosition location;
    private State state;
    private TypesErrors typeError;
    private TypeInfraction typeInfraction;

    public Device(String id, GeoPosition location) {
        this.id = id;
        this.location = location;
        state = State.OPERATIONAL;
    }

    public abstract String getTypeDevice();

    public abstract void SimulateError();

    public abstract void FixError();


    public TypeInfraction getTypeInfraction() {
        return typeInfraction;
    }

    public void setTypeInfraction(TypeInfraction typeInfraction) {
        this.typeInfraction = typeInfraction;
    }

    public TypesErrors getTypeError() {
        return typeError;
    }

    public void setTypeError(TypesErrors typeError) {
        this.typeError = typeError;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoPosition getLocation() {
        return location;
    }

    public void setLocation(GeoPosition location) {
        this.location = location;
    }
}
