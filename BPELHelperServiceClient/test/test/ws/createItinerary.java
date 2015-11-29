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
import ws.helperservice.CreateHelperItineraryResponse;
import ws.helperservice.GetItineraryResponse;
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
        CreateHelperItineraryResponse it = createItinerary("asd");
        System.out.println(it.getItinerary().getItineraryInfo().getItineraryStatus());
    }
    
    @Test
    public void getIt(){
        GetItineraryResponse it = getItinerary("dsa");
        System.out.println(it.getItinerary().getItineraryInfo().getItineraryStatus());
    }

    private static CreateHelperItineraryResponse createItinerary(java.lang.String part1) {
        ws.helperservice.HelperServiceService service = new ws.helperservice.HelperServiceService();
        ws.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.createItinerary(part1);
    }

    private static GetItineraryResponse getItinerary(java.lang.String part1) {
        ws.helperservice.HelperServiceService service = new ws.helperservice.HelperServiceService();
        ws.helperservice.HelperServicePortType port = service.getHelperServicePortTypeBindingPort();
        return port.getItinerary(part1);
    }





  

}
