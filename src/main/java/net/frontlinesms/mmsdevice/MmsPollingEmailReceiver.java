/**
 * 
 */
package net.frontlinesms.mmsdevice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.mms.ImageMmsMessagePart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;
import net.frontlinesms.mms.MmsReceiveException;
import net.frontlinesms.mms.TextMmsMessagePart;
import net.frontlinesms.mms.email.pop.FileSystemMmsReceiver;
import net.frontlinesms.resources.ResourceUtils;

/**
 * @author aga
 *
 */
public class MmsPollingEmailReceiver {

	private static final Logger log = FrontlineUtils.getLogger(MmsPollingEmailReceiver.class);
	
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
			
			String text;
			FrontlineMultimediaMessagePart mmPart;
			if(part instanceof TextMmsMessagePart) {
				TextMmsMessagePart textPart = (TextMmsMessagePart) part;
				text = textPart.toString();
				mmPart = FrontlineMultimediaMessagePart.createTextPart(textPart.getContent());
			} else if(part instanceof ImageMmsMessagePart) {
				ImageMmsMessagePart imagePart = (ImageMmsMessagePart) part;
				text = "Image: " + imagePart.getFilename();
				mmPart = createBinaryPart(imagePart);
			} else {
				text = "Unhandled: " + part.toString();
				mmPart = FrontlineMultimediaMessagePart.createTextPart("Unhandled: TODO handle this!");
			}
			textContent.append(text);
			multimediaParts.add(mmPart);
		}
		
		FrontlineMultimediaMessage message = new FrontlineMultimediaMessage(Type.RECEIVED, textContent.toString(), multimediaParts);
		message.setRecipientMsisdn("set me please"); // FIXME get recipient address from mms
		message.setSenderMsisdn(mms.getSender());
		message.setStatus(Status.RECEIVED);
		return message;
	}

	private FrontlineMultimediaMessagePart createBinaryPart(
			ImageMmsMessagePart imagePart) {
		// save the binary data to file
		FrontlineMultimediaMessagePart fmmPart = FrontlineMultimediaMessagePart.createBinaryPart(imagePart.getFilename());
		writeFile(getFile(fmmPart), imagePart.getData());
		return fmmPart; 
	}
	
	private static void writeFile(File file, byte[] data) {
		FileOutputStream fos = null;
		BufferedOutputStream out = null;
		try {
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file);
			out = new BufferedOutputStream(fos);
			out.write(data);
		} catch (IOException ex) {
			log.warn("Failed to write MMS file: " + file.getAbsolutePath(), ex);
			throw new RuntimeException(ex); // FIXME remove this
		} finally {
			if(out != null) try { out.close(); } catch(IOException ex) { /* ah well :/ */ }
			if(fos != null) try { fos.close(); } catch(IOException ex) { /* ah well :/ */ }
		}
	}

	public static File getFile(FrontlineMultimediaMessagePart part) {
		return new File(new File(ResourceUtils.getPropertiesDirectory(), "mms"), part.getFilename());
	}
}
