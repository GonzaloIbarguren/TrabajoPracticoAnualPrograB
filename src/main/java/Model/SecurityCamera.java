package Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.sql.SQLException;
import java.util.Random;

public class SecurityCamera extends Device implements Runnable{
    private boolean running;

    public SecurityCamera(String id, GeoPosition location) {
        super(id, location);
    }

    @Override
    public String getTypeDevice() {
        return "securityCamera";
    }

    @Override
    public void run() {
        System.out.println("Hilo camara seguridad empezó");
        running = true;

        while (running) {
            try {
                Thread.sleep(1000);

                if (getState() == State.OPERATIONAL){
                    simulateAnomaly();
                }

            } catch (InterruptedException e) {
                System.out.println("Security camera failure.");
                running = false;
            }
            SimulateError();
        }
    }

    public void simulateAnomaly() {

    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.4) {
            int type = random.nextInt(TypesErrors.values().length - 1) + 1;

            setTypeError(TypesErrors.values()[type]);

            setState(State.FAILURE);

            System.err.println("Security Camera " + getId() + " failed: " + getTypeError());

            running = false;
        }
    }

    @Override
    public void FixError() {
        if (getState() == State.FAILURE) {

            System.err.println("Rebooting Security Camera " + getId() + "...");

            setTypeError(TypesErrors.NONE);

            setState(State.OPERATIONAL);

            System.err.println("Security Camera " + getId() + " restored successfully.");

            running = true;

            run();
        }
    }
}
