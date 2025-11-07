package Model;

import java.math.BigDecimal;

public class SpeedFine extends TrafficFine{

    private double automobileSpeed;
    private double speedingLimit;

    public SpeedFine(double automobileSpeed, double speedingLimit, int fineNumber, int pointScoring, TypeInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        super(fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
        this.automobileSpeed = automobileSpeed; // automobile.getCurrentSpeed();
        this.speedingLimit = speedingLimit; //radar.getSpeedLimit();
    }

    public void setAutomobileSpeed(double automobileSpeed){this.automobileSpeed = automobileSpeed;}

    public double getAutomobileSpeed(){return automobileSpeed;}

    public void setSpeedLimit(double speedLimit){this.speedingLimit = speedLimit;}

    public double getSpeedLimit(){return speedingLimit;}
}
