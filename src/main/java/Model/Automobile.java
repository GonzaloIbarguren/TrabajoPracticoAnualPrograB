package Model;

import java.awt.*;

public class Automobile {
    private String licensePlate;
    private String owner;
    private String address;

    public Automobile(String licensePlate,String owner,String address){
        this.address = address;
        this.licensePlate = licensePlate;
        this.owner = owner;
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
