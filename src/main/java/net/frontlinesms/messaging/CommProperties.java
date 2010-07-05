/**
 * 
 */
package net.frontlinesms.messaging;

import serial.SerialClassFactory;
import net.frontlinesms.resources.UserHomeFilePropertySet;

/**
 * @author Alex
 */
public class CommProperties extends UserHomeFilePropertySet {

//> STATIC CONSTANTS
	/** Property key: list of ignored ports.  This is a comma-separated list of COM ports to ignore. */
	private static final String PROPERTY_IGNORE = "ignore";
	/** Property key: package to use for COM access, e.g. gnu.io or javax.comm */
	private static final String PROPERTY_PACKAGE = "package";
	
	/** Singleton instance of this class. */
	private static CommProperties instance;

//> INSTANCE PROPERTIES

//> CONSTRUCTORS
	/**
	 * Create a new Comm properties file.
	 */
	private CommProperties() {
		super("comm");
	}

//> ACCESSORS

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES
	/**
	 * Lazy getter for {@link #instance}
	 * @return The singleton instance of this class
	 */
	public static synchronized CommProperties getInstance() {
		if(instance == null) {
			instance = new CommProperties();
		}
		return instance;
	}

	/** @return the list of Comm ports to ignore. */
	public String[] getIgnoreList() {
		String ignore = super.getProperty(PROPERTY_IGNORE);
		if (ignore == null) return new String[0]; 
		else return ignore.toUpperCase().split(",");
	}
	
	/** @return the name of the comm library to use */
	public String getCommLibraryPackageName() {
		return super.getProperty(PROPERTY_PACKAGE, SerialClassFactory.PACKAGE_RXTX);
	}

//> STATIC HELPER METHODS
}
