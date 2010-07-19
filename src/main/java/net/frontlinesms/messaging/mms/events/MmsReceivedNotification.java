package net.frontlinesms.messaging.mms.events;

import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.events.FrontlineEventNotification;

public class MmsReceivedNotification implements FrontlineEventNotification {
	private FrontlineMultimediaMessage frontlineMultimediaMessage;
	
	public MmsReceivedNotification (FrontlineMultimediaMessage frontlineMultimediaMessage) {
		this.frontlineMultimediaMessage = frontlineMultimediaMessage;
	}

	public FrontlineMultimediaMessage getFrontlineMultimediaMessage() {
		return frontlineMultimediaMessage;
	}

	public void setFrontlineMultimediaMessage(
			FrontlineMultimediaMessage frontlineMultimediaMessage) {
		this.frontlineMultimediaMessage = frontlineMultimediaMessage;
	}
}
