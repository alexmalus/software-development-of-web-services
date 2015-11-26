package dtu.ws.rest.tg;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import ws.lameduck.FlightType;
import ws.niceview.HotelType;

@XmlRootElement
public class Itinerary {
    
    private String state;
    private Set<HotelType> hotels  = new HashSet<>();
    private Set<FlightType> flights = new HashSet<>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public Set<HotelType> getHotels() {
        return hotels;
    }

    public void setHotels(Set<HotelType> hotels) {
        this.hotels = hotels;
    }

    public Set<FlightType> getFlights() {
        return flights;
    }

    public void setFlights(Set<FlightType> flights) {
        this.flights = flights;
    }
        
}
