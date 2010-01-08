/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.ui;

import java.awt.Image;

import net.frontlinesms.ErrorUtils;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.Utils;
import net.frontlinesms.ui.i18n.LanguageBundle;

import org.apache.log4j.Logger;

import thinlet.FrameLauncher;

/**
 * Base UI used for FrontlineSMS.
 */
@SuppressWarnings("serial")
public abstract class FrontlineUI extends ExtendedThinlet implements ThinletUiEventHandler {
	
//> UI DEFINITION FILES
	/** Thinlet UI layout File: alert popup box */
	protected static final String UI_FILE_ALERT = "/ui/dialog/alert.xml";

//> UI COMPONENTS
	/** Component of {@link #UI_FILE_ALERT} which contains the message to display */
	private static final String COMPONENT_ALERT_MESSAGE = "alertMessage";
	
//> INSTANCE PROPERTIES
	/** Logging object */
	protected final Logger log = Utils.getLogger(this.getClass());
	/** The language bundle currently in use */
	public static LanguageBundle currentResourceBundle;
	/** Frame launcher that this UI instance is displayed within.  We need to keep a handle on it so that we can dispose of it when we quit or change UI modes. */
	protected FrameLauncher frameLauncher;
	/** The {@link FrontlineSMS} instance that this UI is attached to. */
	protected FrontlineSMS frontlineController;

	/**
	 * Gets the icon for a specific language bundle
	 * @param languageBundle
	 * @return the flag image for the language bundle, or <code>null</code> if none could be found.
	 */
	public Image getFlagIcon(LanguageBundle languageBundle) {
		String country = languageBundle.getCountry();
		String flagFile = country != null ? "/icons/flags/" + country + ".png" : null;
		return country == null ? null : getIcon(flagFile);
	}
	
	/**
	 * Loads a Thinlet UI descriptor from an XML file.  If there are any
	 * problems loading the file, this will log Throwables thrown and 
	 * allow the program to continue running.
	 * 
	 * {@link #loadComponentFromFile(String, Object)} should always be used by external handlers in preference to this.
	 * @param filename path of the UI XML file to load from
	 * @return thinlet component loaded from the file
	 */
	public Object loadComponentFromFile(String filename) {
		return loadComponentFromFile(filename, this);
	}
	
	/**
	 * Loads a Thinlet UI descriptor from an XML file and sets the provided event handler.
	 * If there are any problems loading the file, this will log Throwables thrown and 
	 * allow the program to continue running.
	 * @param filename path of the UI XML file to load from
	 * @param thinletEventHandler event handler for the UI component
	 * @return thinlet component loaded from the file
	 */
	public Object loadComponentFromFile(String filename, ThinletUiEventHandler thinletEventHandler) {
		log.trace("ENTER");
		try {
			log.debug("Filename [" + filename + "]");
			log.trace("EXIT");
			return parse(filename, thinletEventHandler);
		} catch(Throwable t) {
			log.error("Error parsing file [" + filename + "]", t);
			log.trace("EXIT");
			throw new RuntimeException(t);
		}
	}
	
	/**
	 * Event fired when the browse button is pressed, during an export action. 
	 * This method opens a fileChooser, showing only directories.
	 * The user will select a directory and write the file name he/she wants.
	 * @param textFieldToBeSet The text field whose value should be sert to the chosen file
	 */
	public void showSaveModeFileChooser(Object textFieldToBeSet) {
		new FileChooser(this).showSaveModeFileChooser(textFieldToBeSet);
	}
	
	/**
	 * Event fired when the browse button is pressed, during an import action. 
	 * This method opens a fileChooser, showing directories and files.
	 * The user will select a directory and write the file name he/she wants.
	 * @param textFieldToBeSet The text field whose value should be sert to the chosen file
	 */
	public void showOpenModeFileChooser(Object textFieldToBeSet) {
		new FileChooser(this).showOpenModeFileChooser(textFieldToBeSet);
	}

	/**
	 * Popup an alert to the user with the supplied message.
	 * @param alertMessage
	 */
	public void alert(String alertMessage) {
		Object alertDialog = loadComponentFromFile(UI_FILE_ALERT);
		setText(find(alertDialog, COMPONENT_ALERT_MESSAGE), alertMessage);
		add(alertDialog);
	}
	
	/**
	 * Removes the supplied dialog from the application.
	 * 
	 * @param dialog
	 */
	public void removeDialog(Object dialog) {
		remove(dialog);
	}
	
	/**
	 * Opens a link in the system browser
	 * @param url the url to open
	 * @see Utils#openExternalBrowser(String)
	 */
	public void openBrowser(String url) {
		Utils.openExternalBrowser(url);
	}

	/**
	 * Opens a page of the help manual
	 * @param page The name of the help manual page, including file extension.
	 */
	public void showHelpPage(String page) {
		String url = "help/" + page;
		Utils.openExternalBrowser(url);
	}
	
	/**
	 * Shows an error dialog informing the user that an unhandled error has occurred.
	 */
	@Override
	protected void handleException(Throwable throwable) {
		log.error("Unhandled exception from thinlet.", throwable);
		ErrorUtils.showErrorDialog("Unexpected error", "There was an unexpected error.", throwable, false);
	}
	
	/**
	 * Reloads the ui.
	 * @param useNewFrontlineController <code>true</code> if a new {@link FrontlineSMS} should be isntantiated; <code>false</code> if the current one should be reused
	 */
	final void reloadUI(boolean useNewFrontlineController) {
		this.frameLauncher.dispose();
		this.frameLauncher.setContent(null);
		this.frameLauncher = null;
		this.destroy();
		try {
			if (useNewFrontlineController) {
				// If we're using a new Frontline controller, we have to properly
				// shut down the last one so that ownership of any connected phones
				// is relinquished
				if (frontlineController != null) frontlineController.destroy();
				new UiGeneratorController(new FrontlineSMS(new ThinletDatabaseConnectionTestHandler()), false);
			} else {
				new UiGeneratorController(frontlineController, false);
			}
			
		} catch(Throwable t) {
			log.error("Unable to reload frontlineSMS.", t);
		}
	}
	
	
}
