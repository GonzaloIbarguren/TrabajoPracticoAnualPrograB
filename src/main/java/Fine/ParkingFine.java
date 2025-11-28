package Fine;

import Model.Automobile;
import Model.EventLocation;
import Model.TypesInfraction;

import java.math.BigDecimal;
import java.util.Random;

public class ParkingFine extends TrafficFine{

    private final String[] PARKING_INFRACTIONS = {
            "/trafficInfractionImages/parking-infringement1.jpg",
            "/trafficInfractionImages/parking-infringement2.jpg",
            "/trafficInfractionImages/parking-infringement3.jpeg",
            "/trafficInfractionImages/parking-infringement4.jpeg",
    };

    public ParkingFine(int fineNumber, int pointScoring, TypesInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        super(fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
    }

    @Override
    protected String getInfractionImage(){
        Random random = new Random();

        int index = random.nextInt(PARKING_INFRACTIONS.length);
        return PARKING_INFRACTIONS[index];
    }
}
