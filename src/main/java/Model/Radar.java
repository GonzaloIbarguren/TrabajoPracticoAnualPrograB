package Model;

import java.math.BigDecimal;

public class Radar extends Device implements GenerateFine{

    private double speedLimit;
    private EventLocation speedingLocation;
    private Automobile speedingAutomobile;
    private TypeInfraction speedingType;

    public Radar(double speedLimit){
        this.speedLimit = speedLimit;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public SpeedFine detectAndFine (double automobileSpeed, double speedingLimit, int fineNumber, int pointScoring, TypeInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount){
        if (automobileSpeed  > speedLimit){
            return new SpeedFine(automobileSpeed, speedingLimit, fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
        }
        else
            return null;
    }

    @Override
    public void fineGenerate() {
        speedingLocation = ;
        speedingAutomobile = ;
        speedingType = ;

        TrafficFine radarFine = new SpeedFine();
    }
}
