package Model;

import org.jxmapviewer.viewer.GeoPosition;

public class SecurityCamera extends Device{


    public SecurityCamera(String id, GeoPosition location) {
        super(id, location);
    }

    @Override
    public String getTypeDevice() {
        return "securityCamera";
    }

    @Override
    public void SimulateError() {

    }

    @Override
    public void FixError() {

    }
}
