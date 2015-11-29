/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperService.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.jws.WebService;
import lameduckschema.FlightInfoType;
import niceviewschema.HotelReservationType;
import org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.CreateItineraryResponse;
import org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.GetItineraryResponse;
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

    public org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.GetItineraryResponse getItinerary(java.lang.String part1) {
        //TODO implement this method
        GetItineraryResponse response = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.GetItineraryResponse();
        response.setItinerary(itineraries.get(part1));
        return response;
    }

    public org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.CreateItineraryResponse createItinerary(java.lang.String part1) {
        System.out.println("Started creating Itinerary");
        Itinerary response = new ws.travelgoodschema.Itinerary();
        ItineraryInfoType info = new ItineraryInfoType();
        info.setItineraryID(part1);
        info.setItineraryStatus(BookingStatus.UNCONFIRMED);
        response.setItineraryInfo(info);
        
        response.getItineraryBookingFlightList();
        response.getItineraryBookingHotelList();
        
        itineraries.put(part1,response);
        System.out.println("Finished creating Itinerary");
        CreateItineraryResponse response2 = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.CreateItineraryResponse();
        response2.setItinerary(response);
        return response2;
    }

    public boolean putFlight(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutFlightRequest part1) {
        Itinerary itinerary = itineraries.get(part1.getItineraryID());
        ArrayList<ItineraryBookingFlightType> flights = (ArrayList<ItineraryBookingFlightType>) itinerary.getItineraryBookingFlightList();
        
        
        for (ItineraryBookingFlightType flight : flights) {
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
