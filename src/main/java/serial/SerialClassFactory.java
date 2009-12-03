/**
 * 
 */
package serial;

import org.apache.log4j.Logger;

/**
 * Factory class for getting the serial classes.
 * @author Alex
 */
public class SerialClassFactory {
//> STATIC CONSTANTS
	/** Package name for javax.comm */
	public static final String PACKAGE_JAVAXCOMM = "javax.comm";
	/** Package name for RXTXserial */
	private static final String PACKAGE_RXTX = "gnu.io";
	/** Singleton instance of this class */
	private static SerialClassFactory INSTANCE;

//> INSTANCE PROPERTIES
	/** Logging object */
	private final Logger log = Logger.getLogger(this.getClass().getName());
	/** The name of the package of the serial implementation to use, either {@link #PACKAGE_JAVAXCOMM} or {@value #PACKAGE_RXTX} */
	private final String serialPackageName;

//> CONSTRUCTORS
	/**
	 * Constructs a {@link SerialClassFactory}.
	 * TODO this currently tests RXTX first, but as javax.comm provides better device support, we should really test that first.  Need to isolate how to tell if it works.
	 */
	private SerialClassFactory() {
		String serialPackageName;
		try {
			log.info("Attempting to load serial package: " + PACKAGE_JAVAXCOMM);
			// Javax.Serial will throw: class java.lang.UnsatisfiedLinkError :: no rxtxSerial in java.library.path if it cannot load.
			Class.forName(PACKAGE_JAVAXCOMM + "." + CommPortIdentifier.class.getSimpleName());
			serialPackageName = PACKAGE_JAVAXCOMM;
			log.info("Using serial package: " + PACKAGE_JAVAXCOMM);
		} catch(Throwable t) {
			log.warn("Failed to load serial package: " + PACKAGE_JAVAXCOMM, t);
			// TODO test this package works - it's possible neither does
			// TODO What should we do if neither package works?  It would certainly be useful to know...
			serialPackageName = PACKAGE_RXTX;
			log.info("Using serial package: " + PACKAGE_RXTX);
		}
		// TODO log what we have ended up with
		this.serialPackageName = serialPackageName;
	}

//> ACCESSORS
	/**
	 * @return {@link #serialPackageName}.
	 */
	public String getSerialPackageName() {
		return serialPackageName;
	}

//> INSTANCE HELPER METHODS

//> STATIC FACTORIES

//> STATIC HELPER METHODS
	/**
	 * Get the singleton instance of this class.  If the singleton is not yet
	 * initialized, this method will do that too.
	 * @return the singleton instance of this class
	 * @throws IllegalStateException If there was a problem initialising the class
	 */
	public static final synchronized SerialClassFactory getInstance() throws IllegalStateException {
		if(INSTANCE == null) {
			INSTANCE = new SerialClassFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * Attempt to get a class by name, first from the {@link #PACKAGE_JAVAXCOMM}, and then from {@link #PACKAGE_RXTX}
	 * TODO once we have decided which package we are using, we should probably try exclusively to get classes from that package.  E.g. it might be possible to get javax.comm classes even though the library is broken.  If we can detect that, do it here the first time this method is called.
	 * @param clazz The class whose namesake we should fetch
	 * @return An implementation of the desired class
	 */
	public Class<?> forName(Class<?> clazz) {
		try {
			return Class.forName(this.serialPackageName + "." + clazz.getSimpleName());
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException(clazz.getSimpleName() + " class not found in package " + this.serialPackageName);
		}
	}
}
