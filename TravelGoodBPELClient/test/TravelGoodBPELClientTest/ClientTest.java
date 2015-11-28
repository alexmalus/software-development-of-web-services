/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.GetHotels;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.w3c.dom.Element;
import ws.travelgoodschema.GetHotelsRequest;
import ws.travelgoodschema.GetHotelsResponse;
import ws.travelgoodschema.GetItineraryRequest;
import ws.travelgoodschema.GetItineraryResponse;

/**
 *
 * @author Alex
 */
public class ClientTest {
    private final XMLGregorianCalendar dateArrive;
    private final XMLGregorianCalendar dateDepart;
    
    public ClientTest() throws DatatypeConfigurationException{
        
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(2015, 12, 18, 10, 0));
        dateArrive = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        c.setTime(new Date(2015, 12, 26, 18, 0));
        dateDepart = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
//        reset itineraries which will also destroy their bookedFlights and bookedHotels
    }

    @Test
    public void create_itinerary_test() {
//        in this test we attempt to create an itinerary which have:
//        itineraryId
//        it has an itinerary status = unconfirmed, confirmed, cancelled
//        bookedFlights
//        bookedHotels

//        if it fails, it throws an ItineraryFault
//        to be sure that below tests will succeed or fail(they depend on a itinerary),
//        after this test passes, create itinerary in setupClass
        
        String itineraryID = createItinerary(true);
        System.out.println(itineraryID);

        GetHotelsRequest getHotelsRequest = new GetHotelsRequest();
        getHotelsRequest.setItineraryId(itineraryID);
        
        GetHotels hotelsRequest = new GetHotels();
        hotelsRequest.setCity("Everywhere");
        hotelsRequest.setArrivalDate(dateArrive);
        hotelsRequest.setDepatureDate(dateDepart);
                
        
        getHotelsRequest.setGetHotelsRequest(hotelsRequest);
        
        GetHotelsResponse hotelResponse = getHotels(getHotelsRequest);
        
        System.out.println(hotelResponse.getHotels().get(0).getBookingNumber());
        
        
//        GetItineraryResponse response = new GetItineraryResponse();
//        GetItineraryRequest request = new GetItineraryRequest();
//        request.setItineraryID(itineraryID);
//        response = getItinerary(request);
        

    }

    @Test
    public void add_flight_success_test() {
//        to the created itinerary, attempt to add a flight to bookedFlights
    }

    @Test
    public void add_flight_fail_test() {

    }

    @Test
    public void cancel_flight_success_test() {
//        to the created itinerary, attempt to cancel an already booked flight
//        in order for this test to pass, we need to setup an itinerary and an already booked flight
    }

    @Test
    public void cancel_flight_fail_test() {

    }

    @Test
    public void add_hotel_success_test() {

    }

    @Test
    public void add_hotel_fail_test() {

    }

    @Test
    public void cancel_hotel_success_test() {
//        to the created itinerary, attempt to cancel an already booked hotel
//        in order for this test to pass, we need to setup an itinerary and an already booked hotel
    }

    @Test
    public void cancel_hotel_fail_test() {

    }

    @Test
    public void cancel_planning_test() {
//        destroy itinerary
    }

    @Test
    public void cancel_itinerary_test() {
//        destroy itinerary if it fulfills the requirements
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

    private static GetHotelsResponse getHotels(ws.travelgoodschema.GetHotelsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getHotels(part1);
    }

}
