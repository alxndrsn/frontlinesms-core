/*
 * Copyright (C) 2008 Andrea Zito
 * 
 * This file is part of jMmsLib.
 *
 * jMmsLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or  (at your option) any later version.
 *
 * jMmsLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jMmsLib.  If not, see <http://www.gnu.org/licenses/>.
 */ 
package net.sourceforge.jmmslib;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an mms message.<br>
 * <p>This class is used to fill the field of an mms message and do some basic data
 * validation.</p>
 * 
 * <p>The field required for the various type of mms messages are listed on the document
 * <i>WAP-209-MMSEncapsulation-20020105-a</i>, avaiable at the 
 * <a href="http://www.openmobilealliance.org/tech/affiliates/wap/wapindex.html">Open Mobile Alliance</a> web site. 
 * 
 * @author Andrea Zito
 *
 */
public class MmsMessage {

	private static final String NL = "\r\n";
	
	/*=========================================================================
	 * Header Name Constants
	 *=========================================================================*/
	public static final String MMS_MESSAGE_TYPE = "X-Mms-Message-Type";
	public static final String MMS_TRANSACTION_ID = "X-Mms-Transaction-ID";
	public static final String MMS_VERSION = "X-Mms-MMS-Version";
	public static final String MMS_DATE = "Date";
	public static final String MMS_FROM = "From";
	public static final String MMS_TO = "To";
	public static final String MMS_CC = "CC";
	public static final String MMS_BCC = "BCC";	
	public static final String MMS_SUBJECT = "Subject";
	public static final String MMS_CLASS = "X-Mms-Message-Class";
	public static final String MMS_EXPIRY = "X-Mms-Expiry";
	public static final String MMS_DELIVERY_TIME = "X-Mms-Delivery-Time";
	public static final String MMS_PRIORITY = "X-Mms-Priority";
	public static final String MMS_SENDER_VISIBILITY = "X-Mms-Sender-Visibility";
	public static final String MMS_DELIVERY_REPORT = "X-Mms-Delivery-Report";
	public static final String MMS_READ_REPLY = "X-Mms-Read-Reply";
	public static final String MMS_CONTENT_TYPE = "Content-Type";
	public static final String MMS_RESPONSE_STATUS = "X-Mms-Response-Status";
	public static final String MMS_RESPONSE_TEXT = "X-Mms-Response-Text";
	public static final String MMS_MESSAGE_ID = "Message-ID";

	/*=========================================================================
	 * Header Value Constants
	 *=========================================================================*/
	public static final String CHARSET_US_ASCII = "us-ascii";
	public static final String CHARSET_UTF8 = "utf-8";

	public static final String MMS_MESSAGE_TYPE_SEND_REQUEST = "m-send-req";
	public static final String MMS_MESSAGE_TYPE_SEND_CONF = "m-send-conf";
	
	public static final String MMS_VERSION_1 = "1.0"; 
	
	public static final String MMS_DELIVERY_REPORT_YES = "Yes";
	public static final String MMS_DELIVERY_REPORT_NO = "No";
	
	public static final String MMS_CLASS_PERSONAL = "Personal";
	public static final String MMS_CLASS_ADVERTISEMENT = "Advertisement";
	public static final String MMS_CLASS_INFORMATIONAL = "Informational";
	public static final String MMS_CLASS_AUTO = "Auto";
	
	public static final String MMS_PRIORITY_LOW = "Low";
	public static final String MMS_PRIORITY_NORMAL = "Normal";
	public static final String MMS_PRIORITY_HIGH = "High";
	
	public static final String MMS_READ_REPLY_YES = "Yes";
	public static final String MMS_READ_REPLY_NO = "No";
	
	public static final String MMS_RESPONSE_STATUS_OK = "OK"; 
	public static final String MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED = "Error-unspecified";
	public static final String MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED = "Error-service-denied";
	public static final String MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT = "Error-message-format-corrupt";
	public static final String  MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED = "Error-sending-address-unresolved";
	public static final String MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND = "Error-message-not-found";
	public static final String MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM = "Error-network-problem";
	public static final String MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED = "Error-contant-not-accepted";
	public static final String MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE = "Error-unsupported-message"; 
	
	public static final String MMS_SENDER_VISIBILITY_HIDE = "Hide";
	public static final String MMS_SENDER_VISIBILITY_SHOW = "Show";
	
	public static final String MMS_ADDRESS_TYPE_MOBILE_NUMBER = "/TYPE=PLMN";
	public static final String MMS_ADDRESS_TYPE_MAIL = "";
	public static final String MMS_ADDRESS_TYPE_IPV4 = "/TYPE=IPV4";
	public static final String MMS_ADDRESS_TYPE_IPV6 = "/TYPE=IPV6";
	
	
	/*=========================================================================
	 * CONTENT TYPE CONSTANTS
	 *=========================================================================*/	
	public static final String CTYPE_UNKNOWN = "*/*";
	public static final String CTYPE_TEXT =       "text/*";
	public static final String CTYPE_TEXT_PLAIN = "text/plain";
	public static final String CTYPE_TEXT_HTML =  "text/html";
	public static final String CTYPE_TEXT_WML =  "text/wnd.vap.wml";
	public static final String CTYPE_IMAGE      = "image/*";
	public static final String CTYPE_IMAGE_JPEG = "image/jpeg";
	public static final String CTYPE_IMAGE_GIF  = "image/gif";
	public static final String CTYPE_IMAGE_TIFF = "image/tiff";
	public static final String CTYPE_IMAGE_PNG  = "image/png";
	public static final String CTYPE_IMAGE_VND_WAP_WBMP = "image/vnd.wap.wbmp";
	public static final String CTYPE_MULTIPART         = "multipart/*";
	public static final String CTYPE_MULTIPART_MIXED   = "multipart/mixed";
	public static final String CTYPE_APPLICATION_MULTIPART_MIXED = "application/vnd.wap.multipart.mixed";
	public static final String CTYPE_APPLICATION_MULTIPART_RELATED = "application/vnd.wap.multipart.related";
	
	
	/*=========================================================================
	 * CLASS VARIABLES
	 *=========================================================================*/
	private String mmsMessageType;
	private String mmsTransactionId;
	private String mmsVersion;
	private Date mmsDate;
	private String mmsFrom;
	private ArrayList<String> mmsTo;
	private ArrayList<String> mmsCC;
	private ArrayList<String> mmsBCC;
	private String mmsSubject;
	private String mmsClass;
	
	private Date mmsExpiryTime;
	private Boolean mmsExpiryTimeAbsolute;
	
	private Date mmsDeliveryTime;
	private Boolean mmsDeliveryTimeAbsolute;
	
	private String mmsPriority;
	private String mmsSenderVisibility;
	private Boolean mmsDeliveryReport;
	private Boolean mmsReadReply;
	private String mmsContentType;
	private String mmsResponseStatus;
	private String mmsResponseText;
	private String mmsMessageID;

	private ArrayList<MmsPart> mmsParts;
	
	/*=========================================================================
	 * CONSTRUCTORS
	 *=========================================================================*/
	/**
	 * Creates an MmsMessage object.
	 */
	public MmsMessage(){
		this.mmsTo = new ArrayList<String>(); 
		this.mmsCC = new ArrayList<String>(); 
		this.mmsBCC = new ArrayList<String>();
		this.mmsParts = new ArrayList<MmsPart>();
	}
	
	/*=========================================================================
	 * METHODS
	 *=========================================================================*/
	
	/**
	 * Sets the MMS message type.<br>
	 * 
	 * Supported types:
	 * <ol>
	 * 	<li>{@link MmsMessage#MMS_MESSAGE_TYPE_SEND_REQUEST}</li>
	 * 	<li>{@link MmsMessage#MMS_MESSAGE_TYPE_SEND_CONF}</li>
	 * </ol>
	 * @param type type of the message
	 * @throws MmsMessageException message type not supported
	 */
	public void setMessageType(String type) throws MmsMessageException{
		if (type.equals(MMS_MESSAGE_TYPE_SEND_REQUEST))
			this.mmsMessageType = type;			
		else if (type.equals(MMS_MESSAGE_TYPE_SEND_CONF))
			this.mmsMessageType = type;
		else
			throw new MmsMessageException("Message type \"" + type + "\" not supported.");
	}
	
	/**
	 * Checks if the message type is specified.
	 * @return true if message type is specified, false otherwise
	 */
	public boolean isMessageTypeSet(){
		return mmsMessageType != null;
	}

	/**
	 * Sets the transaction id.
	 * @param id transaction id
	 */
	public void setTransactionID(String id){
		this.mmsTransactionId = id;
	}
	
	/**
	 * Checks if the transaction identifier is set
	 * @return true if set
	 */
	public boolean isTransactionIDSet(){
		return mmsTransactionId != null;
	}
	
	/**
	 * Sets the version of the MMS message.<br>
	 * The only supported value is {@link MmsMessage#MMS_VERSION_1}.
	 * @param version version of the MMS message
	 * @throws MmsMessageException version not supported
	 */
	public void setVersion(String version) throws MmsMessageException{
		if (version.equals(MMS_VERSION_1))
			this.mmsVersion = version;
		else
			throw new MmsMessageException("Message version \"" + version + "\" not supported.");
	}

	/**
	 * Checks if the version is present.
	 * @return true if present, false otherwise
	 */
	public boolean isVersionSet(){
		return mmsVersion != null;
	}
	
	/**
	 * Sets the message date.
	 * @param d date
	 */
	public void setMessageDate(Date d){
		this.mmsDate = d;
	}
		
	/**
	 * Sets the message date.
	 * @param d date
	 */
	public void setMessageDate(long d){
		this.mmsDate = new Date(d);
	}

	/**
	 * Checks if the message date is present.
	 * @return true if present, false otherwise
	 */
	public boolean isMessageDateSet(){
		return mmsDate != null;
	}
	
	/**
	 * Set the message sender.<br>
	 * 
	 * Supported values for <i>addressType</i>
	 * <ol>
	 * 	<li>{@link #MMS_ADDRESS_TYPE_IPV4}</li>
	 * 	<li>{@link #MMS_ADDRESS_TYPE_IPV6}</li>
	 * 	<li>{@link #MMS_ADDRESS_TYPE_MAIL}</li>
	 * 	<li>{@link #MMS_ADDRESS_TYPE_MOBILE_NUMBER}</li>
	 * </ol>
	 * @param sender sender address
	 * @param addressType address type
	 * @throws MmsMessageException address type not supported
	 */
	public void setMessageSender(String sender, String addressType) throws MmsMessageException{
		if (!addressType.equals(MMS_ADDRESS_TYPE_IPV4) &&
			!addressType.equals(MMS_ADDRESS_TYPE_IPV6) &&
			!addressType.equals(MMS_ADDRESS_TYPE_MAIL) &&
			!addressType.equals(MMS_ADDRESS_TYPE_MOBILE_NUMBER)
		) throw new MmsMessageException("Address type \"" + addressType + "\" not supported.");
		this.mmsFrom = sender + addressType;
	}

	/**
	 * Checks if the message sender is specified.
	 * @return true if present
	 */
	public boolean isMessageSenderSet(){
		return mmsFrom != null;
	}
	
	/**
	 * Adds a message receiver.
	 * 
	 * @param receiver receiver address
	 * @param addressType address type
	 * @throws MmsMessageException address type not supported
	 * @see #setMessageSender(String, String)
	 */
	public void addMessageReceiver(String receiver, String addressType) throws MmsMessageException{
		if (!addressType.equals(MMS_ADDRESS_TYPE_IPV4) &&
				!addressType.equals(MMS_ADDRESS_TYPE_IPV6) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MAIL) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MOBILE_NUMBER)
			) throw new MmsMessageException("Address type \"" + addressType + "\" not supported.");
		this.mmsTo.add(receiver + addressType);
	}
	
	/**
	 * Checks if there is at least one receiver.
	 * @return true if a receiver is present
	 */
	public boolean isMessageReceiverSet(){
		return mmsTo.size() > 0;
	}	
	
	/**
	 * Returns the receivers for this MMS message.
	 * @return receivers list
	 */
	public List<String> getMessageReceivers(){
		return mmsTo;
	}
	
	/**
	 * Adds a message CC.
	 * @param receiver CC address
	 * @param addressType address type
	 * @throws MmsMessageException address type not supported
	 * @see #setMessageSender(String, String)
	 */
	public void addMessageCC(String receiver, String addressType) throws MmsMessageException{
		if (!addressType.equals(MMS_ADDRESS_TYPE_IPV4) &&
				!addressType.equals(MMS_ADDRESS_TYPE_IPV6) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MAIL) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MOBILE_NUMBER)
			) throw new MmsMessageException("Address type \"" + addressType + "\" not supported.");
		this.mmsCC.add(receiver + addressType);
	}
	
	/**
	 * Checks if there is at least one message CC.
	 * @return true if one or more CC are present
	 */
	public boolean isMessageCCSet(){
		return mmsCC.size() > 0;
	}
	
	/**
	 * Return the CCs list.
	 * @return CC list
	 */
	public List<String> getMessageCC(){
		return mmsCC;
	}
	
	/**
	 * Adds a message BCC
	 * @param receiver BCC address
	 * @param addressType address type
	 * @throws MmsMessageException address type not supported
	 * @see #setMessageSender(String, String)
	 */
	public void addMessaceBCC(String receiver, String addressType) throws MmsMessageException{
		if (!addressType.equals(MMS_ADDRESS_TYPE_IPV4) &&
				!addressType.equals(MMS_ADDRESS_TYPE_IPV6) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MAIL) &&
				!addressType.equals(MMS_ADDRESS_TYPE_MOBILE_NUMBER)
			) throw new MmsMessageException("Address type \"" + addressType + "\" not supported.");		
		this.mmsBCC.add(receiver + addressType);
	}
	
	/**
	 * Checks if at least one BCC is present.
	 * @return true if present
	 */
	public boolean isMessageBCCSet(){
		return mmsBCC.size() > 0;
	}
	
	/**
	 * Return the BCCs list
	 * @return BCC list
	 */
	public List<String> getMessageBCC(){
		return mmsBCC;
	}	
	
	/**
	 * Sets the message subject
	 * @param subject
	 */
	public void setMessageSubject(String subject){
		this.mmsSubject = subject;
	}
	
	/**
	 * Checks if the subject is present.
	 * @return true if present
	 */
	public boolean isMessageSubjectSet(){
		return mmsSubject != null;
	}
	
	/**
	 * Sets the message class.<br>
	 * 
	 * Supported class:
	 * <ol>
	 * 	<li>{@link #MMS_CLASS_AUTO}</li>
	 * 	<li>{@link #MMS_CLASS_PERSONAL}</li>
	 * 	<li>{@link #MMS_CLASS_INFORMATIONAL}</li>
	 * 	<li>{@link #MMS_CLASS_ADVERTISEMENT}</li>
	 * </ol>	
	 * @param messageClass message class
	 * @throws MmsMessageException the message class is not supported
	 */
	public void setMessageClass(String messageClass) throws MmsMessageException{
		if (!messageClass.equals(MMS_CLASS_AUTO) &&
			!messageClass.equals(MMS_CLASS_PERSONAL) &&
			!messageClass.equals(MMS_CLASS_INFORMATIONAL) &&
			!messageClass.equals(MMS_CLASS_ADVERTISEMENT)
		) throw new MmsMessageException("Message class \"" + messageClass + "\" not supported");
		this.mmsClass = messageClass;
	}
	
	/**
	 * Checks if the message class is present.
	 * @return true if present
	 */
	public boolean isMessageClassSet(){
		return mmsClass != null;
	}
	
	/**
	 * Sets the message expiry time.<br>
	 * 
	 * If the <i>absolute</i> parameter is true, then <i>time</i> is considered the exact date
	 * of expire for the mms message. Otherwise <i>time</i> will be added to the message date
	 * to calculate the exact expire date.
	 * @param time Time value
	 * @param absolute Specifies if the time is relative or absolute
	 */
	public void setMessageExpiryTime(Date time, boolean absolute){
		this.mmsExpiryTimeAbsolute = absolute;
		this.mmsExpiryTime = time;
	}
	
	/**
	 * Checks if the expiry time is specified.
	 * @return true if present
	 */
	public boolean isMessageExpiryTimeSet(){
		return mmsExpiryTime != null;
	}
	
	/**
	 * Sets the message delivery time.
	 * 
	 * @param time time value
	 * @param absolute specifies if the time is relative or absolute
	 * @see #setMessageExpiryTime(Date, boolean)
	 */
	public void setMessageDeliveryTime(Date time, boolean absolute){
		this.mmsDeliveryTimeAbsolute = absolute;
		this.mmsDeliveryTime = time;
	}
	
	/**
	 * Checks if the delivery time is present.
	 * @return true if present
	 */
	public boolean isMessageDeliveryTimeSet(){
		return mmsDeliveryTime != null;
	}	
	
	/**
	 * Sets the message priority.<br>
	 * 
	 * Supported priorities:
	 * <ol>
	 * 	<li>{@link #MMS_PRIORITY_HIGH}</li>
	 * <li>{@link #MMS_PRIORITY_NORMAL}</li>
	 * <li>{@link #MMS_PRIORITY_LOW}</li>
	 * </ol>
	 * @param priority priority of the message
	 * @throws MmsMessageException priority not supported
	 */
	public void setMessagePriority(String priority) throws MmsMessageException{
		if (!priority.equals(MMS_PRIORITY_HIGH) &&
			!priority.equals(MMS_PRIORITY_NORMAL) &&
			!priority.equals(MMS_PRIORITY_LOW)
		) throw new MmsMessageException("Priority \"" + priority + "\" not supported");
		this.mmsPriority = priority;
	}
	
	/**
	 * Checks if the priority is specified.
	 * @return true if specified
	 */
	public boolean isMessagePrioritySet(){
		return mmsPriority != null;
	}	
	
	/**
	 * Sets the sender visibility.<br>
	 * 
	 * Supported visibilities:
	 * <ol>
	 * 	<li>{@link #MMS_SENDER_VISIBILITY_HIDE}</li>
	 * <li>{@link #MMS_SENDER_VISIBILITY_SHOW}</li>
	 * </ol>
	 * 
	 * @param visibility visibility value
	 * @throws MmsMessageException visibility not supported
	 */
	public void setSenderVisibility(String visibility) throws MmsMessageException{
		if (!visibility.equals(MMS_SENDER_VISIBILITY_SHOW) &&
			!visibility.equals(MMS_SENDER_VISIBILITY_HIDE)	
		) throw new MmsMessageException("Sender visibility \"" + visibility + "\" not supported");
		this.mmsSenderVisibility = visibility;
	}
	
	/**
	 * Checks if the visibility is present.
	 * @return true if present
	 */
	public boolean isSenderVisibilitySet(){
		return mmsSenderVisibility != null;
	}
	
	/**
	 * Enables/disables the message delivery report.
	 * @param enabled whether to enable the message delivery report
	 */
	public void setDeliveryReport(boolean enabled){
		this.mmsDeliveryReport = new Boolean(enabled);
	}
	
	/**
	 * Enables/disables the message read reply.
	 * @param enabled whether to enable the message read reply
	 */
	public void setReadReply(boolean enabled){
		this.mmsReadReply = new Boolean(enabled);
	}
	
	/**
	 * Set the response status.<br>
	 * 
	 * Supported responses:
	 * <ol>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE}</li>
	 * 	<li>{@link #MMS_RESPONSE_STATUS_OK}</li>	
	 * </ol>
	 * @param status response status
	 * @throws MmsMessageException status not supported
	 */
	public void setResponseStatus(String status) throws MmsMessageException{
		if (!status.equals(MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED) &&
			!status.equals(MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE) &&
			!status.equals(MMS_RESPONSE_STATUS_OK)
		)throw new MmsMessageException("Status \"" + status + "\" not supported");
		this.mmsResponseStatus = status;
	}
	
	/**
	 * Checks if the response status is present.
	 * @return true if present
	 */
	public boolean isResponseStatusSet(){
		return mmsSubject != null;
	}	
	
	/**
	 * Sets the response text.
	 * @param text response
	 */
	public void setResponseText(String text){
		this.mmsResponseText = text;
	}
	
	/**
	 * Checks if the response text is present.
	 * @return true if present
	 */
	public boolean isResonseTextSet(){
		return mmsResponseText != null;
	}

	public boolean isDeliveryReportSet(){
		return this.mmsDeliveryReport != null;
	}

	public boolean isReadReplySet(){
		return this.mmsReadReply != null;
	}
	

	/**
	 * Sets the message identifier.
	 * @param id message identifier
	 */
	public void setMessageID(String id){
		this.mmsMessageID = id;
	}
	
	/**
	 * Checks if the message identifier is present.
	 * @return true if present
	 */
	public boolean isMessageIDSet(){
		return mmsMessageID != null;
	}	
	
	/**
	 * Sets the content type for the message.<br>
	 * 
	 * Supported content types:
	 * <ol>
	 * 	<li>{@link #CTYPE_APPLICATION_MULTIPART_MIXED}</li>
	 * 	<li>{@link #CTYPE_APPLICATION_MULTIPART_RELATED}</li>
	 * 	<li>{@link #CTYPE_IMAGE}</li>
	 * 	<li>{@link #CTYPE_IMAGE_GIF}</li>
	 * 	<li>{@link #CTYPE_IMAGE_JPEG}</li>
	 * 	<li>{@link #CTYPE_IMAGE_PNG}</li>
	 *  <li>{@link #CTYPE_IMAGE_TIFF}</li>
	 *  <li>{@link #CTYPE_IMAGE_VND_WAP_WBMP}</li>
	 *  <li>{@link #CTYPE_MULTIPART}</li>
	 *  <li>{@link #CTYPE_MULTIPART_MIXED}</li>
	 *  <li>{@link #CTYPE_TEXT}</li>
	 *  <li>{@link #CTYPE_TEXT_HTML}</li>
	 *  <li>{@link #CTYPE_TEXT_PLAIN}</li>
	 *  <li>{@link #CTYPE_TEXT_WML}</li>
	 *  <li>{@link #CTYPE_UNKNOWN}</li>
	 * </ol>
	 * @param type content type
	 */
	public void setMessageContentType(String type){
		this.mmsContentType = type;
	}

	/**
	 * Chekcs if the message content type is present.
	 * @return true if present
	 */
	public boolean isMessageContentTypeSet(){
		return mmsContentType != null;
	}
	
	/**
	 * Add a part to the MMS message.<br>
	 * Checks if the content type of the message is compatible with the content type of the part.
	 * 
	 * @param part part to add to the message
	 * @throws MmsMessageException the part is not compatible with the message
	 */
	public void addPart(MmsPart part) throws MmsMessageException{
		if (mmsContentType.equals(CTYPE_MULTIPART) ||
				mmsContentType.equals(CTYPE_MULTIPART_MIXED) ||
				mmsContentType.equals(CTYPE_APPLICATION_MULTIPART_MIXED) ||
				//Multipart related not yet supported
				//mmsContentType.equals(CTYPE_APPLICATION_MULTIPART_RELATED) ||
				mmsContentType.equals(CTYPE_UNKNOWN))
			mmsParts.add(part);
		else if (mmsParts.size() == 0) mmsParts.add(part);
		else throw new MmsMessageException("Only one part admitted in a non Multipart message");
	}
	
	/**
	 * Return the number of parts in the message.
	 * @return parts number
	 */
	public int getPartsNumber(){
		return mmsParts.size();
	}
	
	/**
	 * Returns the mms parts in the message.
	 * @return mms parts
	 */
	public List<MmsPart> getParts(){
		return mmsParts;
	}
	
	/**
	 * Returns the ith part or null if <i>i</i> is greater then parts number.
	 * @param i part number desidered
	 * @return ith part number
	 */
	public MmsPart getPart(int i){
		try{
			return mmsParts.get(i);
		}catch (IndexOutOfBoundsException e){return null;}
	}
	
	/**
	 * Returns the message type.
	 * @return message type
	 */
	public String getMessageType(){
		return this.mmsMessageType;
	}
	
	/**
	 * Returns the transaction id.
	 * @return transaction id
	 */
	public String getTransactionID(){
		return this.mmsTransactionId;
	}
	
	/**
	 * Return the version of the MMS Protocol.
	 * @return version of MMS
	 */
	public String getVersion(){
		return this.mmsVersion;
	}
	
	/**
	 * Returns the date of the message.
	 * @return message's date
	 */
	public Date getMessageDate(){
		return this.mmsDate;
	}
	
	/**
	 * Return the message sender.
	 * @return sender
	 */
	public String getMessageSender(){
		return this.mmsFrom;
	}

	/**
	 * Return the message subject.
	 * @return message subject
	 */
	public String getMessageSubject(){
		return this.mmsSubject;
	}
	
	/**
	 * Returns the message class.
	 * @return message class
	 */
	public String getMessageClass(){
		return this.mmsClass;
	}
	
	/**
	 * Returns the message expiry time.
	 * @return expiry time
	 */
	public Date getMessageExpiryTime(){
		return this.mmsExpiryTime;
	}
	
	/**
	 * Checks if the expiry time is absolute.
	 * @return true if absolute, false otherwise
	 */
	public boolean isMessageExpiryTimeAbsolute(){
		if (mmsExpiryTimeAbsolute == null) throw new IllegalStateException("Expiry Time not specified");
		return this.mmsExpiryTimeAbsolute.booleanValue();
	}
	
	/**
	 * Returns the delivery time of the message.
	 * @return delivery time
	 */
	public Date getMessageDeliveryTime(){
		return this.mmsDeliveryTime;
	}
	
	/**
	 * Checks if the delivery time is absolute.
	 * @return true if absolute, false otherwise
	 */
	public boolean isMessageDeliveryTimeAbsolute(){
		if (mmsDeliveryTimeAbsolute == null) throw new IllegalStateException("Delivery Time not specified");
		return this.mmsDeliveryTimeAbsolute.booleanValue();
	}
	
	/**
	 * Returns the message priority.
	 * @return message priority
	 */
	public String getMessagePriority(){
		return this.mmsPriority;
	}
	
	/**
	 * Returns the sender visibility.
	 * @return sender visibility
	 */
	public String getSenderVisibility(){
		return this.mmsSenderVisibility;
	}
	
	/**
	 * Checks whether the delivery report is enabled
	 * @return true if enabled
	 */
	public boolean isDeliveryReportEnabled(){
		return this.mmsDeliveryReport;
	}
	
	/**
	 * Checks whether the read reply is enabled
	 * @return true if enabled
	 */
	public boolean isReadReplyEnabled(){
		return this.mmsReadReply;
	}
	
	/**
	 * Returns the response status.
	 * @return response status
	 */
	public String getResponseStatus(){
		return this.mmsResponseStatus;
	}
	
	/**
	 * Returns the response text
	 * @return response text
	 */
	public String getResponseText(){
		return this.mmsResponseText;
	}
	
	/**
	 * Returns the message identifier
	 * @return message identifier
	 */
	public String getMessageID(){
		return this.mmsMessageID;
	}
	
	/**
	 * Returns the message content type.
	 * @return message content type
	 */
	public String getMessageContentType(){
		return this.mmsContentType;
	}	
	
	/**
	 * Produce a string representation of the message easily readable by a human.
	 * @return string representation of the message 
	 */
	public String toString(){
		String mmsMessage = "";
		
		if (!isMessageTypeSet()) return "MessageType not defined";
		if (!isTransactionIDSet()) return "transactionID not defined";
		if (!isVersionSet()) return "Version not defined";
		if (!isMessageContentTypeSet()) return "Content type not defined";		
		
		
		/* MessageType, TransactionId and Version MUST be the first header in this order*/
		mmsMessage += MMS_MESSAGE_TYPE + " " + getMessageType() + NL;
		mmsMessage += MMS_TRANSACTION_ID + " " + getTransactionID() + NL;
		mmsMessage += MMS_VERSION + " " + getVersion() + NL;
		
		if (isMessageIDSet()) mmsMessage += MMS_MESSAGE_ID + " " + getMessageID() + NL;		
		if (isMessageDateSet()) mmsMessage += MMS_DATE + " " + getMessageDate().getTime()/1000 + NL;
		if (isMessageSenderSet()) mmsMessage += MMS_FROM + " " + getMessageSender() + NL;
		if (isMessageReceiverSet()) mmsMessage += MMS_TO + " " + getMessageReceivers() + NL;
		if (isMessageCCSet()) mmsMessage += MMS_CC + " " + getMessageCC() + NL;
		if (isMessageBCCSet()) mmsMessage += MMS_BCC + " " + getMessageBCC() + NL;
		if (isMessageSubjectSet()) mmsMessage += MMS_SUBJECT + " " + getMessageSubject() + NL;
		
		if (isDeliveryReportEnabled())
			if (isDeliveryReportEnabled()) mmsMessage += MMS_DELIVERY_REPORT + " " + MMS_DELIVERY_REPORT_YES + NL;
			else mmsMessage += MMS_DELIVERY_REPORT + " " + MMS_DELIVERY_REPORT_NO + NL;
			
		if (isReadReplyEnabled())		
			if (isReadReplyEnabled()) mmsMessage += MMS_READ_REPLY + " " + MMS_READ_REPLY_YES + NL;
			else mmsMessage += MMS_READ_REPLY + " " + MMS_READ_REPLY_NO + NL;

		if (isSenderVisibilitySet())		
		mmsMessage += MMS_SENDER_VISIBILITY + " " + getSenderVisibility() + NL;
		
		if (isMessageClassSet())		
		mmsMessage += MMS_CLASS + " " + getMessageClass() + NL;
		
		if (isMessageExpiryTimeSet())		
			if (isMessageExpiryTimeAbsolute()) 
				mmsMessage += MMS_EXPIRY + " absolute " + getMessageExpiryTime().getTime()/1000 + NL;
			else mmsMessage += MMS_EXPIRY + " relative " + getMessageExpiryTime().getTime()/1000 + NL;
		
		if (isMessageDeliveryTimeSet())
		if (isMessageDeliveryTimeAbsolute())
			mmsMessage += MMS_DELIVERY_TIME + " absolute " + getMessageDeliveryTime().getTime()/1000 + NL;
		else mmsMessage += MMS_DELIVERY_TIME + " relative " + getMessageDeliveryTime().getTime()/1000 + NL;
		
		if (isMessagePrioritySet()) mmsMessage += MMS_PRIORITY + " " + getMessagePriority() + NL;
		
		/* ContentType MUST be the last header followed by the body */
		if (isMessageContentTypeSet()) mmsMessage += MMS_CONTENT_TYPE + " " + getMessageContentType() + NL;		
		
		Iterator<MmsPart> i = mmsParts.iterator();
		MmsPart part;
		byte[]  partContent;
		while(i.hasNext()){
			part = i.next();
			mmsMessage += "Part " + part.getPartId() + NL;
			mmsMessage += "ContentType " + part.getPartContentType() + NL;
			partContent = part.getPartContent();
			mmsMessage += "ContentLength " + partContent.length + NL;
			for (int j=0; j<partContent.length; j++) mmsMessage += partContent[j];
			mmsMessage += NL;
		}
		
		return mmsMessage;
	}
}
