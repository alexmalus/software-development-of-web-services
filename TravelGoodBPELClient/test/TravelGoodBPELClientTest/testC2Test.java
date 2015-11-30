/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.GetHotels;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.travelgoodbpel.BookItineraryFault;
import ws.travelgoodbpel.CancelBookingFault;
import ws.travelgoodschema.AddItineraryHotelRequest;
import ws.travelgoodschema.BookItineraryRequest;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.CancelBookingRequest;
import ws.travelgoodschema.GetHotelsRequest;
import ws.travelgoodschema.GetHotelsResponse;
import ws.travelgoodschema.GetItineraryRequest;
import ws.travelgoodschema.GetItineraryResponse;
import ws.travelgoodschema.ItineraryBookingHotelType;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author Alex
 */
public class testC2Test {
    private final CreditCardInfoType creditCardAnne;
    private final XMLGregorianCalendar dateArrive;
    private final XMLGregorianCalendar dateDepart;
    
    public testC2Test() throws DatatypeConfigurationException {
        
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
        (cancelling fails) Create an itinerary with three bookings and book it. Make sure that the booking
        status is confirmed for each entry. During cancelling of the trip, the cancellation of the second
        booking should fail. Check that the cancelling resulted in an error condition (e.g. value of status
        variable, exception, HTTP status code). Get the itinerary and check that the returned itinerary
        has cancelled as the first and third booking and confirmed for the second booking.
        */
    }
    
    @Test
    public void testC2(){
        String itineraryID = createItinerary(true);
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
        hotelsRequest2.setCity("Copenhagen");
        hotelsRequest2.setArrivalDate(dateArrive);
        hotelsRequest2.setDepatureDate(dateDepart);
        getHotelsRequest2.setGetHotelsRequest(hotelsRequest2);
        
        GetHotelsResponse hotelResponse2 = getHotels(getHotelsRequest2);
        
        
        // Get the third hotel
        GetHotelsRequest getHotelsRequest3 = new GetHotelsRequest();
        getHotelsRequest3.setItineraryId(itineraryID);
        GetHotels hotelsRequest3 = new GetHotels();
        hotelsRequest3.setCity("Bangladesh");
        hotelsRequest3.setArrivalDate(dateDepart);
        hotelsRequest3.setDepatureDate(dateArrive);
        getHotelsRequest3.setGetHotelsRequest(hotelsRequest3);
        
        GetHotelsResponse hotelResponse3 = getHotels(getHotelsRequest3);
        
        // Add hotels to itinerary
        AddItineraryHotelRequest addHotelRequest = new AddItineraryHotelRequest();
        addHotelRequest.setItineraryId(itineraryID);
        addHotelRequest.setBookingId(hotelResponse.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        addHotelRequest.setBookingId(hotelResponse2.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        addHotelRequest.setBookingId(hotelResponse3.getHotels().get(0).getBookingNumber());
        addItineraryHotel(addHotelRequest);
        BookItineraryRequest bookRequest = new BookItineraryRequest();
        bookRequest.setCreditCardInfo(creditCardAnne);
        bookRequest.setItineraryId(itineraryID);
        
        
        try {
            bookItinerary(bookRequest);
            assertTrue(true);
        } catch (BookItineraryFault ex) {
            assertFalse(true);
            Logger.getLogger(testC2Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        GetItineraryRequest getRequest = new GetItineraryRequest();
        getRequest.setItineraryID(itineraryID);
        
        GetItineraryResponse getResult = getItinerary(getRequest);
        
        assertEquals(3, getResult.getItems().size());
        for(GetItineraryResponse.Items item : getResult.getItems()){
            ItineraryBookingHotelType hotel = item.getHotel();
            assertEquals(BookingStatus.CONFIRMED,hotel.getBookingStatus());
        }
        CancelBookingRequest cancelBookingRequest = new CancelBookingRequest();
        
        cancelBookingRequest.setCreditCardInfo(creditCardAnne);
        cancelBookingRequest.setItineraryId(itineraryID);
        
        try {
            cancelBooking(cancelBookingRequest);
            assertTrue(false);
        } catch (CancelBookingFault ex) {
            assertTrue(true);
            Logger.getLogger(testC2Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GetItineraryResponse getResult2 = getItinerary(getRequest);
        assertEquals(BookingStatus.CANCELLED,getResult2.getItems().get(0).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED,getResult2.getItems().get(1).getHotel().getBookingStatus());
        assertEquals(BookingStatus.CANCELLED,getResult2.getItems().get(2).getHotel().getBookingStatus());
        
        
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

    private static ItineraryInfoType bookItinerary(ws.travelgoodschema.BookItineraryRequest part1) throws BookItineraryFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.bookItinerary(part1);
    }

    private static ItineraryInfoType cancelBooking(ws.travelgoodschema.CancelBookingRequest part1) throws CancelBookingFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.cancelBooking(part1);
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
    
}
