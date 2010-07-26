/**
 * 
 */
package net.frontlinesms.logging.logreader.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.BASE64DecoderStream;

import net.frontlinesms.email.receive.EmailReceiveException;
import net.frontlinesms.email.receive.EmailReceiveProcessor;
import net.frontlinesms.email.receive.EmailReceiver;
import net.frontlinesms.resources.ResourceUtils;

/**
 * Class for reading FrontlineSMS logs emailed to the support email address.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class RemoteLogFetcher implements EmailReceiveProcessor {	
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	/** The {@link PopMessageReceiver} used to connect to the email account. */
	private final EmailReceiver receiver;

//> CONSTRUCTORS
	/**
	 * Create a new {@link RemoteLogFetcher}.
	 * @param hostAddress 
	 * @param hostPort 
	 * @param hostUsername 
	 * @param hostPassword 
	 * @param useSsl 
	 */
	RemoteLogFetcher(String hostAddress, int hostPort, String hostUsername, String hostPassword, boolean useSsl) {
		this.receiver = new EmailReceiver(this);
		receiver.setHostAddress(hostAddress);
		receiver.setHostPort(hostPort);
		receiver.setHostUsername(hostUsername);
		receiver.setHostPassword(hostPassword);
		receiver.setUseSsl(useSsl);
	}
	
//> LOG PROCESSING METHODS
	/**
	 * Retrieve all logs from the inbox.
	 * @throws PopReceiveException if there was a problem receiving messages from the email account.
	 */
	private void processLogs() throws EmailReceiveException {
		this.receiver.receive("INBOX");
	}
	
//> EMAIL PROCESSING METHODS
	/**
	 * Process pop messages and extract log files that they contain. 
	 */
	public void processMessage(Message message, Date date) {
		System.out.println("RemoteLogReader.processPopMessage()");

		try {
			Object content = message.getContent();
			
			if(content instanceof MimeMultipart) {
				MimeMultipart mmContent = (MimeMultipart) content;
				System.out.println("Processing MimeMultipart content...");
				
				for (int i = 0; i < mmContent.getCount(); i++) {
					BodyPart part = mmContent.getBodyPart(i);

					if(part instanceof MimeBodyPart) {
						System.out.println("Processing MimeBodyPart...");
						MimeBodyPart mbp = (MimeBodyPart) part;
						
						System.out.println(mbp.getContent().getClass());
						if(mbp.getContent() instanceof String) {
							String s = (String) mbp.getContent();
							System.out.println("String Content: " + s);
						} else if(mbp.getContent() instanceof BASE64DecoderStream) {
							BASE64DecoderStream bs = (BASE64DecoderStream) mbp.getContent();
							System.out.println("Processing base64 decoder: " + bs);
							
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							int c;
							while((c = bs.read()) != -1) {
								out.write(c);
							}

							File outputDirectory = new File(new File("temp"), getDirectory(message));
							outputDirectory.mkdirs();
							ResourceUtils.unzip(new ByteArrayInputStream(out.toByteArray()),
									outputDirectory, true);
						} else throw new IllegalStateException("Unhandled content type: " + mbp.getContent().getClass());
					} else throw new IllegalStateException("Unhandled body part type: " + part.getClass());
				}
			} else throw new IllegalStateException("Unhandled content type: " + content.getClass());
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}

	/**
	 * Converts a {@link Message}'s FROM address into a suitable directory name.
	 * @param message
	 * @return a valid directory name which should be unique to a particular email address.
	 * @throws MessagingException
	 */
	private String getDirectory(Message message) throws MessagingException {
		String from = message.getFrom()[0].toString();
		from = from.replace('<', '-');
		from = from.replace('>', ' ');
		from = from.trim();
		return from + "/" + message.getSentDate().getTime();
	}

//> ACCESSORS

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
	
//> MAIN METHOD
	/**
	 * @param args 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String hostAddress = args[0];
		int hostPort = Integer.parseInt(args[1]);
		String hostUsername = args[2];
		String hostPassword = args[3];
		boolean useSsl = Boolean.parseBoolean(args[4]);
		RemoteLogFetcher reader = new RemoteLogFetcher(hostAddress, hostPort, hostUsername, hostPassword, useSsl);
		reader.processLogs();
		
		System.out.println("Completed.");
	}
}
