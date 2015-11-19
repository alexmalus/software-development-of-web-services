/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.niceview.BookHotelFault;
import ws.niceview.BookHotelResponse;
import ws.niceview.CancelHotelFault;
import ws.niceview.CancelHotelResponse;
import ws.niceview.GetHotels;
import ws.niceview.GetHotelsResponse;

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
        GetHotels request = new GetHotels();
        request.setCity("Everywhere");
        GetHotelsResponse hotels = getHotels(request);
        
        assertEquals("All of them", hotels.getHotels().get(0).getAddress().getCountry());
        assertEquals(1, hotels.getHotels().size());
    }

    private static BookHotelResponse bookHotel(ws.niceview.BookHotel part1) throws BookHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.bookHotel(part1);
    }

    private static CancelHotelResponse cancelHotel(ws.niceview.CancelHotel part1) throws CancelHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.cancelHotel(part1);
    }

    private static GetHotelsResponse getHotels(ws.niceview.GetHotels part1) {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.getHotels(part1);
    }
}
