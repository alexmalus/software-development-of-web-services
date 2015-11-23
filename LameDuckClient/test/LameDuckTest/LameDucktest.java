/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LameDuckTest;

import dk.dtu.imm.fastmoney.types.AccountType;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.lameduck.FlightInfoType;
import ws.lameduck.GetFlightsResponse;
import dateutils.DateUtils;
import static dateutils.DateUtils.convertDateTimeToGregCal;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.lameduck.BookFlightFault;
import ws.lameduck.BookFlightRequest;
import ws.lameduck.BookFlightResponse;
import ws.lameduck.CancelFlightFault;
import ws.lameduck.CancelFlightResponse;
import ws.lameduck.FlightType;
import ws.lameduck.GetFlightsRequest;

/**
 *
 * @author Alex
 */
public class LameDucktest {
    static final String LAMEDUCK_ACCOUNT_NUMBER = "50208812";
    private CreditCardInfoType creditCardTest_10000; //10000 cash
    private CreditCardInfoType creditCardTest_1000; //1000 cash
    private CreditCardInfoType creditCardTest_0; // 0
    
//    limitation of the way the DB is defined, not part of the WSDL: cannot use it here.
//    FlightsDatabase flight_db = new FlightsDatabase();
          
    public LameDucktest() {
        CreditCardInfoType creditCardTest_10000 = new CreditCardInfoType();
        creditCardTest_10000.setName("Thor-Jensen Claus");
        CreditCardInfoType.ExpirationDate expiration = new CreditCardInfoType.ExpirationDate();
        expiration.setMonth(5);
        expiration.setYear(9);
        creditCardTest_10000.setExpirationDate(expiration);
        creditCardTest_10000.setNumber("50408825");
        
        CreditCardInfoType creditCardTest_1000 = new CreditCardInfoType();
        creditCardTest_1000.setName("Tobiasen Inge");
        expiration.setMonth(9);
        expiration.setYear(10);
        creditCardTest_1000.setExpirationDate(expiration);
        creditCardTest_1000.setNumber("50408823");
        
        CreditCardInfoType creditCardTest_0 = new CreditCardInfoType();
        creditCardTest_0.setName("Donovan Jasper");
        expiration.setMonth(6);
        expiration.setYear(9);
        creditCardTest_0.setExpirationDate(expiration);
        creditCardTest_0.setNumber("50408818");
    }
    
    @Test
    public void test_flight_elements() {
    //        try to get aSingleFlight.
        FlightInfoType single_flight = get_two_flights().get(0);
        assertEquals("1234567", single_flight.getBookingNumber());
        assertEquals(1500, single_flight.getFlightPrice());
        assertEquals(true, single_flight.isCancellable());
        assertEquals("LameDuck", single_flight.getAirlineReservationServiceName());
        assertEquals("SAS", single_flight.getFlight().getCarrierName());
        assertEquals("CPH", single_flight.getFlight().getStartAirport());
        assertEquals("LHR", single_flight.getFlight().getDestinationAirport());
        XMLGregorianCalendar expected_liftoffDate = convertDateTimeToGregCal("2015-11-18", "17:00");
        XMLGregorianCalendar expected_landingDate = convertDateTimeToGregCal("2015-11-18", "19:00");
        assertEquals(expected_liftoffDate, single_flight.getFlight().getLiftoffDate());
        assertEquals(expected_landingDate, single_flight.getFlight().getLandingDate());
    }
    
    @Test
    public void get_flight() {
//        try to get a flight with the given input
        GetFlightsRequest request = new GetFlightsRequest();
        request.setStartDestination("CPH");
        request.setFinalDestination("LHR");
        XMLGregorianCalendar liftoffDate = convertDateTimeToGregCal("2015-11-18", "17:00");
        request.setDateFlight(liftoffDate);
        GetFlightsResponse response = getFlights(request);
        assertFalse(response.getFlightInfoList().isEmpty());
    }
    
    @Test
    public void book_test(){
//        BookFlightRequest book_request = new BookFlightRequest();
//        book_request.setBookingNumber("1234567");
//        book_request.setCreditCardInfo(creditCardTest);
//        
//        try {
//            assertTrue(bookFlight(book_request).isResponse());
//        } catch (BookFlightFault ex) {
//            System.out.println(ex.getMessage());
//            assertFalse(true);
//        }
    }
        
    
    
    public List<FlightInfoType> get_two_flights(){
        GetFlightsResponse flight_info_list = new GetFlightsResponse();
        
        FlightInfoType flight_info_no_1 = new FlightInfoType();
        flight_info_no_1.setBookingNumber("1234567");
        flight_info_no_1.setFlight(createFlight("CPH","LHR",
            DateUtils.convertDateTimeToGregCal("2015-11-18", "17:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-18", "19:00"), "SAS"));
        flight_info_no_1.setFlightPrice(1500);
        flight_info_no_1.setCancellable(true);
        flight_info_no_1.setAirlineReservationServiceName("LameDuck");
        
        FlightInfoType flight_info_no_2 = new FlightInfoType();
        flight_info_no_2.setBookingNumber("1234567");
        flight_info_no_2.setFlight(createFlight("CPH","LHR",
            DateUtils.convertDateTimeToGregCal("2015-11-18", "17:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-18", "19:00"), "SAS"));
        flight_info_no_2.setFlightPrice(1500);
        flight_info_no_2.setCancellable(true);
        flight_info_no_2.setAirlineReservationServiceName("LameDuck");
        
        flight_info_list.getFlightInfoList().add(flight_info_no_1);
        flight_info_list.getFlightInfoList().add(flight_info_no_2);
        
        return flight_info_list.getFlightInfoList();
    }
    
    public static FlightType createFlight(String startAirport, String destinationAirport,
            XMLGregorianCalendar dateTimeLift, XMLGregorianCalendar dateTimeLanding, String carrier){
        FlightType flight = new FlightType();
        flight.setCarrierName(carrier);
        flight.setStartAirport(startAirport);
        flight.setDestinationAirport(destinationAirport);
        flight.setLiftoffDate(dateTimeLift);
        flight.setLandingDate(dateTimeLanding);
        return flight;        
    }

    private static BookFlightResponse bookFlight(ws.lameduck.BookFlightRequest request) throws BookFlightFault {
        ws.lameduck.LameDuckWSDLService service = new ws.lameduck.LameDuckWSDLService();
        ws.lameduck.LameDuck port = service.getLameDuckBindingPort();
        return port.bookFlight(request);
    }

    private static CancelFlightResponse cancelFlight(ws.lameduck.CancelFlightRequest request) throws CancelFlightFault {
        ws.lameduck.LameDuckWSDLService service = new ws.lameduck.LameDuckWSDLService();
        ws.lameduck.LameDuck port = service.getLameDuckBindingPort();
        return port.cancelFlight(request);
    }

    private static GetFlightsResponse getFlights(GetFlightsRequest request) {
        ws.lameduck.LameDuckWSDLService service = new ws.lameduck.LameDuckWSDLService();
        ws.lameduck.LameDuck port = service.getLameDuckBindingPort();
        return port.getFlights(request);
    }
}
