package net.frontlinesms.messaging.mms;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessagePart;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.mms.MmsMultimediaPart;
import net.frontlinesms.mms.MmsMessage;
import net.frontlinesms.mms.MmsMessagePart;
import net.frontlinesms.mms.TextMmsMessagePart;
import net.frontlinesms.mms.email.pop.EmailMmsParser;
import net.frontlinesms.mms.email.pop.parser.AbstractMmsParser;
import net.frontlinesms.mms.email.pop.parser.GenericMmsParser;
import net.frontlinesms.mms.email.pop.parser.ru.BeelineRuMmsParser;
import net.frontlinesms.mms.email.pop.parser.ru.MegafonproRuMmsParser;
import net.frontlinesms.mms.email.pop.parser.uk.O2UkMmsParser;
import net.frontlinesms.mms.email.pop.parser.uk.OrangeUkMmsParser;
import net.frontlinesms.mms.email.pop.parser.uk.ThreeUkMmsParser;
import net.frontlinesms.mms.email.pop.parser.uk.TmobileUkMmsParser;
import net.frontlinesms.mms.email.pop.parser.uk.VodafoneUkMmsParser;
import net.frontlinesms.mms.email.pop.parser.us.AttUsMmsParser;
import net.frontlinesms.resources.ResourceUtils;

public class MmsUtils {
	
	private static final Logger log = FrontlineUtils.getLogger(MmsUtils.class);
	
	
	/** Create a new {@link FrontlineMultimediaMessage} from a {@link MmsMessage} */
	public static FrontlineMultimediaMessage create(MmsMessage mms) {
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
			} else if(part instanceof MmsMultimediaPart) {
				MmsMultimediaPart imagePart = (MmsMultimediaPart) part;
				text = "Image: " + imagePart.getFilename();
				mmPart = createBinaryPart(imagePart);
			} else {
				text = "Unhandled: " + part.toString();
				mmPart = FrontlineMultimediaMessagePart.createTextPart("Unhandled part!");
			}
			textContent.append(text);
			multimediaParts.add(mmPart);
		}
		
		FrontlineMultimediaMessage message = new FrontlineMultimediaMessage(Type.RECEIVED, mms.getSubject(), textContent.toString(), multimediaParts);
		message.setRecipientMsisdn(mms.getReceiver());
		message.setSenderMsisdn(mms.getSender());
		message.setStatus(Status.RECEIVED);
		if (mms.getDate() != null) {
			message.setDate(mms.getDate().getTime());	
		}
		
		return message;
	}

	private static FrontlineMultimediaMessagePart createBinaryPart(
			MmsMultimediaPart imagePart) {
		// save the binary data to file
		FrontlineMultimediaMessagePart fmmPart = FrontlineMultimediaMessagePart.createBinaryPart(imagePart.getFilename()/*, getThumbnail(imagePart)*/);

		File localFile = getFile(fmmPart);
		while(localFile.exists()) {
			// need to handle file collisions here - e.g. rename the file
			fmmPart.setFilename(getAlternateFilename(fmmPart.getFilename()));
			localFile = getFile(fmmPart);
		}
		writeFile(localFile, imagePart.getData());
		return fmmPart; 
	}
	
	private static String getAlternateFilename(String filename) {
		String namePart = FrontlineUtils.getFilenameWithoutAnyExtension(filename);
		String extension = FrontlineUtils.getWholeFileExtension(filename);
		return namePart + '_' + new Random().nextInt(99) + '.' + extension;
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
		} finally {
			if(out != null) try { out.close(); } catch(IOException ex) { /* ah well :/ */ }
			if(fos != null) try { fos.close(); } catch(IOException ex) { /* ah well :/ */ }
		}
	}
	
	public static File getFile(FrontlineMultimediaMessagePart part) {
		return new File(new File(ResourceUtils.getConfigDirectoryPath(), "data/mms"), part.getFilename());
	}
	
	public static List<EmailMmsParser> getAllEmailMmsParsers () {
		return Arrays.asList(new EmailMmsParser[]{
				new ThreeUkMmsParser(),
				new TmobileUkMmsParser(),
				new VodafoneUkMmsParser(),
				new AttUsMmsParser(),
				
				new GenericMmsParser(),
				});
	}
}
