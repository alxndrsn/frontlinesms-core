/**
 * 
 */
package net.frontlinesms.email.smtp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
	 * Create and send an email, using the "from" address derived in {@link #getLocalEmailAddress()}.
	 * @param recipients
	 * @param subject
	 * @param textContent
	 * @throws MessagingException
	 * @see {@link #sendEmail(String, Address, String, String)}
	 */
	public void sendEmail(String recipients, String subject, String textContent) throws EmailException {
		sendEmail(recipients, getLocalEmailAddress(), subject, textContent);
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
	
	/**
	 * Create and send a multipart email with one text part and 1-N file attachements.
	 * @param smtpServer The SMTP server to deliver the email to
	 * @param recipients The recipient email address(es) to use in the {@link RecipientType#TO} field.  These should be specified as per {@link InternetAddress#parse(String)}.
	 * @param fromAddress The address the email should show in the "from" field
	 * @param subject The subject of the email
	 * @param textContent The text content of the email
	 * @param attachments Files to attach to this email
	 * @throws MessagingException if there was a problem sending the email
	 */
	public void sendEmail(String recipients, Address fromAddress, String subject, String textContent, File... attachments) throws EmailException {
	    MimeMessage msg = new MimeMessage(session);

	    try {
	    	msg.setFrom(fromAddress);
		    msg.setRecipients(Message.RecipientType.TO, recipients);
		    msg.setSubject(subject);
		    msg.setSentDate(new Date());
		    
		    // use a MimeMultipart as we need to handle the file attachments
		    Multipart multipart = new MimeMultipart();
		    
		    // Create a message part to represent the body text
		    BodyPart messageBodyPart = new MimeBodyPart();
		    messageBodyPart.setText(textContent);
		    // Add the message body to the mime message
		    multipart.addBodyPart(messageBodyPart);
		    
		    if (attachments != null) {
		    	for(File attachment : attachments) {
			    	MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			
			    	// Use a JAF FileDataSource as it does MIME type detection
			    	DataSource source = new FileDataSource(attachment);
			    	attachmentBodyPart.setDataHandler(new DataHandler(source));
			
			    	// Assume that the filename you want to send is the same as the
			    	// Actual file name - could alter this to remove the file path
			    	attachmentBodyPart.setFileName(attachment.getName());
			
			    	multipart.addBodyPart(attachmentBodyPart);
		    	}
		    }
		
		    // Put all message parts in the message
		    msg.setContent(multipart); 
		    Transport.send(msg);
	    } catch(MessagingException ex) {
	    	log.info("Exception thrown while sending email to " + recipients, ex);
	    	throw new EmailException(ex);
	    }
	}
	
//> INSTANCE HELPER METHODS
	public InternetAddress getLocalEmailAddress(String addressAsString, String personalName) {
		InternetAddress emailAddress = getLocalEmailAddress();
	    try {
	    	if(personalName != null && personalName.length()>0) {
	    		emailAddress.setPersonal(personalName);
	    	}
	    } catch(UnsupportedEncodingException ex) {
	    	log.warn("Unable to set email address personal name: '" + personalName + "'");
	    }
    	if(addressAsString != null && addressAsString.length()>0) {
    		emailAddress.setAddress(addressAsString);
    	}
    	return emailAddress;
	}
	
	public InternetAddress getLocalEmailAddress() {
		InternetAddress emailAddress = InternetAddress.getLocalAddress(session);
	    if (emailAddress == null) emailAddress = new InternetAddress();
	    return emailAddress;
	}
	
//> STATIC HELPER METHODS
}
