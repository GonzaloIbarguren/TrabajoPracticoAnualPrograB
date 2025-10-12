package Model;

public class Location {

    private double latitud;
    private double longitud;

    public Location(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Location() {
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


}
