package net.frontlinesms.ui.handler.settings;

import java.util.Collection;

import net.frontlinesms.EmailSender;
import net.frontlinesms.EmailServerHandler;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.EmailAccount;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.repository.EmailAccountDao;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.email.EmailAccountSettingsDialogHandler;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

import org.apache.log4j.Logger;

/**
 * UI Handler for the sections incorporating a list of email accounts
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
public abstract class SettingsAbstractEmailsSectionHandler extends BaseSectionHandler implements UiSettingsSectionHandler, ThinletUiEventHandler, EventObserver {
	//> UI LAYOUT FILES
	protected static final String UI_FILE_LIST_EMAIL_ACCOUNTS_PANEL = "/ui/core/settings/generic/pnAccountsList.xml";
	
	//> THINLET COMPONENT NAMES
	protected static final String UI_COMPONENT_ACCOUNTS_LIST = "accountsList";
	protected static final String UI_COMPONENT_BT_EDIT = "btEditAccount";
	protected static final String UI_COMPONENT_BT_DELETE = "btDeleteAccount";
	
//> INSTANCE PROPERTIES
	/** Logger */
	protected Logger LOG = FrontlineUtils.getLogger(this.getClass());
	
	protected EmailAccountDao emailAccountDao;
	/** Manager of {@link EmailAccount}s and {@link EmailSender}s */
	protected EmailServerHandler emailManager;
	
	protected boolean isForReceiving;

	private Object accountsListPanel;
	
	public SettingsAbstractEmailsSectionHandler (UiGeneratorController ui, boolean isForReceiving) {
		super(ui);
		this.emailAccountDao = this.uiController.getFrontlineController().getEmailAccountFactory();
		this.emailManager = this.uiController.getFrontlineController().getEmailServerHandler();
		this.isForReceiving = isForReceiving;
		this.accountsListPanel = this.uiController.loadComponentFromFile(UI_FILE_LIST_EMAIL_ACCOUNTS_PANEL, this);
		
		// Register with the EventBus to receive notification of new email accounts
		this.eventBus.registerObserver(this);
	}

	public Object getAccountsListPanel() {
		this.refresh();
		
		return this.accountsListPanel;
	}

	public void refresh() {
		Object table = this.uiController.find(this.accountsListPanel, UI_COMPONENT_ACCOUNTS_LIST);
		if (table != null) {
			this.uiController.removeAll(table);
			Collection<EmailAccount> emailAccounts;
			
			if (this.isForReceiving) {
				emailAccounts = emailAccountDao.getReceivingEmailAccounts();
			} else {
				emailAccounts = emailAccountDao.getSendingEmailAccounts();
			}
			
			for (EmailAccount acc : emailAccounts) {
				this.uiController.add(table, this.uiController.getRow(acc));
			}
			
			new FrontlineUiUpateJob() {
				public void run() {
					enableBottomButtons(null);	
				}
			}.execute();
		}
	}

//> UI EVENT METHODS
		
	public void newEmailAccountSettings () {
		showEmailAccountSettingsDialog(null);
	}
	
	public void editEmailAccountSettings(Object list) {
		Object selected = this.uiController.getSelectedItem(list);
		if (selected != null) {
			EmailAccount emailAccount = (EmailAccount) this.uiController.getAttachedObject(selected);
			showEmailAccountSettingsDialog(emailAccount);
		}
	}
	
	private void showEmailAccountSettingsDialog(EmailAccount emailAccount) {
		EmailAccountSettingsDialogHandler emailAccountSettingsDialogHandler = new EmailAccountSettingsDialogHandler(this.uiController, this.isForReceiving);
		emailAccountSettingsDialogHandler.initDialog(emailAccount);
		this.uiController.add(emailAccountSettingsDialogHandler.getDialog());
	}
	
	public void enableBottomButtons(Object table) {
		if (table == null) {
			table = this.uiController.find(UI_COMPONENT_ACCOUNTS_LIST);
		}
		
		boolean enableEditAndDelete = (this.uiController.getSelectedIndex(table) >= 0);
		
		this.uiController.setEnabled(this.uiController.find(this.accountsListPanel, UI_COMPONENT_BT_EDIT), enableEditAndDelete);
		this.uiController.setEnabled(this.uiController.find(this.accountsListPanel, UI_COMPONENT_BT_DELETE), enableEditAndDelete);
	}
	
	/**
	 * Enables or disables menu options in a List Component's popup list
	 * and toolbar.  These enablements are based on whether any items in
	 * the list are selected, and if they are, on the nature of these
	 * items.
	 * @param list 
	 * @param popup 
	 * @param toolbar
	 * 
	 * TODO check where this is used, and make sure there is no dead code
	 */
	public void enableOptions(Object list, Object popup, Object toolbar) {
		Object[] selectedItems = this.uiController.getSelectedItems(list);
		boolean hasSelection = selectedItems.length > 0;

		if(popup!= null && !hasSelection && "emailServerListPopup".equals(this.uiController.getName(popup))) {
			this.uiController.setVisible(popup, false);
			return;
		}
		
		if (hasSelection && popup != null) {
			// If nothing is selected, hide the popup menu
			this.uiController.setVisible(popup, hasSelection);
		}
		
		if (toolbar != null && !toolbar.equals(popup)) {
			for (Object o : this.uiController.getItems(toolbar)) {
				this.uiController.setEnabled(o, hasSelection);
			}
		}
	}
	
	/**
	 * Removes the selected accounts.
	 */
	public void removeSelectedFromAccountList() {
		LOG.trace("ENTER");
		this.uiController.removeConfirmationDialog();
		Object list = this.uiController.find(this.accountsListPanel, UI_COMPONENT_ACCOUNTS_LIST);
		Object[] selected = this.uiController.getSelectedItems(list);
		for (Object o : selected) {
			EmailAccount acc = this.uiController.getAttachedObject(o, EmailAccount.class);
			LOG.debug("Removing Account [" + acc.getAccountName() + "]");
			emailManager.serverRemoved(acc);
			emailAccountDao.deleteEmailAccount(acc);
		}
		
		this.refresh();
		LOG.trace("EXIT");
	}
	
	/** Handle notifications from the {@link EventBus} */
	public void notify(FrontlineEventNotification event) {
		if(event instanceof DatabaseEntityNotification<?>) {
			if(((DatabaseEntityNotification<?>)event).getDatabaseEntity() instanceof EmailAccount) {
				new FrontlineUiUpateJob() {
					public void run() {
						refresh();
					}
				}.execute();
			}
		}
	}


//> UI PASSTHROUGH METHODS
	/** @see UiGeneratorController#showConfirmationDialog(String, Object) */
	public void showConfirmationDialog(String methodToBeCalled) {
		this.uiController.showConfirmationDialog(methodToBeCalled, this);
	}
	/**
	 * @param page page to show
	 * @see UiGeneratorController#showHelpPage(String)
	 */
	public void showHelpPage(String page) {
		this.uiController.showHelpPage(page);
	}
	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog(Object dialog) {
		this.uiController.removeDialog(dialog);
	}	
//> UI HELPER METHODS
}