package net.frontlinesms.data.domain;

import javax.persistence.Entity;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@Entity
public class FrontlineMultimediaMessagePart {
	private String mimeType;
	private String fileName;
}
