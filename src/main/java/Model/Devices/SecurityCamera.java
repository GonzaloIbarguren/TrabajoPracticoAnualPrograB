package Model.Devices;

import Model.State;
import Model.TypesErrors;
import org.jxmapviewer.viewer.GeoPosition;
import java.util.Random;

public class SecurityCamera extends Device implements Runnable {
    private static final long serialVersionUID = 1L;

    private boolean running;

    public SecurityCamera(String id, GeoPosition location) {
        super(id, location);
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.err.println("Security camera failure.");
                running = false;
            }
            SimulateError();
        }
    }

    @Override
    public void SimulateError() {
        Random random = new Random();

        if (getState() == State.OPERATIONAL && random.nextDouble() < 0.004) {
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

    @Override
    public String getTypeDevice() {
        return "securityCamera";
    }
}
