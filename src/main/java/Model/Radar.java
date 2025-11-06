package Model;

import org.jxmapviewer.viewer.GeoPosition;

public class Radar extends Device {
    private int velocidadMaxima;


    public Radar(String id, GeoPosition location, int velocidadMaxima) {
        super(id, location);
        this.velocidadMaxima = velocidadMaxima;
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

    }

    @Override
    public void FixError() {

    }
}
