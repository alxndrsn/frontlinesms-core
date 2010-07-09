package net.frontlinesms.messaging.mms.events;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.mms.MmsService;
import net.frontlinesms.messaging.mms.MmsServiceStatus;
/**
 * A class for notifications involving MMS.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class MmsServiceStatusNotification implements FrontlineEventNotification {
	private MmsService mmsService;
	private MmsServiceStatus status;
	
	public MmsServiceStatusNotification (MmsService mmsService, MmsServiceStatus status) {
		this.mmsService = mmsService;
		this.status = status;
	}

	public MmsServiceStatus getStatus() {
		return status;
	}

	public MmsService getMmsService() {
		return mmsService;
	}
}
