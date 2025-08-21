package ui;

import Model.TrafficLightController;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

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

public class MapWindows extends JFrame {
    private final List<TrafficLightController> trafficlights = loadTrafficLights();
    private boolean addingTrafficLight = false;
    private boolean deletingTrafficLight = false;


    public MapWindows() {
        super("MAP");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadTrafficLights();



        JXMapViewer map = new JXMapViewer();
        OSMTileFactoryInfo info = new OSMTileFactoryInfo();
        map.setTileFactory(new DefaultTileFactory(info));

        var center = new GeoPosition(-38.0055, -57.5426);
        map.setZoom(6);
        map.setAddressLocation(center);
        PanMouseInputListener panListener = new PanMouseInputListener(map);
        map.addMouseListener(panListener);
        map.addMouseMotionListener(panListener);
        map.addMouseWheelListener(e -> {
            int zoom = map.getZoom();

            if (e.getWheelRotation()< 0 && zoom > 3) {
                map.setZoom(zoom - 1);
            }
            if (e.getWheelRotation()> 0 && zoom < 5) {
                map.setZoom(zoom +1);
            }
        });
        JButton addButton = new JButton("Agregar Semaforo");

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
        add(buttonPanel,BorderLayout.SOUTH);

       map.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {

                   Point clickedPoint = e.getPoint();
                   Rectangle viewport = map.getViewportBounds();
                   int x = viewport.x + clickedPoint.x;
                   int y = viewport.y + clickedPoint.y;
                   Point2D point2D = new Point2D.Double(x, y);
                   GeoPosition geo = map.getTileFactory().pixelToGeo(point2D, map.getZoom());
                   TrafficLightController controller = new TrafficLightController();
                   controller.setLocation(geo);

               if (addingTrafficLight){
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
       });
        map.setOverlayPainter((g,mapViewer,width,height)->{
            for (TrafficLightController controller: trafficlights){
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
        for (TrafficLightController controller : trafficlights){
            new Thread(controller).start();
        }

        add(map);
        setVisible(true);
        startUpdating();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTrafficLights(trafficlights);
            }
        });}

    private void startUpdating() {
        Timer timer = new Timer(2000, e -> updateMap());
        timer.start();
    }
    public void updateMap(){
        repaint();
    }

    private void saveTrafficLights(List<TrafficLightController> list){
        try (ObjectOutputStream oss = new ObjectOutputStream(new FileOutputStream("trafficlights.dat"))){
            oss.writeObject(list);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    private List<TrafficLightController> loadTrafficLights(){
        File file = new File("trafficLights.dat");
        if (!file.exists()) return  new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<TrafficLightController>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}

