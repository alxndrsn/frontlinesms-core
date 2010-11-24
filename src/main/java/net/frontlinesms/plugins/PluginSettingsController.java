/**
 * 
 */
package net.frontlinesms.plugins;

import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

/**
 * Basic interface that all FrontlineSMS plugins having settings must implement.
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public interface PluginSettingsController {
	
	/**
	 * Lets the plugin add subnodes to their main settings node.
	 * <p>If there are no subsettings, this method should do nothing.
	 * @param rootSettingsNode The root node for this plugin in the tree.
	 */
	public void addSubSettingsNodes(Object rootSettingsNode);
	
	/**
	 * @param section
	 * @return The {@link UiSettingsSectionHandler} for the section given in parameter, or <code>null</code> if there is no handler.
	 */
	public UiSettingsSectionHandler getHandlerForSection(String section);
	
	/**
	 * @return The {@link UiSettingsSectionHandler} for the root node in the plugins tree.
	 */
	public UiSettingsSectionHandler getRootPanelHandler();
	
	/**
	 * @return The text to be displayed for the root node in the plugins tree.
	 */
	public String getTitle();

	public Object getRootNode();
}
