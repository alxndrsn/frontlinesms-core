package net.frontlinesms.messaging.mms.events;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.mms.MmsMessage;

public class MmsReceivedNotification implements FrontlineEventNotification {
	private MmsMessage mmsMessage;
	
	public MmsReceivedNotification (MmsMessage mmsMessage) {
		this.mmsMessage = mmsMessage;
	}

	public MmsMessage getMessage() {
		return mmsMessage;
	}

	public void setFrontlineMultimediaMessage(MmsMessage mmsMessage) {
		this.mmsMessage = mmsMessage;
	}
}
