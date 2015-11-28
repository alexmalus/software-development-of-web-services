package rest.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/flights")
public class FlightResource {

    @GET
    public String getFlights(
            @QueryParam("place") String place,
            @QueryParam("time") String time) {
        return "Not implemented yet. Received place: " + place + " and time: " + time;
    }
}
