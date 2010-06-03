/**
 * 
 */
package net.frontlinesms.ui.handler.message;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
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
		
		ui.setText(ui.find(details, "tfStatus"), status);
		ui.setText(ui.find(details, "tfSender"), senderDisplayName);
		ui.setText(ui.find(details, "tfRecipient"), recipientDisplayName);
		ui.setText(ui.find(details, "tfDate"), date);
		
		
		Object contentPanel = ui.find(details, "pnContent");
		for(Object contentComponent : getContentComponents(message)) {
			ui.add(contentPanel, contentComponent);
		}
		
		ui.add(details);
	}

	private Object[] getContentComponents(FrontlineMessage message) {
		if(message instanceof FrontlineMultimediaMessage) {
			FrontlineMultimediaMessage mm = (FrontlineMultimediaMessage) message;
			List<FrontlineMultimediaMessagePart> parts = mm.getMultimediaParts();
			ArrayList<Object> contentComponents = new ArrayList<Object>(parts.size());
			for(FrontlineMultimediaMessagePart part : parts) {
				contentComponents.add(getComponent(part));
			}
			return contentComponents.toArray();
		} else {
			// It#s a standard text message
			Object textContent = ui.createTextarea("tfContent", message.getTextContent(), 8);
			ui.setWeight(textContent, 1, 1);
			return new Object[]{textContent}; 
		}
	}

	private Object getComponent(FrontlineMultimediaMessagePart part) {
		return ui.createLabel(part.getClass().getName());
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
