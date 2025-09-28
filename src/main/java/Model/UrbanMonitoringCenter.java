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
        this.devices = new ArrayList<>();
        //this.map = new MapWindows();
       // windows = new TrafficLightWindows(new TrafficLightController());

        TrafficLight light1 = new TrafficLight();
        TrafficLight light2 = new TrafficLight();
        TypeInfraction typeInfraction = new TypeInfraction(TypesInfraction.RED_LIGHT,1000,1,"The vehicle ran a red light at the intersection of "+light1.getStreet() +" and "+light2.getStreet());
        Automobile automobile = new Automobile("AB098AA","Martin Perez","address");
        EventLocation eventLocation = new EventLocation(1, LocalDateTime.now(),light1.getStreet() + " y "+light2.getStreet());
        TrafficFine Fine = new TrafficFine(342,1,typeInfraction,automobile,eventLocation,BigDecimal.valueOf(23151.33));


    }





    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
