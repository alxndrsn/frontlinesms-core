package net.frontlinesms.settings;

import java.util.HashMap;
import java.util.Map;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;

public class BaseSectionHandler {
	protected EventBus eventBus;
	protected UiGeneratorController uiController;
	protected Object panel;
	protected Map<String, Object> originalValues;

	protected BaseSectionHandler (UiGeneratorController uiController) {
		this.uiController = uiController;
		if (this.uiController instanceof UiGeneratorController) {
			this.eventBus = ((UiGeneratorController) uiController).getFrontlineController().getEventBus();
		}
		
		this.originalValues = new HashMap<String, Object>();
	}
	
	protected void settingChanged(String key, Object newValue) {
		Object oldValue = this.originalValues.get(key);
		if (this.eventBus != null) {
			SettingsChangedEventNotification notification;
			if (newValue == null && oldValue == null || newValue.equals(oldValue)) {
				notification = new SettingsChangedEventNotification(key, true);
			} else {
				notification = new SettingsChangedEventNotification(key, false);
			}
			
			this.eventBus.notifyObservers(notification);
		}
	}
	
	public Object getPanel() {
		return this.panel;
	}
	
//	protected void settingChanged(String key, Object newValue) {
//		Object oldValue = this.originalValues.get(key);
//		
//		if (newValue == null && oldValue == null || newValue.equals(oldValue)) {
//			this.notifyChange(key, true);
//		} else {
//			this.notifyChange(key, false);
//		}
//	}
//	
//	protected void notifyChange(String sectionItem, boolean isUnchange) {
//		if (this.eventBus != null) {
//			this.eventBus.notifyObservers(new SettingsChangedEventNotification(sectionItem, isUnchange));
//		}
//	}
	
	protected Object find (String component) {
		return this.uiController.find(this.panel, component);
	}
}
