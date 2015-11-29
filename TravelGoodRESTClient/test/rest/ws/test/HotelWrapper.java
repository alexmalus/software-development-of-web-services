package rest.ws.test;

import niceviewschema.HotelReservationType;

public class HotelWrapper {

    private HotelReservationType hotel;
    private String state;

    public HotelWrapper() {
    }

    public HotelWrapper(HotelReservationType hotel, String state) {
        this.hotel = hotel;
        this.state = state;
    }

    public HotelReservationType getHotel() {
        return hotel;
    }

    public void setHotel(HotelReservationType hotel) {
        this.hotel = hotel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
