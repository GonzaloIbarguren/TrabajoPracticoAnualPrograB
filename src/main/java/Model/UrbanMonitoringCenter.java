package Model;

import Model.Devices.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;
import ui.MapWindows;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UrbanMonitoringCenter implements Serializable {
    private List<Device> devices;
    private MapWindows map;
    private final String SERIALIZABLE_FILE = "system_state.ser";

    public UrbanMonitoringCenter(){
        if (!loadSystemState()){
            System.out.println("🚨 There's no previous state saved. Starting system...");
            this.devices = loadDevice("deviceLoadup/Device.json");
        } else
            System.out.println("🚨 Starting system from previous state");

        this.map = new MapWindows(devices);

        this.map.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("💾 Saving system's state before exiting...");
                saveSystemState();
            }
        });
    }

    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }

    public void saveSystemState(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZABLE_FILE))){
            oos.writeObject(devices);
            System.out.println("System state saved succesfully at " + SERIALIZABLE_FILE);
        } catch (IOException e){
            System.err.println("Error while saving system's state: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    "Error al guardar: " + e.getMessage() + "\n" + e.getClass().getSimpleName(),
                    "Error Fatal de Guardado", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean loadSystemState(){
        File file = new File(SERIALIZABLE_FILE);

        if (file.exists()){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                this.devices = (List<Device>) ois.readObject();
                return true;
            } catch (IOException | ClassNotFoundException e){
                System.err.println("Error while loading system's state: " + e.getMessage());
                //file.delete();
                return false;
            }
        } else
            return false;
    }

    public static void stateReset() {
        File file = new File("system_state.ser");

        if (file.exists()) {
            file.delete();
            System.out.println("✅ System's state has been successfully reset.");
        }

        System.exit(0);
    }

    public List<Device> loadDevice (String filePath){
        List<Device> listDevice = new ArrayList<>();

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
            if (is == null) throw new RuntimeException("File not found: " + filePath);

            String content = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            is.close();

            JSONObject root = new JSONObject(content);
            JSONArray array = root.getJSONArray("semaphoreControllers");

            for (int i =0;i<array.length();i++){
                JSONObject controller = array.getJSONObject(i);
                String id = controller.getString("id");

                double lat = controller.getDouble("lat");
                double lng = controller.getDouble("lng");

                JSONObject lightAJson = controller.getJSONObject("semA");
                TrafficLight lightA = new TrafficLight(
                        lightAJson.getString("street"),
                        lightAJson.getString("orientation"),
                        lightAJson.getBoolean("main")
                );

                JSONObject lightBJson = controller.getJSONObject("semB");
                TrafficLight lightB = new TrafficLight(
                        lightBJson.getString("street"),
                        lightBJson.getString("orientation"),
                        lightBJson.getBoolean("main")
                );
                listDevice.add(new TrafficLightController(id,new GeoPosition(lat,lng),lightA,lightB));
            }

            array = root.getJSONArray("securityCameras");
            for (int i =0;i<array.length();i++){
                JSONObject controller = array.getJSONObject(i);
                String id = controller.getString("id");

                double lat = controller.getDouble("lat");
                double lng = controller.getDouble("lng");

                listDevice.add(new SecurityCamera(id,new GeoPosition(lat,lng)));
            }

            array = root.getJSONArray("parkingCameras");
            for (int i =0;i<array.length();i++){
                JSONObject controller = array.getJSONObject(i);
                String id = controller.getString("id");

                double lat = controller.getDouble("lat");
                double lng = controller.getDouble("lng");
                int toleranceTime = controller.getInt("toleranceTime");

                listDevice.add(new ParkingCamera(id,new GeoPosition(lat,lng),toleranceTime));
            }

            array = root.getJSONArray("radars");
            for (int i =0;i<array.length();i++){
                JSONObject controller = array.getJSONObject(i);
                String id = controller.getString("id");

                double lat = controller.getDouble("lat");
                double lng = controller.getDouble("lng");
                int maximumVelocity = controller.getInt("maximumVelocity");

                listDevice.add(new Radar(id,new GeoPosition(lat,lng),maximumVelocity));
            }
        } catch (IOException e) {throw new RuntimeException(e);}

        return listDevice;
    }

    public List<Device> getDevices() {return devices;}
    public void setDevices(List<Device> devices) {this.devices = devices;}
}