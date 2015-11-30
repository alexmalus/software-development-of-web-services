/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import static TravelGoodBPELClientTest.testP2.convertDateTimeToGregCal;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.GetHotels;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.travelgoodbpel.BookItineraryFault;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.GetFlightsRequest;
import ws.travelgoodschema.GetFlightsResponse;
import ws.travelgoodschema.GetHotelsRequest;
import ws.travelgoodschema.GetHotelsResponse;
import ws.travelgoodschema.GetItineraryRequest;
import ws.travelgoodschema.GetItineraryResponse;
import ws.travelgoodschema.Itinerary;
import ws.travelgoodschema.ItineraryBookingFlightType;
import ws.travelgoodschema.ItineraryBookingHotelType;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author Alex
 */
public class testP1 {
    private final XMLGregorianCalendar dateArrive;
    private final XMLGregorianCalendar dateDepart;
    static private CreditCardInfoType creditCardTest_10000; //10000 cash
    
    /*
        (planning and booking) Plan a trip by first planning a flight
        (i.e. getting a list of flights and then adding a  flight to the itinerary),
        then by planning a hotel, another flight, a third flight, and finally
        a hotel. Ask for the itinerary and check that it is correct using JUnit's
        assert statements { i.e. assertEquals, assertTrue, . . . { in particular,
        that the booking status for each item is unconfirmed.
        Book the itinerary and ask again for the itinerary. Check that each booking status is now confirmed
        */
    public testP1() throws DatatypeConfigurationException{
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(2015, 12, 18, 10, 0));
        dateArrive = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        c.setTime(new Date(2015, 12, 26, 18, 0));
        dateDepart = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        
        creditCardTest_10000 = new CreditCardInfoType();
        creditCardTest_10000.setName("Tick Joachim");
        CreditCardInfoType.ExpirationDate expiration_1 = new CreditCardInfoType.ExpirationDate();
        expiration_1.setMonth(2);
        expiration_1.setYear(11);
        creditCardTest_10000.setExpirationDate(expiration_1);
        creditCardTest_10000.setNumber("50408824");
    }
    
    @Test
    public void test_p1(){
        String itineraryID = createItinerary(true);
        System.out.println(itineraryID);
        
//        flight        
        lameduckschema.GetFlightsRequest flight_request_1 = new lameduckschema.GetFlightsRequest();
        flight_request_1.setStartDestination("CPH");
        flight_request_1.setFinalDestination("LHR");
        XMLGregorianCalendar liftoffDate_1 = convertDateTimeToGregCal("2015-11-18", "17:00");
        flight_request_1.setDateFlight(liftoffDate_1);
        
        ws.travelgoodschema.GetFlightsRequest outerFlightsRequest_1 = new ws.travelgoodschema.GetFlightsRequest();
        outerFlightsRequest_1.setItineraryId(itineraryID);
        outerFlightsRequest_1.setGetFlightsRequest(flight_request_1);
        GetFlightsResponse flights_response_1 = getFlights(outerFlightsRequest_1);
        
        String flights_response_book_id = flights_response_1.getFlights().get(0).getBookingNumber();
        System.out.println(flights_response_book_id);
        
        ws.travelgoodschema.AddItineraryFlightRequest itinerary_flight_request = new  ws.travelgoodschema.AddItineraryFlightRequest();
        itinerary_flight_request.setBookingId(flights_response_book_id);
        itinerary_flight_request.setItineraryId(itineraryID);
        
        boolean add_itinerary_flight = addItineraryFlight(itinerary_flight_request);
        assertTrue(add_itinerary_flight);
        

        
//        hotel
        GetHotelsRequest getHotelsRequest_1 = new GetHotelsRequest();
        getHotelsRequest_1.setItineraryId(itineraryID);
        
        GetHotels hotelsRequest_1 = new GetHotels();
        hotelsRequest_1.setCity("Bangladesh");
        hotelsRequest_1.setArrivalDate(dateArrive);
        hotelsRequest_1.setDepatureDate(dateDepart);        
        getHotelsRequest_1.setGetHotelsRequest(hotelsRequest_1);
        
        GetHotelsResponse hotelResponse = getHotels(getHotelsRequest_1);
        
        ws.travelgoodschema.AddItineraryHotelRequest hotel_request_1 = new ws.travelgoodschema.AddItineraryHotelRequest();
        hotel_request_1.setBookingId(hotelResponse.getHotels().get(0).getBookingNumber());
        hotel_request_1.setItineraryId(itineraryID);
        boolean add_itinerary_hotel = addItineraryHotel(hotel_request_1);
        assertTrue(add_itinerary_hotel);
        
//        flight
        lameduckschema.GetFlightsRequest flight_request_2 = new lameduckschema.GetFlightsRequest();
        flight_request_2.setStartDestination("LHR");
        flight_request_2.setFinalDestination("CPH");
        XMLGregorianCalendar liftoffDate_2 = convertDateTimeToGregCal("2015-11-19", "11:00");
        flight_request_2.setDateFlight(liftoffDate_2);
        
        ws.travelgoodschema.GetFlightsRequest outerFlightsRequest_2 = new ws.travelgoodschema.GetFlightsRequest();
        outerFlightsRequest_2.setItineraryId(itineraryID);
        outerFlightsRequest_2.setGetFlightsRequest(flight_request_2);
        GetFlightsResponse flights_response_2 = getFlights(outerFlightsRequest_2);
        
        String flights_response_book_id_2 = flights_response_2.getFlights().get(0).getBookingNumber();
        System.out.println(flights_response_book_id_2);
        
        ws.travelgoodschema.AddItineraryFlightRequest itinerary_flight_request_2 = new  ws.travelgoodschema.AddItineraryFlightRequest();
        itinerary_flight_request_2.setBookingId(flights_response_book_id_2);
        itinerary_flight_request_2.setItineraryId(itineraryID);
        
        boolean add_itinerary_flight_2 = addItineraryFlight(itinerary_flight_request_2);
        assertTrue(add_itinerary_flight_2);
        
//        new flight
        lameduckschema.GetFlightsRequest flight_request_3 = new lameduckschema.GetFlightsRequest();
        flight_request_3.setStartDestination("CPH");
        flight_request_3.setFinalDestination("XAN");
        XMLGregorianCalendar liftoffDate_3 = convertDateTimeToGregCal("2015-11-23", "17:00");
        flight_request_3.setDateFlight(liftoffDate_3);
        
        ws.travelgoodschema.GetFlightsRequest outerFlightsRequest_3 = new ws.travelgoodschema.GetFlightsRequest();
        outerFlightsRequest_3.setItineraryId(itineraryID);
        outerFlightsRequest_3.setGetFlightsRequest(flight_request_3);
        GetFlightsResponse flights_response_3 = getFlights(outerFlightsRequest_3);
        
        String flights_response_book_id_3 = flights_response_3.getFlights().get(0).getBookingNumber();
        System.out.println(flights_response_book_id_3);
        
        ws.travelgoodschema.AddItineraryFlightRequest itinerary_flight_request_3 = new  ws.travelgoodschema.AddItineraryFlightRequest();
        itinerary_flight_request_3.setBookingId(flights_response_book_id_2);
        itinerary_flight_request_3.setItineraryId(itineraryID);
        
        boolean add_itinerary_flight_3 = addItineraryFlight(itinerary_flight_request_3);
        assertTrue(add_itinerary_flight_3);
        
//        hotel
        GetHotelsRequest getHotelsRequest_2 = new GetHotelsRequest();
        getHotelsRequest_2.setItineraryId(itineraryID);
        
        GetHotels hotelsRequest_2 = new GetHotels();
        hotelsRequest_2.setCity("Everywhere");
        hotelsRequest_2.setArrivalDate(dateArrive);
        hotelsRequest_2.setDepatureDate(dateDepart);        
        
        getHotelsRequest_2.setGetHotelsRequest(hotelsRequest_2);
        
        GetHotelsResponse hotelResponse_2 = getHotels(getHotelsRequest_2);
        
        ws.travelgoodschema.AddItineraryHotelRequest hotel_request_2 = new ws.travelgoodschema.AddItineraryHotelRequest();
        hotel_request_2.setBookingId(hotelResponse_2.getHotels().get(0).getBookingNumber());
        hotel_request_2.setItineraryId(itineraryID);
        boolean add_itinerary_hotel_2 = addItineraryHotel(hotel_request_2);
        assertTrue(add_itinerary_hotel_2);
        GetItineraryRequest itinerary_request = new GetItineraryRequest();
        itinerary_request.setItineraryID(itineraryID);
        GetItineraryResponse itinerary = getItinerary(itinerary_request);
        for (GetItineraryResponse.Items item : itinerary.getItems()) {
            if(item.getFlight() != null){
                assertEquals(BookingStatus.UNCONFIRMED, item.getFlight().getBookingStatus());
            }
            if(item.getHotel()!= null){
                assertEquals(BookingStatus.UNCONFIRMED, item.getHotel().getBookingStatus());
            }
        }
        
        ws.travelgoodschema.BookItineraryRequest book_itinerary_request = new ws.travelgoodschema.BookItineraryRequest();
        book_itinerary_request.setItineraryId(itineraryID);
        book_itinerary_request.setCreditCardInfo(creditCardTest_10000);
        
        try {
            ItineraryInfoType book_response = bookItinerary(book_itinerary_request);
            assertEquals(BookingStatus.CONFIRMED, book_response.getItineraryStatus());
        } catch (BookItineraryFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
        
        itinerary = getItinerary(itinerary_request);
        for (GetItineraryResponse.Items item : itinerary.getItems()) {
            if(item.getFlight() != null){
                assertEquals(BookingStatus.CONFIRMED, item.getFlight().getBookingStatus());
            }
            if(item.getHotel()!= null){
                assertEquals(BookingStatus.CONFIRMED, item.getHotel().getBookingStatus());
            }
        }
    }

    
    private static String createItinerary(boolean part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static boolean addItineraryFlight(ws.travelgoodschema.AddItineraryFlightRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.addItineraryFlight(part1);
    }

    private static GetHotelsResponse getHotels(ws.travelgoodschema.GetHotelsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getHotels(part1);
    }

    private static ws.travelgoodschema.GetFlightsResponse getFlights(ws.travelgoodschema.GetFlightsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getFlights(part1);
    }

    private static GetItineraryResponse getItinerary(ws.travelgoodschema.GetItineraryRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getItinerary(part1);
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
    
}
