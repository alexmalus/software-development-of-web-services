package dtu.ws.rest.tg;

import java.util.HashMap;
import java.util.HashSet;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.lameduck.FlightType;

@Path("/itineraries")
public class ItineraryResource {

    private static Map<String, Itinerary> itineraries = new HashMap<>();
    private static boolean initialized = false;

    public ItineraryResource() {
        if (!initialized) {
            Itinerary i = new Itinerary();
            itineraries.put("1", i);
            initialized = true;
        }
    }

    @GET
    @Path("/{id}")
    public Response getItinerary(@PathParam("id") String id) {
        Itinerary i = itineraries.get(id);
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
            @PathParam("id") String id,
            @PathParam("hotelName") String hotelName) {

        Itinerary i = itineraries.get(id);
        if (i == null) {
            System.out.println("Itin. is null");
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            System.out.println("Size of hotels: " + i.getHotels().size());
            for (HotelType h : i.getHotels()) {
                System.out.println("Name of hotel: "+h.getName());
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
            @PathParam("id") String id,
            HotelType hotel) {
        Itinerary i = itineraries.get(id);
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
            @PathParam("id") String id,
            String state) {

        Itinerary i = itineraries.get(id);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            i.setState(state);
            return Response.ok(state).build();
        }

    }

}
