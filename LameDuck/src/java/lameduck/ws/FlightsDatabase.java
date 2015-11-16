/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import com.sun.xml.wss.util.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.lameduck.FlightInfoType;
import ws.lameduck.FlightType;
import ws.lameduck.GetFlightsRequest;

/**
 *
 * @author Alex
 */
public class FlightsDatabase {
    GetFlightsRequest flightInfoList = new GetFlightsRequest();
    
    public FlightsDatabase() throws ParseException, DatatypeConfigurationException{
        String format = "yyyy-MM-dd'T'HH:mm";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new SimpleDateFormat(format).parse("2014-12-23T00:00:00-05:00"));
        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar( cal);

//        start_date
//        end_date
        
        FlightInfoType flight_info_1 = new FlightInfoType();
        flight_info_1.setBookingNumber("1234567");
        
        flight_info_1.setFlight(createFlight("SAS","CPH","LHR",
            DateUtils.toXmlGregCal("2015-11-18", "17:00"),
            DateUtils.toXmlGregCal("2015-11-18", "19:00")));
        flight_info_1.setFlightPrice(1500);
        flight_info_1.setAirlineReservationServiceName("LameDuck");
    }
    
    public FlightType createFlight(String carrier, String startAirport, String destinationAirport,
            XMLGregorianCalendar dateTimeLift, XMLGregorianCalendar dateTimeLanding){
        FlightType flight = new FlightType();
        flight.setCarrierName(carrier);
        flight.setStartAirport(startAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setLiftoffDate(dateTimeLift);
        flight.setLandingDate(dateTimeLanding);
        return flight;        
    }
    
}
