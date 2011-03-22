/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
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
package net.frontlinesms.messaging.sms;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.messaging.FrontlineMessagingService;

public interface SmsService extends FrontlineMessagingService {
	/** Set this device to be used for sending SMS messages. */
	public void setUseForSending(boolean use);
	
	/** Check whether this device actually supports SMS receipt. */
	public boolean supportsReceive();

	/** Set this device to be used for receiving messages. */
	public void setUseForReceiving(boolean use);

	/** Adds the supplied message to the outbox. */
	public void sendSMS(FrontlineMessage outgoingMessage);
	
	/** Check whether this device actually supports sending binary sms. */
	public boolean isBinarySendingSupported();

	/** Gets the MSISDN to be displayed for this device. */
	public String getMsisdn();
	
	/**
	 * Checks whether this device supports sending sms messages in the UCS2 characterset.
	 * FIXME this method is unnecessary as all handsets support UCS-2 so far!
	 */
	public boolean isUcs2SendingSupported();
}
