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
package net.frontlinesms.messaging.mms;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.listener.SmsListener;
import net.frontlinesms.messaging.FrontlineMessagingService;

public interface MmsService extends FrontlineMessagingService {
	/** Set this device to be used for receiving messages. */
	public void setUseForReceiving(boolean use);

	/** Adds the supplied message to the outbox. */
	public void sendMMS(FrontlineMessage outgoingMessage);
	
	/** Sets the {@link SmsListener} attached to this {@link MmsService}. */
	public void setMmsListener(SmsListener smsListener);
}
