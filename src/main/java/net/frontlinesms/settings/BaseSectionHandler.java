package net.frontlinesms.settings;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.SettingsChangedEventNotification;

public class BaseSectionHandler {
	protected EventBus eventBus;
	protected UiGeneratorController uiController;
	protected Object panel;

	public BaseSectionHandler (UiGeneratorController uiController) {
		this.uiController = uiController;
		if (this.uiController instanceof UiGeneratorController) {
			this.eventBus = ((UiGeneratorController) uiController).getFrontlineController().getEventBus();
		}
	}
	
	public void settingChanged() {
		if (this.eventBus != null) {
			this.eventBus.notifyObservers(new SettingsChangedEventNotification());
		}
	}

	protected Object find (String component) {
		return this.uiController.find(this.panel, component);
	}
}
