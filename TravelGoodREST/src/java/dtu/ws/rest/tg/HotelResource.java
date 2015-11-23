package dtu.ws.rest.tg;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/hotels")
public class HotelResource {

    @GET
    public String getHotels(
            @QueryParam("place") String place,
            @QueryParam("time") String time) {
        return "Not implemented yet. Received place: " + place + " and time: " + time;
    }
}
