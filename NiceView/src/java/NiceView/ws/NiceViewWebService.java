/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
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
    List<HotelType> hotels = new ArrayList<>();
    
    public NiceViewWebService(){
        HotelType hotel1 = new HotelType();
        hotel1.setName("Mary Gold");
        hotel1.setBookingNumber("000001");
        hotel1.setCancellable(true);
        hotel1.setCreditCardGuarentee(true);
        hotel1.setHotelReservationServiceName("NiceView");
        hotel1.setPrice(new BigInteger("1000"));
        AddressType address1 = new AddressType();
        address1.setCity("Bangladesh");
        address1.setCountry("India");
        address1.setStreet("IndiaStreet");
        address1.setZipCode("123456");
        hotel1.setAddress(address1);
        
        
        HotelType hotel2 = new HotelType();
        hotel2.setName("Hilton");
        hotel2.setBookingNumber("000002");
        hotel2.setCancellable(false);
        hotel2.setCreditCardGuarentee(false);
        hotel2.setHotelReservationServiceName("NiceView");
        hotel2.setPrice(new BigInteger("2000"));
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
                    .equals(part1.getCity()))
                hotelList.add(hotel);
        }
        return response;
    }

    public boolean bookHotel(ws.niceview.BookHotelRequest part1) throws BookHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean cancelHotel(ws.niceview.CancelHotelRequest part1) throws CancelHotelFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
