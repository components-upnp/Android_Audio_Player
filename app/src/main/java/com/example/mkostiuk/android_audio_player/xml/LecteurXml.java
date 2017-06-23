package com.example.mkostiuk.android_audio_player.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by mkostiuk on 23/06/2017.
 */

public class LecteurXml {

    private String udn;
    private String filePath;

    public LecteurXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            boolean isUdn = false;
            boolean isFilePath = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equalsIgnoreCase("UDN"))
                    isUdn = true;
                if (qName.equalsIgnoreCase("FILEPATH"))
                    isFilePath = true;
            }

            @Override
            public void characters(char ch[], int start, int length) {
                if (isFilePath) {
                    isFilePath = false;
                    filePath = new String(ch, start, length);
                    System.err.println(filePath);
                }
                if (isUdn) {
                    isUdn = false;
                    udn = new String(ch, start, length);
                }
            }
        };
        sp.parse(new InputSource(new StringReader(xml)), handler);
    }

    public String getFilePath() {
        return filePath;
    }

    public String getUdn() {
        return udn;
    }

}
