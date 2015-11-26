package NiceView.ws;

import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.HotelType;

/**
 *
 * @author martin
 */
public class HotelReservation {
    private final HotelType hotel;
    private final XMLGregorianCalendar departure;
    private final XMLGregorianCalendar arrival;
    private boolean booked = false;
    private boolean cancelled = false;
    
    public HotelReservation(HotelType hotel, XMLGregorianCalendar arrival, XMLGregorianCalendar departure){
        this.hotel = hotel;
        this.arrival = arrival;
        this.departure = departure;
    }
    
    public boolean BookHotel(){
        if (booked){
            return false;
        }
        booked = true;
        return true;
    }
    public boolean isBooked(){
        return this.booked;
    }
    
    public boolean isCancelled(){
        return this.cancelled;
    }
    
    public boolean CancelHotel(){
        if(cancelled){
            return false;
        }
        this.cancelled = true;
        return true;
    }
    
    public HotelType getHotel(){
        return this.hotel;
    }
    public XMLGregorianCalendar getDepartureDate(){
        return this.departure;
    }
    public XMLGregorianCalendar getArrivalDate(){
        return this.arrival;
    }
    
}
