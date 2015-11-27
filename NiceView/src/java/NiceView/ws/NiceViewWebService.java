/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws;

import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.CreditCardFaultType;
import dk.dtu.imm.fastmoney.types.AccountType;
import dk.dtu.imm.fastmoney.types.CreditCardInfoType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;
import niceviewschema.AddressType;
import niceviewschema.BookHotel;
import ws.niceview.BookHotelFault;
import niceviewschema.BookHotelResponse;
import niceviewschema.CancelHotel;
import ws.niceview.CancelHotelFault;
import niceviewschema.CancelHotelResponse;
import niceviewschema.GetHotels;
import niceviewschema.GetHotelsResponse;
import niceviewschema.HotelFaultType;
import niceviewschema.HotelType;
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
    Map<String, HotelReservation> bookings = new HashMap<>();
    private static final int GROUP_NUMBER = 16;
    private final String NICEVIEW_ACCOUNT_NUMBER = "50308815";
    private final String RESERVATION_SERVICE_NAME = "NiceView";
    
    public NiceViewWebService(){
        HotelType hotel1 = new HotelType();
        hotel1.setName("Mary Gold");
        //hotel1.setBookingNumber("000001");
        hotel1.setCancellable(true);
        hotel1.setCreditCardGuarentee(true);
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
    
    public GetHotelsResponse getHotels(GetHotels part1) {
        GetHotelsResponse response = new GetHotelsResponse();
        ArrayList<HotelReservation> hotelList = (ArrayList)response.getHotels();
        for (HotelType hotel : hotels) {
            if(hotel.getAddress().getCity()
                    .equals(part1.getCity())){
                HotelReservation reservation = new HotelReservation(hotel, part1.getArrivalDate(), part1.getDepatureDate());
                
                reservation.setBookingNumber(Integer.toString(bookingNumber++));
                reservation.setHotelReservationServiceName(RESERVATION_SERVICE_NAME);
                hotelList.add(reservation);
                bookings.put(reservation.getBookingNumber(), new HotelReservation(hotel, part1.getArrivalDate(), part1.getDepatureDate()));
            }
        }
        return response;
    }

    public BookHotelResponse bookHotel(BookHotel part1) throws BookHotelFault {
        BookHotelResponse response = new BookHotelResponse();
        String booking_number = part1.getBookingNumber();
        if(bookings.containsKey(booking_number)){
            HotelReservation hotelReservation = bookings.get(part1.getBookingNumber());
            if (hotelReservation == null) {
                throw bookHotelFault("No reservations found using provided Booking no:" + booking_number);
            }
            HotelType hotel = hotelReservation.getHotel();
            if(hotel.isCreditCardGuarentee()){
                
                CreditCardInfoType creditCard = part1.getCreditCardInfo();
                int amount = hotel.getPrice();
                if(hotelReservation.isBooked()){
                    throw bookHotelFault("The reservation with the given booking number is already booked.");
                }
                
                try {
                    response.setResponse(validateCreditCard(GROUP_NUMBER, creditCard, amount));
                    return response;
                } catch (CreditCardFaultMessage ex) {
                    System.out.println("Fault when booking " + hotel.getName() + " by " + creditCard.getName());
                    throw bookHotelFault(ex.getMessage());
                }
            }
            hotelReservation.BookHotel();
            System.out.println("Booking hotel " + hotel.getName());
            response.setResponse(true);
            return response;
        }
        throw bookHotelFault("No bookings with booking no:" + booking_number);
    }

    public CancelHotelResponse cancelHotel(CancelHotel part1) throws CancelHotelFault {
        CancelHotelResponse response = new CancelHotelResponse();
        AccountType NiceView_account = new AccountType();
        NiceView_account.setName("NiceView");
        NiceView_account.setNumber(NICEVIEW_ACCOUNT_NUMBER);
        
        String booking_number = part1.getBookingNumber();
        
        HotelReservation reservation = bookings.get(booking_number);
        if (reservation == null) {
            throw cancelHotelFault("No reservations found using provided Booking no:" + booking_number);
        }
        HotelType hotel = reservation.getHotel();
        
        int refund_money = hotel.getPrice();
        if(!hotel.isCancellable()){
            throw cancelHotelFault("Hotel cannot be cancelled using provided Booking no:" + booking_number);
        }
        if(reservation.isCancelled()){
            throw cancelHotelFault("Hotel with the given booking number has already been cancelled:" + booking_number);
        }
        
        try {
            refundCreditCard(GROUP_NUMBER, part1.getCreditCardInfo(), refund_money, NiceView_account);
            //booked_flights.remove(flight_info);
        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw cancelHotelFault("Error processing the credit card: " + fault.getMessage());
            } else {
                throw cancelHotelFault("Error processing the credit card: Credit card info is invalid");
            }
        }
        
//      if at this step no error is thrown, it means that the cancelling of flight is successful
        response.setResponse(true);
        return response;
    }

    private boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        synchronized( _locker ) {
            return port.validateCreditCard(group, creditCardInfo, amount);
        }
    }

    private static boolean refundCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        dk.dtu.imm.fastmoney.BankService service = new dk.dtu.imm.fastmoney.BankService();
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.refundCreditCard(group, creditCardInfo, amount, account);
    }

    private CancelHotelFault cancelHotelFault(String message) {
        HotelFaultType hotelFault = new HotelFaultType();
        hotelFault.setMessage(message);
        return new CancelHotelFault(message, hotelFault);
    }
    private BookHotelFault bookHotelFault(String message) {
        HotelFaultType hotelFault = new HotelFaultType();
        hotelFault.setMessage(message);
        return new BookHotelFault(message, hotelFault);
    }



    
}