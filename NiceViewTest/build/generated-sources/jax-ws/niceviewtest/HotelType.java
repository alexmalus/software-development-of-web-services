
package niceviewtest;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HotelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HotelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="bookingNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cancellable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="address" type="{http://NiceView.ws}AddressType"/>
 *         &lt;element name="creditCardGuarentee" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="hotelReservationServiceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HotelType", propOrder = {
    "name",
    "price",
    "bookingNumber",
    "cancellable",
    "address",
    "creditCardGuarentee",
    "hotelReservationServiceName"
})
public class HotelType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger price;
    @XmlElement(required = true)
    protected String bookingNumber;
    protected boolean cancellable;
    @XmlElement(required = true)
    protected AddressType address;
    protected boolean creditCardGuarentee;
    @XmlElement(required = true)
    protected String hotelReservationServiceName;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPrice(BigInteger value) {
        this.price = value;
    }

    /**
     * Gets the value of the bookingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBookingNumber() {
        return bookingNumber;
    }

    /**
     * Sets the value of the bookingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBookingNumber(String value) {
        this.bookingNumber = value;
    }

    /**
     * Gets the value of the cancellable property.
     * 
     */
    public boolean isCancellable() {
        return cancellable;
    }

    /**
     * Sets the value of the cancellable property.
     * 
     */
    public void setCancellable(boolean value) {
        this.cancellable = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddress(AddressType value) {
        this.address = value;
    }

    /**
     * Gets the value of the creditCardGuarentee property.
     * 
     */
    public boolean isCreditCardGuarentee() {
        return creditCardGuarentee;
    }

    /**
     * Sets the value of the creditCardGuarentee property.
     * 
     */
    public void setCreditCardGuarentee(boolean value) {
        this.creditCardGuarentee = value;
    }

    /**
     * Gets the value of the hotelReservationServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHotelReservationServiceName() {
        return hotelReservationServiceName;
    }

    /**
     * Sets the value of the hotelReservationServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHotelReservationServiceName(String value) {
        this.hotelReservationServiceName = value;
    }

}
