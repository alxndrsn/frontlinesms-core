/**
 * 
 */
package net.frontlinesms.plugins;

/**
 * Basic interface that all FrontlineSMS plugins must implement.
 * Implementers must also have an empty no-arg constructor.  This may be called at any time, and
 * should not initialise variables. 
 * @author Alex
 */
public interface PluginSettingsController {
	public String getTitle();
	
	public Object getRootSettingsNode();
	
	public Object getPanelForSection(String section);
}
