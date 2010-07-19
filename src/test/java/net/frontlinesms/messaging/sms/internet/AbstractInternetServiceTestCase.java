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
package net.frontlinesms.messaging.sms.internet;

import net.frontlinesms.AbstractTestCase;
import net.frontlinesms.data.*;
import net.frontlinesms.data.domain.*;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.messaging.sms.internet.AbstractSmsInternetService;

/**
 * Tests send/receiving for internet services
 * 
 * @author Carlos Eduardo Endler Genz
 * @date 19/02/2009
 */
public abstract class AbstractInternetServiceTestCase extends AbstractTestCase {
	private AbstractSmsInternetService internetService;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		internetService = init();
	}
	
	protected abstract AbstractSmsInternetService init() throws DuplicateKeyException;
	protected abstract void executeReceiving(AbstractSmsInternetService internetService);
	protected abstract FrontlineMessage getMessageDetails();
	
	/**
	 * Test method for {@link net.frontlinesms.messaging.sms.internet.AbstractSmsInternetService#sendSmsDirect(net.frontlinesms.data.FrontlineMessage)}.
	 */
	public void testSendSmsDirect() {
		FrontlineMessage message = getMessageDetails();
		assertNotNull(message);
		internetService.sendSmsDirect(message);
		assertEquals(message.getStatus(), Status.SENT);
	}

	/**
	 * Test method for {@link net.frontlinesms.messaging.sms.internet.AbstractSmsInternetService#receiveSms()}.
	 */
	public void testReceiveSms() {
		executeReceiving(internetService);
	}
}
