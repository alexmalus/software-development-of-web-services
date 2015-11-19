/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import dk.dtu.imm.fastmoney.BankService;
import dk.dtu.imm.fastmoney.CreditCardFaultMessage;
import dk.dtu.imm.fastmoney.CreditCardFaultType;
import dk.dtu.imm.fastmoney.types.AccountType;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import lameduck.ws.FlightsDatabase.DateUtils;
import ws.lameduck.BookFlightResponse;
import ws.lameduck.FlightInfoType;
import ws.lameduck.GetFlightsResponse;
import ws.lameduck.BookFlightFault;
import ws.lameduck.BookFlightRequest;
import ws.lameduck.CancelFlightFault;
import ws.lameduck.CancelFlightRequest;
import ws.lameduck.CancelFlightResponse;
import ws.lameduck.FlightFaultType;
import ws.lameduck.GetFlightsRequest;

/**
 *
 * @author Alex
 */
@WebService(serviceName = "LameDuckWSDLService", portName = "LameDuckBindingPort", endpointInterface = "ws.lameduck.LameDuck", targetNamespace = "http://LameDuck.ws", wsdlLocation = "WEB-INF/wsdl/LameDuckService/LameDuckWSDL.wsdl")
public class LameDuckService {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/fastmoney.imm.dtu.dk_8080/BankService.wsdl")
    private BankService service;
//    used when we communicate with the Bank Service
    static final int GROUP_NUMBER = 16;
//    used when we add flights
    private List<FlightInfoType> booked_flights = new ArrayList();
//    has all the demo flights
    private final FlightsDatabase flight_db = new FlightsDatabase();
//    used for transactions with the Bank Service
    static final String LAMEDUCK_ACCOUNT_NUMBER = "50208812";
    
    public GetFlightsResponse getFlights(GetFlightsRequest request) {
        GetFlightsResponse matched_flights = new GetFlightsResponse();
        
        for (FlightInfoType flight_info : flight_db.flight_info_list.getFlightInfoList()) {
            int day = flight_info.getFlight().getLiftoffDate().getDay();
            int month = flight_info.getFlight().getLiftoffDate().getMonth();
            int year = flight_info.getFlight().getLiftoffDate().getYear();
            XMLGregorianCalendar flightDate = DateUtils.convertDateToGregCal(day, month, year);
            
            boolean same_dateflight = request.getDateFlight().toGregorianCalendar().equals(flightDate.toGregorianCalendar());
            boolean same_startdest = flight_info.getFlight().getStartAirport().equals(request.getStartDestination());
            boolean same_finaldest = flight_info.getFlight().getDestinationAirport().equals(request.getFinalDestination());
            
            if (same_dateflight && same_startdest && same_finaldest) {
                matched_flights.getFlightInfoList().add(flight_info);
            }
        }
        
        return matched_flights;        
    }

    public BookFlightResponse bookFlight(BookFlightRequest request) throws BookFlightFault {
        BookFlightResponse response = new BookFlightResponse();
        AccountType LameDuck_account = new AccountType();
        LameDuck_account.setName("LAMEDUCK");
        LameDuck_account.setNumber(LAMEDUCK_ACCOUNT_NUMBER);
        
        String booking_number = request.getBookingNumber();
        FlightInfoType flight_info = new FlightInfoType();

//        check if there is a flight with the given booking number in the db.
        List<FlightInfoType> flight_informations = flight_db.flight_info_list.getFlightInfoList();
        for(FlightInfoType temp_flight_info : flight_informations){
            if (temp_flight_info.getBookingNumber().equals(booking_number)) {
                flight_info = temp_flight_info;
                break;
            }
        }
        
        if (flight_info == null) {
            throw book_flight_fault("No flights found with given Booking no:" + booking_number);
        } else if (flight_already_booked(flight_info)) {
            throw book_flight_fault("Flight already booked with given Booking no:" + booking_number);
        }
        
        int flight_price = flight_info.getFlightPrice();
        try {
            validateCreditCard(GROUP_NUMBER, request.getCreditCardInfo(), flight_price);
            chargeCreditCard(GROUP_NUMBER, request.getCreditCardInfo(), flight_price, LameDuck_account);
            booked_flights.add(flight_info);
        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw book_flight_fault("Error processing the credit card: " + fault.getMessage());
            } else {
                throw book_flight_fault("Error processing the credit card: credit card info is invalid");
            }
        }
        
//      if at this step no error is thrown, it means that the booking of flight is successful
        response.setResponse(true);
        return response;
    }

    public CancelFlightResponse cancelFlight(CancelFlightRequest request) throws CancelFlightFault {
        CancelFlightResponse response = new CancelFlightResponse();
        AccountType LameDuck_account = new AccountType();
        LameDuck_account.setName("LAMEDUCK");
        LameDuck_account.setNumber(LAMEDUCK_ACCOUNT_NUMBER);
          
        int refund_money = request.getPrice() / 2;
        String booking_number = request.getBookingNumber();
        FlightInfoType flight_info = null;
        
        for (FlightInfoType temp_bookedFlight : booked_flights) {
            if (temp_bookedFlight.getBookingNumber().equals(booking_number)) {
                flight_info = temp_bookedFlight;
            }
        }
        if (flight_info == null) {
            throw cancel_flight_fault("No flights found using provided Booking no:" + booking_number);
        }
        if(!flight_info.isCancellable()){
            throw cancel_flight_fault("Flight cannot be cancelled using provided Booking no:" + booking_number);          
        } 
        
        try {
            refundCreditCard(GROUP_NUMBER, request.getCreditCardInfo(), refund_money, LameDuck_account);
            booked_flights.remove(flight_info);
        } catch (CreditCardFaultMessage ex) {
            CreditCardFaultType fault = ex.getFaultInfo();
            if (fault != null) {
                throw cancel_flight_fault("Error processing the credit card: " + fault.getMessage());
            } else {
                throw cancel_flight_fault("Error processing the credit card: Credit card info is invalid");
            }
        }
        
//      if at this step no error is thrown, it means that the cancelling of flight is successful
        response.setResponse(true);
        return response;
    }
    
    private boolean flight_already_booked(FlightInfoType flightInfo) {
        String booking_number = flightInfo.getBookingNumber();
        for (FlightInfoType temp_bookedFlight : booked_flights) {
            if (booking_number.equals(temp_bookedFlight.getBookingNumber())) {
                return true;
            }
        }
        return false;
    }
    
    private BookFlightFault book_flight_fault(String faultInfo) {
        FlightFaultType flightFault = new FlightFaultType();
        flightFault.setFaultMessage(faultInfo);
        return new BookFlightFault(faultInfo, flightFault);
    }
    
    private CancelFlightFault cancel_flight_fault(String faultInfo) {
        FlightFaultType flightFault = new FlightFaultType();
        flightFault.setFaultMessage(faultInfo);
        return new CancelFlightFault(faultInfo, flightFault);
    }
    
//    TODO: implement resetFlights called when an order is being cancelled

    private boolean chargeCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.chargeCreditCard(group, creditCardInfo, amount, account);
    }

    private boolean refundCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount, dk.dtu.imm.fastmoney.types.AccountType account) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.refundCreditCard(group, creditCardInfo, amount, account);
    }

    private boolean validateCreditCard(int group, dk.dtu.imm.fastmoney.types.CreditCardInfoType creditCardInfo, int amount) throws CreditCardFaultMessage {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        dk.dtu.imm.fastmoney.BankPortType port = service.getBankPort();
        return port.validateCreditCard(group, creditCardInfo, amount);
    }
}
