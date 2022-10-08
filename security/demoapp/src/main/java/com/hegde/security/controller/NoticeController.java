package com.hegde.security.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@RestController
public class NoticeController {

    @GetMapping("/notices")
    public String getNotice(String input) {

        // adConnect();

        // wsdlService();

        return "Notice details from database";
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void wsdlService() {

        String name = "ganesh.r.hegde";
        String password = "Keshava1234*";

        String wsURL = "http://stoid.jio.com:7010/ssloidws/ldapauthn";

        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = "";
        String outputString = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;

        String xmlInput2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://services.reliance.com/\">"
                + "<soapenv:Header/><soapenv:Body><ser:ldapConenction><!--Optional:--><arg0>GIS</arg0><!--Optional:--><arg1>ril@12345</arg1></ser:ldapConenction></soapenv:Body></soapenv:Envelope>";

        try {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;

            byte[] buffer = new byte[xmlInput2.length()];
            buffer = xmlInput2.getBytes();

            String SOAPAction = "";
            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String
                    .valueOf(buffer.length));

            httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();

            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);

            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
            System.out.println(outputString);
            System.out.println("");

            // Get the response from the web service call
            Document document = parseXmlFile(outputString);

            NodeList nodeLst = document.getElementsByTagName("ns0:ldapConenctionResponse");
            String webServiceResponse = nodeLst.item(0).getTextContent();
            System.out.println("The response from the web service call is : " + webServiceResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}