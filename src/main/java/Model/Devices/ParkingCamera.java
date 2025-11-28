package Model.Devices;

import Fine.ParkingFine;
import Fine.TrafficFine;
import Model.*;
import dataBase.AutomobileDAO;
import dataBase.TrafficFineDAO;
import org.jxmapviewer.viewer.GeoPosition;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class ParkingCamera extends Device implements Runnable, GenerateFine {
    private static final long serialVersionUID = 1L;

    private int toleranceTime;
    private boolean running;
    private Map<String, LocalDateTime> parkedVehicles = new HashMap<>();
    private Automobile infractedAutomobile;
    private final BigDecimal PARKING_CAMERA_FINE_AMOUNT = BigDecimal.valueOf(85000.00);
    private final int PARKING_CAMERA_POINT_SCORING = 2;

    public ParkingCamera(String id, GeoPosition location, int toleranceTime) {
        super(id, location);
        this.toleranceTime = toleranceTime;
    }

    @Override
    public void run() {
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
        try {
            EventLocation location = new EventLocation(LocalDateTime.now(), "Parking camera ID: " + getId(), getLocation());

            TrafficFine fine = new ParkingFine(
                    0,
                    PARKING_CAMERA_POINT_SCORING,
                    TypesInfraction.ILLEGAL_PARKING,
                    getInfractedAutomobile(),
                    location,
                    PARKING_CAMERA_FINE_AMOUNT
            );

            new TrafficFineDAO().saveFine(fine);
            System.out.println("🚨 Speeding fine generated 🚨");
        } catch (Exception e) {
            System.err.println("Error while trying to save fine: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getTypeDevice() {
        return "parkingCamera";
    }

    public void simulateEntry() throws SQLException {
        Random random = new Random();
        AutomobileDAO dao = new AutomobileDAO();
        Automobile a = dao.getRandomAutomobile();

        if (random.nextDouble() < 0.1) {
            String licensePlate = a.getLicensePlate();

            if (!getParkedVehicles().containsKey(licensePlate)){
                getParkedVehicles().put(licensePlate,LocalDateTime.now());
            }
        }
    }

    public void simulateExit(){
        Random random = new Random();

        if (!getParkedVehicles().isEmpty() && random.nextDouble() < 0.05){
            List<String> plates = new ArrayList<>(getParkedVehicles().keySet());
            String onExitPlate = plates.get(random.nextInt(plates.size()));

            getParkedVehicles().remove(onExitPlate);
        }
    }

    public void checkInfraction(){
        LocalDateTime now = LocalDateTime.now();
        List<String> infractionPlates = new ArrayList<>();
        AutomobileDAO automobileDAO = new AutomobileDAO();

        for (Map.Entry<String, LocalDateTime> entry : getParkedVehicles().entrySet()){
            String plate = entry.getKey();
            LocalDateTime entryTime = entry.getValue();

            Duration parkedSeconds = Duration.between(entryTime, now);

            if (parkedSeconds.getSeconds() > getToleranceTime()){
                try{
                    Automobile automobile = automobileDAO.findAutomobileByPlate(plate);

                    setInfractedAutomobile(automobile);

                    fineGenerate();

                    setInfractedAutomobile(null);

                    infractionPlates.add(plate);
                } catch (SQLException e) {
                    System.err.println("Couldn't find automobile related to this plate ("+ plate + ")");
                }
            }
        }

        for (String plate : infractionPlates){
            getParkedVehicles().remove(plate);
        }
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.001) {
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

    public Map<String, LocalDateTime> getParkedVehicles() {return parkedVehicles;}
    public void setParkedVehicles(Map<String, LocalDateTime> parkedVehicles) {this.parkedVehicles = parkedVehicles;}

    public int getToleranceTime() {return toleranceTime;}
    public void setToleranceTime(int toleranceTime) {this.toleranceTime = toleranceTime;}

    public Automobile getInfractedAutomobile() {return infractedAutomobile;}
    public void setInfractedAutomobile(Automobile infractedAutomobile) {this.infractedAutomobile = infractedAutomobile;}
}
