/**
 * 
 */
package net.frontlinesms.plugins;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import thinlet.Thinlet;

/**
 * General properties for a {@link PluginController} class.
 * @author Alex
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginControllerProperties {
	/** A string marking an unset property. */
	public final String NO_VALUE = "_____*****-----surely there is a convention for this?";
	
	/** The human-readable name for this plugin, to be displayed in menus etc. */
	String i18nKey();
	/** The default name for this plugin */
	String name();
	/** The path to the icon for this plugin, on the classpath.  The icon is loaded with {@link Thinlet#getIcon(String)} */
	String iconPath();
	/**
	 * <p>Gets the location of the Spring config for this plugin.</p>
	 * <p>If the config is on the classpath, this should be detailed like:
	 * <code>classpath:package1/package2/pluginname-spring-hibernate.xml</code></p>
	 * @return the location of the Spring config for this plugin, or {@link #NO_VALUE} if none is required.
	 */
	String springConfigLocation();
	/**
	 * <p>Gets the location of the hibernate config for this plugin.</p>
	 * <p>If the config is on the classpath, this should be detailed like:
	 * <code>classpath:package1/package2/pluginname.hibernate.cfg.xml</code></p>
	 * @return the location of the hibernate config for this plugin, or {@link #NO_VALUE} if none is required.
	 */
	String hibernateConfigPath();
}
