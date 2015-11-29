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
import lameduckschema.FlightInfoType;
import niceviewschema.HotelReservationType;
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
    HashMap<String, FlightInfoType> flightsBookingIds = new HashMap<>();
    HashMap<String, HotelReservationType> hotelsBookingIds = new HashMap<>();
    
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
        Itinerary itinerary = itineraries.get(part1.getItineraryID());
        ArrayList<ItineraryBookingFlightType> flights = (ArrayList<ItineraryBookingFlightType>) itinerary.getItineraryBookingFlightList();
        
        
        for(Iterator<ItineraryBookingFlightType> i = flights.iterator(); i.hasNext();){
            ItineraryBookingFlightType flight = i.next();
            if(flight.getFlightInfoList().getBookingNumber().equals(part1.getBookingNumber())){
                flight.setBookingStatus(part1.getBookingStatus());
                flight.setFlightInfoList(flightsBookingIds.get(part1.getItineraryID()));
                return true;
            }
        }
        ItineraryBookingFlightType booking = new ItineraryBookingFlightType();
        booking.setBookingStatus(part1.getBookingStatus());
        booking.setFlightInfoList(flightsBookingIds.get(part1.getItineraryID()));
        flights.add(booking);
        return true;
    }

    public boolean putHotel(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutHotelRequest part1) {
        Itinerary itinerary = itineraries.get(part1.getItineraryID());
        ArrayList<ItineraryBookingHotelType> hotels = (ArrayList<ItineraryBookingHotelType>) itinerary.getItineraryBookingHotelList();
        
        
        for (ItineraryBookingHotelType hotel : hotels) {
            if(hotel.getHotelInfoList().getBookingNumber().equals(part1.getBookingNumber())){
                hotel.setBookingStatus(part1.getBookingStatus());
                hotel.setHotelInfoList(hotelsBookingIds.get(part1.getItineraryID()));
                return true;
            }
        }
        ItineraryBookingHotelType booking = new ItineraryBookingHotelType();
        booking.setBookingStatus(part1.getBookingStatus());
        booking.setHotelInfoList(hotelsBookingIds.get(part1.getItineraryID()));
        hotels.add(booking);
        return true;
    }

    public boolean addBookingsNumbers(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.AddBookingIds part1) {
//        part1.getFlighstInfo()
//        part1.getHotelInfo()
//        part1.getItineraryID()
        for (HotelReservationType hotel : part1.getHotelInfo()) {
            hotelsBookingIds.put(part1.getItineraryID(), hotel);
        }
        for(FlightInfoType flight : part1.getFlighstInfo()){
            flightsBookingIds.put(part1.getItineraryID(), flight);
        }
        
        return true;
    }
    
}
