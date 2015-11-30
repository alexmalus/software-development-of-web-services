package rest.ws.test;

import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lameduckschema.FlightInfoType;
import lameduckschema.GetFlightsResponse;
import niceviewschema.GetHotelsResponse;
import niceviewschema.HotelReservationType;
import niceviewschema.HotelType;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class TravelGoodTest {

    private static final Client client = ClientBuilder.newClient();
    private static final WebTarget t = client.target("http://localhost:8080/tg/api/");

    @Before
    public void resetWebservice() {
        // Before every test, call the reset endpoint
        t.path("itineraries")
                .path("reset")
                .request()
                .put(Entity.entity("reset", MediaType.TEXT_PLAIN));
    }

    @Test
    public void testP1() {
        // Create new itinerary
        long id = createItinerary();

        // Search for flights and add the first result
        List<FlightInfoType> flights = searchFlights("CPH", "LHR", "2015-11-18", "17:00");
        String bookingNumber = addFlight(id, flights.get(0));

        // Search for hotel and add the first result
        long time = new Date().getTime();
        List<HotelReservationType> hotels = searchHotels(time, time, "Bangladesh");
        String hotelName = addHotel(id, hotels.get(0));

        // Search for two more times flights and add the first result
        flights = searchFlights("LHR", "CPH", "2015-11-19", "11:00");
        bookingNumber = addFlight(id, flights.get(0));
        flights = searchFlights("CPH", "CAA", "2015-11-18", "23:00");
        bookingNumber = addFlight(id, flights.get(0));

        // Search for hotel once again and add the first result
        hotels = searchHotels(time, time, "Everywhere");
        hotelName = addHotel(id, hotels.get(0));

        // Get the itinerary
        Itinerary i = getItinerary(id);

        // Check that it contains the hotels and flights and that 
        // they are 'unconfirmed'
        assertTrue(i.getFlights().size() == 3);
        assertTrue(i.getHotels().size() == 2);
        for (FlightWrapper f : i.getFlights()) {
            assertEquals("unconfirmed", f.getState());
        }
        for (HotelWrapper h : i.getHotels()) {
            assertEquals("unconfirmed", h.getState());
        }

        // Book the itinerary
        bookItinerary(id);

        // Get itinerary again and check that everything is 'confirmed'
        i = getItinerary(id);
        for (FlightWrapper f : i.getFlights()) {
            assertEquals("confirmed", f.getState());
        }
        for (HotelWrapper h : i.getHotels()) {
            assertEquals("confirmed", h.getState());
        }
    }

    @Test
    public void testP2() {
        // Create itinerary, search a flight and add it
        long id = createItinerary();
        List<FlightInfoType> flights = searchFlights("CPH", "LHR", "2015-11-18", "17:00");
        String bookingNumber = addFlight(id, flights.get(0));

        // Cancel the itinerary in the planning phase
        cancelItinerary(id);

        // Check that the resource was deleted from the service
        // by receiving a NOT_FOUND response when trying to get it
        Response r = t.path("itineraries").path(id + "").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), r.getStatus());
    }

    @Test
    public void testB() {
        // Create new itinerary
        long id = createItinerary();

        // Add a flight and a hotel
        List<FlightInfoType> flights = searchFlights("CPH", "LHR", "2015-11-18", "17:00");
        String bookingNumber = addFlight(id, flights.get(0));
        long time = new Date().getTime();
        List<HotelReservationType> hotels = searchHotels(time, time, "Bangladesh");
        addHotel(id, hotels.get(0));

        // Add a hotel containing wrong booking information so
        // it fails during the booking
        hotels = searchHotels(time, time, "Bangladesh");
        HotelReservationType hotel = hotels.get(0);
        hotel.setBookingNumber("sdfsdfçsdfsdf");
        HotelType wrong = new HotelType();
        wrong.setName("sdfsdff");
        hotel.setHotel(wrong);
        addHotel(id, hotel);

        // Try to book the itinerary and check that it fails
        Response r = bookItinerary(id);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        // Get the itinerary and check that the first hotel was cancelled 
        // and the other bookings were left 'unconfirmed'
        Itinerary i = getItinerary(id);
        assertEquals("cancelled", i.getHotels().get(0).getState());
        assertEquals("unconfirmed", i.getHotels().get(1).getState());
        assertEquals("unconfirmed", i.getFlights().get(0).getState());

        // Check that the state of the itinerary is 'cancelled'
        assertEquals("cancelled", i.getState());
    }

    @Test
    public void testC1() {
        // Create new itinerary and add 2 flights and 1 hotel
        long id = createItinerary();
        List<FlightInfoType> flights = searchFlights("CPH", "LHR", "2015-11-18", "17:00");
        String bookingNumber = addFlight(id, flights.get(0));
        long time = new Date().getTime();
        List<HotelReservationType> hotels = searchHotels(time, time, "Bangladesh");
        String hotelName = addHotel(id, hotels.get(0));
        flights = searchFlights("LHR", "CPH", "2015-11-19", "11:00");
        bookingNumber = addFlight(id, flights.get(0));

        // Book the itinerary and check that everything is 'confirmed'
        bookItinerary(id);
        Itinerary i = getItinerary(id);
        for (FlightWrapper f : i.getFlights()) {
            assertEquals("confirmed", f.getState());
        }
        for (HotelWrapper h : i.getHotels()) {
            assertEquals("confirmed", h.getState());
        }
        assertEquals("confirmed", i.getState());

        // Cancel the itinerary and check that everything is 'cancelled
        cancelItinerary(id);
        i = getItinerary(id);
        for (FlightWrapper f : i.getFlights()) {
            assertEquals("cancelled", f.getState());
        }
        for (HotelWrapper h : i.getHotels()) {
            assertEquals("cancelled", h.getState());
        }
        assertEquals("cancelled", i.getState());
    }

    @Test
    public void testC2() {
        // Create new itinerary and add 2 hotels and 1 flight
        long id = createItinerary();
        long time = new Date().getTime();
        List<HotelReservationType> hotels = searchHotels(time, time, "Bangladesh");
        addHotel(id, hotels.get(0));
        // The hotels in 'Copenhagen' are not cancellable so we will
        // add one to cause failure during the cancelling phase
        hotels = searchHotels(time, time, "Copenhagen");
        addHotel(id, hotels.get(0));
        List<FlightInfoType> flights = searchFlights("LHR", "CPH", "2015-11-19", "11:00");
        addFlight(id, flights.get(0));

        // Book the itinerary and check that we get an 'OK' status
        Response r = bookItinerary(id);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());

        // Get itinerary and check that everything is 'confirmed'
        Itinerary i = getItinerary(id);
        for (FlightWrapper f : i.getFlights()) {
            assertEquals("confirmed", f.getState());
        }
        for (HotelWrapper h : i.getHotels()) {
            assertEquals("confirmed", h.getState());
        }
        assertEquals("confirmed", i.getState());

        // Try to cancel the itinerary and check that we get an error status
        r = cancelItinerary(id);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), r.getStatus());

        // Get itinerary and check that everything is 'cancelled'
        // but the cancelling that failed is left 'confirmed'
        i = getItinerary(id);
        assertEquals("cancelled", i.getHotels().get(0).getState());
        assertEquals("confirmed", i.getHotels().get(1).getState());
        assertEquals("cancelled", i.getFlights().get(0).getState());
        assertEquals("cancelled", i.getState());

    }

    private long createItinerary() {
        Itinerary i = new Itinerary();
        i.setState("unconfirmed");

        Response r = t.path("itineraries")
                .request()
                .post(Entity.entity(i, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), r.getStatus());
        assertNotNull(r.getHeaderString("Location"));

        String path = URI.create(r.getHeaderString("Location")).getPath();
        long id = Long.parseLong(path.replaceFirst(".*/([^/?]+).*", "$1"));

        assertTrue(id > 0);
        return id;

    }

    private Itinerary getItinerary(long id) {
        Itinerary i = t.path("itineraries")
                .path(id + "")
                .request()
                .get(Itinerary.class);

        assertNotNull(i);
        return i;
    }

    private void deleteItinerary(long id) {
        Response r = t.path("itineraries")
                .path(id + "")
                .request()
                .delete();

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), r.getStatus());

    }

    private List<FlightInfoType> searchFlights(
            String origin,
            String destination,
            String day,
            String time) {

        // Search flights
        GetFlightsResponse flightsResponse = t.path("flights")
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("day", day)
                .queryParam("time", time)
                .request()
                .get(GetFlightsResponse.class
                );

        assertNotNull(flightsResponse);

        List<FlightInfoType> flights = flightsResponse.getFlightInfoList();

        assertNotNull(flights);
        assertTrue(flights.size() > 0);

        return flights;
    }

    private List<HotelReservationType> searchHotels(
            long arrival,
            long departure,
            String city) {

        GetHotelsResponse hotelsResponse = t.path("hotels")
                .queryParam("arrival", arrival)
                .queryParam("departure", departure)
                .queryParam("city", city)
                .request()
                .get(GetHotelsResponse.class
                );
        assertNotNull(hotelsResponse);

        List<HotelReservationType> hotels = hotelsResponse.getHotels();

        assertNotNull(hotels);

        assertTrue(hotelsResponse.getHotels().size() > 0);

        return hotels;
    }

    private String addFlight(long id, FlightInfoType flight) {
        Response r = t.path("itineraries")
                .path("" + id)
                .path("flights")
                .request()
                .post(Entity.entity(flight, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), r.getStatus());
        assertNotNull(r.getHeaderString("Location"));

        String path = URI.create(r.getHeaderString("Location")).getPath();
        String bookingNumber = path.replaceFirst(".*/([^/?]+).*", "$1");

        assertNotNull(bookingNumber);
        return bookingNumber;
    }

    private String addHotel(long id, HotelReservationType hotel) {
        Response r = t.path("itineraries")
                .path("" + id)
                .path("hotels")
                .request()
                .post(Entity.entity(hotel, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), r.getStatus());
        assertNotNull(r.getHeaderString("Location"));

        String path = URI.create(r.getHeaderString("Location")).getPath();
        String hotelName = path.replaceFirst(".*/([^/?]+).*", "$1");

        assertNotNull(hotelName);
        return hotelName;
    }

    private Response bookItinerary(long id) {
        Response r = t.path("itineraries")
                .path("" + id)
                .path("state")
                .request()
                .put(Entity.entity("confirmed", MediaType.TEXT_PLAIN));
        return r;
    }

    private Response cancelItinerary(long id) {
        Response r = t.path("itineraries")
                .path("" + id)
                .path("state")
                .request()
                .put(Entity.entity("cancelled", MediaType.TEXT_PLAIN));
        return r;
    }
}
