/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.ws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.CreateItineraryResponse;
import org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.GetItineraryResponse;
import ws.travelgoodschema.Itinerary;

/**
 *
 * @author martin
 */
public class createItinerary {
    
    public createItinerary() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void createIt(){
        CreateItineraryResponse it = createItinerary("asd");
        System.out.println(it.getItinerary().getItineraryInfo().getItineraryStatus());
    }
    
    @Test
    public void getIt(){
        GetItineraryResponse it = getItinerary("dsa");
        System.out.println(it.getItinerary().getItineraryInfo().getItineraryStatus());
    }



    private static boolean addBookingsNumbers(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.AddBookingIds part1) {
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService service = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService();
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.addBookingsNumbers(part1);
    }





    private static boolean putFlight(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutFlightRequest part1) {
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService service = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService();
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.putFlight(part1);
    }

    private static boolean putHotel(org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.PutHotelRequest part1) {
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService service = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService();
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.putHotel(part1);
    }

    private static CreateItineraryResponse createItinerary(java.lang.String part1) {
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService service = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService();
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static GetItineraryResponse getItinerary(java.lang.String part1) {
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService service = new org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServiceService();
        org.netbeans.j2ee.wsdl.bpelhelperservice.java.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.getItinerary(part1);
    }


  

}
