/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import lameduck.ws.FlightsDatabase.DateUtils;
import ws.lameduck.BookFlightResponse;
import ws.lameduck.FlightInfoType;
import ws.lameduck.GetFlightsResponse;

/**
 *
 * @author Alex
 */
@WebService(serviceName = "LameDuckWSDLService", portName = "LameDuckBindingPort", endpointInterface = "ws.lameduck.LameDuck", targetNamespace = "http://LameDuck.ws", wsdlLocation = "WEB-INF/wsdl/LameDuckService/LameDuckWSDL.wsdl")
public class LameDuckService {
//    used when we comunicate with the Bank Service
    static final int GROUP_NUMBER = 16;
//    used when we add flights
    private List<FlightInfoType> booked_flights = new ArrayList();
//    used for transactions with the Bank Service
    static final String LAMEDUCK_ACCOUNT_NUMBER = "50208812";
    
    public ws.lameduck.GetFlightsResponse getFlights(ws.lameduck.GetFlightsRequest request) {
        GetFlightsResponse matched_flights = new GetFlightsResponse();
        FlightsDatabase flight_db = new FlightsDatabase();
        
        for (FlightInfoType flight_info : flight_db.flight_info_list.getFlightInfoList()) {
            int day = flight_info.getFlight().getLiftoffDate().getDay();
            int month = flight_info.getFlight().getLiftoffDate().getMonth();
            int year = flight_info.getFlight().getLiftoffDate().getYear();
            XMLGregorianCalendar flightDate = DateUtils.convertDateToGregCal(day, month, year);
            
            boolean datesSame = request.getDateFlight().toGregorianCalendar().equals(flightDate.toGregorianCalendar());
            boolean startAirSame = flight_info.getFlight().getStartAirport().equals(request.getStartDestination());
            boolean destAirSame = flight_info.getFlight().getDestinationAirport().equals(request.getFinalDestination());
            
            if (datesSame && startAirSame && destAirSame) {
                matched_flights.getFlightInfoList().add(flight_info);
            }
        }
        
        return matched_flights;        
    }

    public ws.lameduck.BookFlightResponse bookFlight(ws.lameduck.BookFlightRequest request) throws ws.lameduck.BookFlightFault {
        BookFlightResponse response = null;
//        AccountType LameDuck_account = new AccountType();
        
//        after all steps successfully complete, response set to true and returned
        response.setResponse(true);
        return response;
//        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.lameduck.CancelFlightResponse cancelFlight(ws.lameduck.CancelFlightRequest request) throws ws.lameduck.CancelFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
