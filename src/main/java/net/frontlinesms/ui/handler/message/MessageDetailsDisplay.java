/**
 * 
 */
package net.frontlinesms.ui.handler.message;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.mmsdevice.MmsPollingEmailReceiver;
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
			
			if(parts.size() == 0) {
				return new Object[]{ui.createLabel("[i18n] No content.")}; // FIXME i18n
			} else {
				return getContentComponents(parts);
			}
		} else {
			// It's a standard text message
			Object textContent = ui.createTextarea("tfContent", message.getTextContent(), 8);
			ui.setWeight(textContent, 1, 1);
			return new Object[]{textContent}; 
		}
	}

	private Object[] getContentComponents(List<FrontlineMultimediaMessagePart> parts) {
		ArrayList<Object> contentComponents = new ArrayList<Object>(parts.size());
		for(FrontlineMultimediaMessagePart part : parts) {
			contentComponents.add(getComponent(part));
		}
		return contentComponents.toArray();
	}

	private Object getComponent(FrontlineMultimediaMessagePart part) {
		Object component;
		if(!part.isBinary()) {
			component = ui.createTextarea("", part.getTextContent(), 0);
		} else {
			Object panel = ui.createPanel("");
			ui.setColumns(panel, 2);
			Object label = ui.createLabel(part.getFilename());
			ui.add(panel, label);
			ui.add(panel, ui.createButton("[i18n] Open", "openMultimediaPart('" + MmsPollingEmailReceiver.getFile(part).getPath() + "')", panel, this));
			ui.setWeight(label, 1, 0);
			component = panel;
		}
		ui.setWeight(component, 1, 1);
		return component;
	}
	
	public void openMultimediaPart(String filename) {
		FrontlineUtils.openExternalBrowser(filename);
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
