/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import static TravelGoodBPELClientTest.ClientTest.convertDateTimeToGregCal;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
import ws.travelgoodschema.AddItineraryFlightRequest;
import ws.travelgoodschema.AddItineraryHotelRequest;
import ws.travelgoodschema.BookItineraryRequest;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.GetFlightsResponse;
import ws.travelgoodschema.GetHotelsRequest;
import ws.travelgoodschema.GetHotelsResponse;
import ws.travelgoodschema.GetItineraryRequest;
import ws.travelgoodschema.GetItineraryResponse;
import ws.travelgoodschema.ItineraryBookingFlightType;
import ws.travelgoodschema.ItineraryBookingHotelType;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author Alex
 */
public class testB {
    private final XMLGregorianCalendar dateArrive;
    private final XMLGregorianCalendar dateDepart;
    private final CreditCardInfoType creditCardInge; //1000 cash
    
    public testB() throws DatatypeConfigurationException {creditCardInge = new CreditCardInfoType();
        creditCardInge.setName("Tobiasen Inge");
        CreditCardInfoType.ExpirationDate expirationInge = new CreditCardInfoType.ExpirationDate();
        expirationInge.setMonth(9);
        expirationInge.setYear(10);
        creditCardInge.setExpirationDate(expirationInge);
        creditCardInge.setNumber("50408823");
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(2015, 12, 18, 10, 0));
        dateArrive = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        c.setTime(new Date(2015, 12, 26, 18, 0));
        dateDepart = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        /*
        (booking fails) Plan an itinerary with three bookings (mixed 
        flights amd hotels). Get the itinerary
        and make sure that the booking status is unconfirmed for each entry. 
        Then book the itinerary.
        During booking, the second booking should fail. Get the itinerary and check that the result of the
        bookTrip operation records a failure and that the returned itinerary has cancelled as the booking
        status of the first booking and unconfirmed for the status of the second and third booking.
        */
    }
    
    @Test
    public void testB(){
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
        addHotelRequest.setItineraryId(hotelResponse.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        addHotelRequest.setItineraryId(hotelResponse2.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        
        // Get itinerary
        GetItineraryRequest getItineraryRequest = new GetItineraryRequest();
        getItineraryRequest.setItineraryID(itineraryID);
        GetItineraryResponse getItinenaryResponse = getItinerary(getItineraryRequest);
        
        // Check that there are exactly 3 elements in the itinerary
        assertEquals(3, getItinenaryResponse.getItems().size());
        
        for (GetItineraryResponse.Items item : getItinenaryResponse.getItems()) {
            if(item.getFlight() != null){
                assertEquals(BookingStatus.UNCONFIRMED, item.getFlight().getBookingStatus());
            }
            if(item.getHotel()!= null){
                assertEquals(BookingStatus.UNCONFIRMED, item.getHotel().getBookingStatus());
            }
        }
        
        BookItineraryRequest bookRequest = new BookItineraryRequest();
        bookRequest.setItineraryId(itineraryID);
        bookRequest.setCreditCardInfo(creditCardInge);
        try {
            bookItinerary(bookRequest);
            assertFalse(true);
        } catch (BookItineraryFault ex) {
            assertTrue(true);
            Logger.getLogger(testB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // We check that the first is cancelled and the other two are unconfirmed
        GetItineraryResponse getItinenaryResponse2 = getItinerary(getItineraryRequest);
        assertEquals(BookingStatus.CANCELLED, getItinenaryResponse2.getItems().get(0).getHotel().getBookingStatus());
        assertEquals(BookingStatus.UNCONFIRMED, getItinenaryResponse2.getItems().get(1).getHotel().getBookingStatus());
        assertEquals(BookingStatus.UNCONFIRMED, getItinenaryResponse2.getItems().get(2).getHotel().getBookingStatus());
        
        
        
    }

    private static String createItinerary(boolean part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static GetHotelsResponse getHotels(ws.travelgoodschema.GetHotelsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getHotels(part1);
    }
    /**
     *
     * @param date will have the form YY-MM-DD
     * @param time will have the form HH:MM
     * @return an XMLGregorianCalendar with minutes, hours, days, months and year set
     */
    public static XMLGregorianCalendar convertDateTimeToGregCal(String date, String time) {
        final String DATE_FORMAT = "%sT%s:00.000+00:00";
        String date_time = String.format(DATE_FORMAT, date, time);
        DatatypeFactory data_fact;
        data_fact = null;
        try {
            data_fact = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }

    return data_fact.newXMLGregorianCalendar(date_time);
    }

    private static GetFlightsResponse getFlights(ws.travelgoodschema.GetFlightsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getFlights(part1);
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

    private static GetItineraryResponse getItinerary(ws.travelgoodschema.GetItineraryRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getItinerary(part1);
    }

    private static ItineraryInfoType bookItinerary(ws.travelgoodschema.BookItineraryRequest part1) throws BookItineraryFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.bookItinerary(part1);
    }
}
