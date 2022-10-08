
package com.reliance.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.reliance.services package. 
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

    private final static QName _LdapConenction_QNAME = new QName("http://services.reliance.com/", "ldapConenction");
    private final static QName _LdapConenctionResponse_QNAME = new QName("http://services.reliance.com/", "ldapConenctionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.reliance.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LdapConenction }
     * 
     */
    public LdapConenction createLdapConenction() {
        return new LdapConenction();
    }

    /**
     * Create an instance of {@link LdapConenctionResponse }
     * 
     */
    public LdapConenctionResponse createLdapConenctionResponse() {
        return new LdapConenctionResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LdapConenction }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LdapConenction }{@code >}
     */
    @XmlElementDecl(namespace = "http://services.reliance.com/", name = "ldapConenction")
    public JAXBElement<LdapConenction> createLdapConenction(LdapConenction value) {
        return new JAXBElement<LdapConenction>(_LdapConenction_QNAME, LdapConenction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LdapConenctionResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LdapConenctionResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://services.reliance.com/", name = "ldapConenctionResponse")
    public JAXBElement<LdapConenctionResponse> createLdapConenctionResponse(LdapConenctionResponse value) {
        return new JAXBElement<LdapConenctionResponse>(_LdapConenctionResponse_QNAME, LdapConenctionResponse.class, null, value);
    }

}
