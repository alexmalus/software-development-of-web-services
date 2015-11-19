/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import ws.niceview.AddressType;
import ws.niceview.BookHotelFault;
import ws.niceview.CancelHotelFault;
import ws.niceview.GetHotelsResponse;
import ws.niceview.HotelType;

/**
 *
 * @author martin
 */
@WebService(serviceName = "NiceViewService", portName = "NiceViewPortTypeBindingPort", endpointInterface = "ws.niceview.NiceViewPortType", targetNamespace = "http://NiceView.ws", wsdlLocation = "WEB-INF/wsdl/NiceViewWebService/NiceView.wsdl")
public class NiceViewWebService {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
    List<HotelType> hotels = new ArrayList<>();
    int bookingNumber = 1;
    Map<String, HotelType> bookings = new HashMap<>();
    
    public NiceViewWebService(){
        HotelType hotel1 = new HotelType();
        hotel1.setName("Mary Gold");
        //hotel1.setBookingNumber("000001");
        hotel1.setCancellable(true);
        hotel1.setCreditCardGuarentee(true);
        hotel1.setHotelReservationServiceName("NiceView");
        hotel1.setPrice(1000);
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
    
    public ws.niceview.GetHotelsResponse getHotels(ws.niceview.GetHotelRequestType part1) {
        GetHotelsResponse response = new GetHotelsResponse();
        ArrayList<HotelType> hotelList = (ArrayList)response.getNewElement();
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

    public boolean bookHotel(ws.niceview.BookHotelRequest part1) throws BookHotelFault {
        if(bookings.containsKey(part1.getBookingNumber())){
            HotelType hotel = bookings.get(part1.getBookingNumber());
            if(hotel.isCreditCardGuarentee()){
                throw new UnsupportedOperationException("Not implemented yet.");
            }
            System.out.println("Booking hotel " + hotel.getName());
            return true;
        }
        throw new BookHotelFault("No bookings with this booking number.", null);
    }

    public boolean cancelHotel(ws.niceview.CancelHotelRequest part1) throws CancelHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    
}
