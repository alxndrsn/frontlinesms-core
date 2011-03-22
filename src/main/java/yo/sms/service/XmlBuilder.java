/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2011 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package yo.sms.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Helper methods for manipulating XML documents
 * @author Eric <elwanga@yo.co.ug>
 *
 */
public class XmlBuilder {
    static DocumentBuilderFactory documentBuilderFactory = CreateDocumentBuilderFactory();

    private static DocumentBuilderFactory CreateDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // need to set this to true so that getLocalName and getNamespaceURI function correctly
        factory.setNamespaceAware(true);
        return factory;
    }

    /**
     * Convert the given string into an XML DOM
     */
    public static Document parseXml(String xmlString) throws SAXException, IOException, ParserConfigurationException {
        return parseXml(new StringReader(xmlString));
    }

    /**
     * Convert the given reader into an XML DOM
     */
    public static Document parseXml(Reader reader) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(reader);
        return documentBuilder.parse(inputSource);
    }

    /**
     * Uses our shared document builder factory to create a new DOM
     */
    public static Document createDocument() throws ParserConfigurationException {
        return documentBuilderFactory.newDocumentBuilder().newDocument();
    }
}
