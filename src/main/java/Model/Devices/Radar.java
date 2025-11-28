package Model.Devices;

import Fine.SpeedFine;
import Fine.TrafficFine;
import Model.*;
import dataBase.AutomobileDAO;
import dataBase.TrafficFineDAO;
import org.jxmapviewer.viewer.GeoPosition;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class Radar extends Device implements Runnable, GenerateFine {
    private static final long serialVersionUID = 1L;

    private int maximumVelocity;
    private boolean running;
    private int speedInfringement;

    private final BigDecimal SPEED_EXCESS_FINE_BASE_AMOUNT = BigDecimal.valueOf(180000.00);
    private final int SPEED_EXCESS_BASE_POINT_SCORING = 5;
    private final double SPEED_EXCESS_RECHARGE_PER_INFRINGEMENT = 0.05;
    private final int SPEED_EXCESS_ADDITIONAL_POINTS_PER_INFRINGEMENT = 1;

    public Radar(String id, GeoPosition location, int maximumVelocity) {
        super(id, location);
        this.maximumVelocity = maximumVelocity;
    }

    @Override
    public void run() {
        running = true;

        while (running){
            try {
                Thread.sleep(3000);

                if (getState() == State.OPERATIONAL)
                    simulateSpeeding();

            } catch (InterruptedException | SQLException e) {
                System.out.println("Radar failure.");
                running = false;
            }
            SimulateError();
        }
    }

    public void simulateSpeeding() throws SQLException {
        Random random = new Random();

        if (random.nextDouble() < 0.1){
            speedInfringement = random.nextInt(0, 115);

            if (getSpeedInfringement() > getMaximumVelocity()){
                System.out.println("A car went over the speed limit ("+ getSpeedInfringement() + ")");
                fineGenerate();
            }
        }
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.005){
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;
            setTypeError(TypesErrors.values()[type]);
            setState(State.FAILURE);
            System.err.println("Radar " + getId() + " failure: " + getTypeError());
            running = false;
        }
    }

    @Override
    public void fineGenerate(){
        try {
            int maxSpeed = getMaximumVelocity();
            int actualSpeed = getSpeedInfringement();

            double excess = ((double) (actualSpeed - maxSpeed) / maxSpeed) * 100.0;
            int increment = (int) Math.floor(excess/ 10.0);

            int finalPointsScoring = SPEED_EXCESS_BASE_POINT_SCORING + (increment * SPEED_EXCESS_ADDITIONAL_POINTS_PER_INFRINGEMENT);
            double totalRecharge = increment * SPEED_EXCESS_RECHARGE_PER_INFRINGEMENT;

            BigDecimal recharge = BigDecimal.ONE.add(BigDecimal.valueOf(totalRecharge));
            BigDecimal finalAmount = SPEED_EXCESS_FINE_BASE_AMOUNT.multiply(recharge).setScale(2, RoundingMode.HALF_UP);

            Automobile randomAuto = new AutomobileDAO().getRandomAutomobile();

            EventLocation location = new EventLocation(LocalDateTime.now(), "Radar ID: " + getId(), getLocation());

            TrafficFine fine = new SpeedFine(
                    actualSpeed,
                    maxSpeed,
                    0,
                    finalPointsScoring,
                    TypesInfraction.SPEEDING,
                    randomAuto,
                    location,
                    finalAmount
            );

            new TrafficFineDAO().saveFine(fine);
            System.out.println("🚨 Speeding fine generated 🚨");

        } catch (Exception e) {
            System.err.println("Critic error in Radar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void FixError() {
        if (getState() == State.FAILURE) {
            System.err.println("Rebooting Radar " + getId() + "...");

            setTypeError(TypesErrors.NONE);
            setState(State.OPERATIONAL);

            System.err.println("Radar " + getId() + " restored successfully.");

            running = true;
            run();
        }
    }

    @Override
    public String getTypeDevice() {return "radar";}

    public int getMaximumVelocity() {return maximumVelocity;}
    public void setMaximumVelocity(int maximumVelocity) {this.maximumVelocity = maximumVelocity;}

    public int getSpeedInfringement() {return speedInfringement;}
    public void setSpeedInfringement(int speedInfringement) {this.speedInfringement = speedInfringement;}
}
