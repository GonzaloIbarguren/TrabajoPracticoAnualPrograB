package Fine;

import Model.Automobile;
import Model.EventLocation;
import Model.TypesInfraction;

import java.math.BigDecimal;
import java.util.Random;

public class RedLightFine extends TrafficFine{

    private final String[] TRAFFIC_LIGHT_INFRACTIONS = {
            "/trafficInfractionImages/traffic-light-infraction1.jpg",
            "/trafficInfractionImages/traffic-light-infraction2.png",
            "/trafficInfractionImages/traffic-light-infraction3.jpg",
            "/trafficInfractionImages/traffic-light-infraction4.jpg",
    };

    public RedLightFine(int fineNumber, int pointScoring, TypesInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        super(fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
    }

    @Override
    protected String getInfractionImage(){
        Random random = new Random();

        int index = random.nextInt(TRAFFIC_LIGHT_INFRACTIONS.length);
        return TRAFFIC_LIGHT_INFRACTIONS[index];
    }
}
