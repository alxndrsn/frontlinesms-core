/**
 * 
 */
package net.frontlinesms.ui;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.resources.ResourceUtils;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author kadu <kadu@masabi.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public abstract class FileChooser implements ThinletUiEventHandler {
//> UI FILES
	/** Thinlet UI layout File: file choosing dialog */
	private static final String UI_FILE_FILE_CHOOSER_FORM = "/ui/core/util/dgFileChooser.xml";
	
//> INSTANCE PROPERTIES
	private Logger log = FrontlineUtils.getLogger(this.getClass());
	final FrontlineUI ui;
	private final Mode mode;
	private File currentDirectory;
	
//> UI COMPONENTS
	private Object dialogComponent;
	private Object lbTitle;
	private Object lsFiles;
	private Object tfFilename;
	private Object btGoUp;
	private Object btDone;
	
//> CONSTRUCTORS
	FileChooser(Mode mode, FrontlineUI ui) {
		assert(mode != null) : "Must specify a mode.";
		assert(ui != null) : "Must supply a UI controller.";
		
		this.mode = mode;
		this.ui = ui;
	}
	
//> UI EVENT METHODS
	public void handleListExecute() {
		Object selectedItem = ui.getSelectedItem(this.lsFiles);
		File selectedFile = ui.getAttachedObject(selectedItem, File.class);
		if(selectedFile.isDirectory()) {
			update(selectedFile);
		} else {
			choosingCompleted(selectedFile);
		}
	}
	
	public void handleListSelectionChanged() {
		Object selectedItem = ui.getSelectedItem(this.lsFiles);
		File file = ui.getAttachedObject(selectedItem, File.class);
		String textFieldValue = file.isFile() ? file.getName() : "";
		ui.setText(tfFilename, textFieldValue);
	}
	
	/**
	 * Enters the selected directory and show its files in the list.
	 * @param tfFilename 
	 * @param list 
	 * @param dialog 
	 */
	public void goToDir() {
		File file = new File(ui.getText(this.tfFilename));
		if (!file.exists() || !file.isDirectory()) {
			ui.alert(InternationalisationUtils.getI18NString(FrontlineSMSConstants.MESSAGE_DIRECTORY_NOT_FOUND));
		} else {
			update(file);
		}
		ui.setText(this.tfFilename, "");
	}
	/**
	 * Go up a directory, if possible, and show its files in the list.
	 */
	public void goUp() {
		if(this.currentDirectory != null) {
			update(this.currentDirectory.getParentFile());
		}
	}
	
	public void done() {
		if(currentDirectory != null) {
			if(mode == Mode.OPEN) {
				Object selectedItem = ui.getSelectedItem(this.lsFiles);
				if(selectedItem != null) {
					File selectedFile = ui.getAttachedObject(selectedItem, File.class);
					if(selectedFile.isFile()) {
						choosingCompleted(selectedFile);
					} else if(selectedFile.isDirectory()) {
						update(selectedFile);
					}
				}
			} else if(mode == Mode.SAVE) {
				String savePath = this.currentDirectory.getAbsolutePath();
				if(!savePath.endsWith(File.separator)) savePath += File.separator;
				savePath += ui.getText(this.tfFilename);
				choosingCompleted(savePath);
			} else throw new IllegalStateException();
		}
	}
	
	public void remove() {
		ui.remove(this.dialogComponent);
	}
	
//> UI HELPER METHODS
	private void choosingCompleted(File selectedFile) {
		choosingCompleted(selectedFile.getAbsolutePath());
	}
	
	private void choosingCompleted(String selectedFilePath) {
		setTextfield(selectedFilePath);
		remove();
	}
	
	/** Set the textfield value by whatever means seems approprate */
	protected abstract void setTextfield(String selectedFilePath);
	
	/** 
	 * Updates the file list with the contents of the supplied file.
	 * @param directory the new directory whose contents to display, or <code>null</code> if the roots should be shown.
	 */
	private void update(File directory) {
		// set the current directory field
		this.currentDirectory = directory;
		
		// Set the title
		String title = directory == null ? "" : directory.getAbsolutePath();
		ui.setText(this.lbTitle, title);
		
		// clear the list
		ui.removeAll(this.lsFiles);
		
		// add the file's children, or if file is null, show the root's children
		for(File f : getOrderedContents(directory)) {
			ui.add(this.lsFiles, getListItem(f));
		}
		
		// enable/disable the GoUp button depending whether this is a root, or it is top-level dir and there are no roots
		boolean enableGoUp = directory != null && (directory.getParent() != null || File.listRoots().length > 0);
		ui.setEnabled(this.btGoUp, enableGoUp);
		ui.setEnabled(this.btDone, directory != null);
	}
	
	private Object getListItem(File f) {
		String label = f.getName();
		if(label == null || label.length()==0) {
			// "Roots", e.g. the disk drives on a windows system, will often return nothing for getName().
			// In this case, we need to get the path instead.
			label = f.getPath();
		}
		Object item = ui.createListItem(label, f);
		ui.setAttachedObject(item, f);
		if (f.isDirectory()) {
			if(log.isDebugEnabled()) log.debug("Directory [" + f.getAbsolutePath() + "]");
			ui.setIcon(item, Icon.FOLDER_CLOSED);
		} else {
			if(log.isDebugEnabled()) log.debug("File [" + f.getAbsolutePath() + "]");
			ui.setIcon(item, Icon.FILE);
		}
		return item;
	}
	
	private List<File> getOrderedContents(File directory) {
		File[] contents = directory == null ? File.listRoots() : directory.listFiles();
		List<File> orderedContents = new ArrayList<File>();
		if (contents != null) {
			for (File f : contents) {
				orderedContents.add(f);
			}
			Collections.sort(orderedContents, new FrontlineUtils.FileComparator());
		}
		return orderedContents;
	}
	
	private Object find(String name) {
		return ui.find(this.dialogComponent, name);
	}
	
	private void show() {
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_FILE_CHOOSER_FORM, this);
		ui.add(this.dialogComponent);
		this.lsFiles = find("lsFiles");
		this.btGoUp = find("btGoUp");
		this.btDone = find("btDone");
		this.tfFilename = find("tfFilename");
		this.lbTitle = find("lbTitle");
		update(new File(ResourceUtils.getUserHome()));
	}
	
//> STATIC HELPERS
	public static void main(String[] args) {
		File[] roots = File.listRoots();
		for(int i=0;i<roots.length;i++)
		    System.out.println("Root["+i+"]:" + roots[i]);
	}
	
//> STATIC FACTORIES
	/**
	 * Event fired when the browse button is pressed, during an export action. 
	 * This method opens a fileChooser, showing directories and files.
	 * The user will select a directory and write the file name he/she wants.
	 * @param targetTextfield The text field whose value should be sert to the chosen file
	 */
	public static void showSaveModeFileChooser(FrontlineUI ui, Object targetTextfield) {
		showFileChooser(Mode.SAVE, ui, targetTextfield);
	}

	/**
	 * Event fired when the browse button is pressed, during an export action. 
	 * This method opens a fileChooser, showing directories and files.
	 * The user will select a directory and write the file name he/she wants.
	 * @param targetTextfield The text field whose value should be sert to the chosen file
	 * @param eventHandler 
	 * @param setMethodName 
	 */
	public static void showSaveModeFileChooser(FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		showFileChooser(Mode.SAVE, ui, eventHandler, setMethodName);
	}
	
	/**
	 * Event fired when the browse button is pressed, during an import action. 
	 * This method opens a fileChooser, showing directories and files.
	 * The user will select a directory and write the file name he/she wants.
	 * @param targetTextfield The text field whose value should be sert to the chosen file
	 */
	public static void showOpenModeFileChooser(FrontlineUI ui, Object targetTextfield) {
		showFileChooser(Mode.OPEN, ui, targetTextfield);
	}

	private static void showFileChooser(Mode mode, FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		FileChooser chooser = new SetterCallingFileChooser(mode, ui, eventHandler, setMethodName);
		chooser.show();
	}

	private static void showFileChooser(Mode mode, FrontlineUI ui, Object targetTextfield) {
		FileChooser chooser = new DirectSetFileChooser(mode, ui, targetTextfield);
		chooser.show();
	}
}

enum Mode {
	OPEN, SAVE;
}

class DirectSetFileChooser extends FileChooser {
	private final Object targetTextfield;
	
	public DirectSetFileChooser(Mode mode, FrontlineUI ui, Object targetTextfield) {
		super(mode, ui);
		assert(targetTextfield != null) : "Must supply a target textfield.";
		this.targetTextfield = targetTextfield;
	}
	
	@Override
	protected void setTextfield(String selectedFilePath) {
		ui.setText(this.targetTextfield, selectedFilePath);	
	}
}

class SetterCallingFileChooser extends FileChooser {
	private final ThinletUiEventHandler eventHandler;
	private final Method setMethod;

	public SetterCallingFileChooser(Mode mode, FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		super(mode, ui);
		
		this.eventHandler = eventHandler;
		
		try {
			this.setMethod = eventHandler.getClass().getMethod(setMethodName, String.class);
			assert(setMethod != null) : "Must provide a valid setMethod name.";
			assert(!Modifier.isStatic(setMethod.getModifiers())) : "Set method must be an instance method.";
		} catch (SecurityException ex) {
			throw new AssertionError(ex.getMessage());
		} catch (NoSuchMethodException ex) {
			throw new AssertionError(ex.getMessage());
		}
	}
	
	@Override
	public void setTextfield(String selectedFilePath) {
		try {
			setMethod.invoke(this.eventHandler, selectedFilePath);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException(ex);
		} catch (InvocationTargetException ex) {
			throw new IllegalStateException(ex);
		}
	}
}