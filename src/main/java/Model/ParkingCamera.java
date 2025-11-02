package Model;

import org.jxmapviewer.viewer.GeoPosition;

public class ParkingCamera extends Device implements GenerateFine{
    private int toleranceTime;
    public ParkingCamera(String id, GeoPosition location, int toleranceTime) {
        super(id, location);
        this.toleranceTime = toleranceTime;
    }

    public int getToleranceTime() {
        return toleranceTime;
    }

    public void setToleranceTime(int toleranceTime) {
        this.toleranceTime = toleranceTime;
    }

    @Override
    public void fineGenerate() {

    }

    @Override
    public String getTypeDevice() {
        return "parkingCamera";
    }
}
