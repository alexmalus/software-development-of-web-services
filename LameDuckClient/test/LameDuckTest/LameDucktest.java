/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LameDuckTest;

import org.junit.Test;
import static org.junit.Assert.*;
import lameduckschema.FlightInfoType;
import lameduckschema.GetFlightsResponse;
import dateutils.DateUtils;
import static dateutils.DateUtils.convertDateTimeToGregCal;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import ws.lameduck.BookFlightFault;
import lameduckschema.BookFlightRequest;
import ws.lameduck.CancelFlightFault;
import lameduckschema.CancelFlightRequest;
import lameduckschema.FlightType;
import lameduckschema.GetFlightsRequest;

/**
 *
 * @author Alex
 */
public class LameDucktest {
    static final String LAMEDUCK_ACCOUNT_NUMBER = "50208812";
    static private CreditCardInfoType creditCardTest_10000; //10000 cash
    static private CreditCardInfoType creditCardTest_1000; //1000 cash
    static private CreditCardInfoType creditCardTest_0; // 0
    static private CreditCardInfoType creditCardTest_wrong; // wrong credentials
    
    @BeforeClass
    public static void setUpClass() {
        creditCardTest_10000 = new CreditCardInfoType();
        creditCardTest_1000 = new CreditCardInfoType();
        creditCardTest_0 = new CreditCardInfoType();
        creditCardTest_wrong = new CreditCardInfoType();
        
        creditCardTest_10000.setName("Tick Joachim");
        CreditCardInfoType.ExpirationDate expiration_1 = new CreditCardInfoType.ExpirationDate();
        expiration_1.setMonth(2);
        expiration_1.setYear(11);
        creditCardTest_10000.setExpirationDate(expiration_1);
        creditCardTest_10000.setNumber("50408824");
        
        creditCardTest_1000.setName("Tobiasen Inge");
        CreditCardInfoType.ExpirationDate expiration_2 = new CreditCardInfoType.ExpirationDate();
        expiration_2.setMonth(9);
        expiration_2.setYear(10);
        creditCardTest_1000.setExpirationDate(expiration_2);
        creditCardTest_1000.setNumber("50408823");
        
        creditCardTest_0.setName("Donovan Jasper");
        CreditCardInfoType.ExpirationDate expiration_3 = new CreditCardInfoType.ExpirationDate();
        expiration_3.setMonth(6);
        expiration_3.setYear(9);
        creditCardTest_0.setExpirationDate(expiration_3);
        creditCardTest_0.setNumber("50408818");
        
        creditCardTest_wrong.setName("Wife of Donovan Jasper");
        CreditCardInfoType.ExpirationDate expiration_4 = new CreditCardInfoType.ExpirationDate();
        expiration_4.setMonth(6);
        expiration_4.setYear(9);
        creditCardTest_0.setExpirationDate(expiration_4);
        creditCardTest_0.setNumber("50408818");
    }
    
    @AfterClass
    public static void tearDownClass() {
        resetFlights(true);
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
    public void book_test_success(){
//        this test attempts to book a flight which has/has not been booked already
//        either this test may succeed or book_test_duplicate_success depending on Junit's randomness
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234567");
        book_request.setCreditCardInfo(creditCardTest_10000);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void book_test_credit_limit(){
//        this test attempts to book a flight with an exact sum of money
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234568");
        book_request.setCreditCardInfo(creditCardTest_1000);
        
        try {
            assertTrue(bookFlight(book_request));
            System.out.println("Booking successfully for bookingNumber:" + book_request.getBookingNumber());
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void book_test_already_booked(){
//        this test attempts to book a flight which has/has not been booked already
//        either this test may succeed or book_test_success depending on Junit's randomness
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234567");
        book_request.setCreditCardInfo(creditCardTest_10000);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void book_test_empty_pocket(){
//        this test attempts to book a flight with an empty pocket
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234569");
        book_request.setCreditCardInfo(creditCardTest_0);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void book_test_wrong_credentials(){
//        this test attempts to book a flight with wrong credentials
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234570");
        book_request.setCreditCardInfo(creditCardTest_wrong);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void book_test_wrong_booking_number(){
//        this test attempts to book a flight with wrong booking number
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("7654321");
        book_request.setCreditCardInfo(creditCardTest_10000);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test
    public void refund_booking_success(){
//        this test first successfully books a flight, and then refunds it.
        BookFlightRequest book_request = new BookFlightRequest();
        book_request.setBookingNumber("1234571");
        book_request.setCreditCardInfo(creditCardTest_1000);
        
        try {
            assertTrue(bookFlight(book_request));
        } catch (BookFlightFault ex) {
            System.out.println("Refund booking failed: " + ex.getMessage());
            assertFalse(true);
        }
//        assuming nothing breaks so far, flight has been successfully booked, we can now attempt to cancel
        CancelFlightRequest cancel_request = new CancelFlightRequest();
        cancel_request.setBookingNumber(book_request.getBookingNumber());
        cancel_request.setCreditCardInfo(book_request.getCreditCardInfo());
        int flight_price = getFlightInfoByBookingNumber(book_request.getBookingNumber()).getFlightPrice();
        cancel_request.setPrice(flight_price);
        try {
            assertTrue(cancelFlight(cancel_request));
        } catch (CancelFlightFault ex) {
            System.out.println("Cancelling failed: " + ex.getMessage());
            assertFalse(true);
        }
    }
    
    //                         auxiliary functions                     \\
    //    limitation of the way the DB is defined, not part of the WSDL: cannot use it here.
    //    FlightsDatabase flight_db = new FlightsDatabase();
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
        flight_info_no_2.setBookingNumber("1234571");
        flight_info_no_2.setFlight(createFlight("CPH","UAH",
            DateUtils.convertDateTimeToGregCal("2015-11-19", "11:00"),
            DateUtils.convertDateTimeToGregCal("2015-11-19", "13:00"), "SAS"));
        flight_info_no_2.setFlightPrice(500);
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
    
    public FlightInfoType getFlightInfoByBookingNumber(String booking_number) {
        List<FlightInfoType> flight_informations = get_two_flights();
        for (FlightInfoType temp_booked_flight : flight_informations) {
            if (booking_number.equals(temp_booked_flight.getBookingNumber())) {
                return temp_booked_flight;
            }
        }
        FlightInfoType default_response = new FlightInfoType();
        return default_response;
    }

    private static boolean bookFlight(lameduckschema.BookFlightRequest bookFlightRequest) throws BookFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.bookFlight(bookFlightRequest);
    }

    private static boolean cancelFlight(lameduckschema.CancelFlightRequest cancelFlightRequest) throws CancelFlightFault {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.cancelFlight(cancelFlightRequest);
    }

    private static GetFlightsResponse getFlights(lameduckschema.GetFlightsRequest getFlightsInput) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.getFlights(getFlightsInput);
    }

    private static boolean resetFlights(boolean resetFlightsRequest) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.resetFlights(resetFlightsRequest);
    }

   

}
