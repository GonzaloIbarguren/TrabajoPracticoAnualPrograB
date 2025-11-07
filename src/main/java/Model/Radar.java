package Model;

import dataBase.AutomobileDAO;
import org.jxmapviewer.viewer.GeoPosition;

import java.sql.SQLException;
import java.util.Random;

public class Radar extends Device implements Runnable, GenerateFine{
    private int velocidadMaxima;
    private boolean running;

    public Radar(String id, GeoPosition location, int velocidadMaxima) {
        super(id, location);
        this.velocidadMaxima = velocidadMaxima;
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


            } catch (InterruptedException | SQLException e) {
                System.out.println("Radar failure.");
                running = false;
            }
        }
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(int velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }


    /*public boolean detectarExcesoVelocidad(Automobile auto, int velocidadActual) {
        return velocidadActual > velocidadMaxima;
    }*/

    @Override
    public String getTypeDevice() {
        return "radar";
    }

    public void simulateSpeeding() throws SQLException {
        Random random = new Random();
        AutomobileDAO dao = new AutomobileDAO();
        Automobile a = new Automobile();
        a = dao.getRandomAutomobile();

        if (random.nextDouble() < 0.2){
            int simulatedVelocity = random.nextInt(0, 450);

            if (simulatedVelocity > getVelocidadMaxima()){
                String licensePlate = a.getLicensePlate();
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
            running = false;
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
