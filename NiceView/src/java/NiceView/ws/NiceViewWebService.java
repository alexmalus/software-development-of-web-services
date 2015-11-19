/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws;

import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import ws.niceview.AddressType;
import ws.niceview.BookHotelFault;
import ws.niceview.BookHotelResponse;
import ws.niceview.CancelHotelFault;
import ws.niceview.CreditCardType;
import ws.niceview.GetHotelsResponse;
import ws.niceview.HotelType;
/**
 *
 * @author martin
 */
@WebService(serviceName = "NiceViewService", portName = "NiceViewPortTypeBindingPort", endpointInterface = "ws.niceview.NiceViewPortType", targetNamespace = "http://NiceView.ws", wsdlLocation = "WEB-INF/wsdl/NiceViewWebService/NiceView.wsdl")
public class NiceViewWebService {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
    private BankService service;
    private static Object _locker = new Object();

    List<HotelType> hotels = new ArrayList<>();
    int bookingNumber = 1;
    Map<String, HotelType> bookings = new HashMap<>();
    private static final int GROUP_NUMBER = 16;
    
    public NiceViewWebService(){
        HotelType hotel1 = new HotelType();
        hotel1.setName("Mary Gold");
        //hotel1.setBookingNumber("000001");
        hotel1.setCancellable(true);
        hotel1.setCreditCardGuarentee(true);
        hotel1.setHotelReservationServiceName("NiceView");
        hotel1.setPrice(999);
        AddressType address1 = new AddressType();
        address1.setCity("Bangladesh");
        address1.setCountry("India");
        address1.setStreet("IndiaStreet");
        address1.setZipCode("123456");
        hotel1.setAddress(address1);
        
        
        HotelType hotel2 = new HotelType();
        hotel2.setName("Hilton");
        //hotel2.setBookingNumber("000002");
        hotel2.setCancellable(false);
        hotel2.setCreditCardGuarentee(false);
        hotel2.setHotelReservationServiceName("NiceView");
        hotel2.setPrice(2000);
        AddressType address2 = new AddressType();
        address2.setCity("Everywhere");
        address2.setCountry("All of them");
        address2.setStreet("This and that");
        address2.setZipCode("654321");
        hotel2.setAddress(address2);
        
        hotels.add(hotel1);
        hotels.add(hotel2);
        
    }
    
    public ws.niceview.GetHotelsResponse getHotels(ws.niceview.GetHotels part1) {
        GetHotelsResponse response = new GetHotelsResponse();
        ArrayList<HotelType> hotelList = (ArrayList)response.getHotels();
        for (HotelType hotel : hotels) {
            if(hotel.getAddress().getCity()
                    .equals(part1.getCity())){
                hotel.setBookingNumber(Integer.toString(bookingNumber++));
                hotelList.add(hotel);
                bookings.put(hotel.getBookingNumber(), hotel);
            }
        }
        return response;
    }

    public ws.niceview.BookHotelResponse bookHotel(ws.niceview.BookHotel part1) throws BookHotelFault {
        BookHotelResponse response = new BookHotelResponse();
        if(bookings.containsKey(part1.getBookingNumber())){
            HotelType hotel = bookings.get(part1.getBookingNumber());
            if(hotel.isCreditCardGuarentee()){
                CreditCardType creditCard = part1.getCreditCard();
                dk.dtu.imm.fastmoney.types.CreditCardInfoType cdi = new dk.dtu.imm.fastmoney.types.CreditCardInfoType();
                cdi.setName(creditCard.getName());
                cdi.setNumber(Integer.toString(creditCard.getCreditCardNumber()));
                
                CreditCardType.ExpirationDate expirationIn = creditCard.getExpirationDate();
                CreditCardInfoType.ExpirationDate expirationOut = new CreditCardInfoType.ExpirationDate();
                expirationOut.setMonth(expirationIn.getMonth());
                expirationOut.setYear(expirationIn.getYear());
                cdi.setExpirationDate(expirationOut);
                
                int amount = bookings.get(part1.getBookingNumber()).getPrice();
                
                try {
                    response.setResponse(validateCreditCard(GROUP_NUMBER, cdi, amount));
                    return response;
                } catch (CreditCardFaultMessage ex) {
                    System.out.println("Fault when booking " + hotel.getName() + " by " + creditCard.getName());
                    ws.niceview.HotelFaultType faultInfo = new ws.niceview.HotelFaultType();
                    faultInfo.setMessage(ex.getMessage());
                    BookHotelFault fault = new BookHotelFault(ex.getMessage(), faultInfo);
                    throw fault;
                }
            }
            System.out.println("Booking hotel " + hotel.getName());
            response.setResponse(true);
            return response;
        }
        throw new BookHotelFault("No bookings with this booking number.", null);
    }

    public ws.niceview.CancelHotelResponse cancelHotel(ws.niceview.CancelHotel part1) throws CancelHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        synchronized( _locker ) {
            return port.validateCreditCard(group, creditCardInfo, amount);
        }
    }


    
}