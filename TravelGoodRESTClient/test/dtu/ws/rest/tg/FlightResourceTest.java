package dtu.ws.rest.tg;

import javax.ws.rs.client.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlightResourceTest {

    private static final Client client = ClientBuilder.newClient();
    private static final WebTarget r = client.target("http://localhost:8080/tg/api/flights");

    @Test
    public void testGetFlights() {
        // TODO - Do actual implementation of this
        String placeQuery = "somewhere";
        String timeQuery = "whenever";
        String result = r.queryParam("place", placeQuery).queryParam("time", timeQuery).request().get(String.class);

        assertEquals("Not implemented yet. Received place: " + placeQuery + " and time: " + timeQuery, result);
    }

}
