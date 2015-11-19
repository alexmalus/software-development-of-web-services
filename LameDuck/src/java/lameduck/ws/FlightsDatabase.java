package lameduck.ws;

import java.text.ParseException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.lameduck.FlightInfoType;
import ws.lameduck.FlightType;
import ws.lameduck.GetFlightsResponse;

/**
 *
 * @author Alex
 */
public class FlightsDatabase {
    GetFlightsResponse flight_info_list = new GetFlightsResponse();
    
    public FlightsDatabase(){
        FlightInfoType flight_info_no_1 = new FlightInfoType();
        flight_info_no_1.setBookingNumber("1234567");
        flight_info_no_1.setFlight(createFlight("SAS","CPH","LHR",
            DateUtils.convertDateTimeToGregCal("2015-11-18", "17:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-18", "19:00")));
        flight_info_no_1.setFlightPrice(1500);
        flight_info_no_1.setAirlineReservationServiceName("LameDuck");
        
        FlightInfoType flight_info_no_2 = new FlightInfoType();
        flight_info_no_2.setBookingNumber("1167612");
        flight_info_no_2.setFlight(createFlight("RYAN","LHR","CPH",
            DateUtils.convertDateTimeToGregCal("2015-11-19", "11:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-21", "01:00")));
        flight_info_no_2.setFlightPrice(1000);
        flight_info_no_2.setAirlineReservationServiceName("LameDuck");
        
        FlightInfoType flight_info_no_3 = new FlightInfoType();
        flight_info_no_3.setBookingNumber("1616212");
        flight_info_no_3.setFlight(createFlight("VOYA","CPH","XAN",
            DateUtils.convertDateTimeToGregCal("2015-11-23", "17:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-24", "04:00")));
        flight_info_no_3.setFlightPrice(9999);
        flight_info_no_3.setAirlineReservationServiceName("LameDuck");
        
        FlightInfoType flight_info_no_4 = new FlightInfoType();
        flight_info_no_4.setBookingNumber("1862912");
        flight_info_no_4.setFlight(createFlight("SAS","CPH","CAA",
            DateUtils.convertDateTimeToGregCal("2015-11-18", "23:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-20", "02:00")));
        flight_info_no_4.setFlightPrice(1200);
        flight_info_no_4.setAirlineReservationServiceName("LameDuck");
        
        FlightInfoType flight_info_no_5 = new FlightInfoType();
        flight_info_no_5.setBookingNumber("1009121");
        flight_info_no_5.setFlight(createFlight("SAS","CPH","UAH",
            DateUtils.convertDateTimeToGregCal("2015-11-19", "11:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-19", "13:00")));
        flight_info_no_5.setFlightPrice(1500);
        flight_info_no_5.setAirlineReservationServiceName("LameDuck");
        
        flight_info_list.getFlightInfoList().add(flight_info_no_1);
        flight_info_list.getFlightInfoList().add(flight_info_no_2);
        flight_info_list.getFlightInfoList().add(flight_info_no_3);
        flight_info_list.getFlightInfoList().add(flight_info_no_4);
        flight_info_list.getFlightInfoList().add(flight_info_no_5);
    }
    
    public static FlightType createFlight(String carrier, String startAirport, String destinationAirport,
            XMLGregorianCalendar dateTimeLift, XMLGregorianCalendar dateTimeLanding){
        FlightType flight = new FlightType();
        flight.setCarrierName(carrier);
        flight.setStartAirport(startAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setLiftoffDate(dateTimeLift);
        flight.setLandingDate(dateTimeLanding);
        return flight;        
    }
    
    public static class DateUtils {

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
        
        public static XMLGregorianCalendar convertDateToGregCal(int day, int month, int year) {
            final String DATE_FORMAT = "%s-%02d-%02d";
            String date = String.format(DATE_FORMAT, year, month, day);
            String time = "00:00";
            
            return convertDateTimeToGregCal(date, time);
        }
    }
}
