package com.example.martechpraktikosdarbascurrencyapi;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;

public class Parser {
    private static final String TAG = "Parser";

    public static ArrayList<String> parseXMLResponse(InputStream inputStream) throws Exception {
        ArrayList<String> result = new ArrayList<>();

        // Naudojame SAX parserį XML atsakymui nuskaityti
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        DefaultHandler handler = new DefaultHandler() {
            boolean isCurrencyCode = false;
            boolean isExchangeRate = false;
            String currentCurrencyCode = "";

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("item")) {
                    currentCurrencyCode = "";
                } else if (qName.equalsIgnoreCase("targetCurrency")) {
                    isCurrencyCode = true;
                } else if (qName.equalsIgnoreCase("exchangeRate")) {
                    isExchangeRate = true;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (isCurrencyCode) {
                    currentCurrencyCode = new String(ch, start, length).trim();
                    isCurrencyCode = false;
                } else if (isExchangeRate) {
                    String rate = new String(ch, start, length).trim();
                    if (!currentCurrencyCode.isEmpty() && !rate.isEmpty()) {
                        Log.d(TAG, "Gautas valiutos kursas: " + currentCurrencyCode + " - " + rate);
                        result.add(currentCurrencyCode + " - " + rate);
                    }
                    isExchangeRate = false;
                }
            }
        };
        saxParser.parse(new InputSource(inputStream), handler);
        return result;
    }
}