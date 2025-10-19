package Model;

import dataBase.DataBaseConnection;
import dataBase.TrafficFineDAO;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TrafficLightController extends Device implements Runnable, Serializable, GenerateFine{
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalTime starTimeIntermittent, endTimeIntermittent;
    private int durationRed,durationGreen,durationYellow,durationTwoRed;
    private TrafficLight light1,light2;
    private boolean running;
    private LocalDateTime startCycle;




    public TrafficLightController() {
        super(null,null);
    }

    public TrafficLightController(String id, double latitude, double longitude, TrafficLight light1, TrafficLight light2) {
        GeoPosition pos = new GeoPosition(latitude,longitude);
        super(id,pos);

        this.light1 = light1;
        this.light2 = light2;

        durationGreen = 40000;
        durationYellow = 4000;
        durationTwoRed = 3000;
        durationRed = 30000;
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
    @Override
    public void fineGenerate() {

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

    public void changeMainLight(){
            if (light1.getMain()) {
                light2.setMain(true);
                light1.setMain(false);
            }else {
                light1.setMain(true);
                light2.setMain(false);
            }
    }


    public TrafficLight getLightSecundary() {
        if (light1.getMain())
            return light2;
        else
            return light1;
    }
    public TrafficLight getLightMain() {
        if (light1.getMain())
            return light1;
        else
            return light2;
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
