package rest.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lameduckschema.GetFlightsRequest;
import lameduckschema.GetFlightsResponse;
import niceviewschema.GetHotels;
import static dateutils.DateUtils.convertDateTimeToGregCal;

@Path("/flights")
public class FlightResource {

    @GET
    public GetFlightsResponse getFlights(
            @QueryParam("origin") String origin,
            @QueryParam("destination") String destination,
            @QueryParam("time") String time,
            @QueryParam("day") String day) {

        GetFlightsRequest request = new GetFlightsRequest();
        XMLGregorianCalendar date = convertDateTimeToGregCal(day, time);

        request.setDateFlight(date);
        request.setStartDestination(origin);
        request.setFinalDestination(destination);

        return getFlights_1(request);

    }

    private static GetFlightsResponse getFlights_1(lameduckschema.GetFlightsRequest getFlightsInput) {
        ws.lameduck.LameDuckService service = new ws.lameduck.LameDuckService();
        ws.lameduck.LameDuckPortType port = service.getLameDuckPortTypeBindingPort();
        return port.getFlights(getFlightsInput);
    }
}
