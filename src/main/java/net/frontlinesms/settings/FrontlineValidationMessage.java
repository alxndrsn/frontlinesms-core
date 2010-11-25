package net.frontlinesms.settings;

import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class FrontlineValidationMessage {
	String i18nKey;
	String[] details;
	
	public FrontlineValidationMessage (String i18nKey, String[] details) {
		this.i18nKey = i18nKey;
		this.details = details;
	}
	
	public String getLocalisedMessage() {
	    return InternationalisationUtils.getI18nString(i18nKey, details);
	}
}
