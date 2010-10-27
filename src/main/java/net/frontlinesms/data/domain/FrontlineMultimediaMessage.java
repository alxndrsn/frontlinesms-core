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
		super(type, textContent);

		if (subject == null) subject = "";
		this.subject = subject;
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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FrontlineMultimediaMessage)) {
			return false; 
		}
		
		FrontlineMultimediaMessage multimediaMessage = (FrontlineMultimediaMessage) obj;
		if ((this.subject != null && multimediaMessage.getSubject() == null) 
				|| (this.subject == null && multimediaMessage.getSubject() != null)
				|| (this.subject != null && multimediaMessage.getSubject() != null 
						&& !this.subject.equals(multimediaMessage.getSubject()))) {
			return false;
		} else if (!sameMultimediaParts(multimediaMessage)) {
			return false;
		}
		
		// Finally, let the super comparator check if the basic fields
		// (sender, recipient, ...) are equals.
		return super.equals(obj);
	}

	private boolean sameMultimediaParts(FrontlineMultimediaMessage multimediaMessage) {
		if (this.multimediaParts.size() != multimediaMessage.getMultimediaParts().size()) {
			return false;
		}
		
		// Clone the local multimedia parts to prevent loss
		List<FrontlineMultimediaMessagePart> theseMultimediaParts = new ArrayList<FrontlineMultimediaMessagePart>(this.multimediaParts);
		for (FrontlineMultimediaMessagePart part : multimediaMessage.getMultimediaParts()) {
			if (!theseMultimediaParts.remove(part)) {
				return false;
			}
		}
		
		return true;
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
