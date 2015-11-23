package dtu.ws.rest.tg;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/itineraries")
public class ItineraryResource {

    @GET
    @Path("/{id}")
    public String getItinerary(@PathParam("id") String id) {
        return "Not implemented yet";
    }

    @POST
    @Path("/{id}/hotels")
    public String addHotel(
            @PathParam("id") String id,
            String hotel) {
        return "Not implemented yet";
    }

    @POST
    @Path("/{id}/flights")
    public String addFlight(
            @PathParam("id") String id,
            String flight) {
        return "Not implemented yet";
    }

    @DELETE
    @Path("/{id}/hotels/{hotelId}")
    public String removeHotel(
            @PathParam("id") String  id,
            @PathParam("hotelId") String hotelId,
            String hotel) {
        return "Not implemented yet";
    }

    @DELETE
    @Path("/{id}/flights/{flightId}")
    public String removeFlight(
            @PathParam("id") String id,
            @PathParam("flightId") String flightId,
            String flight) {
        return "Not implemented yet";
    }

    @PUT
    @Path("/{id}/state")
    public String changeState(
            @PathParam("id") String id,
            String state) {
        return "Not implemented yet";
    }

}
