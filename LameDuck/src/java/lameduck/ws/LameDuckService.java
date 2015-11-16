/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lameduck.ws;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import ws.lameduck.FlightInfoType;
import ws.lameduck.GetFlightsResponse;

/**
 *
 * @author Alex
 */
@WebService(serviceName = "LameDuckWSDLService", portName = "LameDuckBindingPort", endpointInterface = "ws.lameduck.LameDuck", targetNamespace = "http://LameDuck.ws", wsdlLocation = "WEB-INF/wsdl/LameDuckService/LameDuckWSDL.wsdl")
public class LameDuckService {
    static final int GROUP_NUMBER = 16;
    private List<FlightInfoType> bookedFlights = new ArrayList();
    static final String lameduck_acc_no = "23212312";
    
    public ws.lameduck.GetFlightsResponse getFlights(ws.lameduck.GetFlightsRequest request) {
        GetFlightsResponse matchedFlights = new GetFlightsResponse();
        
//        FlightInfoType newItem;
//        matchedFlights.getFlightInfoList().add(newItem);
        return matchedFlights;        
    }

    public ws.lameduck.BookFlightResponse bookFlight(ws.lameduck.BookFlightRequest request) throws ws.lameduck.BookFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.lameduck.CancelFlightResponse cancelFlight(ws.lameduck.CancelFlightRequest request) throws ws.lameduck.CancelFlightFault {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
