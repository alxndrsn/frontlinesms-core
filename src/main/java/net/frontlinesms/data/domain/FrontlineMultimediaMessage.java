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
	
//> CONSTRUCTORS
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
		super(type, textContent);

		if (subject == null) subject = "";
		this.subject = subject;
	}

//> ACCESSORS
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<FrontlineMultimediaMessagePart> getMultimediaParts() {
		return Collections.unmodifiableList(this.multimediaParts);
	}
	public void setMultimediaParts(List<FrontlineMultimediaMessagePart> multimediaParts) {
		this.multimediaParts = multimediaParts;
	}
	public boolean hasBinaryPart() {
		for (FrontlineMultimediaMessagePart part : this.getMultimediaParts()) {
			if (part.isBinary()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean truncate) {
		StringBuilder textContent = new StringBuilder();
		
		if (this.subject != null && !this.subject.trim().isEmpty()) {
			textContent.append("Subject: " + this.subject);
		}
		
		if (this.multimediaParts != null) {
			for(FrontlineMultimediaMessagePart part : this.multimediaParts) {
				if(textContent.length() > 0) textContent.append("; ");
				textContent.append(part.toString(truncate));
			}
		}

		return textContent.toString();
	}

//> STATIC METHODS
	public static boolean appearsToBeToString(String toString) {
		return toString.contains("File:");
	}
	
	public static FrontlineMultimediaMessage createMessageFromContentString(String messageContent, boolean truncate) {
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
		multimediaMessage.setTextMessageContent(multimediaMessage.toString(truncate));
		
		return multimediaMessage;
	}
}
