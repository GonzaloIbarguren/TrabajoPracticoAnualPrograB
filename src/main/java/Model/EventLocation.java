package Model;

import Model.Devices.Device;
import org.jxmapviewer.viewer.GeoPosition;

import java.time.LocalDateTime;

public class EventLocation {
        private LocalDateTime dateTime;
        private String address;
        private GeoPosition location;
        private Device device;
        
        public EventLocation(LocalDateTime dateTime,String address,GeoPosition location){
                this.address = address;
                this.dateTime = dateTime;
                this.location = location;
        }

        public String getAddress() {
                return address;
        }
        public void setAddress(String address) {
                this.address = address;
        }

        public LocalDateTime getDateTime() {
                return dateTime;
        }
        public void setDateTime(LocalDateTime dateTime) {
                this.dateTime = dateTime;
        }

        public GeoPosition getLocation() {return location;}
        public void setLocation(GeoPosition location) {this.location = location;}

        public Device getDevice() {return device;}
        public void setDevice(Device device) {this.device = device;}
}
