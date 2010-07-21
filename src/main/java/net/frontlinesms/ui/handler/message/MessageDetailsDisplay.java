/**
 * 
 */
package net.frontlinesms.ui.handler.message;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.messaging.mms.MmsUtils;
import net.frontlinesms.ui.FrontlineUiUtils;
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

	private static final String I18N_MESSAGE_NO_CONTENT = "message.no.content";

	private static final String UI_COMPONENT_PN_CONTENT = "pnContent";
	private static final String UI_COMPONENT_PN_SUBJECT = "pnSubject";
	private static final String UI_COMPONENT_TF_CONTENT = "tfContent";
	private static final String UI_COMPONENT_TF_DATE = "tfDate";
	private static final String UI_COMPONENT_TF_SENDER = "tfSender";
	private static final String UI_COMPONENT_TF_RECIPIENT = "tfRecipient";
	private static final String UI_COMPONENT_TF_STATUS = "tfStatus";
	private static final String UI_COMPONENT_TF_SUBJECT = "tfSubject";

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
		
		ui.setText(ui.find(details, UI_COMPONENT_TF_STATUS), status);
		ui.setText(ui.find(details, UI_COMPONENT_TF_SENDER), senderDisplayName);
		ui.setText(ui.find(details, UI_COMPONENT_TF_RECIPIENT), recipientDisplayName);
		ui.setText(ui.find(details, UI_COMPONENT_TF_DATE), date);
		
		if (message instanceof FrontlineMultimediaMessage && ((FrontlineMultimediaMessage) message).getSubject().length() > 0) {
			ui.setText(ui.find(details, UI_COMPONENT_TF_SUBJECT), ((FrontlineMultimediaMessage) message).getSubject());
			ui.setVisible(ui.find(details, UI_COMPONENT_PN_SUBJECT), true);
		}
		
		Object contentPanel = ui.find(details, UI_COMPONENT_PN_CONTENT);
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
				return new Object[]{ ui.createLabel(InternationalisationUtils.getI18NString(I18N_MESSAGE_NO_CONTENT)) }; // FIXME i18n
			} else {
				return getContentComponents(parts);
			}
		} else {
			// It's a standard text message
			Object textContent = ui.createTextarea(UI_COMPONENT_TF_CONTENT, message.getTextContent(), 8);
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
			component = ui.createTextarea("", part.getTextContent(), (part.toString().length() + 44) / 45);
			this.ui.setEditable(component, false);
		} else {
			Object panel = ui.createPanel("");
			ui.setColumns(panel, 1);

			String openAction = "openMultimediaPart('" + MmsUtils.getFile(part).getPath() + "')";
			
			Image thumb = null;
			try {
				thumb = FrontlineUiUtils.getLimitedSizeImage(ImageIO.read(MmsUtils.getFile(part)), 64, 64);
			} catch(Exception ex) {}
			if(thumb != null) {
				Object thumbComponent = ui.createLink("", openAction, panel, this);
				ui.setIcon(thumbComponent, thumb);
				ui.add(panel, thumbComponent);
			}
			
			ui.add(panel, ui.createLink("[ " + part.getFilename() + " ]", openAction, panel, this));
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
	
	public static void main(String[] args) {
		len("Relaxing on a Sunday with a nice cuppa and a");
		len("pair of flip flops. This is my first MMS on this");
	}
	

	private static void len(String string) {
		System.out.println(string + " : " + string.length());
	}
}
