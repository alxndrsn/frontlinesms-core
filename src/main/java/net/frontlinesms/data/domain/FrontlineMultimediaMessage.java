/**
 * 
 */
package net.frontlinesms.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@Entity
public class FrontlineMultimediaMessage extends FrontlineMessage {
	private String subject;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<FrontlineMultimediaMessagePart> multimediaParts;
	
	/** Empty constructor for Hibernate */
	FrontlineMultimediaMessage() {}

	/**
	 * @param type
	 * @param subject this must not be <code>null</code>
	 * @param textContent
	 * @param multimediaParts
	 */
	public FrontlineMultimediaMessage(Type type, String subject, String textContent, List<FrontlineMultimediaMessagePart> multimediaParts) {
		super(type, textContent);

		if (subject == null) subject = "";
		
		this.subject = subject;
		this.setMultimediaParts(multimediaParts);
	}
	
	public FrontlineMultimediaMessage(Type type, String subject, String textContent) {
		new FrontlineMultimediaMessage(type, subject, textContent, null);
	}

	public List<FrontlineMultimediaMessagePart> getMultimediaParts() {
		return Collections.unmodifiableList(this.multimediaParts);
	}
	public boolean hasBinaryPart() {
		for (FrontlineMultimediaMessagePart part : this.getMultimediaParts()) {
			if (part.isBinary()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Creates an incoming multimedia message in the internal data structure.
	 * @param dateReceived The date this message was received.
	 * @param senderMsisdn The MSISDN (phone number) of the sender of this message.
	 * @param recipientMsisdn The MSISDN (phone number) of the recipient of this message.
	 * @param messageContent The text content of this message.
	 * @returna Message object representing the sent message.
	 */
	public static FrontlineMultimediaMessage createIncomingMultimediaMessage(long dateReceived, String senderMsisdn, String recipientMsisdn, String messageContent) {
		FrontlineMultimediaMessage m = createMessageFromContentString(messageContent);
		m.setStatus(Status.RECEIVED);
		m.setDate(dateReceived);
		m.setSenderMsisdn(senderMsisdn);
		m.setRecipientMsisdn(recipientMsisdn);

		return m;
	}
	
	public static FrontlineMultimediaMessage createMessageFromContentString(String messageContent) {
		FrontlineMultimediaMessage multimediaMessage = new FrontlineMultimediaMessage(Type.RECEIVED, "", "");
		
		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
		
		Pattern textPattern = Pattern.compile("\"(.*)\"");
		Pattern binaryFilePattern = Pattern.compile("File: (.*)");
		Pattern subjectPattern = Pattern.compile("Subject: (.*)");
		Matcher matcher;
		
		for (String part : messageContent.split(";")) {
			if ((matcher = subjectPattern.matcher(part.trim())).find()) {
				multimediaMessage.setSubject(matcher.group(1));
			} else if ((matcher = binaryFilePattern.matcher(part.trim())).find()) {
				multimediaParts.add(FrontlineMultimediaMessagePart.createBinaryPart(matcher.group(1)));
			} else if ((matcher = textPattern.matcher(part.trim())).find()) {
				multimediaParts.add(FrontlineMultimediaMessagePart.createTextPart(matcher.group(1)));
			}
		}
		
		multimediaMessage.setMultimediaParts(multimediaParts);
		
		return multimediaMessage;
	}
	
	public String getFullContent() {
		StringBuilder textContent = new StringBuilder();
		
		if (this.subject != null && !this.subject.trim().isEmpty()) {
			textContent.append("Subject: " + this.subject);
		}
		
		for(FrontlineMultimediaMessagePart part : this.multimediaParts) {
			if(textContent.length() > 0) textContent.append("; ");
			
			String text;
			if (part.isBinary()) {
				text = "File: " + part.getFilename();
			} else if (!(text = part.getTextContent().trim()).isEmpty()) {
				text = "\"" + text + "\"";
			}
		
			textContent.append(text);
		}

		return textContent.toString();
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}

	public void setMultimediaParts(List<FrontlineMultimediaMessagePart> multimediaParts) {
		this.multimediaParts = multimediaParts;
	}
}
