/**
 * 
 */
package net.frontlinesms.mmsdevice;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.mms.ImageMmsMessagePart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;
import net.frontlinesms.mms.MmsReceiveException;
import net.frontlinesms.mms.TextMmsMessagePart;
import net.frontlinesms.mms.email.pop.ClasspathMimeMessage;
import net.frontlinesms.mms.email.pop.FileSystemMmsReceiver;

/**
 * @author aga
 *
 */
public class MmsPollingEmailReceiver {

	public Collection<FrontlineMultimediaMessage> dbgCreateMessagesFromClasspath() {
		ArrayList<FrontlineMultimediaMessage> messages = new ArrayList<FrontlineMultimediaMessage>();
	
		Collection<MmsMessage> mms;
		try {
			mms = new FileSystemMmsReceiver("../MyMmsGateway/src/test/resources/net/frontlinesms/mms/email/pop/parser/uk/").receive();
		} catch (MmsReceiveException e) {
			throw new RuntimeException(e);
		}
		
		for(MmsMessage mm : mms) {
			messages.add(create(mm));
		}
		
		return messages;
	}

	/** Create a new {@link FrontlineMultimediaMessage} from a {@link MmsMessage} */
	private FrontlineMultimediaMessage create(MmsMessage mms) {
		StringBuilder textContent = new StringBuilder();
		List<FrontlineMultimediaMessagePart> multimediaParts = new ArrayList<FrontlineMultimediaMessagePart>();
		for(MmsMessagePart part : mms.getParts()) {
			if(textContent.length() > 0) textContent.append("; ");
			if(part instanceof TextMmsMessagePart) {
				textContent.append(((TextMmsMessagePart) part).toString());
			} else if(part instanceof ImageMmsMessagePart) {
				textContent.append("Image: " + (((ImageMmsMessagePart) part).getFilename()));
			}
		}
		
		FrontlineMultimediaMessage message = new FrontlineMultimediaMessage(Type.RECEIVED, textContent.toString(), multimediaParts);
		message.setRecipientMsisdn("set me please"); // FIXME get recipient address from mms
		message.setSenderMsisdn(mms.getSender());
		message.setStatus(Status.RECEIVED);
		return message;
	}
}
