//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.09.21 at 04:55:56 PM IST 
//


package com.hegde.security.wsdl_demo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hegde.security.wsdl_demo package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hegde.security.wsdl_demo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetArticleRequest }
     * 
     */
    public GetArticleRequest createGetArticleRequest() {
        return new GetArticleRequest();
    }

    /**
     * Create an instance of {@link GetArticleResponse }
     * 
     */
    public GetArticleResponse createGetArticleResponse() {
        return new GetArticleResponse();
    }

    /**
     * Create an instance of {@link Article }
     * 
     */
    public Article createArticle() {
        return new Article();
    }

}
