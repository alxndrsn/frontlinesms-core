/**
 * 
 */
package net.frontlinesms.ui;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.Utils;
import net.frontlinesms.resources.ResourceUtils;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import thinlet.Thinlet;

/**
 * @author kadu
 * @author alex
 */
public class FileChooser implements ThinletUiEventHandler {
//> UI FILES
	/** Thinlet UI layout File: file choosing dialog */
	protected static final String UI_FILE_FILE_CHOOSER_FORM = "/ui/dialog/fileChooserForm.xml";
	
//> UI COMPONENTS
	private static final String COMPONENT_FILE_CHOOSER_LIST = "fileChooser_list";
	private static final String COMPONENT_LABEL_DIRECTORY = "lbDirectory";
	
//> INSTANCE PROPERTIES
	private Logger log = Utils.getLogger(this.getClass());
	private final FrontlineUI ui;
	
//> CONSTRUCTORS
	public FileChooser(FrontlineUI ui) {
		this.ui = ui;
	}
	
	
//> UI EVENT METHODS
	/**
	 * Handles the double-click action in the file chooser list. Double-clicking
	 * in a directory means to get into it. However, double-clicking a file during an
	 * import action, means to select it as the desired file.
	 * @param fileList 
	 * @param dialog 
	 */
	public void fileList_doubleClicked(Object fileList, Object dialog) {
		log.trace("ENTER");
		Object selected = ui.getSelectedItem(fileList);
		Object file = ui.getAttachedObject(selected);
		if (file instanceof File) {
			File f = (File) file;
			if (f.isDirectory()) {
				log.debug("Selected directory [" + f.getAbsolutePath() + "]");
				addFilesToList(f, fileList, dialog);
			} else if (ui.getProperty(dialog, FrontlineSMSConstants.PROPERTY_TYPE).equals(FrontlineSMSConstants.OPEN_MODE)) {
				log.debug("Selected file [" + f.getAbsolutePath() + "]");
				//This is the selected file.
				//TODO Should call the method to execute the import action.
				ui.setText(ui.getAttachedObject(dialog), f.getAbsolutePath());
				ui.remove(dialog);
			}
		}
		log.trace("EXIT");
	}

	/**
	 * Enters the selected directory and show its files in the list.
	 * @param tfFilename 
	 * @param list 
	 * @param dialog 
	 */
	public void goToDir(Object tfFilename, Object list, Object dialog) {
		File file = new File(ui.getString(tfFilename, Thinlet.TEXT));
		if (!file.exists()) {
			ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_DIRECTORY_NOT_FOUND));
		} else {
			addFilesToList(file, list, dialog);
		}
		ui.setText(tfFilename, "");
	}
	
	/**
	 * Adds the files under the desired directory to the file list in the file chooser dialog.
	 * @param parent 
	 * @param list 
	 * @param dialog 
	 */
	public void addFilesToList(File parent, Object list, Object dialog) {
		log.trace("ENTER");
		log.debug("Adding files under [" + parent.getAbsolutePath() + "]");
		ui.removeAll(list);
		ui.setAttachedObject(list, parent);
		ui.setText(ui.find(dialog, COMPONENT_LABEL_DIRECTORY), parent.getAbsolutePath());
		LinkedList<File> files = new LinkedList<File>();
		for (File f : parent.listFiles()) {
			files.add(f);
		}
		Collections.sort(files, new Utils.FileComparator());
		for (File child : files) {
			if (child.isDirectory() || (!child.isDirectory() && ui.getProperty(dialog, FrontlineSMSConstants.PROPERTY_TYPE).equals(FrontlineSMSConstants.OPEN_MODE))) {
				Object item = ui.createListItem(child.getName(), child);
				ui.setAttachedObject(item, child);
				if (child.isDirectory()) {
					log.debug("Directory [" + child.getAbsolutePath() + "]");
					ui.setIcon(item, Icon.FOLDER_CLOSED);
				} else {
					log.debug("File [" + child.getAbsolutePath() + "]");
					ui.setIcon(item, Icon.FILE);
				}
				ui.add(list, item);
			}
		}
		log.trace("EXIT");
	}

	/**
	 * Go up a directory, if possible, and show its files in the list.
	 * @param list 
	 * @param dialog 
	 */
	public void goUp(Object list, Object dialog) {
		Object file = ui.getAttachedObject(list);
		File fileInDisk = (File) file;
		if (fileInDisk.getParent() == null) {
			ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_IMPOSSIBLE_TO_GO_UP_A_DIRECTORY));
		} else {
			addFilesToList(fileInDisk.getParentFile(), list, dialog);
		}
	}
	
	/**
	 * Event fired when the browse button is pressed, during an export action. 
	 * This method opens a fileChooser, showing only directories.
	 * The user will select a directory and write the file name he/she wants.
	 * @param textFieldToBeSet The text field whose value should be sert to the chosen file
	 */
	public void showSaveModeFileChooser(Object textFieldToBeSet) {
		showFileChooser(textFieldToBeSet, FrontlineSMSConstants.SAVE_MODE);
	}
	
	/**
	 * Event fired when the browse button is pressed, during an import action. 
	 * This method opens a fileChooser, showing directories and files.
	 * The user will select a directory and write the file name he/she wants.
	 * @param textFieldToBeSet The text field whose value should be sert to the chosen file
	 */
	public void showOpenModeFileChooser(Object textFieldToBeSet) {
		showFileChooser(textFieldToBeSet, FrontlineSMSConstants.OPEN_MODE);
	}
	/**
	 * This method opens a fileChooser, showing directories and files.
	 * @param textFieldToBeSet The text field whose value should be sert to the chosen file
	 * @param fileMode either {@link FrontlineSMSConstants#OPEN_MODE} or {@link FrontlineSMSConstants#SAVE_MODE}
	 */
	private void showFileChooser(Object textFieldToBeSet, String fileMode) {
		Object fileChooserDialog = ui.loadComponentFromFile(UI_FILE_FILE_CHOOSER_FORM, this);
		ui.setAttachedObject(fileChooserDialog, textFieldToBeSet);
		ui.putProperty(fileChooserDialog, FrontlineSMSConstants.PROPERTY_TYPE, fileMode);
		addFilesToList(new File(ResourceUtils.getUserHome()), ui.find(fileChooserDialog, COMPONENT_FILE_CHOOSER_LIST), fileChooserDialog);
		ui.add(fileChooserDialog);
	}
	
	/**
	 * Method called when the user finishes to browse files and select one to be the export file.
	 * @param list 
	 * @param dialog 
	 * @param filename 
	 */
	public void doSelection(Object list, Object dialog, String filename) {
		log.trace("ENTER");
		Object selected = ui.getSelectedItem(list);
		if (selected == null) {
			selected = ui.getAttachedObject(list);
		} else {
			selected = ui.getAttachedObject(selected);
		}
		File sel = (File) selected;
		if (ui.getProperty(dialog, FrontlineSMSConstants.PROPERTY_TYPE).equals(FrontlineSMSConstants.SAVE_MODE)) {	
			String filePath = sel.getAbsolutePath();
			if (!filePath.endsWith(File.separator)) {
				filePath += File.separator;
			}
			log.debug("Selected Directory [" + filePath + "]");
			ui.setText(ui.getAttachedObject(dialog), filePath + filename);
		} else {
			if (selected == null) {
				ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_NO_FILE_SELECTED));
				log.trace("EXIT");
				return;
			}
			//This is the selected file.
			ui.setText(ui.getAttachedObject(dialog), sel.getAbsolutePath());
		}
		ui.remove(dialog);
		log.trace("EXIT");
	}
	
//> UI PASS-THRU METHODS
	/** Remove the supplied thinlet dialog */
	public void removeDialog(Object dialog) {
		ui.removeDialog(dialog);
	}
	
//> STATIC HELPERS
	
	public static void main(String[] args) {
		File[] roots = File.listRoots();
		for(int i=0;i<roots.length;i++)
		    System.out.println("Root["+i+"]:" + roots[i]);
	}
}
