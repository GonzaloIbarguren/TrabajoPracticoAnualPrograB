package ui;

import Model.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;


import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapWindows extends JFrame {
    private List<TrafficLightController> controllers;
    private List<SecurityCamera>  securityCameras;
    private List<ParkingCamera> parkingCameras;
    private List<Radar> radars;
    //private final List<Radar> radars = new CopyOnWriteArrayList<>(loadRadarsFromTxt());

    public MapWindows(List<Device> deviceList) {
        super("MAP");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        List<TrafficLightController> lightsControllers = new ArrayList<>();
        List<SecurityCamera>  securityCameras = new ArrayList<>();
        List<ParkingCamera> parkingCameras = new ArrayList<>();
        List<Radar> radars = new ArrayList<>();

        for (Device d : deviceList) {
            switch (d.getTypeDevice()) {
                case "trafficLightController":
                    lightsControllers.add((TrafficLightController) d);
                    break;
                case "securityCamera":
                    securityCameras.add((SecurityCamera) d);
                    break;
                case "parkingCamera":
                    parkingCameras.add((ParkingCamera) d);
                    break;
                case "radar":
                    radars.add((Radar) d);
            }
        }


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


       map.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               Point clickedPoint = e.getPoint();
               Rectangle viewport = map.getViewportBounds();
               int x = viewport.x + clickedPoint.x;
               int y = viewport.y + clickedPoint.y;
               Point2D point2D = new Point2D.Double(x, y);
               GeoPosition geo = map.getTileFactory().pixelToGeo(point2D, map.getZoom());

               //System.out.println(+ geo.getLatitude()+","+ geo.getLongitude());

               TrafficLightController clickedTrafficLight = null;
               double threshold = 0.0001;
                for (TrafficLightController controller : lightsControllers){
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



        map.setOverlayPainter((g, mapViewer, width, height) -> {
            for (TrafficLightController controller : lightsControllers) {
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
           for (Radar radar : radars) {
                Point2D pt = mapViewer.getTileFactory().geoToPixel(
                        radar.getLocation(),
                        mapViewer.getZoom()
                );
                Rectangle viewport = mapViewer.getViewportBounds();

                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());

                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/RedRadar.PNG")));
                Image img = icon.getImage();
                g.drawImage(img, x-10, y-10, 20, 20, null);
            }
            for (SecurityCamera securityCamera: securityCameras) {
                Point2D pt = mapViewer.getTileFactory().geoToPixel(
                        securityCamera.getLocation(),
                        mapViewer.getZoom()
                );
                Rectangle viewport = mapViewer.getViewportBounds();

                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());

                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/SecurityCamera.png")));
                Image img = icon.getImage();
                g.drawImage(img, x-10, y-10, 20, 20, null);
            }
            for (ParkingCamera parkingCamera: parkingCameras) {
                Point2D pt = mapViewer.getTileFactory().geoToPixel(
                        parkingCamera.getLocation(),
                        mapViewer.getZoom()
                );
                Rectangle viewport = mapViewer.getViewportBounds();

                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());

                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ParkingCamera.png")));
                Image img = icon.getImage();
                g.drawImage(img, x-10, y-10, 20, 20, null);
            }


        });
        add(map);
        setVisible(true);
        startUpdating();


       for (TrafficLightController controller : lightsControllers) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(controller).start();

        }

        for (ParkingCamera controller : parkingCameras){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(controller).start();
        }

        for (Radar controller : radars){
            try{
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(controller).start();
        }
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

    private List<Radar> loadRadarsFromTxt() {
        List<Radar> list = new ArrayList<>();
        InputStream input = getClass().getClassLoader().getResourceAsStream("Radar.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                double lat = Double.parseDouble(parts[0].trim());
                double lon = Double.parseDouble(parts[1].trim());
                int velocidadMax = Integer.parseInt(parts[2].trim());

                Radar radar = new Radar(
                        "Radar_" + list.size(),
                        new GeoPosition(lat,lon),
                        velocidadMax );

                list.add(radar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }



}