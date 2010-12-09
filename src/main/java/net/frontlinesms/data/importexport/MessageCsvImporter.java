/**
 * 
 */
package net.frontlinesms.data.importexport;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.log4j.Logger;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.data.importexport.MessageCsvImportReport.MessageCsvImportReportState;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.ui.i18n.FileLanguageBundle;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.LanguageBundle;

/**
 * @author aga
 *
 */
public class MessageCsvImporter extends CsvImporter {
	
//> INSTANCE PROPERTIES
	private final Logger LOG = FrontlineUtils.getLogger(this.getClass());

//> CONSTRUCTORS
	public MessageCsvImporter(File importFile) throws CsvParseException {
		super(importFile);
	}

//> IMPORT METHODS
	/**
	 * Import messages from a CSV file.
	 * @param importFile the file to import from
	 * @param messageDao
	 * @param rowFormat 
	 * @throws IOException If there was a problem accessing the file
	 * @throws CsvParseException If there was a problem with the format of the file
	 */
	public MessageCsvImportReport importMessages(MessageDao messageDao, CsvRowFormat rowFormat) throws CsvParseException {
		LOG.trace("ENTER");
		
		int multimediaMessageCount = 0;
		LanguageBundle usedLanguageBundle = null;
			
		for(String[] lineValues : super.getRawValues()) {
			String typeString = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_MESSAGE_TYPE);
			String status = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_MESSAGE_STATUS);
			
			Status statusType;
			try {
				statusType = Status.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException ex) {
				return new MessageCsvImportReport(MessageCsvImportReportState.FAILURE);
			}
			
			String sender = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_SENDER_NUMBER);
			String recipient = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_RECIPIENT_NUMBER);
			String dateString = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_MESSAGE_DATE);
			String content = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_MESSAGE_CONTENT);
			
			long date;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				ParsePosition pos = new ParsePosition(0);
				date = formatter.parse(dateString, pos).getTime();
			} catch (Exception e) {
				date = System.currentTimeMillis(); // TODO is this really what we want to do with an ill-formatted date?
			}
			
			FrontlineMessage message;
			
			// To avoid checking the language bandle used everytime, we store it 
			if (usedLanguageBundle == null) {
				usedLanguageBundle = getUsedLanguageBundle(typeString);
			}
			Type type = getTypeFromString(typeString, usedLanguageBundle); // FIXME what if language bundle is still null??
			//Status status = getStatusFromString(statusString);
			
			if (FrontlineMultimediaMessage.appearsToBeToString(content)) {
				// Then it's a multimedia message
				message = FrontlineMultimediaMessage.createMessageFromContentString(content, false);
				message.setDate(date);
				message.setSenderMsisdn(sender);
				message.setRecipientMsisdn(recipient);
				++multimediaMessageCount;
			} else {
				if (type.equals(Type.OUTBOUND)) {
					message = FrontlineMessage.createOutgoingMessage(date, sender, recipient, content);
				} else {
					message = FrontlineMessage.createIncomingMessage(date, sender, recipient, content);
				}
			}
			
			message.setStatus(statusType);
			messageDao.saveMessage(message);
		}
		
		LOG.trace("EXIT");
		return new MessageCsvImportReport(multimediaMessageCount);
	}

//> HELPER METHODS

//> STATIC HELPER METHODS
	public static Type getTypeFromString(String typeString, LanguageBundle languageBundle) {
		if (typeString.equalsIgnoreCase(InternationalisationUtils.getI18nString(FrontlineSMSConstants.COMMON_SENT, languageBundle))) {
			return Type.OUTBOUND;
		} else if (typeString.equalsIgnoreCase(InternationalisationUtils.getI18nString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
			return Type.RECEIVED;
		}
		
		return Type.UNKNOWN;
	}
	
	/** Attempt to match the Language bundle used for message types to a particular language bundle */
	public static LanguageBundle getUsedLanguageBundle(String typeString) {
		Collection<FileLanguageBundle> languageBundles = InternationalisationUtils.getLanguageBundles();
		for (FileLanguageBundle languageBundle : languageBundles) {
			if (typeString.equalsIgnoreCase(InternationalisationUtils.getI18nString(FrontlineSMSConstants.COMMON_SENT, languageBundle))
					|| typeString.equalsIgnoreCase(InternationalisationUtils.getI18nString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
				return languageBundle;
			}
		}

		return null;
	}
}
