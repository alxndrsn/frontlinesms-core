/**
 * 
 */
package net.frontlinesms.ui;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;

/**
 * @author kadu <kadu@masabi.com>
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public abstract class FileChooser {
//> INSTANCE PROPERTIES
	private Logger log = FrontlineUtils.getLogger(this.getClass());
	final FrontlineUI ui;
	private JFileChooser fc;
	private FileFilter fileFilter;
	
//> CONSTRUCTORS
	FileChooser(FrontlineUI ui) {
		assert(ui != null) : "Must supply a UI controller.";
		
		this.ui = ui;
	}
	
//> UI HELPER METHODS
	/** Set the textfield value by whatever means seems approprate */
	protected abstract void setTextfield(String selectedFilePath);
	
	/** Show the file chooser.  This method should be called only once per instance of {@link FileChooser} */
	public void show() {
		fc = new JFileChooser();
		if(this.fileFilter != null) {
			fc.setFileFilter(this.fileFilter);
		}
		
		final FileChooser owner = this;
		new Thread() {
			public void run() {
				int returnVal = fc.showDialog(null, "OK"); // FIXME set approve button text
				if(returnVal == JFileChooser.APPROVE_OPTION){
					String selectedFilePath = fc.getSelectedFile().getAbsolutePath();
					owner.setTextfield(selectedFilePath);
				}
			}
		}.start();
	}

	public void setFileFilter(FileFilter filter) {
		this.fileFilter = filter;
	}
	
//> STATIC HELPERS
	public static void main(String[] args) {
		File[] roots = File.listRoots();
		for(int i=0;i<roots.length;i++)
		    System.out.println("Root["+i+"]:" + roots[i]);
	}
	
//> STATIC FACTORIES
	public static void showFileChooser(FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		FileChooser chooser = createFileChooser(ui, eventHandler, setMethodName);
		chooser.show();
	}

	public static void showFileChooser(FrontlineUI ui, Object targetTextfield) {
		FileChooser chooser = createFileChooser(ui, targetTextfield);
		chooser.show();
	}
	public static FileChooser createFileChooser(FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		FileChooser chooser = new SetterCallingFileChooser(ui, eventHandler, setMethodName);
		return chooser;
	}

	public static FileChooser createFileChooser(FrontlineUI ui, Object targetTextfield) {
		FileChooser chooser = new DirectSetFileChooser(ui, targetTextfield);
		return chooser;
	}
}

class DirectSetFileChooser extends FileChooser {
	private final Object targetTextfield;
	
	public DirectSetFileChooser(FrontlineUI ui, Object targetTextfield) {
		super(ui);
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

	public SetterCallingFileChooser(FrontlineUI ui, ThinletUiEventHandler eventHandler, String setMethodName) {
		super(ui);
		
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