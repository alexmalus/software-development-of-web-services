package rest.ws;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import niceviewschema.HotelType;
import lameduckschema.FlightType;

@XmlRootElement
public class Itinerary {
    
    private long id;
    private String state;
    private Set<HotelType> hotels  = new HashSet<>();
    private Set<FlightType> flights = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
