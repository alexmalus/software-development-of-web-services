/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ws.niceview.BookHotel;
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
public class bookTest {
    private final CreditCardInfoType creditCardJoachim; //10000 cash
    private final CreditCardInfoType creditCardInge; //1000 cash
    private final CreditCardInfoType creditCardAnne;
    
    public bookTest() {
        creditCardJoachim = new CreditCardInfoType();
        creditCardJoachim.setName("Tick Joachim");
        CreditCardInfoType.ExpirationDate expiration = new CreditCardInfoType.ExpirationDate();
        expiration.setMonth(2);
        expiration.setYear(11);
        creditCardJoachim.setExpirationDate(expiration);
        creditCardJoachim.setNumber("50408824");
        
        
        
        creditCardInge = new CreditCardInfoType();
        creditCardInge.setName("Tobiasen Inge");
        CreditCardInfoType.ExpirationDate expirationInge = new CreditCardInfoType.ExpirationDate();
        expirationInge.setMonth(9);
        expirationInge.setYear(10);
        creditCardInge.setExpirationDate(expirationInge);
        creditCardInge.setNumber("50408823");
        
        creditCardAnne = new CreditCardInfoType();
        creditCardAnne.setName("Dirach Anne-Louise");
        CreditCardInfoType.ExpirationDate expirationAnne = new CreditCardInfoType.ExpirationDate();
        expirationAnne.setMonth(1);
        expirationAnne.setYear(10);
        creditCardAnne.setExpirationDate(expirationAnne);
        creditCardAnne.setNumber("50408819");
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    @Test 
    public void testBookHotelJoachim(){
        GetHotels request = new GetHotels();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels(request);
        assertTrue(hotels.getHotels().get(0).isCreditCardGuarentee());
        
        BookHotel bookRequest = new BookHotel();
        bookRequest.setBookingNumber(hotels.getHotels().get(0).getBookingNumber());
        bookRequest.setCreditCardInfo(creditCardJoachim);
        
        try {
            boolean response = bookHotel(bookRequest).isResponse();
            assertTrue(response);
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelInge(){
        GetHotels request = new GetHotels();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels(request);
        assertTrue(hotels.getHotels().get(0).isCreditCardGuarentee());
        
        BookHotel bookRequest = new BookHotel();
        bookRequest.setBookingNumber(hotels.getHotels().get(0).getBookingNumber());
        bookRequest.setCreditCardInfo(creditCardInge);
        
        try {
            assertTrue(bookHotel(bookRequest).isResponse());
        } catch (BookHotelFault ex) {
            System.out.println("Inge failed:");
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelAnne(){
        GetHotels request = new GetHotels();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels(request);
        assertTrue(hotels.getHotels().get(0).isCreditCardGuarentee());
        
        BookHotel bookRequest = new BookHotel();
        bookRequest.setBookingNumber(hotels.getHotels().get(0).getBookingNumber());
        bookRequest.setCreditCardInfo(creditCardAnne);
        
        try {
            assertTrue(bookHotel(bookRequest).isResponse());
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelAnneChangedExpiration(){
        GetHotels request = new GetHotels();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels(request);
        assertTrue(hotels.getHotels().get(0).isCreditCardGuarentee());
        
        BookHotel bookRequest = new BookHotel();
        bookRequest.setBookingNumber(hotels.getHotels().get(0).getBookingNumber());
        CreditCardInfoType.ExpirationDate expirationDate = creditCardAnne.getExpirationDate();
        expirationDate.setMonth(3);
        creditCardAnne.setExpirationDate(expirationDate);
        bookRequest.setCreditCardInfo(creditCardAnne);
        
        try {
            assertFalse(bookHotel(bookRequest).isResponse());
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertTrue(true);
        }
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
