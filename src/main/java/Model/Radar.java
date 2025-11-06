package Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.Random;

public class Radar extends Device implements Runnable{
    private int velocidadMaxima;
    private boolean running;


    public Radar(String id, GeoPosition location, int velocidadMaxima) {
        super(id, location);
        this.velocidadMaxima = velocidadMaxima;
    }

    @Override
    public void run() {
        System.out.println("Hilo radar empezó.");
        running = true;

        while (running){
            try {
                Thread.sleep(40000);

            } catch (InterruptedException e) {
            }
        }
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(int velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }


    public boolean detectarExcesoVelocidad(Automobile auto, int velocidadActual) {
        return velocidadActual > velocidadMaxima;
    }

    @Override
    public String getTypeDevice() {
        return "radar";
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextInt() < 0.5){
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;
            setTypeError(TypesErrors.values()[type]);
            setState(State.FAILURE);
            System.err.println("⚠️ Radar " + getId() + " failure: " + getTypeError());
            running = true;
        }
    }

    @Override
    public void FixError() {

    }
}
