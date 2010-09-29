/**
 * 
 */
package net.frontlinesms.plugins;

/**
 * Basic interface that all FrontlineSMS plugins having settings must implement.
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public interface PluginSettingsController {
	public String getTitle();
	
	public void addSubSettingsNodes(Object rootSettingsNode);
	
	public Object getPanelForSection(String section);
	
	public Object getRootPanel();
}
