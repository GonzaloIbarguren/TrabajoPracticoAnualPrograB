package Model;

import java.math.BigDecimal;

public class RedLightFine extends TrafficFine{

    public RedLightFine(int fineNumber, int pointScoring, TypesInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        super(fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
    }
}
