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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

/**
 * Encodes an MmsMessage class in a binary MMS object ready to be sent to an MMSC.
 * <p>The encoding follows the specification provided by 
 * <a href="http://www.openmobilealliance.org/tech/affiliates/wap/wapindex.html">Open Mobile Alliance</a>. Relevant documents are
 * <i>WAP-209-MMSEncapsulation-20020105-a</i> and <i>WAP-230-WSP-20010705-a</i>.</p>
 * <p>
 * 	A simple usage scenario of MmsEncoder is as follow:
 * 	<pre>
 * 	  MmsMessage mms = new MmsMessage();
 * 	  
 * 	  //fill out mms fields...
 * 	  MmsEncoder mmsEncoder = new MmsEncoder(mms);
 * 	  byte[] encodedMMS = mmmsEncoder.encodeMessage();
 * 	</pre
 * </p>
 * <p>
 * 	To see what are the supported fields and values of an mms refer to the documentation of MmsMessage
 * </p>
 * 
 * @author Andrea Zito
 * @see MmsMessage
 * @version 0.8
 *
 */
public class MmsEncoder {
	/*=========================================================================
	 * Header Name Constants
	 *=========================================================================*/
	protected static final byte H_BASE = (byte)0x80;
	
	protected static final byte H_MMS_MESSAGE_TYPE = (byte)0x0C;
	protected static final byte H_MMS_TRANSACTION_ID = (byte)0x18;
	protected static final byte H_MMS_VERSION = (byte)0x0D;
	protected static final byte H_MMS_DATE = (byte)0x05;	
	protected static final byte H_MMS_FROM = (byte)0x09;	
	protected static final byte H_MMS_TO = (byte)0x17;	
	protected static final byte H_MMS_CC = (byte)0x02;	
	protected static final byte H_MMS_BCC = (byte)0x01;		
	protected static final byte H_MMS_SUBJECT = (byte)0x16;		
	protected static final byte H_MMS_CLASS = (byte)0x0A;			
	protected static final byte H_MMS_EXPIRY = (byte)0x08;		
	protected static final byte H_MMS_DELIVERY_TIME = (byte)0x07;			
	protected static final byte H_MMS_PRIORITY = (byte)0x0F;		
	protected static final byte H_MMS_SENDER_VISIBILITY = (byte)0x14;			
	protected static final byte H_MMS_DELIVERY_REPORT = (byte)0x06;			
	protected static final byte H_MMS_READ_REPLY = (byte)0x10;			
	protected static final byte H_MMS_CONTENT_TYPE = (byte)0x04;			
	protected static final byte H_MMS_RESPONSE_STATUS = (byte)0x12;			
	protected static final byte H_MMS_RESPONSE_TEXT = (byte)0x13;		
	protected static final byte H_MMS_MESSAGE_ID = (byte)0x0B;
	
	protected static final byte P_CHARSET = (byte)0x81;
	
	/*=========================================================================
	 * Header Value Constants
	 *=========================================================================*/
	public static final byte NULL_CHAR = (byte)0x00;
	
	public static final byte CHARSET_US_ASCII = 0x03;
	public static final byte CHARSET_UTF8 = 0x6A;
	
	public static final byte MMS_MESSAGE_TYPE_SEND_REQUEST = (byte)0x80;
	public static final byte MMS_MESSAGE_TYPE_SEND_CONF = (byte)0x81;

	public static final byte MMS_VERSION_1 = (byte)0x90;

	private static final byte MMS_SENDER_PRESENT = (byte)0x80;
	private static final byte MMS_SENDER_INSERT = (byte)0x81;
	
	public static final byte MMS_DELIVERY_REPORT_YES = (byte)0x80;
	public static final byte MMS_DELIVERY_REPORT_NO = (byte)0x81;
	
	public static final byte MMS_CLASS_PERSONAL = (byte)0x80;
	public static final byte MMS_CLASS_ADVERISEMENT = (byte)0x81;
	public static final byte MMS_CLASS_INFORMATIONAL = (byte)0x82;
	public static final byte MMS_CLASS_AUTO = (byte)0x83;
	
	private static final byte MMS_EXPIRY_TIME_ABSOLUTE = (byte)0x80;
	private static final byte MMS_EXPIRY_TIME_RELATIVE = (byte)0x81;
	
	private static final byte MMS_DELIVERY_TIME_ABSOLUTE = (byte)0x80;
	private static final byte MMS_DELIVERY_TIME_RELATIVE = (byte)0x81;	
	
	public static final byte MMS_PRIORITY_LOW = (byte)128;
	public static final byte MMS_PRIORITY_NORMAL = (byte)129;
	public static final byte MMS_PRIORITY_HIGH = (byte)130;
	
	public static final byte MMS_READ_REPLY_YES = (byte)128;
	public static final byte MMS_READ_REPLY_NO = (byte)129;
	
	public static final byte MMS_RESPONSE_STATUS_OK = (byte)0x80; 
	public static final byte MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED = (byte)0x81;
	public static final byte MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED = (byte)0x82;
	public static final byte MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT = (byte)0x83;
	public static final byte MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED = (byte)0x84;
	public static final byte MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND = (byte)0x85;
	public static final byte MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM = (byte)0x86;
	public static final byte MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED = (byte)0x87;
	public static final byte MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE = (byte)0x88; 
	
	public static final byte MMS_SENDER_VISIBILITY_HIDE = (byte)128;
	public static final byte MMS_SENDER_VISIBILITY_SHOW = (byte)129;
	
	public static final String MMS_ADDRESS_TYPE_MOBILE_NUMBER = "/TYPE=PLMN";
	public static final String MMS_ADDRESS_TYPE_MAIL = "";
	public static final String MMS_ADDRESS_TYPE_IPV4 = "/TYPE=IPV4";
	public static final String MMS_ADDRESS_TYPE_IPV6 = "/TYPE=IPV6";

	/*=========================================================================
	 * CONTENT TYPE CONSTANTS
	 *=========================================================================*/	
	public static final byte CTYPE_UNKNOWN = 0x00;
	public static final byte CTYPE_TEXT = 0x01;
	public static final byte CTYPE_TEXT_HTML =  0x02;
	public static final byte CTYPE_TEXT_PLAIN = 0x03;
	public static final byte CTYPE_TEXT_WML =  0x08;
	public static final byte CTYPE_IMAGE = 0x1C;
	public static final byte CTYPE_IMAGE_JPEG = 0x1E;
	public static final byte CTYPE_IMAGE_GIF  = 0x1D;
	public static final byte CTYPE_IMAGE_TIFF = 0x1F;
	public static final byte CTYPE_IMAGE_PNG  = 0x20;
	public static final byte CTYPE_IMAGE_VND_WAP_WBMP = 0x21;
	public static final byte CTYPE_MULTIPART =  0x0B;
	public static final byte CTYPE_MULTIPART_MIXED = 0x0C;
	public static final byte CTYPE_APPLICATION_MULTIPART_MIXED = 0x23;
	public static final byte CTYPE_APPLICATION_MULTIPART_RELATED = 0x33;	
	
	
	/*=========================================================================
	 * CLASS VARIABLES
	 *=========================================================================*/
	private MmsMessage mmsMessage;
	
	/*=========================================================================
	 * CONSTRUCTORS
	 *=========================================================================*/
	/**
	 * Creates an MmsEncoder object for the specified MMS message
	 */
	public MmsEncoder(MmsMessage mms){
		this.mmsMessage = mms;
	}
	
	/*=========================================================================
	 * METHODS
	 *=========================================================================*/
	/**
	 * Encodes the message type.
	 * @return encoded byte
	 * @see MmsMessage#setMessageType(String)
	 */
	private byte encodeMessageType(String messageType) throws MmsEncodingException{
		if (messageType.equals(MmsMessage.MMS_MESSAGE_TYPE_SEND_REQUEST)) return MMS_MESSAGE_TYPE_SEND_REQUEST;
		else if (messageType.equals(MmsMessage.MMS_MESSAGE_TYPE_SEND_CONF)) return MMS_MESSAGE_TYPE_SEND_CONF;
		else throw new MmsEncodingException("Cannot encode message type: " + messageType);
	}

	/**
	 * Encodes a long value in the LongInteger format as
	 * specified by the WAP-203-WSP document.<br>
	 * 
	 * The fist field of the returned array contains the number of
	 * 8-bit portions that forms the value
	 * @param value long to be encoded
	 * @return encoded byte array
	 */
	private static byte[] encodeLongInteger(long value){
		byte[] buf = encodeMultiOctetInteger(value);
		byte[] result = new byte[buf.length+1];
		result[0] = (byte)buf.length;
		System.arraycopy(buf, 0, result, 1, buf.length);
		return result;
	}
	
	/**
	 * Encodes a long value in the MultiOctetInteger format as
	 * specified by the WAP-203-WSP document.<br>
	 * 
	 * The binary representation is splitted in 8-bit portions.
	 * @param value long integer to be encoded
	 * @return encoded byte array
	 */
	private static byte[] encodeMultiOctetInteger(long value){
		byte[] buf = new byte[8]; 
		long temp=0;
		int count=0;
		long mask=0;
		
		if (value == 0) return new byte[]{0x0};
		
		for (int i=1; i<=8; i++){
			mask = ((0XFFFFFFFFFFFFFFFFL) >>> 64-(8*i));
			temp = (value & mask) >>> (8*(i-1));
			buf[i-1] = (byte)(temp);
			if (temp==0) count++;
			else count=0;
		}
		
		byte[] result = new byte[8-count];
		int d = 0;
		for (int i=7-count; i>=0; i--) result[d++] = buf[i];
		return result;		
	}
	
	/**
	 * Encodes an unsigned integer in the "Uintvar" format as
	 * specified by WAP-203-WDP document.<br>
	 * 
	 * The binary representation of the value is splitted in 
	 * 7-bit portions. The most significant bit is setted to 1
	 * in all the portions but the last.
	 * 
	 * Example: 0x87A5 (1000 0111 1010 0101)
	 * 
	 * <pre>
	 * +---+---------+  +---+---------+  +---+---------+   
	 * | 1 | 0000010 |  | 1 | 0001111 |  | 0 | 0100101 |
	 * +-------------+  +---+---------+  +---+---------+
	 * </pre>
	 * @param value integer to encode
	 * @return encoded byte array
	 */
	private static byte[] encodeUintvar(int value){
		byte[] buf = new byte[5];
		int temp=0;
		int count=0;
		int mask=0;
		if (value == 0) return new byte[]{0x0};
		
		for (int i=1; i<=5; i++){
			mask = ((0XFFFFFFFF) >>> 32-(7*i));
			temp = (value & mask) >>> (7*(i-1));
			buf[i-1] = (byte)(0x80 | temp);
			if (temp==0) count++;
			else count=0;
		}
		
		buf[0] &= 0x7F;
		byte[] result = new byte[5-count];
		int d = 0;
		for (int i=4-count; i>=0; i--) result[d++] = buf[i];
		return result;
	}

	/**
	 * Encode the MMS version.<br>
	 *
	 * Actually only version 1 is supported.
	 * @param version MMS version of the message
	 * @return encoded version byte
	 * @throws MmsEncodingException MMS version not supported
	 * @see MmsMessage#setVersion(String)
	 */
	private byte encodeVersion(String version) throws MmsEncodingException{
		if (version.equals(MmsMessage.MMS_VERSION_1)) return MMS_VERSION_1;
		else throw new MmsEncodingException("MMS version not supported: " + version);
	}
	
	/**
	 * Encodes a date.<br> 
	 * 
	 * Dates are expressed in seconds as long integer values starting from 1970/01/01 00:00:00 GMT. 
	 * 
	 * @param d date to encode
	 * @return encoded byte array
	 */
	private byte[] encodeDate(Date d){
		long seconds = d.getTime()/1000;
		byte[] encodedDate = encodeLongInteger(seconds);

		return encodedDate;
	}
	
	/**
	 * Encodes the sender visibility.
	 *
	 * @param visibility sender visibility
	 * @return encoded byte
	 * @throws MmsEncodingException Sender visibility not supported
	 * @see MmsMessage#setSenderVisibility(String)
	 */
	private byte encodeSenderVisibility(String visibility) throws MmsEncodingException{
		if (visibility.equals(MmsMessage.MMS_SENDER_VISIBILITY_SHOW))
			return MMS_SENDER_VISIBILITY_SHOW;
		else if (visibility.equals(MmsMessage.MMS_SENDER_VISIBILITY_HIDE))
			return MMS_SENDER_VISIBILITY_HIDE;
		else throw new MmsEncodingException("Sender visibility not supported: " + visibility);
			
	}
	
	/**
	 * Encodes the message class.
	 * 
	 * @param msgClass message class
	 * @return encoded byte
	 * @throws MmsEncodingException Message class not supported
	 * @see MmsMessage#setMessageClass(String)
	 */
	private byte encodeMessageClass(String msgClass) throws MmsEncodingException{
		if (msgClass.equals(MmsMessage.MMS_CLASS_PERSONAL)) return MMS_CLASS_PERSONAL;
		else if (msgClass.equals(MmsMessage.MMS_CLASS_AUTO)) return MMS_CLASS_AUTO;
		else if (msgClass.equals(MmsMessage.MMS_CLASS_ADVERTISEMENT)) return MMS_CLASS_ADVERISEMENT;
		else if (msgClass.equals(MmsMessage.MMS_CLASS_INFORMATIONAL)) return MMS_CLASS_INFORMATIONAL;
		else throw new MmsEncodingException("Message class not supported: " + msgClass);
	}
	
	/**
	 * Encodes the message priority 
	 * @param priority message priority
	 * @return encoded byte
	 * @throws MmsEncodingException priority not supported
	 * @see MmsMessage#setMessagePriority(String)
	 */
	private byte encodeMessagePriority(String priority) throws MmsEncodingException{
		if (priority.equals(MmsMessage.MMS_PRIORITY_HIGH)) return MMS_PRIORITY_HIGH;
		else if (priority.equals(MmsMessage.MMS_PRIORITY_NORMAL)) return MMS_PRIORITY_NORMAL;
		else if (priority.equals(MmsMessage.MMS_PRIORITY_LOW)) return MMS_PRIORITY_LOW;
		else throw new MmsEncodingException("Message priority not supported: " + priority);
	}

	/**
	 * Encodes the message content type.<br>
	 * 
	 * <p>Although MmsMessage.CTYPE_APPLICATION_MULTIPART_RELATED can be set as the content
	 * type of an MmsMessage object, support for encoding these type of messages is not yet 
	 * implemented.</p> 
	 * @param contentType content type
	 * @return encoded byte
	 * @throws MmsEncodingException content type not supported
	 * @see MmsMessage#setMessageContentType(String)
	 */
	private byte encodeContentType(String contentType)throws MmsEncodingException{
		if (contentType.equals(MmsMessage.CTYPE_TEXT)) return CTYPE_TEXT;
		else if (contentType.equals(MmsMessage.CTYPE_TEXT_PLAIN)) return CTYPE_TEXT_PLAIN;
		else if (contentType.equals(MmsMessage.CTYPE_TEXT_HTML)) return CTYPE_TEXT_HTML;
		else if (contentType.equals(MmsMessage.CTYPE_TEXT_WML)) return CTYPE_TEXT_WML;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE)) return CTYPE_IMAGE;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE_GIF)) return CTYPE_IMAGE_GIF;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE_JPEG)) return CTYPE_IMAGE_JPEG;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE_PNG)) return CTYPE_IMAGE_PNG;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE_TIFF)) return CTYPE_IMAGE_TIFF;
		else if (contentType.equals(MmsMessage.CTYPE_IMAGE_VND_WAP_WBMP)) return CTYPE_IMAGE_VND_WAP_WBMP;
		else if (contentType.equals(MmsMessage.CTYPE_MULTIPART)) return CTYPE_MULTIPART;
		else if (contentType.equals(MmsMessage.CTYPE_MULTIPART_MIXED)) return CTYPE_MULTIPART_MIXED;
		else if (contentType.equals(MmsMessage.CTYPE_APPLICATION_MULTIPART_MIXED)) return CTYPE_APPLICATION_MULTIPART_MIXED;
		else if (contentType.equals(MmsMessage.CTYPE_APPLICATION_MULTIPART_RELATED)) 
			throw new MmsEncodingException("Content Type multipart related not supported yet");
		else throw new MmsEncodingException("Content type not supported: " + contentType);
	}

	/**
	 * Encodes the charset.
	 * @param charset
	 * @return encoded byte
	 * @throws MmsEncodingException charset not supported
	 * @see MmsPart#setPartCharset(String)
	 */
	private byte encodeCharset(String charset) throws MmsEncodingException{
		if (charset.equals(MmsMessage.CHARSET_US_ASCII)) return CHARSET_US_ASCII;
		else if (charset.equals(MmsMessage.CHARSET_UTF8)) return CHARSET_UTF8;
		else throw new MmsEncodingException("Charset not supported: " + charset);
	}
	
	/**
	 * Encodes a message part.
	 * 
	 * @param part message part to encode
	 * @return encoeded byte array
	 * @throws MmsEncodingException error encoding part
	 */
	private byte[] encodePart(MmsPart part) throws MmsEncodingException{
		/*
		 * Part encoding:
		 *  - HeadersLength: Uintvar     - Length of the header portion of the part
		 *  - DataLength   : Uintvar     - Length of the data portion of the part
		 *  - ContentType  : MultiOctect - Encoded content type of the data
		 *  - Headers      : (HeadersLength-length(ContentType)) octets
		 *  - Data         : DataLength octets 
		 */		
		byte cType = encodeContentType(part.getPartContentType());
		byte[] cTypeHeader;
		byte headersLength;
		//If the content type of the part is text
		if (cType >= CTYPE_TEXT && cType <= CTYPE_TEXT_WML){
			//Build a MultiOctet for the ContentType
			cTypeHeader = new byte[4];
			cTypeHeader[0] = 3;
			cTypeHeader[1] = (byte)(cType + 0x80);
			cTypeHeader[2] = P_CHARSET;
			cTypeHeader[3] = (byte)(encodeCharset(part.getPartCharset()) + 0x80);
			
			headersLength = 4;
		}else{
			cTypeHeader = new byte[1];
			cTypeHeader[0] = (byte)(cType + 0x80);
			headersLength = 1;
		}
		
		byte[] data = part.getPartContent();
		int dataLength = data.length;
		byte[] dataLengthEncoded = encodeUintvar(dataLength);
		
		//Result array
		byte[] result = new byte[1 + dataLengthEncoded.length + headersLength + dataLength];

		result[0] = headersLength;
		int offset=1;			
		System.arraycopy(dataLengthEncoded, 0, result, offset, dataLengthEncoded.length);
		offset+=dataLengthEncoded.length;
		System.arraycopy(cTypeHeader, 0, result, offset, cTypeHeader.length);
		offset+=cTypeHeader.length;
		System.arraycopy(data, 0, result, offset, data.length);
		
		return result;		
	}
	
	/**
	 * Encodes the MmsMessage object associated with this instance.
	 * 
	 * @return encoded mms message
	 * @throws MmsEncodingException error encoding message
	 */
	public byte[] encodeMessage() throws MmsEncodingException{
		ByteArrayOutputStream mmsBuffer = new ByteArrayOutputStream();
		try{
			/*
			 * The firsts headers MUST be in order
			 * - MessageType
			 * - Transaction Id
			 * - Version
			 */
			if (mmsMessage.isMessageTypeSet()){
				mmsBuffer.write(H_MMS_MESSAGE_TYPE + H_BASE);
				mmsBuffer.write(encodeMessageType(mmsMessage.getMessageType()));
			}
			else throw new MmsEncodingException("Message type not specified");

			if (mmsMessage.isTransactionIDSet()){ 
				mmsBuffer.write(H_MMS_TRANSACTION_ID + H_BASE);
				mmsBuffer.write(mmsMessage.getTransactionID().getBytes());
				mmsBuffer.write(NULL_CHAR);
			}else throw new MmsEncodingException("Transaction id not specified");

			if (mmsMessage.isVersionSet()){
				mmsBuffer.write(H_MMS_VERSION + H_BASE);
				mmsBuffer.write(encodeVersion(mmsMessage.getVersion()));
			}
			else throw new MmsEncodingException("MMS version not specified");

			/* Now the order of the headers is not important */

			//Date header
			if (mmsMessage.isMessageDateSet()){
				mmsBuffer.write(H_MMS_DATE + H_BASE);
				mmsBuffer.write(encodeDate(mmsMessage.getMessageDate()));			
			}

			//From header
			mmsBuffer.write(H_MMS_FROM + H_BASE);
			if (mmsMessage.isMessageSenderSet()){
				String from = mmsMessage.getMessageSender();
				/*
				 * Write the length of the header value. Consider the 
				 * length of the address, the address present token and
				 * the null character
				 */
				mmsBuffer.write(encodeUintvar(from.length()+2));
				mmsBuffer.write(MMS_SENDER_PRESENT);
				mmsBuffer.write(from.getBytes());
				mmsBuffer.write(NULL_CHAR);
			}else{
				//If there isn't a sender, the length consider only the token
				mmsBuffer.write(1);
				mmsBuffer.write(MMS_SENDER_INSERT);
			}

			//To header
			if (mmsMessage.isMessageReceiverSet()){
				Iterator<String> i = mmsMessage.getMessageReceivers().iterator();
				String to;
				while(i.hasNext()){
					to = i.next();
					mmsBuffer.write(H_MMS_TO + H_BASE);
					mmsBuffer.write(to.getBytes());
					mmsBuffer.write(NULL_CHAR);
				}
			}

			//CC header
			if (mmsMessage.isMessageCCSet()){
				Iterator<String> i = mmsMessage.getMessageCC().iterator();
				String cc;
				while(i.hasNext()){
					cc = i.next();
					mmsBuffer.write(H_MMS_CC + H_BASE);
					mmsBuffer.write(cc.getBytes());
					mmsBuffer.write(NULL_CHAR);
				}
			}

			//To header
			if (mmsMessage.isMessageBCCSet()){
				Iterator<String> i = mmsMessage.getMessageBCC().iterator();
				String bcc;
				while(i.hasNext()){
					bcc = i.next();
					mmsBuffer.write(H_MMS_BCC + H_BASE);
					mmsBuffer.write(bcc.getBytes());
					mmsBuffer.write(NULL_CHAR);
				}
			}		

			//Subject
			if (mmsMessage.isMessageSubjectSet()){
				mmsBuffer.write(H_MMS_SUBJECT + H_BASE);
				mmsBuffer.write(mmsMessage.getMessageSubject().getBytes());
				mmsBuffer.write(NULL_CHAR);
			}		

			//Delivery Report
			if (mmsMessage.isDeliveryReportSet()){
				mmsBuffer.write(H_MMS_DELIVERY_REPORT + H_BASE);
				if (mmsMessage.isDeliveryReportEnabled()) mmsBuffer.write(MMS_DELIVERY_REPORT_YES);
				else mmsBuffer.write(MMS_DELIVERY_REPORT_NO);
			}		

			//Sender Visibility
			if (mmsMessage.isSenderVisibilitySet()){
				mmsBuffer.write(H_MMS_SENDER_VISIBILITY + H_BASE);
				mmsBuffer.write(encodeSenderVisibility(mmsMessage.getSenderVisibility()));
			}		
			
			//Read Reply
			if (mmsMessage.isReadReplySet()){
				mmsBuffer.write(H_MMS_READ_REPLY + H_BASE);
				if (mmsMessage.isReadReplyEnabled()) mmsBuffer.write(MMS_READ_REPLY_YES);
				else mmsBuffer.write(MMS_READ_REPLY_NO);
			}			

			//Message Class
			if (mmsMessage.isMessageClassSet()){
				mmsBuffer.write(H_MMS_CLASS + H_BASE);
				mmsBuffer.write(encodeMessageClass(mmsMessage.getMessageClass()));
			}		

			//Expiry Time
			if (mmsMessage.isMessageExpiryTimeSet()){
				mmsBuffer.write(H_MMS_EXPIRY + H_BASE);

				byte[] expiry = encodeDate(mmsMessage.getMessageExpiryTime());
				/*
				 * The length field considers the length of the data value,
				 * plus the absolute flag, plus the length of the uintval
				 */
				mmsBuffer.write(encodeUintvar(expiry[0] + 2));
				if (mmsMessage.isMessageExpiryTimeAbsolute())
					mmsBuffer.write(MMS_EXPIRY_TIME_ABSOLUTE);
				else
					mmsBuffer.write(MMS_EXPIRY_TIME_RELATIVE);

				mmsBuffer.write(expiry);
			}

			//Delivery Time
			if (mmsMessage.isMessageExpiryTimeSet()){
				mmsBuffer.write(H_MMS_DELIVERY_TIME + H_BASE);

				byte[] delivery = encodeDate(mmsMessage.getMessageDeliveryTime());
				/*
				 * The length field considers the length of the data value,
				 * plus the absolute flag, plus the length of the uintval
				 */
				mmsBuffer.write(encodeUintvar(delivery[0] + 2));
				if (mmsMessage.isMessageExpiryTimeAbsolute())
					mmsBuffer.write(MMS_DELIVERY_TIME_ABSOLUTE);
				else
					mmsBuffer.write(MMS_DELIVERY_TIME_RELATIVE);

				mmsBuffer.write(delivery);
			}		

			//Priority
			if (mmsMessage.isMessagePrioritySet()){
				mmsBuffer.write(H_MMS_PRIORITY + H_BASE);
				mmsBuffer.write(encodeMessagePriority(mmsMessage.getMessagePriority()));
			}

			byte mmsCType;	
			/*
			 * The last header MUST be ContentType followed by the body
			 */
			if (mmsMessage.isMessageContentTypeSet()){
				mmsBuffer.write(H_MMS_CONTENT_TYPE + H_BASE);
				mmsCType = encodeContentType(mmsMessage.getMessageContentType());
				mmsBuffer.write(mmsCType + 0x80);

			}else throw new MmsEncodingException("Message content type not specified");

			/*
			 * Here begin the encoding of the body of the MMS message.
			 * The header of a multipart message is composed by:
			 *  - Entries number - Uintvar
			 */
			if (mmsCType == CTYPE_MULTIPART || mmsCType == CTYPE_MULTIPART_MIXED ||
					mmsCType == CTYPE_APPLICATION_MULTIPART_MIXED){

				//Write the header of the multipart message
				mmsBuffer.write(encodeUintvar(mmsMessage.getPartsNumber()));
				Iterator<MmsPart> i = mmsMessage.getParts().iterator();

				//Write each part
				while (i.hasNext()){
					mmsBuffer.write(encodePart(i.next()));
				}
			}else throw new MmsEncodingException("MMS message content type must be multipart");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{ mmsBuffer.close();	}catch(IOException e){}
		}
		return mmsBuffer.toByteArray();
	}
}
