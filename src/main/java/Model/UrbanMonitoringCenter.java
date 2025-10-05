package Model;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UrbanMonitoringCenter{
    private boolean running;
    private List<Device> devices;
    private List<TrafficLightController> controllers = loadTrafficsLights("TrafficLigths.json");
    private MapWindows map;
    private TrafficLightWindows windows;

    public UrbanMonitoringCenter() {
        this.devices = new ArrayList<>();
        this.map = new MapWindows(controllers);



    }
    public List<TrafficLightController> loadTrafficsLights (String filePath){
        List<TrafficLightController> listControllers = new ArrayList<>();

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
                listControllers.add(new TrafficLightController(id,lat,lng,lightA,lightB));
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listControllers;
    }

    public static void main(String[] arg){
        new UrbanMonitoringCenter();
    }
}
