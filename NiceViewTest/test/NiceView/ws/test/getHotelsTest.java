/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NiceView.ws.test;

import niceviewtest.GetHotelRequestType;
import niceviewtest.GetHotelsResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martin
 */
public class getHotelsTest {
    
    public getHotelsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void getHotelsTest(){
        niceviewtest.GetHotelRequestType request = new GetHotelRequestType();
        request.setCity("Bangladesh");
        
        GetHotelsResponse response = getHotels(request);
        assertEquals("Mary Gold", response.getNewElement().get(0).getName());
        assertEquals(1, response.getNewElement().size());
        
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    private static GetHotelsResponse getHotels(niceviewtest.GetHotelRequestType part1) {
        niceviewtest.NiceViewService service = new niceviewtest.NiceViewService();
        niceviewtest.NiceViewPortType port = service.getNiceViewPortTypeBindingPort();
        return port.getHotels(part1);
    }
}
