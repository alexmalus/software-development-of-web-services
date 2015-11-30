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

    private static long idCounter = 0;
    private static final Map<Long, Itinerary> itineraries = new HashMap<>();

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
        idCounter++;
        itineraries.put(idCounter, i);
        return Response.created(URI.create("itineraries/" + idCounter)).build();
    }

    @GET
    @Produces("applicatin/json")
    @Path("/{id}")
    public Response getItinerary(@PathParam("id") long id) {
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(itineraries.get(id)).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/hotels")
    public Response addHotel(
            @PathParam("id") long id,
            HotelReservationType hotel) {
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        if (i.getHotels() == null) {
            i.setHotels(new ArrayList<>());
        }

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
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        if (i.getFlights() == null) {
            i.setFlights(new ArrayList<>());
        }
        i.getFlights().add(new FlightWrapper(flight, "unconfirmed"));
        return Response.created(URI.create("itineraries/" + id + "/flights/"
                + flight.getBookingNumber())).build();
    }

    @DELETE
    @Path("/{id}/hotels/{hotelName}")
    public Response removeHotel(
            @PathParam("id") long id,
            @PathParam("hotelName") String name) {

        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        for (HotelWrapper h : i.getHotels()) {
            if (h.getHotel().getHotel().getName().equals(name)) {
                i.getHotels().remove(h);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @DELETE
    @Path("/{id}/flights/{bookingNumber}")
    public Response removeFlight(
            @PathParam("id") long id,
            @PathParam("bookingNumber") String bookingNumber) {

        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        for (FlightWrapper h : i.getFlights()) {
            if (h.getFlight().getBookingNumber().equals(bookingNumber)) {
                i.getFlights().remove(h);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Consumes("text/plain")
    @Path("/{id}/state")
    public Response changeState(
            @PathParam("id") long id,
            String targetState) {
        if (!itineraries.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Itinerary i = itineraries.get(id);
        if (targetState.equals("confirmed")) {
            bookItinerary(i);
            i.setState(targetState);
            return Response.ok().build();
        } else if (targetState.equals("cancelled") && i.getState().equals("unconfirmed")) {
            itineraries.remove(id);
            return Response.ok().build();
        } else if (targetState.equals("cancelled") && i.getState().equals("confirmed")) {
            cancelItinerary(i);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Consumes("text/plain")
    @Path("/reset")
    public Response reset(String reset) {
        if (reset.equals("reset")) {
            itineraries.clear();
            resetFlights(true);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
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

    private void bookItinerary(Itinerary i) {
        for (HotelWrapper h : i.getHotels()) {
            try {
                BookHotel request = new BookHotel();
                request.setBookingNumber(h.getHotel().getBookingNumber());
                request.setCreditCardInfo(creditCardJoachim);
                bookHotel(request);
                h.setState("confirmed");
            } catch (BookHotelFault e) {
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

        if (!cancelledOk) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

}
