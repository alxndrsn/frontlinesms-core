package net.frontlinesms.ui.handler.help;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class AboutDialog implements ThinletUiEventHandler {
	private static final String UI_FILE_ABOUT_PANEL = "/ui/core/dgAbout.xml";

	private final UiGeneratorController ui;
	private Object dialog;

	public AboutDialog(UiGeneratorController uiGeneratorController) {
		this.ui = uiGeneratorController;
	}
	
	public void show() {
		this.dialog = ui.loadComponentFromFile(UI_FILE_ABOUT_PANEL, this);
		String version = InternationalisationUtils.getI18nString(FrontlineSMSConstants.I18N_APP_VERSION, BuildProperties.getInstance().getVersion());
		ui.setText(ui.find(dialog, "version"), version);
		ui.add(dialog);
	}

	public void removeDialog() {
		ui.remove(this.dialog);
	}
	
	public void openBrowser(String url) {
		ui.openBrowser(url);
	}
}
