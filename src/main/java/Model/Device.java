package Model;

import org.jxmapviewer.viewer.GeoPosition;

public abstract class Device  {
    private String id;
    private GeoPosition location;



    public Device(String id, GeoPosition location) {
        this.id = id;
        this.location = location;
    }

    public abstract String getTypeDevice();




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
