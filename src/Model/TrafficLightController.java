package Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class TrafficLightController extends Device implements Runnable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalTime starTimeIntermittent, endTimeIntermittent;
    private int durationRed,durationGreen,durationYellow,durationTwoRed;
    private GeoPosition location;
    private String street1,street2;
    private TrafficLight light1,light2;
    private boolean running;
    private LocalDateTime startCycle;



    public TrafficLightController(String street1,String street2) {
        this.durationGreen = 40000;
        this.durationYellow = 4000;
        this.durationTwoRed = 3000;
        this.durationRed = 30000;
        this.startCycle = LocalDateTime.now();
        light1 = new TrafficLight(street1);
        light2 = new TrafficLight(street2);
        light1.setState(Color.GREEN);
        light2.setState(Color.RED);
        light1.setMain();
    }

    @Override
    public void run() {
        System.out.println("El hilo empezo");
        running = true;
        while (running) {
            try {
                    if (isIntermittentTime()){
                        RunIntermittentMode();
                    }else{
                        RunNormalCycle();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Traffic Ligth error");
                    running = false;
                }
            }
    }

        private Boolean isIntermittentTime(){
        if (starTimeIntermittent == null || endTimeIntermittent == null) return false;
        LocalTime now = LocalTime.now();
        return(now.isAfter(starTimeIntermittent) && now.isBefore(endTimeIntermittent));
    }
    private void RunIntermittentMode() throws  InterruptedException{
        System.out.println("Intermitencia");
        while (running && isIntermittentTime()){
            light1.setState(Color.YELLOW);
            light2.setState(Color.YELLOW);
            Thread.sleep(2000);

            light1.setState(Color.BLACK);
            light2.setState(Color.BLACK);
            Thread.sleep(2000);
        }
    }

    public void RunNormalCycle() throws InterruptedException{
                    light2.setState(Color.RED);
                    light1.setState(Color.GREEN);
                    System.out.println("verde");
                    Thread.sleep(durationGreen);
                    System.out.println("amarillo");
                    light1.nextState();
                    Thread.sleep(durationYellow);
                    light1.nextState();
                    System.out.println("doble rojo");
                    Thread.sleep(durationTwoRed);
                    light2.nextState();
                    System.out.println("Rojo");
                    Thread.sleep(durationRed);
                    light2.nextState();
                    System.out.println("sec amarillo");
                    Thread.sleep(durationYellow);
                    light2.nextState();
                    System.out.println("doble rojos");
                    Thread.sleep(durationTwoRed);

    }
    public void setIntermittentTime(LocalTime start,LocalTime end){
        this.starTimeIntermittent = start;
        this.endTimeIntermittent = end;
    }

    public void stop(){
        this.running = false;}

    public GeoPosition getLocation() {
        return location;
    }

    public void setLocation(GeoPosition location) {
        this.location = location;
    }

    public TrafficLight getLightMain() {
        return light1;
    }

    public void setDurationTwoRed(int durationTwoRed) {
        this.durationTwoRed = durationTwoRed;
    }

    public void setDurationYellow(int durationYellow) {
        this.durationYellow = durationYellow;
    }

    public void setDurationGreen(int durationGreen) {
        this.durationGreen = durationGreen;
    }

    public void setDurationRed(int durationRed) {
        this.durationRed = durationRed;
    }
}
