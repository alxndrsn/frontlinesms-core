package net.frontlinesms.settings;

import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class FrontlineValidationMessage {
	String i18nKey;
	String[] details;
	String icon;
	
	public FrontlineValidationMessage (String i18nKey, String[] details, String icon) {
		this.i18nKey = i18nKey;
		this.details = details;
		this.icon = icon;
	}
	
	public String getLocalisedMessage() {
	    return InternationalisationUtils.getI18nString(i18nKey, details);
	}

	public String getIcon() {
		return icon;
	}
}
