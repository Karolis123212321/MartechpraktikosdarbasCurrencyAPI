package com.example.martechpraktikosdarbascurrencyapi;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;

public class Parser {
    public static ArrayList<String> parseSOAPResponse(InputStream inputStream) throws Exception {
        ArrayList<String> result = new ArrayList<>();

        // Naudojame SAX parserį SOAP atsakymui nuskaityti
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        DefaultHandler handler = new DefaultHandler() {
            boolean isExchangeRateResult = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (localName.equalsIgnoreCase("getCurrentExchangeRateResult")) {
                    isExchangeRateResult = true;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (isExchangeRateResult) {
                    String rate = new String(ch, start, length).trim();
                    if (!rate.isEmpty()) {
                        result.add("USD - " + rate);
                    }
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (localName.equalsIgnoreCase("getCurrentExchangeRateResult")) {
                    isExchangeRateResult = false;
                }
            }
        };
        saxParser.parse(new InputSource(inputStream), handler);
        return result;
    }
}