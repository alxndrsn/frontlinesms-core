package net.frontlinesms.ui.handler.help;

import java.net.URI;
import java.net.URISyntaxException;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class ContributeDialog implements ThinletUiEventHandler {
	private static final String UI_FILE_CONTRIBUTE_DIALOG = "/ui/core/dgContribute.xml";
	
	private static final String I18N_CONTRIBUTE_EXPLANATION = "contribute.explanation";
	private static final String I18N_CONTRIBUTE_EMAIL_US = "contribute.click.to.email.us";
	
	private final UiGeneratorController ui;
	private Object dialog;

	public ContributeDialog(UiGeneratorController uiGeneratorController) {
		ui = uiGeneratorController;
	}
	
	public void show() {
		init();
		ui.add(dialog);
	}

//> UI EVENT METHODS
	public void emailMyExperience() {
		StringBuilder body = new StringBuilder();
		body.append("Name of organisation: \n\n");
		body.append("Area of work: \n\n");
		body.append("Country/region of work: \n\n");
		body.append("Sector (e.g. health, human rights etc.): \n\n");
		body.append("Short description of your use of SMS (e.g. keeping in touch with staff in the field, monitoring well maintenance, providing information to remote farmers): \n\n");
		mailTo("you2us@frontlinesms.com", "Contribute to FrontlineSMS", body.toString());
	}

	/** Opens a mailto window */
	public void mailTo(String emailAddress) {
		mailTo(emailAddress, "", "");
	}
	
	public void emailForGuestPost() {
		mailTo("you2us@frontlinesms.com", "Writing a guest blog post for FrontlineSMS.com", "");
	}
	
	public void removeDialog() {
		ui.remove(this.dialog);
	}
	public void showHelpPage(String page) {
		ui.showHelpPage(page);
	}
	public void showStatsDialog() {
		ui.showStatsDialog();
	}
	
//> PRIVATE METHODS
	private void init() {
		this.dialog = ui.loadComponentFromFile(UI_FILE_CONTRIBUTE_DIALOG, this);
		
		Object pnExplanation = find("pnExplanation");
		
		for (String label : InternationalisationUtils.getI18nStrings(I18N_CONTRIBUTE_EXPLANATION)) {
			ui.add(pnExplanation, ui.createLabel(label));
		}
		
		Object linkWorking = find("linkWorking");
		Object linkGuestPost = find("linkGuestPost");
		Object linkNotWorking = find("linkNotWorking");
		
		ui.setText(linkWorking, InternationalisationUtils.getI18nString(I18N_CONTRIBUTE_EMAIL_US, "you2us@frontlinesms.com"));
		ui.setText(linkGuestPost, InternationalisationUtils.getI18nString(I18N_CONTRIBUTE_EMAIL_US, "you2us@frontlinesms.com"));
		ui.setText(linkNotWorking, InternationalisationUtils.getI18nString(I18N_CONTRIBUTE_EMAIL_US, "frontlinesupport@kiwanja.net"));
	}
	
	/**
	 * Opens a mailto window
	 * @param emailAddress
	 */
	private void mailTo(String emailAddress, String subject, String body) {
		if (subject.length() > 0) {
			subject = "?subject=" + subject;
		}
		if (body.length() > 0) {
			body = (subject.length() > 0 ? "&" : "?") + "body=" + body;
		}
		this.ui.alert(subject + body);
		try {
			FrontlineUtils.openDefaultMailClient(new URI("mailto", emailAddress, subject + body));
		} catch (URISyntaxException e1) {}
	}
	
	private Object find(String name) {
		return ui.find(this.dialog, name);
	}
}
