/**
 * 
 */
package net.frontlinesms.ui.handler.core;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_DATABASE_CONNECTION_PROBLEM;

import thinlet.FrameLauncher;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * UI Controller used when there is a problem 
 * @author Alex
 */
@SuppressWarnings("serial")
public class DatabaseConnectionFailedDialog extends FrontlineUI implements DatabaseSettingsChangedCallbackListener {
	
//> STATIC CONSTANTS
	/** Thinlet UI File: the dialog for the user to attempt reconnection or modify database settings */
	private static final String UI_FILE_CONNECTION_PROBLEM_DIALOG = "/ui/core/database/dgConnectionProblem.xml";
	
	/** The label displaying the error message */
	private static final String COMPONENT_DATABASE_PROBLEM_TEXT_ERROR_MESSAGE = "textErrorMessage";

//> INSTANCE PROPERTIES
	/** Lock for {@link #ensureConnected(DatabaseConnectionTester)} while the UI is handling connection. */
	private final Object LOCK = new Object();
	/** For {@link #acq} to check if it should keep blocking. */
	private boolean keepBlocking;
	
//> CONSTRUCTORS
	/** private constructor to enforce use of factories */
	private DatabaseConnectionFailedDialog() {}

	/** The database settings have been changed, so we can unload the UI and return from the block. */
	public void handleDatabaseSettingsChanged() {
		terminate();
	}

	/** Blocking call to get new database settings. */
	public void acquireSettings() {
		showFrameLauncher();
		synchronized(LOCK) {
			keepBlocking = true;
			while(keepBlocking) {
				try {
					LOCK.wait();
				} catch (InterruptedException ex) {}
			}
		}
	}

	/** Show the UI. */
	private void showFrameLauncher() {
		frameLauncher = new FrameLauncher(InternationalisationUtils.getI18nString(COMMON_DATABASE_CONNECTION_PROBLEM), this, 400, 315, getIcon(Icon.FRONTLINE_ICON));
		frameLauncher.setResizable(true);
	}
	
	/** Called when {@link #acquireSettings()} should return. */
	private void terminate() {
		// We've acquired new settings, so remove the UI, wake up the sleeping thread and give control back to it
		this.frameLauncher.dispose();
		synchronized (LOCK) {
			this.keepBlocking = false;
			LOCK.notify();
		}
	}
	
//> PUBLIC UI METHODS
	/** Show the database settings panel. */
	public void showSettingsPage() {
		DatabaseSettingsPanel settingsPanel = DatabaseSettingsPanel.createNew(this, null);
		settingsPanel.setCancelEnabled(false);

		boolean needToRestartApplication = false;
		settingsPanel.showAsDialog(needToRestartApplication);
	}
	
	/** Reconnect button has been pressed */
	public void reconnect() {
		terminate();
	}
	
//> STATIC FACTORY
	/** Create a new {@link DatabaseConnectionFailedDialog}. */
	public static final DatabaseConnectionFailedDialog create(Exception ex) {
		// Display the UI for re-attempting connection
		DatabaseConnectionFailedDialog ui = new DatabaseConnectionFailedDialog();
		Object problemDialog = ui.loadComponentFromFile(UI_FILE_CONNECTION_PROBLEM_DIALOG);
		ui.setText(ui.find(problemDialog, COMPONENT_DATABASE_PROBLEM_TEXT_ERROR_MESSAGE), ex.getMessage());
		
		// TODO show details of the exception
		// TODO show exeption type
		// TODO show exception message
		// TODO show exception stack trace
		
		ui.add(problemDialog);
		return ui;
	}

////> INSTANCE PROPERTIES
//	/** Lock for {@link #ensureConnected(DatabaseConnectionTester)} while the UI is handling connection. */
//	private final Object CONNECTING_LOCK = new Object();
//	/** Logging object */
//	private final Logger log = Utils.getLogger(this.getClass());
//	/** For {@link #ensureConnected(DatabaseConnectionTester)} to check if it should keep blocking. */
//	private boolean keepBlocking;
//	/** Conneciton tester. */
//	private DatabaseConnectionTester connectionTester;
//	
//	private Object dialogComponent;
//
////> CONSTRUCTORS
//	/** Create a new instance of this class. */
//	public ThinletDatabaseConnectionTestHandler() {}
//	
////> TEST METHODS
//	/** @see DatabaseConnectionTestHandler#ensureConnected(DatabaseConnectionTester) */
//	public void ensureConnected(DatabaseConnectionTester connectionTester) throws DatabaseConnectionFailedException {
//		if(this.connectionTester != null) throw new IllegalStateException("DatabaseConnectionTestHandler.ensureConnected should only be called once.");
//		this.connectionTester = connectionTester;
//		
//		// Attempt to connect three times, waiting longer between each one
//		if(this.connectionTester.checkConnection()) return;
//		sleep(1000);
//		if(this.connectionTester.checkConnection()) return;
//		sleep(2000);
//		if(this.connectionTester.checkConnection()) return;
//		sleep(4000);
//		if(this.connectionTester.checkConnection()) {
//			// The connection is working fine.
//			return;
//		} else {
//			// Display the UI for re-attempting connection
//			Object problemDialog = loadComponentFromFile(UI_FILE_CONNECTION_PROBLEM_DIALOG);
//			this.dialogComponent = problemDialog;
//			this.add(problemDialog);
//			
//			frameLauncher = new FrameLauncher("Database Connection Problem", this, 510, 380, getIcon(Icon.FRONTLINE_ICON));
//			frameLauncher.setResizable(false);
//			
//			// Wait until the UI has triggered a successful connection attempt
//			synchronized(CONNECTING_LOCK) {
//				keepBlocking = true;
//				while(keepBlocking) {
//					try {
//						CONNECTING_LOCK.wait();
//					} catch (InterruptedException ex) {}
//				}
//			}
//		}
//	}
//
////> ACCESSORS
//
////> INSTANCE HELPER METHODS
//	/** Callback from {@link DatabaseSettingsPanel} when the settings have been changed. */
//	public void handleDatabaseSettingsChanged() {
//		reconnect();
//	}
//	
////> PUBLIC UI METHODS
//	/** Attempt to reconnect with the current settings. */
//	public void reconnect() {
//		log.trace("ThinletDatabaseConnectionTestHandler.reconnect() : ENTRY");
//		if(this.connectionTester.checkConnection()) {
//			// We've connected successfully, so wake up the sleeping thread and give control back to it
//			synchronized (CONNECTING_LOCK) {
//				this.keepBlocking = false;
//				CONNECTING_LOCK.notify();
//			}
//			this.frameLauncher.dispose();
//		}
//		log.trace("ThinletDatabaseConnectionTestHandler.reconnect() : EXIT");
//	}
//	
//	/** Show the database settings panel. */
//	public void showSettingsPage() {
//		DatabaseSettingsPanel settingsPanel = DatabaseSettingsPanel.createNew(this);
//		settingsPanel.setCancelEnabled(false);
//		settingsPanel.setSettingsChangedCallbackListener(this);
//		this.removeAll(dialogComponent);
//		this.add(dialogComponent, settingsPanel.getPanelComponent());
//	}
//
////> STATIC FACTORIES
//
////> STATIC HELPER METHODS
//	/**
//	 * Put the thread to sleep.
//	 * @param millis number of milliseconds to sleep for 
//	 */
//	private static void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
