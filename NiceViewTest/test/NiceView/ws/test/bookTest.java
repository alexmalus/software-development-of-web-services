/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

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
public class bookTest {
    private final CreditCardType creditCardJoachim; //10000 cash
    private final CreditCardType creditCardInge; //1000 cash
    private final CreditCardType creditCardAnne;
    
    public bookTest() {
        creditCardJoachim = new CreditCardType();
        creditCardJoachim.setName("Tick Joachim");
        CreditCardType.ExpirationDate expiration = new CreditCardType.ExpirationDate();
        expiration.setMonth(2);
        expiration.setYear(11);
        creditCardJoachim.setExpirationDate(expiration);
        creditCardJoachim.setCreditCardNumber(50408824);
        
        
        
        creditCardInge = new CreditCardType();
        creditCardInge.setName("Tobiasen Inge");
        CreditCardType.ExpirationDate expirationInge = new CreditCardType.ExpirationDate();
        expirationInge.setMonth(9);
        expirationInge.setYear(10);
        creditCardInge.setExpirationDate(expirationInge);
        creditCardInge.setCreditCardNumber(50408823);
        
        creditCardAnne = new CreditCardType();
        creditCardAnne.setName("Dirach Anne-Louise");
        CreditCardType.ExpirationDate expirationAnne = new CreditCardType.ExpirationDate();
        expirationAnne.setMonth(1);
        expirationAnne.setYear(10);
        creditCardAnne.setExpirationDate(expirationAnne);
        creditCardAnne.setCreditCardNumber(50408819);
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    @Test 
    public void testBookHotelJoachim(){
        GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels_1(request);
        assertTrue(hotels.getNewElement().get(0).isCreditCardGuarentee());
        
        BookHotelRequest bookRequest = new BookHotelRequest();
        bookRequest.setBookingNumber(hotels.getNewElement().get(0).getBookingNumber());
        bookRequest.setCreditCard(creditCardJoachim);
        
        try {
            assertTrue(bookHotel(bookRequest));
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelInge(){
        GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels_1(request);
        assertTrue(hotels.getNewElement().get(0).isCreditCardGuarentee());
        
        BookHotelRequest bookRequest = new BookHotelRequest();
        bookRequest.setBookingNumber(hotels.getNewElement().get(0).getBookingNumber());
        bookRequest.setCreditCard(creditCardInge);
        
        try {
            assertTrue(bookHotel(bookRequest));
        } catch (BookHotelFault ex) {
            System.out.println("Inge failed:");
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelAnne(){
        GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels_1(request);
        assertTrue(hotels.getNewElement().get(0).isCreditCardGuarentee());
        
        BookHotelRequest bookRequest = new BookHotelRequest();
        bookRequest.setBookingNumber(hotels.getNewElement().get(0).getBookingNumber());
        bookRequest.setCreditCard(creditCardAnne);
        
        try {
            assertTrue(bookHotel(bookRequest));
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertFalse(true);
        }
    }
    
    @Test 
    public void testBookHotelAnneChangedExpiration(){
        GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Bangladesh");
        GetHotelsResponse hotels = getHotels_1(request);
        assertTrue(hotels.getNewElement().get(0).isCreditCardGuarentee());
        
        BookHotelRequest bookRequest = new BookHotelRequest();
        bookRequest.setBookingNumber(hotels.getNewElement().get(0).getBookingNumber());
        CreditCardType.ExpirationDate expirationDate = creditCardAnne.getExpirationDate();
        expirationDate.setMonth(3);
        creditCardAnne.setExpirationDate(expirationDate);
        bookRequest.setCreditCard(creditCardAnne);
        
        try {
            assertFalse(bookHotel(bookRequest));
        } catch (BookHotelFault ex) {
            System.out.println(ex.getMessage());
            assertTrue(true);
        }
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
