package Model;

import dataBase.TrafficFineDAO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;
import ui.MapWindows;
import ui.TrafficLightWindows;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UrbanMonitoringCenter{
    private boolean running;
    private List<Device> devices = loadDevice("Device.json");

    private MapWindows map;
    private TrafficLightWindows windows;

    public UrbanMonitoringCenter()  {
        this.map = new MapWindows(devices);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Device> loadDevice (String filePath){
        List<Device> listDevice = new ArrayList<>();

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
            if (is == null) throw new RuntimeException("No se encontró el archivo: " + filePath);

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


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listDevice;
    }

    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
