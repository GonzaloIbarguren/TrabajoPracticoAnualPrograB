package Model;

import java.awt.*;

public class Automobile {
    private int id;
    private String licensePlate;
    private String owner;
    private String address;
    private AutomobileModel model;

    public Automobile() {
    }

    public Automobile(int id, String licensePlate, String owner, String address,AutomobileModel model){
        this.id = id;
        this.address = address;
        this.licensePlate = licensePlate;
        this.owner = owner;
        this.model = model;
    }

    public AutomobileModel getModel() {
        return model;
    }

    public void setModel(AutomobileModel model) {
        this.model = model;
    }

    public int getId() {
        return id;
    }

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
