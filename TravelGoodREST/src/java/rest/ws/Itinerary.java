package rest.ws;

import java.util.List;
import java.util.Map;
import lameduckschema.FlightInfoType;
import niceviewschema.HotelReservationType;

public class Itinerary {

    private long id;
    private String state;
    private List<HotelWrapper> hotels;
    private List<FlightWrapper> flights;

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

    public List<HotelWrapper> getHotels() {
        return hotels;
    }

    public void setHotels(List<HotelWrapper> hotels) {
        this.hotels = hotels;
    }

    public List<FlightWrapper> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightWrapper> flights) {
        this.flights = flights;
    }

}
