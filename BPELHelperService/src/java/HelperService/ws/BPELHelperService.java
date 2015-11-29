/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperService.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import org.apache.jasper.tagplugins.jstl.ForEach;
import ws.travelgoodschema.BookingStatus;
import ws.travelgoodschema.Itinerary;
import ws.travelgoodschema.ItineraryBookingFlightType;
import ws.travelgoodschema.ItineraryBookingHotelType;
import ws.travelgoodschema.ItineraryInfoType;

/**
 *
 * @author martin
 */
@WebService(serviceName = "HelperServiceService", portName = "HelperServicePortTypeBindingPort", endpointInterface = "org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/BPELHelperService/java/HelperService", wsdlLocation = "WEB-INF/wsdl/BPELHelperService/HelperService.wsdl")
public class BPELHelperService {
    HashMap<String, Itinerary> itineraries = new HashMap<>();
    ArrayList flightsBookingIds = new ArrayList();
    ArrayList hotelsBookingIds = new ArrayList();
    
    public ws.travelgoodschema.Itinerary getItinerary(java.lang.String part1) {
        return itineraries.get(part1);
    }

    public ws.travelgoodschema.Itinerary createItinerary(java.lang.String part1) {
        Itinerary response = new ws.travelgoodschema.Itinerary();
        ItineraryInfoType info = new ItineraryInfoType();
        info.setItineraryID(part1);
        info.setItineraryStatus(BookingStatus.UNCONFIRMED);
        response.setItineraryInfo(info);
        
        response.getItineraryBookingFlightList();
        response.getItineraryBookingHotelList();
        
        itineraries.put(part1,response);
        return response;
    }

    public boolean putFlight(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutFlightRequest part1) {
        Itinerary itinerary = itineraries.get(part1);
        ArrayList<ItineraryBookingFlightType> flights = (ArrayList<ItineraryBookingFlightType>) itinerary.getItineraryBookingFlightList();
        
        
        for(Iterator<ItineraryBookingFlightType> i = flights.iterator(); i.hasNext();){
            ItineraryBookingFlightType flight = i.next();
            if(flight.getFlightInfoList().getBookingNumber() == part1.)
        }
        
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean putHotel(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutHotelRequest part1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean addBookingsNumbers(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.AddBookingIds part1) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
