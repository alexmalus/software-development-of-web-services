package rest.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import niceviewschema.HotelType;

@Path("/itineraries")
public class ItineraryResource {

    private static long idCounter = 1;
    private static List<Itinerary> itineraries = new ArrayList<>();
    private static boolean initialized = false;

    public ItineraryResource() {

    }

    @POST
    @Produces("application/xml")
    public Response createItinerary(Itinerary i) {
        i.setId(idCounter);
        idCounter++;
        itineraries.add(i);
        return Response.ok(i).build();
    }

    @GET
    @Path("/{id}")
    public Response getItinerary(@PathParam("id") int id) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(i).build();
        }
    }

    @GET
    @Path("/{id}/hotels/{hotelName}")
    @Produces("application/xml")
    public Response getHotel(
            @PathParam("id") int id,
            @PathParam("hotelName") String hotelName) {

        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            System.out.println("Itin. is null");
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            System.out.println("Size of hotels: " + i.getHotels().size());
            for (HotelType h : i.getHotels()) {
                System.out.println("Name of hotel: " + h.getName());
                if (h.getName().equals(hotelName)) {
                    return Response.ok(h).build();
                }
            }
            System.out.println("Hotel isn't there");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    @Path("/{id}/hotels")
    public Response addHotel(
            @PathParam("id") int id,
            HotelType hotel) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            i.getHotels().add(hotel);
            return Response.ok(hotel).build();
        }
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
            @PathParam("id") String id,
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
    public Response changeState(
            @PathParam("id") int id,
            String state) {

        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            i.setState(state);
            return Response.ok(state).build();
        }

    }

}
