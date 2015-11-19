
package niceviewtest;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the niceviewtest package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetHotelsRequest_QNAME = new QName("http://NiceView.ws", "getHotelsRequest");
    private final static QName _CancelHotelResponse_QNAME = new QName("http://NiceView.ws", "cancelHotelResponse");
    private final static QName _BookHotelResponse_QNAME = new QName("http://NiceView.ws", "bookHotelResponse");
    private final static QName _FaultElement_QNAME = new QName("http://NiceView.ws", "faultElement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: niceviewtest
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreditCardType }
     * 
     */
    public CreditCardType createCreditCardType() {
        return new CreditCardType();
    }

    /**
     * Create an instance of {@link GetHotelsResponse }
     * 
     */
    public GetHotelsResponse createGetHotelsResponse() {
        return new GetHotelsResponse();
    }

    /**
     * Create an instance of {@link HotelType }
     * 
     */
    public HotelType createHotelType() {
        return new HotelType();
    }

    /**
     * Create an instance of {@link BookHotelRequest }
     * 
     */
    public BookHotelRequest createBookHotelRequest() {
        return new BookHotelRequest();
    }

    /**
     * Create an instance of {@link GetHotelRequestType }
     * 
     */
    public GetHotelRequestType createGetHotelRequestType() {
        return new GetHotelRequestType();
    }

    /**
     * Create an instance of {@link CancelHotelRequest }
     * 
     */
    public CancelHotelRequest createCancelHotelRequest() {
        return new CancelHotelRequest();
    }

    /**
     * Create an instance of {@link HotelFaultType }
     * 
     */
    public HotelFaultType createHotelFaultType() {
        return new HotelFaultType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link CreditCardType.ExpirationDate }
     * 
     */
    public CreditCardType.ExpirationDate createCreditCardTypeExpirationDate() {
        return new CreditCardType.ExpirationDate();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHotelRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NiceView.ws", name = "getHotelsRequest")
    public JAXBElement<GetHotelRequestType> createGetHotelsRequest(GetHotelRequestType value) {
        return new JAXBElement<GetHotelRequestType>(_GetHotelsRequest_QNAME, GetHotelRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NiceView.ws", name = "cancelHotelResponse")
    public JAXBElement<Boolean> createCancelHotelResponse(Boolean value) {
        return new JAXBElement<Boolean>(_CancelHotelResponse_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NiceView.ws", name = "bookHotelResponse")
    public JAXBElement<Boolean> createBookHotelResponse(Boolean value) {
        return new JAXBElement<Boolean>(_BookHotelResponse_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HotelFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://NiceView.ws", name = "faultElement")
    public JAXBElement<HotelFaultType> createFaultElement(HotelFaultType value) {
        return new JAXBElement<HotelFaultType>(_FaultElement_QNAME, HotelFaultType.class, null, value);
    }

}
