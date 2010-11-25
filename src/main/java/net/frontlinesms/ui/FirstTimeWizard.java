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

import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.AppProperties;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.ui.i18n.FileLanguageBundle;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.LanguageBundle;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;
import thinlet.FrameLauncher;

/**
 * This class is responsible for showing the first time wizard and handling its events.
 * 
 * @author Carlos Eduardo Genz kadu(at)masabi(dot)com
 * @author Alex Anderson
 */
@SuppressWarnings("serial")
@TextResourceKeyOwner(prefix={"MESSAGE_", "I18N"})
public class FirstTimeWizard extends FrontlineUI {
	
//> STATIC CONSTANTS
	/** The default country to use - Great Britain */
	private static final String COUNTRY_GB = "gb";	

//> i18N KEYS
	/** [i18n key] The title of the {@link FirstTimeWizard}'s {@link Frame}. */
	public static final String I18N_FIRST_TIME_WIZARD_TITLE = "common.first.time.wizard";
	
//> UI COMPONENT NAMES
	private static final String COMPONENT_LANGUAGES_LIST = "languagesList";
	
//> UI LAYOUT FILE PATHS
	/** [ui layout file path] The language selection page */
	private static final String UI_FILE_LANGUAGE_SELECTION = "/ui/wizard/languageSelect.xml";
	/** [ui layout file path] The final page displayed before the standard FrontlineSMS UI is displayed. */
	private static final String UI_FILE_START_FORM = "/ui/wizard/startForm.xml";
	
//> INSTANCE VARIABLES
	/** The page from {@link #pages} that we are currently viewing.  This is used for searching. */
	private Object currentPage;
	/** Index in {@link #pages} that the {@link #currentPage} is located at. */
	private int currentPageIndex;
	/** List of pages to be displayed by the wizard.  These pages are in order. */
	private List<Object> pages = new ArrayList<Object>();
	/** Thinlet/AWT {@link FrameLauncher} used for displaying the {@link FirstTimeWizard} user interface. */
	private FrameLauncher frameLauncher;
	/** The instance of the {@link FrontlineSMS} engine that will be started once the wizard has completed */
	private FrontlineSMS frontline;

//> CONSTRUCTORS
	/**
	 * 
	 * @param frontline 
	 */
	public FirstTimeWizard(FrontlineSMS frontline) {
		this.frontline = frontline;
		
		frameLauncher = new FrameLauncher(InternationalisationUtils.getI18nString(I18N_FIRST_TIME_WIZARD_TITLE), this, 510, 380, getIcon(Icon.FRONTLINE_ICON));
		frameLauncher.setResizable(false);
		showLanguageSelection();
	}

//> UI METHODS
	
	/**
	 * Save the selected language
	 * @param list
	 */
	public void setLanguage(Object list) {
		Object sel = getSelectedItem(list);
		AppProperties appProperties = AppProperties.getInstance();
		String filename = getAttachedObject(sel).toString();
		appProperties.setLanguageFilename(filename);
		LanguageBundle languageBundle = InternationalisationUtils.getLanguageBundle(new File(filename));
		FrontlineUI.currentResourceBundle = languageBundle;
		setResourceBundle(languageBundle.getProperties(), languageBundle.isRightToLeft());
		Font font = languageBundle.getFont();
		if(font != null) {
			setFont(new Font(font.getName(), getFont().getStyle(), getFont().getSize()));
		}
		appProperties.saveToDisk();
		frameLauncher.setTitle(InternationalisationUtils.getI18nString(I18N_FIRST_TIME_WIZARD_TITLE));
		loadPages();
		showPage(0);
	}
	
	/**
	 * If the user never has used frontline, we go to the end of the wizard,
	 * otherwise we go one page further.
	 * @param yes
	 */
	public void selectedUsedBefore(boolean yes) {
		if(yes) gotoNextPage();
		else gotoLastPage(); // TODO This behaviour disagrees with javadoc
	}
	
	/** Go to the previous page. */
	public void goBack() {
		showPage(currentPageIndex - 1);
	}
	
	/**
	 * Start the FrontlineSMS user interface. 
	 * @throws Throwable if {@link Throwable} was thrown by {@link UiGeneratorController}'s constructor.
	 */
	public void startFrontline() throws Throwable {
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setShowWizard(false);
		appProperties.saveToDisk();
		
		frameLauncher.dispose();
		new UiGeneratorController(frontline, false);
	}

//> INSTANCE HELPER METHODS
	/** Populate and display the language selection form. */
	private void showLanguageSelection() {
		Object language = loadComponentFromFile(UI_FILE_LANGUAGE_SELECTION);
		Object languagesList = find(language, COMPONENT_LANGUAGES_LIST);
		for (FileLanguageBundle languageBundle : InternationalisationUtils.getLanguageBundles()) {
			Object item = createListItem(languageBundle.getLanguageName(), languageBundle.getFile().getAbsolutePath());
			setIcon(item, getFlagIcon(languageBundle));
			int index = -1;
			if (languageBundle.getCountry().equals(COUNTRY_GB)) {
				setSelected(item, true);
				index = 0;
			}
			add(languagesList, item, index);
		}
		if (currentPage != null) remove(currentPage);
		currentPage = language;
		add(language);
	}

	/** Proceed to the page in {@link #pages} */
	private void gotoNextPage() {
		showPage(this.currentPageIndex + 1);
	}

	/** Proceed to the last page in {@link #pages} */
	private void gotoLastPage() {
		showPage(this.pages.size() - 1);
	}

	/**
	 * Show a particular page.
	 * The page is selected by index from {@link #pages}.
	 * @param newPageIndex the index into {@link #pages} of the next page to show. */
	private void showPage(int newPageIndex) {
		remove(currentPage);
		this.currentPageIndex = newPageIndex;
		if (currentPageIndex != -1) {
			currentPage = pages.get(currentPageIndex);
			add(currentPage);
		} else {
			showLanguageSelection();
		}
	}

	/** Loads all pages into the list of {@link #pages} */
	private void loadPages() {
		currentPageIndex = 0;
		pages.clear();
		// FIXME we should re-add these 2 pages when the data importer from DbController has been implemented.
//		pages.add(loadComponentFromFile(UI_FILE_USED_BEFORE));
//		pages.add(loadComponentFromFile(UI_FILE_IMPORT_DATA));
		pages.add(loadComponentFromFile(UI_FILE_START_FORM));
	}
}
