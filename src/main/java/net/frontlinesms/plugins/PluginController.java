/**
 * 
 */
package net.frontlinesms.plugins;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.LanguageBundle;

/**
 * Basic interface that all FrontlineSMS plugins must implement.
 * @author Alex
 */
public interface PluginController {
	/**
	 * Gets the name for this plugin.  This should be internationalised if that is suitable.
	 * @return The name of this plugin.
	 */
	public String getName();
	
	/**
	 * Initialise the plugin from the {@link FrontlineSMS} controller instance. 
	 * @param frontlineController {@link FrontlineSMS} instance that this plugin is "plugged-in" to.
	 * @param applicationContext {@link ApplicationContext} for FrontlineSMS config
	 * @throws PluginInitialisationException if there was an identified problem initialising the plugin
	 */
	public void init(FrontlineSMS frontlineController, ApplicationContext applicationContext) throws PluginInitialisationException;
	
	/**
	 * Gets the tab for this plugin 
	 * @param uiController {@link UiGeneratorController} instance that will be the parent of this tab.
	 * @return the tab to display for this plugin
	 */
	public Object getTab(UiGeneratorController uiController);

	/**
	 * <p>Gets the location of the Spring config for this plugin.</p>
	 * <p>If the config is on the classpath, this should be detailed like:
	 * <code>classpath:package1/package2/pluginname-spring-hibernate.xml</code></p>
	 * @return the location of the Spring config for this plugin, or <code>null</code> if none is required.
	 */
	public String getSpringConfigPath();
	
	/**
	 * <p>Gets the location of the hibernate config for this plugin.</p>
	 * <p>If the config is on the classpath, this should be detailed like:
	 * <code>classpath:package1/package2/pluginname.hibernate.cfg.xml</code></p>
	 * @return the location of the hibernate config for this plugin, or <code>null</code> if none is required.
	 */
	public String getHibernateConfigPath();

	/**
	 * Gets the default language bundle for text strings used in the UI of this plugin.
	 * @return map of text keys to English translations of strings used in the UI of this plugin, or <code>null</code> if no text keys are required for this plugin
	 */
	public Map<String, String> getDefaultTextResource();
	
	/**
	 * Get the language bundle for text string to be used for the UI of this plugin in a particular language.
	 * @param locale the {@link Locale} of the translation to use
	 * @return map of text keys to translations of strings used in the UI of this plugin, or null if there is no translation available for this plugin.
	 */
	public Map<String, String> getTextResource(Locale locale);
}
