package NiceView.ws;

import javax.xml.datatype.XMLGregorianCalendar;
import niceviewschema.HotelReservationType;
import niceviewschema.HotelType;

/**
 *
 * @author martin
 */
public class HotelReservation extends HotelReservationType{
    private boolean booked = false;
    private boolean cancelled = false;
    
    public HotelReservation(HotelType hotel, XMLGregorianCalendar arrival, XMLGregorianCalendar departure){
        this.setHotel(hotel);
        this.setArrivalDate(arrival);
        this.setDepartureDate(departure);
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
    
    
}
