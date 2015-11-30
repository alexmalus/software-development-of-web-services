package rest.ws;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import lameduckschema.BookFlightRequest;
import lameduckschema.CancelFlightRequest;
import lameduckschema.FlightInfoType;
import niceviewschema.BookHotel;
import niceviewschema.BookHotelResponse;
import niceviewschema.CancelHotel;
import niceviewschema.CancelHotelResponse;
import niceviewschema.HotelReservationType;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;
import ws.niceview.BookHotelFault;
import ws.niceview.CancelHotelFault;

@Path("/itineraries")
public class ItineraryResource {

    private static final CreditCardInfoType creditCardJoachim
            = new CreditCardInfoType();
    private static final Map<Long, Itinerary> itineraries = new HashMap<>();
    private static long idCounter = 0;

    public ItineraryResource() {
        creditCardJoachim.setName("Tick Joachim");
        CreditCardInfoType.ExpirationDate expiration
                = new CreditCardInfoType.ExpirationDate();
        expiration.setMonth(2);
        expiration.setYear(11);
        creditCardJoachim.setExpirationDate(expiration);
        creditCardJoachim.setNumber("50408824");
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createItinerary(Itinerary i) {
        // Add itinerary to the map and return a '201 Created' response
        // with the path to the new resource in the header named 'Location'
        idCounter++;
        itineraries.put(idCounter, i);
        return Response.created(URI.create("itineraries/" + idCounter)).build();
    }

    @GET
    @Produces("applicatin/json")
    @Path("/{id}")
    public Response getItinerary(@PathParam("id") long id) {
        // Check that the requested itinerary is there
        if (!itineraries.containsKey(id)) {
            // If it's not there, return a '404 Not' Found response
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Return the Itinerary serialized in JSON as 
        // the body of a '200 OK' response
        return Response.ok(itineraries.get(id)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/hotels")
    public Response addHotel(
            @PathParam("id") long id,
            HotelReservationType hotel) {
        // Check that the itinerary is there
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        // If the List of Hotels is null, create a new ArrayList
        if (i.getHotels() == null) {
            i.setHotels(new ArrayList<>());
        }
        // Return a '201 Created' response with the path to the 
        // new resource in the header name 'Location'
        i.getHotels().add(new HotelWrapper(hotel, "unconfirmed"));
        return Response.created(URI.create("itineraries/" + id + "/hotels/"
                + hotel.getBookingNumber())).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/flights")
    public Response addFlight(
            @PathParam("id") long id,
            FlightInfoType flight) {
        // Check that the itinerary is there
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        // If the List of Flights is null, create a new ArrayList
        if (i.getFlights() == null) {
            i.setFlights(new ArrayList<>());
        }
        // Return a '201 Created' response with the path to the 
        // new resource in the header name 'Location'
        i.getFlights().add(new FlightWrapper(flight, "unconfirmed"));
        return Response.created(URI.create("itineraries/" + id + "/flights/"
                + flight.getBookingNumber())).build();
    }

    @DELETE
    @Path("/{id}/hotels/{hotelName}")
    public Response removeHotel(
            @PathParam("id") long id,
            @PathParam("hotelName") String name) {
        // Check that the itinerary is there
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        // Find the requested hotel
        for (HotelWrapper h : i.getHotels()) {
            if (h.getHotel().getHotel().getName().equals(name)) {
                // Remove the hotel from the list and return a
                // '204 No Content' response
                i.getHotels().remove(h);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        // If the hotel was not in the list, retun '404 Not Found'
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @DELETE
    @Path("/{id}/flights/{bookingNumber}")
    public Response removeFlight(
            @PathParam("id") long id,
            @PathParam("bookingNumber") String bookingNumber) {
        // Check that the itinerary is there
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        // Find the requested flight
        for (FlightWrapper h : i.getFlights()) {
            if (h.getFlight().getBookingNumber().equals(bookingNumber)) {
                // Remove the flight from the list and return a
                // '204 No Content' response
                i.getFlights().remove(h);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        // If the flight was not in the list, retun '404 Not Found'
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Consumes("text/plain")
    @Path("/{id}/state")
    public Response changeState(
            @PathParam("id") long id,
            String targetState) {
        // Check that the itinerary is there
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        if (targetState.equals("confirmed")) {
            // If the targetState is 'confirmed' we proceed 
            // to book the itinerary
            bookItinerary(i);
            i.setState(targetState);
            return Response.ok().build();
        } else if (targetState.equals("cancelled") && i.getState().equals("unconfirmed")) {
            // If the targetState is 'cancelled' and the itinerary
            // is not booked yet, we remove it from the map 
            itineraries.remove(id);
            return Response.ok().build();
        } else if (targetState.equals("cancelled") && i.getState().equals("confirmed")) {
            // If the targetState is 'cancelled' and the itinerary
            // is booked already, we proceed to cancel the bookings
            cancelItinerary(i);
            return Response.ok().build();
        }
        // If the targetState is not 'confirmed' or 'cancelled',
        // return a 400 Bad Request response
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Consumes("text/plain")
    @Path("/reset")
    public Response reset(String reset) {
        // Clear the map and request the other services to reset
        if (reset.equals("reset")) {
            itineraries.clear();
            resetFlights(true);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private void bookItinerary(Itinerary i) {
        // Try to book every hotel and flight inside the itinerary
        for (HotelWrapper h : i.getHotels()) {
            try {
                BookHotel request = new BookHotel();
                request.setBookingNumber(h.getHotel().getBookingNumber());
                request.setCreditCardInfo(creditCardJoachim);
                bookHotel(request);
                h.setState("confirmed");
            } catch (BookHotelFault e) {
                // If it fails, cancel the whole itinerary 
                // and respond with an error response
                cancelItinerary(i);
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        for (FlightWrapper f : i.getFlights()) {
            try {
                BookFlightRequest request = new BookFlightRequest();
                request.setBookingNumber(f.getFlight().getBookingNumber());
                request.setCreditCardInfo(creditCardJoachim);
                bookFlight(request);
                f.setState("confirmed");
            } catch (BookFlightFault e) {
                cancelItinerary(i);
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }

        }
        i.setState("confirmed");

    }

    private void cancelItinerary(Itinerary i) {
        i.setState("cancelled");
        boolean cancelledOk = true;
        // Try to book every hotel and flight inside the itinerary
        for (HotelWrapper h : i.getHotels()) {
            if (h.getState().equals("confirmed")) {
                try {
                    CancelHotel request = new CancelHotel();
                    request.setBookingNumber(h.getHotel().getBookingNumber());
                    request.setCreditCardInfo(creditCardJoachim);
                    cancelHotel(request);
                    h.setState("cancelled");
                } catch (CancelHotelFault e) {
                    cancelledOk = false;
                }
            }
        }
        for (FlightWrapper f : i.getFlights()) {
            try {
                CancelFlightRequest request = new CancelFlightRequest();
                request.setBookingNumber(f.getFlight().getBookingNumber());
                request.setCreditCardInfo(creditCardJoachim);
                request.setPrice(1000);
                cancelFlight(request);
                f.setState("cancelled");
            } catch (CancelFlightFault e) {
                cancelledOk = false;
            }
        }
        // If one of the cancellations failed, respond 
        // with a '500 Internal Server Error' 
        if (!cancelledOk) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private static BookHotelResponse bookHotel(niceviewschema.BookHotel part1) throws BookHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.bookHotel(part1);
    }

    private static CancelHotelResponse cancelHotel(niceviewschema.CancelHotel part1) throws CancelHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.cancelHotel(part1);
    }

    private static boolean bookFlight(lameduckschema.BookFlightRequest bookFlightRequest) throws BookFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.bookFlight(bookFlightRequest);
    }

    private static boolean cancelFlight(lameduckschema.CancelFlightRequest cancelFlightRequest) throws CancelFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.cancelFlight(cancelFlightRequest);
    }

    private static boolean resetFlights(boolean resetFlightsRequest) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.resetFlights(resetFlightsRequest);
    }

}
