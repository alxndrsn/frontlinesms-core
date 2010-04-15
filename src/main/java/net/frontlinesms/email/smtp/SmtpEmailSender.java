/**
 * 
 */
package net.frontlinesms.email.smtp;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import net.frontlinesms.Utils;
import net.frontlinesms.email.EmailException;

/**
 * Class for handling direct submission of emails to SMTP servers.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class SmtpEmailSender {
	/** Logging object */
	private final Logger log = Utils.getLogger(this.getClass());
	/** SMTP email session */
	private final Session session;
	
	public SmtpEmailSender(String smtpServer) {
		Properties props = new Properties();
	    props.put("mail.smtp.host", smtpServer);
	    this.session = Session.getInstance(props, null);		
	}
	
	/**
	 * Create and send an email, using the "from" address derived in {@link #getLocalFromAddress()}.
	 * @param recipients
	 * @param subject
	 * @param textContent
	 * @throws MessagingException
	 * @see {@link #sendEmail(String, Address, String, String)}
	 */
	public void sendEmail(String recipients, String subject, String textContent) throws EmailException {
		sendEmail(recipients, getLocalFromAddress(), subject, textContent);
	}
	
	/**
	 * Create and send an email.
	 * @param smtpServer The SMTP server to deliver the email to
	 * @param recipients The recipient email address(es) to use in the {@link RecipientType#TO} field.  These should be specified as per {@link InternetAddress#parse(String)}.
	 * @param fromAddress The address the email should show in the "from" field
	 * @param subject The subject of the email
	 * @param textContent The text content of the email
	 * @throws MessagingException if there was a problem sending the email
	 */
	public void sendEmail(String recipients, Address fromAddress, String subject, String textContent) throws EmailException {
	    MimeMessage msg = new MimeMessage(session);

	    try {
	    	msg.setFrom(fromAddress);
		    msg.setRecipients(Message.RecipientType.TO, recipients);
		    msg.setSubject(subject);
		    msg.setSentDate(new Date());
		    msg.setText(textContent); 
		    
		    Transport.send(msg);
	    } catch(MessagingException ex) {
	    	log.info("Exception thrown while sending email to " + recipients, ex);
	    	throw new EmailException(ex);
	    }
	}
	
//> INSTANCE HELPER METHODS
	private Address getLocalFromAddress() {
		InternetAddress emailAddress = InternetAddress.getLocalAddress(session);
	    if (emailAddress == null) emailAddress = new InternetAddress();
	    return emailAddress;
	}
	
//> STATIC HELPER METHODS
}
