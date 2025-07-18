import Model.Device;
import ui.MapWindows;

import java.util.ArrayList;
import java.util.List;

public class UrbanMonitoringCenter{
    private boolean running;
    private List<Device> devices;
    private MapWindows map;

    public UrbanMonitoringCenter() {
        this.devices = new ArrayList<>();
        this.map = new MapWindows();




    }





    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
