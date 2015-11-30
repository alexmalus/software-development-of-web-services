package rest.ws.test;

import java.util.List;

public class Itinerary {

    private int id;
    private String state;
    private List<HotelWrapper> hotels;
    private List<FlightWrapper> flights;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
