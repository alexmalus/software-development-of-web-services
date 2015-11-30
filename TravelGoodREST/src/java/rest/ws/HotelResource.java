package rest.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.GetHotels;
import niceviewschema.GetHotelsResponse;
import niceviewschema.HotelReservationType;

@Path("/hotels")
public class HotelResource {

    @GET
    public GetHotelsResponse getHotels(
            @QueryParam("city") String city,
            @QueryParam("arrival") long arrival,
            @QueryParam("departure") long departure) {

        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date(arrival));
            DatatypeFactory df = DatatypeFactory.newInstance();
            XMLGregorianCalendar arrivalDate = df.newXMLGregorianCalendar(c);
            XMLGregorianCalendar departureDate = df.newXMLGregorianCalendar(c);
            GetHotels request = new GetHotels();
            request.setCity(city);
            request.setArrivalDate(arrivalDate);
            request.setDepatureDate(departureDate);
            return getHotels_1(request);
        } catch (DatatypeConfigurationException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    private static GetHotelsResponse getHotels_1(niceviewschema.GetHotels part1) {
        ws.niceview.NiceViewService service = new ws.niceview.NiceViewService();
        ws.niceview.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.getHotels(part1);
    }
}
