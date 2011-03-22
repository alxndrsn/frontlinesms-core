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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Stack;
import java.util.Map.Entry;

public class XmlEntityBuilder {
    private StringBuilder entity;
    private Stack<String> xmlElementNames;

    public XmlEntityBuilder() {
        this.entity = new StringBuilder();
        this.xmlElementNames = new Stack<String>();
        this.entity.append("<?xml version=\"1.0\"?>");
    }
    
    public String getStringEntity() {
        if (this.xmlElementNames.size() > 0) {
            throw new IllegalStateException("there are still outstanding elements that were not closed with writeEndElement");
        }
        return this.entity.toString();
    }
    
    public void writeStartElement(String elementName) {
        writeStartElement(elementName, "", "");
    }
    
    public void writeStartElement(String elementName, String elementNamespace) {
        writeStartElement(elementName, elementNamespace, "");
    }

    public void writeStartElement(String elementName, String namespaceUri, String namespacePrefix) {
        if (elementName == null || elementName.length() == 0) {
            throw new IllegalArgumentException("elementName must be non-empty");
        }

        if (namespaceUri == null) {
            throw new IllegalArgumentException("namespaceUri cannot be null");
        }

        if (namespacePrefix == null) {
            throw new IllegalArgumentException("namespacePrefix cannot be null");
        }

        HashMap<String, String> attributes = null;
        if (namespaceUri.length() > 0) {
            attributes = new HashMap<String, String>();
            String namespaceAttributeName = "xmlns";
            if (namespacePrefix.length() > 0) {
                namespaceAttributeName += (":" + namespacePrefix);
            }
            attributes.put(namespaceAttributeName, namespaceUri);
        }

        writeStartElement(elementName, attributes);
    }

    /**
     * Overload that accepts a set of attributes. Assumes that the attribute names are prefix-qualified if that is
     * required
     */
    public void writeStartElement(String elementName, HashMap<String, String> attributes) {
        if (elementName == null || elementName.length() == 0) {
            throw new IllegalArgumentException("elementName must be non-empty");
        }

        this.xmlElementNames.push(elementName);
        this.entity.append("<" + elementName);
        if (attributes != null && !attributes.isEmpty()) {
            for (Entry<String, String> attribute : attributes.entrySet()) {
                this.entity.append(" " + attribute.getKey() + "=\"" + attribute.getValue() + "\"");
            }
        }
        this.entity.append(">");
    }


    public void writeText(String text) {
        this.entity.append(escapeText(text));
    }

    public void writeEndElement() {
        if (this.xmlElementNames.size() == 0) {
            throw new IllegalStateException("writeEndElement must be matched by an earlier call to writeStartElement");
        }
        String elementName = this.xmlElementNames.pop();
        this.entity.append("</" + elementName + ">");
    }

    /**
     * Escapes an XML string (i.e. 
     * becomes &lt;br/&gt;) There are 5 entities of interest (<, >, ", ', and &)
     */
    private static String escapeText(String rawText) {
        StringBuilder escapedText = new StringBuilder();
        StringCharacterIterator characterIterator = new StringCharacterIterator(rawText);
        char currentCharacter = characterIterator.current();
        while (currentCharacter != CharacterIterator.DONE) {
            switch (currentCharacter) {
                case '<':
                    escapedText.append("&lt;");
                    break;

                case '>':
                    escapedText.append("&gt;");
                    break;

                case '"':
                    escapedText.append("&quot;");
                    break;

                case '\'':
                    escapedText.append("&apos;");
                    break;

                case '&':
                    escapedText.append("&amp;");
                    break;

                default:
                    escapedText.append(currentCharacter);
                    break;
            }
            currentCharacter = characterIterator.next();
        }
        return escapedText.toString();
    }

}
