/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.travelgoodbpel.CancelBookingFault;
import ws.travelgoodbpel.CancelItineraryFault;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.GetFlightsRequest;
import ws.travelgoodschema.GetFlightsResponse;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author Alex
 */
public class testP2 {
    

    public testP2() {
        /*
        (cancel planning) Plan a trip by first getting a list of flights and then adding a 
        flight to the itinerary.
        Then cancel planning.
        */
    }
    
    @Test
    public void test_p2(){
        String itineraryID = createItinerary(true);
        System.out.println(itineraryID);
        
        GetFlightsRequest flight_request = new GetFlightsRequest();
        lameduckschema.GetFlightsRequest ldGetFlightsRequest = new lameduckschema.GetFlightsRequest();
        ldGetFlightsRequest.setStartDestination("CPH");
        ldGetFlightsRequest.setFinalDestination("LHR");
        XMLGregorianCalendar liftoffDate = convertDateTimeToGregCal("2015-11-18", "17:00");
        ldGetFlightsRequest.setDateFlight(liftoffDate);
        
        flight_request.setGetFlightsRequest(ldGetFlightsRequest);
        flight_request.setItineraryId(itineraryID);
        
        GetFlightsResponse flights_response = getFlights(flight_request);
        String flights_response_book_id = flights_response.getFlights().get(0).getBookingNumber();
        System.out.println(flights_response_book_id);
        
        ws.travelgoodschema.AddItineraryFlightRequest itinerary_flight_request = new  ws.travelgoodschema.AddItineraryFlightRequest();
        itinerary_flight_request.setBookingId(flights_response_book_id);
        itinerary_flight_request.setItineraryId(itineraryID);
        
        boolean add_itinerary_flight = addItineraryFlight(itinerary_flight_request);
        assertTrue(add_itinerary_flight);
        
//        cancel planning
        ws.travelgoodschema.CancelItineraryRequest cancel_itinerary = new ws.travelgoodschema.CancelItineraryRequest();
        cancel_itinerary.setItineraryId(itineraryID);
        try {
            BookingStatus booking_phase = cancelItinerary(cancel_itinerary).getItineraryStatus();
            System.out.println(booking_phase);
        } catch (CancelItineraryFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
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


    private static boolean addItineraryFlight(ws.travelgoodschema.AddItineraryFlightRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.addItineraryFlight(part1);
    }

    private static String createItinerary(boolean part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static ItineraryInfoType cancelItinerary(ws.travelgoodschema.CancelItineraryRequest part1) throws CancelItineraryFault {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.cancelItinerary(part1);
    }

    private static ws.travelgoodschema.GetFlightsResponse getFlights(ws.travelgoodschema.GetFlightsRequest part1) {
        ws.travelgoodbpel.TravelGoodBPELService service = new ws.travelgoodbpel.TravelGoodBPELService();
        ws.travelgoodbpel.TravelGoodBPELPortType port = service.getTravelGoodBPELPortTypeBindingPort();
        return port.getFlights(part1);
    }

    
}