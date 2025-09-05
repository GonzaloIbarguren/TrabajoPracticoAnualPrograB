import Model.Device;
import Model.TrafficLightController;
import ui.MapWindows;
import ui.TrafficLightWindows;

import java.util.ArrayList;
import java.util.List;

public class UrbanMonitoringCenter{
    private boolean running;
    private List<Device> devices;
    private MapWindows map;
    private TrafficLightWindows windows;

    public UrbanMonitoringCenter() {
        this.devices = new ArrayList<>();
        this.map = new MapWindows();




    }





    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
