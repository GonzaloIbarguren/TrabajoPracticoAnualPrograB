package Model;

import java.time.LocalDateTime;

public class EventLocation {
        private int id;
        private LocalDateTime dateTime;
        private String address;
        private Location location;
        private Device device;
        
        public EventLocation(int id, LocalDateTime dateTime,String address){
                this.address = address;
                this.id = id;
                this.dateTime = dateTime;
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

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }
}
