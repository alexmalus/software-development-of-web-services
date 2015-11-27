/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.niceview.BookHotelFault;
import niceviewschema.BookHotelResponse;
import ws.niceview.CancelHotelFault;
import niceviewschema.CancelHotelResponse;
import niceviewschema.GetHotels;
import niceviewschema.GetHotelsResponse;
import niceviewschema.HotelType;

/**
 *
 * @author martin
 */
public class NiceViewJUnitTest {
    private final XMLGregorianCalendar dateDepart;
    private final XMLGregorianCalendar dateArrive;
    
    public NiceViewJUnitTest() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date(2015, 12, 18, 10, 0));
        dateArrive = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        c.setTime(new Date(2015, 12, 26, 18, 0));
        dateDepart = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
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
    public void testGetHotels(){
        GetHotels request = new GetHotels();
        request.setCity("Everywhere");
        request.setArrivalDate(dateArrive);
        request.setDepatureDate(dateDepart);
        
        GetHotelsResponse hotels = getHotels(request);
        assertTrue(hotels.getHotels().size()>0);
        HotelType hotel = hotels.getHotels().get(0).getHotel();
        assertEquals("All of them", hotel.getAddress().getCountry());
        assertEquals(1, hotels.getHotels().size());
    }

    private static BookHotelResponse bookHotel(niceviewschema.BookHotel part1) throws BookHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.bookHotel(part1);
    }

    private static CancelHotelResponse cancelHotel(niceviewschema.CancelHotel part1) throws CancelHotelFault {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.cancelHotel(part1);
    }

    private static GetHotelsResponse getHotels(niceviewschema.GetHotels part1) {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.getHotels(part1);
    }


}
