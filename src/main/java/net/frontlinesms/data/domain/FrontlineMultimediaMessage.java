/**
 * 
 */
package net.frontlinesms.data.domain;

import java.util.List;

import javax.persistence.Entity;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@Entity
public class FrontlineMultimediaMessage extends FrontlineMessage {
	private List<FrontlineMultimediaMessagePart> multimediaParts;
}
