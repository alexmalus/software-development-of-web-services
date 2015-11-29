package rest.ws;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.ArrayList;
import java.util.List;
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
import lameduckschema.FlightInfoType;
import niceviewschema.BookHotel;
import niceviewschema.BookHotelResponse;
import niceviewschema.CancelHotelResponse;
import niceviewschema.HotelReservationType;
import ws.lameduck.BookFlightFault;
import ws.lameduck.CancelFlightFault;
import ws.niceview.BookHotelFault;
import ws.niceview.CancelHotelFault;

@Path("/itineraries")
public class ItineraryResource {

    private static long idCounter = 1;
    private static List<Itinerary> itineraries = new ArrayList<>();
    private static boolean initialized = false;

    private static final CreditCardInfoType creditCardJoachim = new CreditCardInfoType();

    public ItineraryResource() {
        creditCardJoachim.setName("Tick Joachim");
        CreditCardInfoType.ExpirationDate expiration = new CreditCardInfoType.ExpirationDate();
        expiration.setMonth(2);
        expiration.setYear(11);
        creditCardJoachim.setExpirationDate(expiration);
        creditCardJoachim.setNumber("50408824");
    }

    @POST
    @Produces("application/json")
    public Itinerary createItinerary(Itinerary i) {
        i.setId(idCounter);
        idCounter++;
        itineraries.add(i);
        System.out.println("Itin size: " + itineraries.size());
        System.out.println("ID: " + i.getId());
        return i;
    }

    @GET
    @Produces("applicatin/json")
    @Path("/{id}")
    public Itinerary getItinerary(@PathParam("id") int id) {
        System.out.println("ID: " + id);
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return i;
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItinerary(@PathParam("id") int id) {
        System.out.println("ID: " + id);
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            itineraries.remove(i);
            return Response.ok().build();
        }
    }

    @GET
    @Path("/{id}/hotels/{hotelName}")
    @Produces("application/json")
    public Response getHotel(
            @PathParam("id") int id,
            @PathParam("hotelName") String hotelName) {

        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            System.out.println("Itin. is null");
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            System.out.println("Size of hotels: " + i.getHotels().size());
            for (HotelWrapper h : i.getHotels()) {
                System.out.println("Name of hotel: " + h.getHotel().getHotel().getName());
                if (h.getHotel().getHotel().getName().equals(hotelName)) {
                    return Response.ok(h).build();
                }
            }
            System.out.println("Hotel isn't there");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/hotels")
    public Response addHotel(
            @PathParam("id") int id,
            HotelReservationType hotel) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (i.getHotels() == null) {
                i.setHotels(new ArrayList<>());
            }

            i.getHotels().add(new HotelWrapper(hotel, "unconfirmed"));
            return Response.ok(hotel).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{id}/flights")
    public Response addFlight(
            @PathParam("id") int id,
            FlightInfoType flight) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (i.getFlights() == null) {
                i.setFlights(new ArrayList<>());
            }
            i.getFlights().add(new FlightWrapper(flight, "unconfirmed"));
            return Response.ok(flight).build();
        }
    }

    @DELETE
    @Path("/{id}/hotels/{hotelName}")
    public Response removeHotel(
            @PathParam("id") int id,
            @PathParam("hotelName") String name) {

        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            for (HotelWrapper h : i.getHotels()) {
                if (h.getHotel().getHotel().getName().equals(name)) {
                    i.getHotels().remove(h);
                    return Response.status(Response.Status.OK).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}/flights/{bookingNumber}")
    public Response removeFlight(
            @PathParam("id") int id,
            @PathParam("bookingNumber") String bookingNumber) {

        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            for (FlightWrapper h : i.getFlights()) {
                if (h.getFlight().getBookingNumber().equals(bookingNumber)) {
                    i.getFlights().remove(h);
                    return Response.status(Response.Status.OK).build();
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
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

    @POST
    @Path("/{id}/book")
    public Response bookItinerary(@PathParam("id") int id) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            for (HotelWrapper h : i.getHotels()) {
                BookHotel request = new BookHotel();
                request.setBookingNumber(h.getHotel().getBookingNumber());
            }
            return Response.ok().build();
        }
    }

    @PUT
    @Consumes("text/plain")
    @Path("/{id}/hotels/{hotelName}/state")
    public Response bookHotel(@PathParam("id") int id,
            @PathParam("hotelName") String hotelName,
            String state) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            for (HotelWrapper h : i.getHotels()) {
                if (h.getHotel().getHotel().getName().equals(hotelName)) {
                    if (state.equals("confirmed")) {
                        System.out.println("Booking hotel : " + h.getHotel().getHotel().getName());
                        BookHotelResponse response = bookHotel(h);
                        System.out.println("Hotel booked : " + response.isResponse());

                        if (response.isResponse()) {
                            h.setState(state);
                            return Response.ok().build();
                        } else {
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Consumes("text/plain")
    @Path("/{id}/flights/{bookingNumber}/state")
    public Response bookFlight(@PathParam("id") int id,
            @PathParam("bookingNumber") String bookingNumber,
            String state) {
        Itinerary i = itineraries.get(id - 1);
        if (i == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            for (FlightWrapper f : i.getFlights()) {
                if (f.getFlight().getBookingNumber().equals(bookingNumber)) {
                    if (state.equals("confirmed")) {
                        System.out.println("Booking flight : " + f.getFlight().getBookingNumber());
                        if (bookFlight(f)) {
                            System.out.println("Flight booked : " + true);
                            f.setState(state);
                            return Response.ok().build();
                        } else {
                            System.out.println("Hotel booked : " + false);
                            return Response.status(Response.Status.BAD_REQUEST).build();
                        }
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private static BookHotelResponse bookHotel(HotelWrapper h) {
        try {
            BookHotel request = new BookHotel();
            request.setBookingNumber(h.getHotel().getBookingNumber());
            request.setCreditCardInfo(creditCardJoachim);
            return bookHotel(request);
        } catch (BookHotelFault f) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean bookFlight(FlightWrapper f) {
        try {
            BookFlightRequest request = new BookFlightRequest();
            request.setBookingNumber(f.getFlight().getBookingNumber());
            request.setCreditCardInfo(creditCardJoachim);
            boolean b = bookFlight(request);
            System.out.println("Returning: " + b);
            return b;
        } catch (BookFlightFault fault) {
            System.out.println("Throwing stuff");
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
