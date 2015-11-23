package dtu.ws.rest.tg;

import javax.ws.rs.client.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItineraryResourceTest {
    
    private static final Client client = ClientBuilder.newClient();
    private static final WebTarget r = client.target("http://localhost:8080/tg/api/itineraries");
    
    @Test
    public void testGetItinerary(){
        String result = r.path("whatever").request().get(String.class);
        assertEquals("Not implemented yet", result);
    }
    
}
