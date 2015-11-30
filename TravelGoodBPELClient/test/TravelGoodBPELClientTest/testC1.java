/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import static TravelGoodBPELClientTest.testB.convertDateTimeToGregCal;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lameduckschema.GetFlightsRequest;
import niceviewschema.GetHotels;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.travelgoodbpel.BookItineraryFault;
import ws.travelgoodbpel.CancelBookingFault;
import ws.travelgoodschema.AddItineraryFlightRequest;
import ws.travelgoodschema.AddItineraryHotelRequest;
import ws.travelgoodschema.BookItineraryRequest;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.CancelBookingRequest;
import ws.travelgoodschema.GetFlightsResponse;
import ws.travelgoodschema.GetHotelsRequest;
import ws.travelgoodschema.GetHotelsResponse;
import ws.travelgoodschema.GetItineraryRequest;
import ws.travelgoodschema.GetItineraryResponse;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author Alex
 */
public class testC1 {
    private final CreditCardInfoType creditCardAnne;
    private final XMLGregorianCalendar dateArrive;
    private final XMLGregorianCalendar dateDepart;
    
    public testC1() throws DatatypeConfigurationException {
        
        creditCardAnne = new CreditCardInfoType();
        creditCardAnne.setName("Dirach Anne-Louise");
        CreditCardInfoType.ExpirationDate expirationAnne = new CreditCardInfoType.ExpirationDate();
        expirationAnne.setMonth(1);
        expirationAnne.setYear(10);
        creditCardAnne.setExpirationDate(expirationAnne);
        creditCardAnne.setNumber("50408819");
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(2015, 12, 18, 10, 0));
        dateArrive = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        c.setTime(new Date(2015, 12, 26, 18, 0));
        dateDepart = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        /*
        (cancel booking) Create an itinerary with three bookings (mixed 
        fliights and hotels) and book it.
        Get the itinerary and make sure that the booking status is confirmed for each entry. Cancel the
        trip and check that now the booking status is cancelled for all bookings of the itinerary.
        */
        resetFlights(true);
    }
    @Test
    public void testC1(){
        String itineraryID = createItinerary(true);
        System.out.println(itineraryID);

        // Get the first hotel
        GetHotelsRequest getHotelsRequest = new GetHotelsRequest();
        getHotelsRequest.setItineraryId(itineraryID);
        
        GetHotels hotelsRequest = new GetHotels();
        hotelsRequest.setCity("Everywhere");
        hotelsRequest.setArrivalDate(dateArrive);
        hotelsRequest.setDepatureDate(dateDepart);
        getHotelsRequest.setGetHotelsRequest(hotelsRequest);
        
        GetHotelsResponse hotelResponse = getHotels(getHotelsRequest);
        
        
        // Get the second hotel
        GetHotelsRequest getHotelsRequest2 = new GetHotelsRequest();
        getHotelsRequest2.setItineraryId(itineraryID);
        GetHotels hotelsRequest2 = new GetHotels();
        hotelsRequest2.setCity("Bangladesh");
        hotelsRequest2.setArrivalDate(dateDepart);
        hotelsRequest2.setDepatureDate(dateArrive);
        getHotelsRequest2.setGetHotelsRequest(hotelsRequest2);
        
        GetHotelsResponse hotelResponse2 = getHotels(getHotelsRequest2);
        
        
        // Get the flight
        GetFlightsRequest flightsRequest = new GetFlightsRequest();
        flightsRequest.setFinalDestination("LHR");
        flightsRequest.setStartDestination("CPH");
        XMLGregorianCalendar liftoffDate = convertDateTimeToGregCal("2015-11-18", "17:00");
        flightsRequest.setDateFlight(liftoffDate);
        
        ws.travelgoodschema.GetFlightsRequest outerFlightsRequest = new ws.travelgoodschema.GetFlightsRequest();
        outerFlightsRequest.setGetFlightsRequest(flightsRequest);
        outerFlightsRequest.setItineraryId(itineraryID);
        GetFlightsResponse flightsResponse = getFlights(outerFlightsRequest);
        
        // Add flight to itinerary
        AddItineraryFlightRequest addFlightRequest = new AddItineraryFlightRequest();
        addFlightRequest.setBookingId(flightsResponse.getFlights().get(0).getBookingNumber());
        addFlightRequest.setItineraryId(itineraryID);
        addItineraryFlight(addFlightRequest);
        
        // Add hotels to itinerary
        AddItineraryHotelRequest addHotelRequest = new AddItineraryHotelRequest();
        addHotelRequest.setItineraryId(itineraryID);
        addHotelRequest.setBookingId(hotelResponse.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        addHotelRequest.setBookingId(hotelResponse2.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        
        BookItineraryRequest bookRequest = new BookItineraryRequest();
        bookRequest.setCreditCardInfo(creditCardAnne);
        bookRequest.setItineraryId(itineraryID);
        try {
            bookItinerary(bookRequest);
            assertTrue(true);
        } catch (BookItineraryFault ex) {
            assertFalse(true);
            Logger.getLogger(testC1.class.getName()).log(Level.SEVERE, null, ex);
        }
        GetItineraryRequest getItineraryRequest = new GetItineraryRequest();
        
        getItineraryRequest.setItineraryID(itineraryID);
        GetItineraryResponse getItinenaryResponse2 = getItinerary(getItineraryRequest);
        assertEquals(BookingStatus.CONFIRMED, getItinenaryResponse2.getItems().get(2).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED, getItinenaryResponse2.getItems().get(1).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED, getItinenaryResponse2.getItems().get(0).getFlight().getBookingStatus());
        CancelBookingRequest cancelRequest = new CancelBookingRequest();
        cancelRequest.setCreditCardInfo(creditCardAnne);
        cancelRequest.setItineraryId(itineraryID);
        
        try {
            cancelBooking(cancelRequest);
            assertTrue(true);
        } catch (CancelBookingFault ex) {
            assertFalse(true);
            Logger.getLogger(testC1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GetItineraryResponse getItinenaryResponse3 = getItinerary(getItineraryRequest);
        assertEquals(BookingStatus.CANCELLED, getItinenaryResponse3.getItems().get(2).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CANCELLED, getItinenaryResponse3.getItems().get(1).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CANCELLED, getItinenaryResponse3.getItems().get(0).getFlight().getBookingStatus());
        
        
    }

    private static boolean addItineraryFlight(ws.travelgoodschema.AddItineraryFlightRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.addItineraryFlight(part1);
    }

    private static boolean addItineraryHotel(ws.travelgoodschema.AddItineraryHotelRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.addItineraryHotel(part1);
    }

    private static ItineraryInfoType bookItinerary(ws.travelgoodschema.BookItineraryRequest part1) throws BookItineraryFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.bookItinerary(part1);
    }

    private static GetHotelsResponse getHotels(ws.travelgoodschema.GetHotelsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getHotels(part1);
    }

    private static GetFlightsResponse getFlights(ws.travelgoodschema.GetFlightsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getFlights(part1);
    }

    private static GetItineraryResponse getItinerary(ws.travelgoodschema.GetItineraryRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getItinerary(part1);
    }

    private static String createItinerary(boolean part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static ItineraryInfoType cancelBooking(ws.travelgoodschema.CancelBookingRequest part1) throws CancelBookingFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.cancelBooking(part1);
    }

    private static boolean resetFlights(boolean resetFlightsRequest) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.resetFlights(resetFlightsRequest);
    }
    
}
