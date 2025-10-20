package Model;

import org.jxmapviewer.viewer.GeoPosition;

public class ParkingCamera extends Device implements GenerateFine{
    private int toleranceTime;
    public ParkingCamera(String id, GeoPosition location) {
        super(id, location);
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
}
