package net.frontlinesms.ui.events;

import net.frontlinesms.events.FrontlineEventNotification;
/**
 * A superclass for notifications involving a tab change in the UI.
 * 
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */

public class TabChangedNotification implements FrontlineEventNotification {
	private String newTabName;
	
	public TabChangedNotification (String newTabName) {
		this.newTabName = newTabName;
	}

	public String getNewTabName() {
		return newTabName;
	}
}
