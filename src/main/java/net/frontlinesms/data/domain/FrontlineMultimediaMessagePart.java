package net.frontlinesms.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@Entity
public class FrontlineMultimediaMessagePart {
	/** Unique id for this entity.  This is for hibernate usage. */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false) @SuppressWarnings("unused")
	private long id;
	private String content;
	/** <code>true</code> if {@link #content} links to the binary file name; <code>false</code> if {@link #content} contains the text content of this part */
	private boolean binary;
	
	FrontlineMultimediaMessagePart() {}
	
	private FrontlineMultimediaMessagePart(boolean binary, String content) {
		this.binary = binary;
		this.content = content;
	}
	
	public String getFilename() {
		if(!isBinary()) throw new IllegalStateException("Should not be calling this method on a text part.");
		return this.content;
	}
	
	public String getTextContent() {
		if(isBinary()) throw new IllegalStateException("Should not be calling this method on a binary part.");
		return this.content;
	}
	
	public boolean isBinary() {
		return this.binary;
	}

//> FACTORY METHODS
	public static FrontlineMultimediaMessagePart createTextPart(String textContent) {
		return new FrontlineMultimediaMessagePart(false, textContent);
	}
	public static FrontlineMultimediaMessagePart createBinaryPart(String filename) {
		return new FrontlineMultimediaMessagePart(true, filename);
	}
}
