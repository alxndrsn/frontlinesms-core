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
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Mobile Originating service for creating message store requests and processing
 * responses
 * 
 * @author Eric
 * 
 */
public class MOService {
    private static final String MESSAGE_STORE_TAG = "MessageStoreRequest";
    private static final String ACCOUNT_TAG = "AccountNumber";
    private static final String PASSWORD_TAG = "Password";
    private static final String MESSAGE_COUNT_TAG = "NumberOfMessages";
    private static final String RETRIEVE_SMS = "/retrievesms";
    private static final String STORE_RESPONSE_TAG = "MessageStoreResponse";
    private static final String STATUS_TAG = "Status";
    private static final String ERROR_MESSAGE = "ErrorMsg";
    private static final String RETRIEVED_COUNT_TAG = "RetrievedMessageCount";
    private static final String REMAINING_COUNT_TAG = "RemainingMessageCount";
    private static final String STORE_LIMIT_TAG = "MessageStoreLimit";
    private static final String RETRIEVED_MESSAGES_TAG = "RetrievedMessages";
    private static final String MESSAGE_TAG = "MessageStoreLimit";
    private static final String ID_TAG = "Id";
    private static final String TEXT_TAG = "Text";
    private static final String SENDER_TAG = "Sender";
    private static final String DESTINATION_TAG = "Destination";
    private static final String DATE_TAG = "DeliveryTime";
    /** Number of messages required by the client in the response **/
    private static final String MESSAGE_REQUEST_LIMIT = "1";

    /** Logging object */
    private static Logger LOG = FrontlineUtils.getLogger(MOService.class);

    public synchronized FrontlineMessage[] retrieveMoMessages(String data, boolean secure) throws IOException, SAXException, ParserConfigurationException {
	String response = HttpConnection.postData(data, RETRIEVE_SMS, secure);
	return processMessages(response);
    }

    /**
     * Query messages from the gateway
     * 
     * @param accountNumber
     *            Account name or Username
     * @param username
     *            Username or account name
     * @param password
     *            Account password
     * @return Message Store XML request
     */
    public String getMessageStoreXmlRequestEntity(String accountNumber, String username, String password) {
	final int ACCOUNT_NUMBER = 1;
	final int PASSWORD = 2;
	final int MESSAGE_COUNT = 3;

	String[] startTags = { MESSAGE_STORE_TAG, ACCOUNT_TAG, PASSWORD_TAG, MESSAGE_COUNT_TAG };
	XmlEntityBuilder xmlEntityBuilder = new XmlEntityBuilder();
	for (int i = 0; i < 4; i++) {
	    xmlEntityBuilder.writeStartElement(startTags[i]);
	    switch (i) {
	    case ACCOUNT_NUMBER:// A/c# or username
		xmlEntityBuilder.writeText(username);
		xmlEntityBuilder.writeEndElement();
		break;
	    case PASSWORD:
		xmlEntityBuilder.writeText(password);
		xmlEntityBuilder.writeEndElement();
		break;
	    case MESSAGE_COUNT:
		xmlEntityBuilder.writeText(MESSAGE_REQUEST_LIMIT);
		xmlEntityBuilder.writeEndElement();
		break;
	    }
	}
	// Close off root tag
	xmlEntityBuilder.writeEndElement();

	return xmlEntityBuilder.getStringEntity();
    }

    /**
     * Processes message store response for FrontlineSMS
     * 
     * @param xml
     *            Response XML
     * @return FrontlineSMS format messages
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private FrontlineMessage[] processMessages(String xml) throws SAXException, IOException, ParserConfigurationException {
	Document doc = XmlBuilder.parseXml(xml);
	// Testing. TODO Handle status and count
	NodeList nodeList = doc.getElementsByTagName(STATUS_TAG);
	LOG.info("Status: " + nodeList.item(0).getTextContent());
	if (nodeList.item(0).getTextContent().equals("ERROR")) {
	    // Ignoring error message?
	    nodeList = doc.getElementsByTagName(ERROR_MESSAGE);
	    LOG.debug("MO Error: " + nodeList.item(0).getTextContent());
	    LOG.info("[DEBUG] MO Error: " + nodeList.item(0).getTextContent());
	    return null;
	}
	nodeList = doc.getElementsByTagName(RETRIEVED_COUNT_TAG);
	LOG.info(nodeList.item(0).getTextContent());
	nodeList = doc.getElementsByTagName(REMAINING_COUNT_TAG);
	LOG.info(nodeList.item(0).getTextContent());
	nodeList = doc.getElementsByTagName(STORE_LIMIT_TAG);
	LOG.info(nodeList.item(0).getTextContent());

	nodeList = doc.getElementsByTagName(RETRIEVED_MESSAGES_TAG);
	// Get list of <Message></Message>
	NodeList messageNodes = nodeList.item(0).getChildNodes();
	int messages = messageNodes.getLength();
	FrontlineMessage[] frontlineMessages = new FrontlineMessage[messages];
	for (int i = 0; i < messages; i++) {
	    Node message = messageNodes.item(i);
	    nodeList = message.getChildNodes();
	    String[] messageDetail = new String[4];
	    int length = nodeList.getLength();
	    for (int j = 0; j < length; j++) {
		String nodeName = nodeList.item(j).getNodeName();
		if (nodeName.equalsIgnoreCase(TEXT_TAG)) {
		    messageDetail[3] = nodeList.item(j).getTextContent();
		} else if (nodeName.equalsIgnoreCase(SENDER_TAG)) {
		    messageDetail[1] = nodeList.item(j).getTextContent();
		} else if (nodeName.equalsIgnoreCase(DESTINATION_TAG)) {
		    messageDetail[2] = nodeList.item(j).getTextContent();
		} else if (nodeName.equalsIgnoreCase(DATE_TAG)) {
		    messageDetail[0] = nodeList.item(j).getTextContent();
		}
	    }
	    long epoch = 0;
	    try {
		epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageDetail[0]).getTime();
	    } catch (ParseException e) {
		LOG.error("[DEBUG] Parse date Error: " + e);
	    }
	    FrontlineMessage frontlineMessage = FrontlineMessage.createIncomingMessage(epoch, messageDetail[1], messageDetail[2], messageDetail[3]);
	    frontlineMessages[i] = frontlineMessage;
	}
	return frontlineMessages;
    }

}
