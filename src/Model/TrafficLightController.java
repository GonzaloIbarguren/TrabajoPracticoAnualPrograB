package Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.Duration;

public class TrafficLightController extends Device implements Runnable, Serializable {
    private LocalTime starTimeIntermittent, endTimeIntermittent;
    private int durationRed,durationGreen,durationYellow,durationTwoRed;
    private TrafficLight semaphore1,semaphore2;
    private GeoPosition location;
    private TrafficLight light1,light2;
    private boolean running;

    public TrafficLightController() {
        this.durationGreen = 40000;
        this.durationYellow = 4000;
        this.durationTwoRed = 3000;
        this.durationRed = 30000;
        light1 = new TrafficLight();
        light2 = new TrafficLight();
        light1.setState(Color.GREEN);
        light2.setState(Color.RED);
        light1.setMain();
    }

    @Override
    public void run() {
        System.out.println("El hilo empezo");
        running = true;
        if (light1.getState().equals(Color.GREEN))
        while (running) {
            try {
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
                light1.nextState();

            } catch (InterruptedException e) {
                System.out.println("Traffic Ligth error");
                running = false;
            }
        }
        if (light1.getState().equals(Color.RED))
            while (running) {
                try {
                    System.out.println("Rojo");
                    Thread.sleep(durationRed);
                    light2.nextState();
                    System.out.println("sec amarillo");
                    Thread.sleep(durationYellow);
                    light2.nextState();
                    System.out.println("doble rojos");
                    Thread.sleep(durationTwoRed);
                    light1.nextState();
                    System.out.println("verde");
                    Thread.sleep(durationGreen);
                    System.out.println("amarillo");
                    light1.nextState();
                    Thread.sleep(durationYellow);
                    light1.nextState();
                    System.out.println("doble rojo");
                    Thread.sleep(durationTwoRed);
                    light2.nextState();

                } catch (InterruptedException e) {
                    System.out.println("Traffic Ligth error");
                    running = false;
                }
            }
        if (light1.getState().equals(Color.YELLOW))
            while (running) {
                try {
                    System.out.println("amarillo");
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
                    light1.nextState();
                    System.out.println("verde");
                    Thread.sleep(durationGreen);
                    light1.nextState();

                } catch (InterruptedException e) {
                    System.out.println("Traffic Ligth error");
                    running = false;
                }
            }
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
