/**
 * 
 */
package net.frontlinesms.ui.handler.message;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 *
 */
public class MessageDetailsDisplay implements ThinletUiEventHandler {
	/** Path to the Thinlet XML layout file for the message details form */
	public static final String UI_FILE_MSG_DETAILS_FORM = "/ui/core/messages/dgMessageDetails.xml";

	private final UiGeneratorController ui;
	
	public MessageDetailsDisplay(UiGeneratorController ui) {
		this.ui = ui;
	}

	public void show(FrontlineMessage message) {
		Object details = ui.loadComponentFromFile(UI_FILE_MSG_DETAILS_FORM, this);
		String senderDisplayName = ui.getSenderDisplayValue(message);
		String recipientDisplayName = ui.getRecipientDisplayValue(message);
		String status = UiGeneratorController.getMessageStatusAsString(message);
		String date = InternationalisationUtils.getDatetimeFormat().format(message.getDate());
		String content = message.getTextContent();
		
		ui.setText(ui.find(details, "tfStatus"), status);
		ui.setText(ui.find(details, "tfSender"), senderDisplayName);
		ui.setText(ui.find(details, "tfRecipient"), recipientDisplayName);
		ui.setText(ui.find(details, "tfDate"), date);
		ui.setText(ui.find(details, "tfContent"), content);
		
		ui.add(details);
	}

	/**
	 * Remove the supplied dialog from view.
	 * @param dialog the dialog to remove
	 * @see UiGeneratorController#removeDialog(Object)
	 */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}
