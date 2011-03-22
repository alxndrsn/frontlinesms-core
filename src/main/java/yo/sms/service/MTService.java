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

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;

import org.apache.log4j.Logger;

/**
 * Creates our Mobile Terminating requests.
 * 
 * @author Eric
 * 
 */
public class MTService {

    private static final String ROOT_TAG = "YbsSmgw";
    private static final String REQUEST_TAG = "Request";
    private static final String METHOD_TAG = "Method";
    private static final String ACCOUNT_TAG = "Account";
    private static final String USERNAME_TAG = "Username";
    private static final String PASSWORD_TAG = "Password";
    private static final String MESSAGES_ROOT_TAG = "Messages";
    private static final String MESSAGE_TAG = "Message";
    private static final String DESTINATION_TAG = "Destination";
    private static final String CONTENT_TAG = "Content";
    private static final String SENDER_TAG = "Sender";
    private static final String SEND_SMS = "/sendfrontlinesms";

    /** Logging object */
    private static Logger LOG = FrontlineUtils.getLogger(MTService.class);

    /**
     * Posts XML requests to our SMS gateway
     * 
     * @param xmlText
     * @return The gateway response string
     * @throws IOException
     */
    public String postXmlRequest(String xmlText, boolean secure) throws IOException {
	String response = HttpConnection.postData(xmlText, SEND_SMS, secure);
	return response;
    }

    /**
     * Builds the XML request for sending an SMS
     * 
     * @param accountNumber
     * @param username
     * @param password
     * @param message
     *            The FrontlineSMS message
     * @return XML string entity
     */
    public String buildXmlRequestEntity(String accountNumber, String username, String password, FrontlineMessage message) {
	final int METHOD = 2;
	final int ACCOUNT_NUMBER = 3;
	final int USERNAME = 4;
	final int PASSWORD = 5;

	String[] elementNames = { ROOT_TAG, REQUEST_TAG, METHOD_TAG, ACCOUNT_TAG, USERNAME_TAG, PASSWORD_TAG, MESSAGES_ROOT_TAG };
	XmlEntityBuilder xmlEntityBuilder = new XmlEntityBuilder();
	for (int i = 0; i < 7; i++) {
	    xmlEntityBuilder.writeStartElement(elementNames[i]);
	    switch (i) {
	    case METHOD:
		xmlEntityBuilder.writeText("SendSms");
		xmlEntityBuilder.writeEndElement();
		break;
	    case ACCOUNT_NUMBER:
		xmlEntityBuilder.writeText(accountNumber);
		xmlEntityBuilder.writeEndElement();
		break;
	    case USERNAME:
		xmlEntityBuilder.writeText(username);
		xmlEntityBuilder.writeEndElement();
		break;
	    case PASSWORD:
		xmlEntityBuilder.writeText(password);
		xmlEntityBuilder.writeEndElement();
		break;
	    }
	}
	xmlEntityBuilder.writeStartElement(MESSAGE_TAG);
	xmlEntityBuilder.writeStartElement(CONTENT_TAG);
	xmlEntityBuilder.writeText(message.getTextContent());
	xmlEntityBuilder.writeEndElement();
	xmlEntityBuilder.writeStartElement(SENDER_TAG);
	xmlEntityBuilder.writeText(message.getSenderMsisdn());
	xmlEntityBuilder.writeEndElement();
	xmlEntityBuilder.writeStartElement(DESTINATION_TAG);
	xmlEntityBuilder.writeText(message.getRecipientMsisdn());
	xmlEntityBuilder.writeEndElement();
	// Close message, messages, request, and root elements
	for (int j = 0; j < 4; j++) {
	    xmlEntityBuilder.writeEndElement();
	}
	return xmlEntityBuilder.getStringEntity();
    }

}
