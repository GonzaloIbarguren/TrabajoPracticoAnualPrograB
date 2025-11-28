package Model;

import java.io.Serializable;

public class Automobile implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String licensePlate;
    private String owner;
    private String address;
    private String color;
    private int id_model;

    public Automobile() {
    }

    public Automobile(int id, String licensePlate, String owner, String address, String color, int id_model){
        this.id = id;
        this.address = address;
        this.licensePlate = licensePlate;
        this.owner = owner;
        this.color = color;
        this.id_model = id_model;

    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public int getId_model() {
        return id_model;
    }
    public void setId_model(int id_model) {
        this.id_model = id_model;
    }

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
