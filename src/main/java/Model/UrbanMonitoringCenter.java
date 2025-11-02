package Model;

import ui.MapWindows;
import ui.TrafficLightWindows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrbanMonitoringCenter{
    private boolean running;
    private List<Device> devices;
    private MapWindows map;
    private TrafficLightWindows windows;

    public UrbanMonitoringCenter() {
        this. devices = new ArrayList<>();
        this.map = new MapWindows();
       // windows = new TrafficLightWindows(new TrafficLightController());

    }





    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
