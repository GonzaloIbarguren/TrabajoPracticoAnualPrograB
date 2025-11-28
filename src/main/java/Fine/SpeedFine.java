package Fine;

import Model.Automobile;
import Model.EventLocation;
import Model.TypesInfraction;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

public class SpeedFine extends TrafficFine{

    private double automobileSpeed;
    private double speedingLimit;
    private final String[] SPEED_INFRACTIONS = {
        "/trafficInfractionImages/speed-excess1.jpg",
        "/trafficInfractionImages/speed-excess2.jpg",
    };

    public SpeedFine(double automobileSpeed, double speedingLimit, int fineNumber, int pointScoring, TypesInfraction typeInfraction, Automobile automobile, EventLocation locationEvent, BigDecimal baseAmount) {
        super(fineNumber, pointScoring, typeInfraction, automobile, locationEvent, baseAmount);
        this.automobileSpeed = automobileSpeed;
        this.speedingLimit = speedingLimit;
    }

    @Override
    protected String getInfractionImage(){
        Random random = new Random();

        int index = random.nextInt(SPEED_INFRACTIONS.length);
        return SPEED_INFRACTIONS[index];
    }

    @Override
    protected void infractionDetails(PDPageContentStream content) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.newLineAtOffset(90, 540);
        content.showText("Speed Detected: " + getAutomobileSpeed() + " km/h");
        content.newLineAtOffset(90, -15);
        content.showText("Zone Limit: " + getSpeedLimit() + " km/h");
        content.endText();
    }

    public void setAutomobileSpeed(double automobileSpeed){this.automobileSpeed = automobileSpeed;}
    public double getAutomobileSpeed(){return automobileSpeed;}
    public void setSpeedLimit(double speedLimit){this.speedingLimit = speedLimit;}
    public double getSpeedLimit(){return speedingLimit;}
}
