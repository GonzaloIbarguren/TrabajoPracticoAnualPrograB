package Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.Random;

public class Radar extends Device implements Runnable, GenerateFine{
    private int maximumVelocity;
    private boolean running;

    public Radar(String id, GeoPosition location, int maximumVelocity) {
        super(id, location);
        this.maximumVelocity = maximumVelocity;
    }

    @Override
    public void run() {
        System.out.println("Hilo radar empezó.");
        running = true;

        while (running){
            try {
                Thread.sleep(6000);

                if (getState() == State.OPERATIONAL)
                    simulateSpeeding();


            } catch (InterruptedException e) {
                System.out.println("Radar failure.");
                running = false;
            }
        }
    }

    public int getMaximumVelocity() {
        return maximumVelocity;
    }

    public void setMaximumVelocity(int maximumVelocity) {
        this.maximumVelocity = maximumVelocity;
    }

    @Override
    public String getTypeDevice() {
        return "radar";
    }

    public void simulateSpeeding(){
        Random random = new Random();

        if (random.nextDouble() < 0.2){
            int simulatedVelocity = random.nextInt(0, 450);

            if (simulatedVelocity > getMaximumVelocity()){
                String licensePlate = generateRandomLicensePlate();
                fineGenerate();
            }
        }
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextInt() < 0.15){
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;
            setTypeError(TypesErrors.values()[type]);
            setState(State.FAILURE);
            System.err.println("⚠️ Radar " + getId() + " failure: " + getTypeError());
            running = true;
        }
    }

    @Override
    public void fineGenerate(){
        
    }

    @Override
    public void FixError() {

        if (getState() == State.FAILURE) {
            System.err.println("👷‍♂️ Rebooting Parking Camera " + getId() + "...");

            setTypeError(TypesErrors.NONE);
            setState(State.OPERATIONAL);

            System.err.println("✅ Parking Camera " + getId() + " restored successfully.");

            running = true;
            run();
        }
    }
}
