package rest.ws.test;

import java.util.Date;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lameduckschema.FlightInfoType;
import lameduckschema.GetFlightsResponse;
import niceviewschema.GetHotelsResponse;
import niceviewschema.HotelReservationType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItineraryResourceTest {

    private static final Client client = ClientBuilder.newClient();
    private static final WebTarget r = client.target("http://localhost:8080/tg/api/");

    @Test
    public void testP1() {
        // Creating itinerary
        Itinerary i = new Itinerary();
        Itinerary itinerary = r.path("itineraries")
                .request()
                .post(Entity.entity(i, MediaType.APPLICATION_JSON), Itinerary.class);

        assertNotNull(itinerary);
        assertTrue(itinerary.getId() > 0);

        // Search flights
        GetFlightsResponse flightsResponse = r.path("flights")
                .queryParam("origin", "CPH")
                .queryParam("destination", "LHR")
                .queryParam("time", "17:00")
                .queryParam("day", "2015-11-18")
                .request()
                .get(GetFlightsResponse.class);

        assertNotNull(flightsResponse);
        assertNotNull(flightsResponse.getFlightInfoList());
        assertTrue(flightsResponse.getFlightInfoList().size() > 0);

        // Add flight
        FlightInfoType flight1 = r.path("itineraries").path("" + itinerary.getId()).path("flights")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(flightsResponse.getFlightInfoList().get(0), MediaType.APPLICATION_JSON),
                        FlightInfoType.class);
        assertNotNull(flight1);
        assertNotNull(flight1.getFlight());

        // Search hotels 
        GetHotelsResponse hotelsResponse = r.path("hotels")
                .queryParam("arrival", new Date().getTime())
                .queryParam("departure", new Date().getTime())
                .queryParam("city", "Bangladesh")
                .request()
                .get(GetHotelsResponse.class);
        assertNotNull(hotelsResponse);
        assertNotNull(hotelsResponse.getHotels());
        assertTrue(hotelsResponse.getHotels().size() > 0);

        // Add hotel
        HotelReservationType hotel1 = r.path("itineraries").path("" + itinerary.getId()).path("hotels")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(hotelsResponse.getHotels().get(0), MediaType.APPLICATION_JSON),
                        HotelReservationType.class);
        assertNotNull(hotel1);
        assertNotNull(hotel1.getHotel());

        // Search more flights
        flightsResponse = r.path("flights")
                .queryParam("origin", "LHR")
                .queryParam("destination", "CPH")
                .queryParam("time", "11:00")
                .queryParam("day", "2015-11-19")
                .request()
                .get(GetFlightsResponse.class);
        assertNotNull(flightsResponse);
        assertNotNull(flightsResponse.getFlightInfoList());
        assertTrue(flightsResponse.getFlightInfoList().size() > 0);

        // Add second flight
        FlightInfoType flight2 = r.path("itineraries").path("" + itinerary.getId()).path("flights")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(flightsResponse.getFlightInfoList().get(0), MediaType.APPLICATION_JSON),
                        FlightInfoType.class);
        assertNotNull(flight2);
        assertNotNull(flight2.getFlight());

        // Search even more flights
        flightsResponse = r.path("flights")
                .queryParam("origin", "CPH")
                .queryParam("destination", "XAN")
                .queryParam("time", "17:00")
                .queryParam("day", "2015-11-23")
                .request()
                .get(GetFlightsResponse.class);
        assertNotNull(flightsResponse);
        assertNotNull(flightsResponse.getFlightInfoList());
        assertTrue(flightsResponse.getFlightInfoList().size() > 0);

        // Add third flight
        FlightInfoType flight3 = r.path("itineraries").path("" + itinerary.getId()).path("flights")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(flightsResponse.getFlightInfoList().get(0), MediaType.APPLICATION_JSON),
                        FlightInfoType.class);
        assertNotNull(flight3);
        assertNotNull(flight3.getFlight());

        // Search more hotels 
        hotelsResponse = r.path("hotels")
                .queryParam("arrival", new Date().getTime())
                .queryParam("departure", new Date().getTime())
                .queryParam("city", "Everywhere")
                .request()
                .get(GetHotelsResponse.class);
        assertNotNull(hotelsResponse);
        assertNotNull(hotelsResponse.getHotels());
        assertTrue(hotelsResponse.getHotels().size() > 0);

        // Add second hotel
        HotelReservationType hotel2 = r.path("itineraries").path("" + itinerary.getId()).path("hotels")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(hotelsResponse.getHotels().get(0), MediaType.APPLICATION_JSON),
                        HotelReservationType.class);
        assertNotNull(hotel2);
        assertNotNull(hotel2.getHotel());

        // Get itinerary
        itinerary = r.path("itineraries").path("" + itinerary.getId())
                .request()
                .get(Itinerary.class);

        assertNotNull(itinerary);
        assertTrue(itinerary.getFlights().size() == 3);
        assertTrue(itinerary.getHotels().size() == 2);

        for (FlightWrapper f : itinerary.getFlights()) {
            assertEquals("unconfirmed", f.getState());

        }
        for (HotelWrapper h : itinerary.getHotels()) {
            assertEquals("unconfirmed", h.getState());
        }

        bookItinerary(itinerary);

        // Get itinerary
        itinerary = r.path("itineraries").path("" + itinerary.getId())
                .request()
                .get(Itinerary.class);

        for (FlightWrapper f : itinerary.getFlights()) {
            assertEquals("confirmed", f.getState());
            System.out.println("Confirmed: " + f.getFlight().getBookingNumber());
        }

        for (HotelWrapper h : itinerary.getHotels()) {
            assertEquals("confirmed", h.getState());
            System.out.println("Confirmed: " + h.getHotel().getHotel().getName());
        }
    }

    @Test
    public void testP2() {
        // Creating itinerary
        Itinerary i = new Itinerary();
        Itinerary itinerary = r.path("itineraries")
                .request()
                .post(Entity.entity(i, MediaType.APPLICATION_JSON), Itinerary.class);
        assertNotNull(itinerary);
        assertTrue(itinerary.getId() > 0);

        // Search flights
        GetFlightsResponse flightsResponse = r.path("flights")
                .queryParam("origin", "CPH")
                .queryParam("destination", "LHR")
                .queryParam("time", "17:00")
                .queryParam("day", "2015-11-18")
                .request()
                .get(GetFlightsResponse.class);

        assertNotNull(flightsResponse);
        assertNotNull(flightsResponse.getFlightInfoList());
        assertTrue(flightsResponse.getFlightInfoList().size() > 0);

        // Add flight
        FlightInfoType flight1 = r.path("itineraries").path("" + itinerary.getId()).path("flights")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(flightsResponse.getFlightInfoList().get(0), MediaType.APPLICATION_JSON),
                        FlightInfoType.class);
        assertNotNull(flight1);
        assertNotNull(flight1.getFlight());

        // Cancel itinerary
        Response response = r.path("itineraries").path("" + itinerary.getId())
                .request()
                .delete();
        assertEquals(Response.ok().build().getStatus(), response.getStatus());

    }

    private void bookHotel(int itineraryId, HotelWrapper hotel) {
        r.path("itineraries")
                .path("" + itineraryId)
                .path("hotels")
                .path(hotel.getHotel().getHotel().getName())
                .path("state")
                .request()
                .put(Entity.entity("confirmed", MediaType.TEXT_PLAIN));
    }

    private void bookFlight(int itineraryId, FlightWrapper flight) {
        r.path("itineraries")
                .path("" + itineraryId)
                .path("flights")
                .path(flight.getFlight().getBookingNumber())
                .path("state")
                .request()
                .put(Entity.entity("confirmed", MediaType.TEXT_PLAIN));
    }

    private void bookItinerary(Itinerary i) {
        for (HotelWrapper h : i.getHotels()) {
            bookHotel(i.getId(), h);
        }

        for (FlightWrapper f : i.getFlights()) {
            bookFlight(i.getId(), f);
        }
    }
}
