/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TravelGoodBPELClientTest;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Alex
 */
public class ClientTest {
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
//        reset itineraries which will also destroy their bookedFlights and bookedHotels
    }
    
    @Test
    public void create_itinerary_test(){
//        in this test we attempt to create an itinerary which have:
//        itineraryId
//        it has an itinerary status = unconfirmed, confirmed, cancelled
//        bookedFlights
//        bookedHotels
        
//        if it fails, it throws an ItineraryFault
        
//        to be sure that below tests will succeed or fail(they depend on a itinerary),
//        after this test passes, create itinerary in setupClass
    }
    
    @Test
    public void add_flight_success_test(){
//        to the created itinerary, attempt to add a flight to bookedFlights
    }
    
    @Test
    public void add_flight_fail_test(){
        
    }
    
    @Test
    public void cancel_flight_success_test(){
//        to the created itinerary, attempt to cancel an already booked flight
//        in order for this test to pass, we need to setup an itinerary and an already booked flight
    }
    
    @Test
    public void cancel_flight_fail_test(){
        
    }
    
    @Test
    public void add_hotel_success_test(){
        
    }
    
    @Test
    public void add_hotel_fail_test(){
        
    }
    
    @Test
    public void cancel_hotel_success_test(){
//        to the created itinerary, attempt to cancel an already booked hotel
//        in order for this test to pass, we need to setup an itinerary and an already booked hotel
    }
    
    @Test
    public void cancel_hotel_fail_test(){
        
    }
    
    @Test
    public void cancel_planning_test(){
//        destroy itinerary
    }
    
    @Test
    public void cancel_itinerary_test(){
//        destroy itinerary if it fulfills the requirements
    }
    
}
