package ui;

import Model.Orientation;
import Model.TrafficLightController;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapWindows extends JFrame {
    private final List<TrafficLightController> trafficlights = new CopyOnWriteArrayList<>(loadTrafficLights());
    private boolean addingTrafficLight = false;
    private boolean deletingTrafficLight = false;

    public MapWindows() {
        super("MAP");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        JXMapViewer map = new JXMapViewer();
        TileFactoryInfo info = new TileFactoryInfo(
                4, 20, 20,
                256, true, true,
                "https://cartodb-basemaps-a.global.ssl.fastly.net/light_all/",
                "x", "y", "z") {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                int invZoom = getMaximumZoomLevel() - zoom;
                return this.baseURL + invZoom + "/" + x + "/" + y + ".png";
            }
        };

        map.setTileFactory(new DefaultTileFactory(info));

        var center = new GeoPosition(-38.0055, -57.5426);
        map.setZoom(6);
        map.setAddressLocation(center);
        PanMouseInputListener panListener = new PanMouseInputListener(map);
        map.addMouseListener(panListener);
        map.addMouseMotionListener(panListener);
        map.addMouseWheelListener(e -> {
            int zoom = map.getZoom();

            if (e.getWheelRotation() < 0 && zoom > 3) {
                map.setZoom(zoom - 1);
            }
            if (e.getWheelRotation() > 0 && zoom < 5) {
                map.setZoom(zoom + 1);
            }
        });
      /* JButton addButton = new JButton("Agregar Semaforo");

        JButton deleteButton = new JButton("Eliminar Semaforo");
        addButton.addActionListener( e->{
            addingTrafficLight = !addingTrafficLight;
            addButton.setText(addingTrafficLight ? "Agrgacion activa":"Agregar semaforos");
            if (deletingTrafficLight) {
                deletingTrafficLight = false;
                deleteButton.setText("Eliminar Semaforo");
            }
        });

        deleteButton.addActionListener(e -> {
            deletingTrafficLight = !deletingTrafficLight;
            deleteButton.setText(deletingTrafficLight? "Eliminacion Activa": "Eliminar Semaforo");
            if (addingTrafficLight){
                addingTrafficLight = false;
                addButton.setText("Agregar Semaforos");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);buttonPanel.add(deleteButton);
        add(buttonPanel,BorderLayout.SOUTH);*/

       map.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               Point clickedPoint = e.getPoint();
               Rectangle viewport = map.getViewportBounds();
               int x = viewport.x + clickedPoint.x;
               int y = viewport.y + clickedPoint.y;
               Point2D point2D = new Point2D.Double(x, y);
               GeoPosition geo = map.getTileFactory().pixelToGeo(point2D, map.getZoom());

               System.out.println(+ geo.getLatitude()+","+ geo.getLongitude());

               TrafficLightController clickedTrafficLight = null;
               double threshold = 0.0001;
                for (TrafficLightController controller : trafficlights){
                    double dx = Math.abs(controller.getLocation().getLatitude() - geo.getLatitude());
                    double dy = Math.abs(controller.getLocation().getLongitude() - geo.getLongitude());

                    if (dx < threshold && dy < threshold){
                        clickedTrafficLight = controller;
                    }
                }
                if (clickedTrafficLight !=null){
                    new  TrafficLightWindows(clickedTrafficLight);
                }
           }
       });

    /*  map.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {

                   Point clickedPoint = e.getPoint();
                   Rectangle viewport = map.getViewportBounds();
                   int x = viewport.x + clickedPoint.x;
                   int y = viewport.y + clickedPoint.y;
                   Point2D point2D = new Point2D.Double(x, y);
                   GeoPosition geo = map.getTileFactory().pixelToGeo(point2D, map.getZoom());

               if (addingTrafficLight){
                   String street1 = JOptionPane.showInputDialog("Ingrese la primera calle:");
                   String street2 = JOptionPane.showInputDialog("Ingrese la segunda calle:");
                   TrafficLightController controller = new TrafficLightController(street1,street2);
                   controller.setLocation(geo);
                   trafficlights.add(controller);
                   new Thread(controller).start();
                   map.repaint();
               } else if (deletingTrafficLight) {
                   TrafficLightController toRemove = null;
                   double threshold = 0.0001;

                   for(TrafficLightController lightController : trafficlights){
                       double dx = Math.abs(lightController.getLocation().getLatitude()-geo.getLatitude());
                       double dy = Math.abs(lightController.getLocation().getLongitude()-geo.getLongitude());

                       if (dx < threshold && dy < threshold){
                           toRemove = lightController;
                           break;
                       }
                   }
                   if (toRemove != null){
                     trafficlights.remove(toRemove);
                     map.repaint();
                   }
               }

           }
       });*/

        map.setOverlayPainter((g, mapViewer, width, height) -> {
            for (TrafficLightController controller : trafficlights) {
                GeoPosition pos = controller.getLocation();
                Color color = controller.getLightMain().getState();
                Point2D worldPt = mapViewer.getTileFactory().geoToPixel(pos, mapViewer.getZoom());
                Rectangle viewport = mapViewer.getViewportBounds();

                int x = (int) (worldPt.getX() - viewport.getX());
                int y = (int) (worldPt.getY() - viewport.getY());

                int size = 15;
                g.setColor(color);
                g.fillOval(x - size / 2, y - size / 2, size, size);
                g.setColor(Color.BLACK);
                g.drawOval(x - size / 2, y - size / 2, size, size);
            }
        });
        add(map);
        setVisible(true);
        startUpdating();
       for (TrafficLightController controller : trafficlights) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(controller).start();

        }

    /*    addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (TrafficLightController controller : trafficlights) {
                    controller.getLightMain().setState(Color.YELLOW);
                }
                saveTrafficLights(trafficlights);
            }
        });*/
    }

    private void startUpdating() {
        Timer timer = new Timer(500, e -> updateMap());
        timer.start();
    }

    public void updateMap() {
        repaint();
    }

    private void saveTrafficLights(List<TrafficLightController> list) {
        try (ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream("trafficLights.dat"))) {
            oss.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<TrafficLightController> loadTrafficLightsFromTxt() {
        List<TrafficLightController> list = new ArrayList<>();

        File file = new File("TrafficLight.txt");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 6) continue; // línea inválida, la salteamos

                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());
                String street1 = parts[2].trim();
                Orientation dir1 = Orientation.valueOf(parts[3].trim().toUpperCase());
                String street2 = parts[4].trim();
                Orientation dir2 = Orientation.valueOf(parts[5].trim().toUpperCase());

                TrafficLightController controller = new TrafficLightController();
                controller.setLocation(new GeoPosition(lat, lon));
                controller.getLightMain().setStreet(street1);
                controller.getLightMain().setDirection(dir1);
                controller.getLightSecundary().setStreet(street2);
                controller.getLightSecundary().setDirection(dir2);
                controller.getLightMain().setState(Color.YELLOW);

                list.add(controller);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    private List<TrafficLightController> loadTrafficLights() {
        File file = new File("TrafficLights.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<TrafficLightController>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // si no existe el .dat, cargar desde txt
        return loadTrafficLightsFromTxt();
    }
}