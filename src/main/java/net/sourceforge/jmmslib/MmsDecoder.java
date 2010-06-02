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

/**
 * Decodes a binary mms object in a MmsMessage class.
 * <p>Actually this class is very limited and it is only capable of decoding <i>send_conf</i>
 * mms messages.</p>
 * @author Andrea Zito
 *
 */
public class MmsDecoder {
	/*=========================================================================
	 * CLASS VARIABLES
	 *=========================================================================*/
	private byte[] binaryMms;
	
	/*=========================================================================
	 * CONSTRUCTORS
	 *=========================================================================*/
	/**
	 * Creates an MmsDecoder object for the specified binary mms object
	 */
	public MmsDecoder(byte[] binaryMms){
		this.binaryMms = binaryMms;
	}
	
	/*=========================================================================
	 * METHODS
	 *=========================================================================*/
	/**
	 * Docedes the message type.<br>
	 * Actually the only supported value is {@link MmsMessage#MMS_MESSAGE_TYPE_SEND_CONF}
	 */
	private String decodeMessageType(byte msgType) throws MmsDecoderException{
		if (msgType == MmsEncoder.MMS_MESSAGE_TYPE_SEND_CONF) return MmsMessage.MMS_MESSAGE_TYPE_SEND_CONF;
		else throw new MmsDecoderException("Response message type not supported.");
	}
	
	/**
	 * Decodes the response status.
	 * 
	 * @param status
	 * @return name of the specified status
	 * @throws MmsDecoderException status not supported
	 * @see MmsMessage#setResponseStatus(String)
	 */
	private String decodeResponseStatus(byte status) throws MmsDecoderException{
		if (status == MmsEncoder.MMS_RESPONSE_STATUS_OK) return MmsMessage.MMS_RESPONSE_STATUS_OK;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED;
		else if (status == MmsEncoder.MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE) return MmsMessage.MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE;
		else throw new MmsDecoderException("Response status not supported");
	}
	
	/**
	 * Decodes the message version.
	 * 
	 * @param version encoded version
	 * @return message version
	 * @throws MmsMessageException
	 */
	private String decodeMessageVersion(byte version) throws MmsMessageException{
		if (version == MmsEncoder.MMS_VERSION_1) return MmsMessage.MMS_VERSION_1;
		else throw new MmsMessageException("Response message version not supported");
	}

	/**
	 * Decodes a <i>send_conf</i> mms message.
	 * 
	 * @return decoded message
	 * @throws MmsDecoderException error decoding the message
	 * @throws MmsMessageException error filling up the value of the message 
	 */
	public MmsMessage decodeMessage() throws MmsDecoderException, MmsMessageException{
		MmsMessage mms = new MmsMessage();

		try{
			//Parse Message Type
			if (binaryMms[0] != (MmsEncoder.H_MMS_MESSAGE_TYPE + MmsEncoder.H_BASE))
				throw new MmsDecoderException("Malformed response message");

			mms.setMessageType(decodeMessageType(binaryMms[1]));

			//Parse Transaction ID		
			if (binaryMms[2] != MmsEncoder.H_MMS_TRANSACTION_ID + MmsEncoder.H_BASE)
				throw new MmsDecoderException("Malformed response message");

			int i=0;
			StringBuffer transId = new StringBuffer("");

			for (i=3; binaryMms[i]!=0; i++){
				transId.append((char)binaryMms[i]);
			}

			mms.setTransactionID(transId.toString());

			//Parse Message Version
			if (binaryMms[++i] != (MmsEncoder.H_MMS_VERSION + MmsEncoder.H_BASE))
				throw new MmsDecoderException("Malformed response message");

			mms.setVersion(decodeMessageVersion(binaryMms[++i]));

			//Parse Message Status
			if (binaryMms[++i] != (MmsEncoder.H_MMS_RESPONSE_STATUS + MmsEncoder.H_BASE))
				throw new MmsDecoderException("Malformed response message");

			mms.setResponseStatus(decodeResponseStatus(binaryMms[++i]));

			//Parse Message Status Text
			if (binaryMms.length >= ++i && binaryMms[i] == (MmsEncoder.H_MMS_RESPONSE_TEXT + MmsEncoder.H_BASE)){
				StringBuffer responseText= new StringBuffer("");

				for (i=i+1; binaryMms[i]!=0; i++){
					responseText.append((char)binaryMms[i]);
				}

				mms.setResponseText(responseText.toString());
			}
		}catch (ArrayIndexOutOfBoundsException e){
			throw new MmsDecoderException("Malformed response message");
		}
		
		return mms;
	}
}
