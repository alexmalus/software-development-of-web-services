/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import niceviewtest.BookHotelFault;
import niceviewtest.BookHotelRequest;
import niceviewtest.CancelHotelFault;
import niceviewtest.CreditCardType;
import niceviewtest.GetHotelRequestType;
import niceviewtest.GetHotelsResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martin
 */
public class NiceViewJUnitTest {
    
    public NiceViewJUnitTest() {
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
    @Test
    public void testGetHotels() {
        GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Everywhere");
        GetHotelsResponse hotels = getHotels_1(request);
        
        assertEquals("All of them", hotels.getNewElement().get(0).getAddress().getCountry());
        assertEquals(1, hotels.getNewElement().size());
    }
    
    
    
    
    

    private static boolean bookHotel(niceviewtest.BookHotelRequest part1) throws BookHotelFault {
        niceviewtest.NiceViewService service = new niceviewtest.NiceViewService();
        niceviewtest.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.bookHotel(part1);
    }

    private static boolean cancelHotel(niceviewtest.CancelHotelRequest part1) throws CancelHotelFault {
        niceviewtest.NiceViewService service = new niceviewtest.NiceViewService();
        niceviewtest.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.cancelHotel(part1);
    }

    private static GetHotelsResponse getHotels_1(niceviewtest.GetHotelRequestType part1) {
        niceviewtest.NiceViewService service = new niceviewtest.NiceViewService();
        niceviewtest.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.getHotels(part1);
    }
}
