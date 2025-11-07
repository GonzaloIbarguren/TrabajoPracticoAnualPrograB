package Model;

import dataBase.AutomobileDAO;
import dataBase.DataBaseConnection;
import dataBase.TrafficFineDAO;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;


public class TrafficLightController extends Device implements Runnable, GenerateFine{
    private LocalTime starTimeIntermittent, endTimeIntermittent;
    private int durationRed,durationGreen,durationYellow,durationTwoRed;
    private TrafficLight light1,light2;
    private boolean running,intermittent;

    public TrafficLightController(String id,GeoPosition pos, TrafficLight light1, TrafficLight light2) {
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
                isIntermittentTime();
                    if (intermittent){
                        RunIntermittentMode();
                    }else{
                        RunNormalCycle();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Traffic Ligth error");
                    running = false;
                }
            SimulateError();
            }
    }
    @Override
    public void fineGenerate() {
        try {
            Automobile randomAuto = new AutomobileDAO().getRandomAutomobile();

            EventLocation location = new EventLocation(LocalDateTime.now(),light1.getStreet()+" y "+light2.getStreet(),getLocation());
            TrafficFine fine = new RedLightFine(0,100,TypesInfraction.RED_LIGHT, randomAuto, location, BigDecimal.valueOf(10000));

            new TrafficFineDAO().saveFine(fine);
            System.out.println("Fine generated: " + fine);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void maybeGenerateFine() {
        Random random = new Random();
        double probability = 0.05;

        if (random.nextDouble() < probability) {
            System.out.println(" A vehicle ran the red light at " + getId());
            fineGenerate();
        }
    }

    @Override
    public void SimulateError() {
        Random random = new Random();
        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.10) {
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;
            setTypeError(TypesErrors.values()[type]);
            setState(State.FAILURE);
            System.err.println("Traffic light " + getId() + " failed: " + getTypeError());
            light1.setState(Color.MAGENTA);
            light2.setState(Color.MAGENTA);
            running = false;
        }
    }

    @Override
    public void FixError() {
        if (getState() == State.FAILURE) {
            System.err.println("Rebooting traffic light " + getId() + "...");

            setTypeError(TypesErrors.NONE);
            setState(State.OPERATIONAL);

            light1.setState(Color.RED);
            light2.setState(Color.RED);

            System.err.println("Traffic light " + getId() + " restored successfully.");

            if (!running) {
                running = true;
                intermittent = true;

                Thread t = new Thread(this);
                t.start();
            }
        }
    }



    private Boolean isIntermittentTime(){
        if (starTimeIntermittent == null || endTimeIntermittent == null) return false;
        LocalTime now = LocalTime.now();
        intermittent = now.isAfter(starTimeIntermittent) && now.isBefore(endTimeIntermittent);
        return intermittent;
    }
    public void RunIntermittentMode() throws InterruptedException {
        System.out.println("Intermitencia");
        if (running){
            light1.setState(Color.YELLOW);
            light2.setState(Color.YELLOW);
            Thread.sleep(2000);

            light1.setState(Color.BLACK);
            light2.setState(Color.BLACK);
            Thread.sleep(2000);
        }
    }

    public void RunNormalCycle() throws InterruptedException{
        if (running) {
            light2.setState(Color.RED);
            light1.setState(Color.GREEN);
            System.out.println("verde");
            Thread.sleep(durationGreen);
            System.out.println("amarillo");
            light1.nextState();
            Thread.sleep(durationYellow);
            light1.nextState();

            maybeGenerateFine();

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

    public boolean getIntermittent() {
        return intermittent;
    }

    public void setIntermittent(boolean intermittent) {
        this.intermittent = intermittent;
    }

    @Override
    public String getTypeDevice() {
        return "trafficLightController";
    }


}
