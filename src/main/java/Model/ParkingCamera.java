package Model;

import dataBase.AutomobileDAO;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class ParkingCamera extends Device implements Runnable, GenerateFine{
    private int toleranceTime;
    private boolean running;
    private Map<String, LocalDateTime> parkedVehicles = new HashMap<>();

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
    public void run() {
        System.out.println("Hilo camara estacionamiento empezó");
        running = true;

        while (running) {
            try {
                Thread.sleep(3000);

                if (getState() == State.OPERATIONAL){
                    simulateExit();
                    checkInfraction();
                    simulateEntry();
                }

            } catch (InterruptedException e) {
                System.out.println("Parking camera failure.");
                running = false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            SimulateError();
        }

    }

    @Override
    public void fineGenerate() {

    }

    @Override
    public String getTypeDevice() {
        return "parkingCamera";
    }

    public void simulateEntry() throws SQLException {
        Random random = new Random();
        AutomobileDAO dao = new AutomobileDAO();
        Automobile a = new Automobile();
        a = dao.getRandomAutomobile();

        if (random.nextDouble() < 0.6) {
            String licensePlate = a.getLicensePlate();

            if (!getParkedVehicles().containsKey(licensePlate)){
                getParkedVehicles().put(licensePlate,LocalDateTime.now());
            }
        }
    }

    public void simulateExit(){
        Random random = new Random();

        if (!getParkedVehicles().isEmpty() && random.nextDouble() < 0.3){
            List<String> plates = new ArrayList<>(getParkedVehicles().keySet());
            String onExitPlate = plates.get(random.nextInt(plates.size()));

            getParkedVehicles().remove(onExitPlate);
        }
    }

    public void checkInfraction(){
        LocalDateTime now = LocalDateTime.now();
        List<String> infractionPlates = new ArrayList<>();

        for (Map.Entry<String, LocalDateTime> entry : getParkedVehicles().entrySet()){
            String plate = entry.getKey();
            LocalDateTime entryTime = entry.getValue();

            Duration parkedSeconds = Duration.between(entryTime, now);

            if (parkedSeconds.getSeconds() > getToleranceTime()){
                fineGenerate();
                infractionPlates.add(plate);
            }
        }

        for (String plate : infractionPlates){
            getParkedVehicles().remove(plate);
        }
    }

    public Map<String, LocalDateTime> getParkedVehicles() {
        return parkedVehicles;
    }

    public void setParkedVehicles(Map<String, LocalDateTime> parkedVehicles) {
        this.parkedVehicles = parkedVehicles;
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.4) {
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;

            setTypeError(TypesErrors.values()[type]);

            setState(State.FAILURE);

            System.err.println("Parking camera " + getId() + " failed: " + getTypeError());

            running = false;
        }
    }

    @Override
    public void FixError() {

        if (getState() == State.FAILURE) {

            System.err.println("Rebooting Parking Camera " + getId() + "...");

            setTypeError(TypesErrors.NONE);

            setState(State.OPERATIONAL);

            System.err.println("Parking Camera " + getId() + " restored successfully.");

            running = true;

            run();
        }
    }
}
