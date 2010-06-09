/**
 * 
 */
package net.frontlinesms.ui.handler.message;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.mmsdevice.MmsDeviceUtils;
import net.frontlinesms.ui.FrontlineUiUtils;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
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
			component = ui.createTextarea("", part.getTextContent(), (part.toString().length() + 44) / 45);
		} else {
			Object panel = ui.createPanel("");
			ui.setColumns(panel, 1);

			File mediaFile = MmsDeviceUtils.getFile(part);
			String openAction = "openMultimediaPart('" + mediaFile.getPath() + "')";
			
			Image thumb = null;
			try {
				thumb = FrontlineUiUtils.getLimitedSizeImage(ImageIO.read(mediaFile), 64, 64);
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
