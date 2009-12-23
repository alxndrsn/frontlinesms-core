/**
 * 
 */
package net.frontlinesms.plugins.translation;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.UiGeneratorController;

/**
 * @author alexanderson
 *
 */
public class TranslationPluginController extends BasePluginController {
//> STATIC CONSTANTS
	/** Filename and path of the XML for the HTTP Trigger tab. */
	private static final String UI_FILE_TAB = "/ui/plugins/translation/translationTab.xml";
	
//> INSTANCE METHODS
	/** @see net.frontlinesms.plugins.PluginController#getTab(net.frontlinesms.ui.UiGeneratorController) */
	public Object initThinletTab(UiGeneratorController uiController) {
		TranslationThinletTabController tabController = new TranslationThinletTabController(this, uiController);

		Object translationTab = uiController.loadComponentFromFile(UI_FILE_TAB, tabController);
		tabController.setTabComponent(translationTab);
		
		tabController.init();
		
		return translationTab;
	}

	/** @see net.frontlinesms.plugins.PluginController#deinit() */
	public void deinit() {
		// Nothing to do here yet.  May want to warn if the tab is disabled without saving current modifications though.
	}

	/** @see net.frontlinesms.plugins.PluginController#init(net.frontlinesms.FrontlineSMS, org.springframework.context.ApplicationContext) */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		// TODO Auto-generated method stub
	}

}
