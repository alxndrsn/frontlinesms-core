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
package net.frontlinesms.email;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import net.frontlinesms.EmailSender;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.email.receive.EmailReceiveProtocol;
import net.frontlinesms.email.receive.EmailReceiveUtils;

import org.apache.log4j.Logger;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public class EmailUtils {
	private static Logger LOG = FrontlineUtils.getLogger(EmailUtils.class);
	
	public static final String IMAP = "IMAP";
	public static final String POP3 = "POP3";
	public static final String SMTP = "SMTP";
	public static final String SMTPS = "smtps";
	private static final String TIMEOUT = "5000";
	
	private static final boolean STARTTLS = true;
	private static final boolean AUTH = true;
	private static final boolean DEBUG_SESSION = false;
	
	private static final Object SMTP_TRANSPORT_CLASS = "com.sun.mail.smtp.SMTPTransport";
	private static final Object SMTPS_TRANSPORT_CLASS = "com.sun.mail.smtp.SMTPSSLTransport";
	
	/**
	 * Connects to the supplied server using the supplied information.
	 * 
	 * @param smtpHost
	 * @param from
	 * @param pass
	 * @param useSSL
	 * @param session
	 * @return
	 * @throws MessagingException
	 */
	public static Transport connect(String smtpHost, String from, int serverPort, String pass, boolean useSSL, Session session) throws MessagingException {
		Transport transport = session.getTransport();
        transport.connect(smtpHost, serverPort, from, pass);
        return transport;
	}
	
	
	/**
	 * Returns a new session for this server, using SMTP, SMTPS or POP according to supplied
	 * information.
	 * 
	 * @param host
	 * @param from
	 * @param pass
	 * @param useSSL
	 * @return
	 */
	private static Session getSession(String host, String from, int serverPort, String pass, boolean useSSL) {
		Properties props = getPropertiesForHost(host, serverPort, useSSL);
		Session session;
		if (useSSL) {
			session = Session.getInstance(props);
		} else {
			session = Session.getInstance(props, new EmailSender.FrontlineEmailAuthenticator(from, pass));
		}
		session.setDebug(DEBUG_SESSION);
		
		return session;
	}
	
	/**
	 * Returns the properties for this server, using SMTP, SMTPS OR POP according to
	 * supplied information.
	 * 
	 * @param host
	 * @param useSSL
	 * @return
	 */
	public static Properties getPropertiesForHost(String host, int serverPort, boolean useSSL) {
		Properties props = new Properties();
		String protocol = (useSSL ? SMTPS : SMTP);
		
		props.put("mail.transport.protocol", protocol);
		props.put("mail." + protocol + ".host", host);
		props.put("mail." + protocol + ".port", String.valueOf(serverPort));
		props.put("mail." + protocol + ".host", host);
		props.put("mail." + protocol + ".auth", String.valueOf(AUTH));
		props.put("mail." + protocol + ".timeout", TIMEOUT);
		props.put("mail." + protocol + ".connectiontimeout", TIMEOUT);

		if (!useSSL) {
			props.put("mail.smtp.starttls.enable", String.valueOf(STARTTLS));
			props.put("mail." + protocol + ".class", SMTP_TRANSPORT_CLASS);
		} else {
			props.put("mail." + protocol + ".class", SMTPS_TRANSPORT_CLASS);
		}
		
		return props;
	}

	/**
	 * Try to connect to the supplied server.
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param useSSL
	 * @return TRUE if successful, FALSE otherwise.
	 * @throws MessagingException 
	 * @throws NoSuchProviderException 
	 */
	public static boolean testConnection(boolean isForReceiving, String host, String username, int hostPort, String password, boolean useSSL, String protocol) throws MessagingException {
		LOG.trace("ENTER");
		boolean connectionOk = false;
		
		if (isForReceiving) {
			Store store = EmailReceiveUtils.getStore(host, username, hostPort, password, useSSL, EmailReceiveProtocol.valueOf(protocol));
			
			try {
				LOG.trace("Connecting to email store: " + host + ":" + hostPort);
				store.connect(host, hostPort, username, password);
				connectionOk = true;
			} finally {
				// Attempt to close the message store
				try { store.close(); } catch(MessagingException ex) { LOG.warn("Error closing POP store.", ex); }
			}	
		} else {
			Session session = getSession(host, username, hostPort, password, useSSL);

			connect(host, username, hostPort, password, useSSL, session);
			connectionOk = true;
		}

		LOG.debug("Returning [" + connectionOk + "]");
		LOG.trace("EXIT");
		
		return connectionOk;
	}
}
