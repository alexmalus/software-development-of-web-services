package rest.ws.test;

import lameduckschema.FlightInfoType;

public class FlightWrapper {

    private FlightInfoType flight;
    private String state;

    public FlightWrapper() {
    }

    public FlightWrapper(FlightInfoType flight, String state) {
        this.flight = flight;
        this.state = state;
    }

    public FlightInfoType getFlight() {
        return flight;
    }

    public void setFlight(FlightInfoType flight) {
        this.flight = flight;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
