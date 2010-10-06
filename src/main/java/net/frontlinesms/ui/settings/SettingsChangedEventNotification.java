package net.frontlinesms.ui.settings;

import net.frontlinesms.events.FrontlineEventNotification;

public class SettingsChangedEventNotification implements FrontlineEventNotification {
	private String sectionItem;
	private boolean isUnchange;
	
	public SettingsChangedEventNotification (String sectionItem, boolean isUnchange) {
		this.sectionItem = sectionItem;
		this.isUnchange = isUnchange;
	}

	public String getSectionItem() {
		return sectionItem;
	}

	public boolean isUnchange() {
		return isUnchange;
	}
}
