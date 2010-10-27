/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.csv;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.domain.FrontlineMultimediaMessage;
import net.frontlinesms.data.domain.FrontlineMessage.Status;
import net.frontlinesms.data.domain.FrontlineMessage.Type;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.ui.i18n.FileLanguageBundle;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.LanguageBundle;

import org.apache.log4j.Logger;

/**
 * This file contains methods for importing data to the FrontlineSMS service
 * from CSV files.
 * 
 * FIXME display a meaningful message if import fails!
 * 
 * @author Carlos Eduardo Genz 
 * <li> kadu(at)masabi(dot)com
 * @author Alex Anderson 
 * <li> alex(at)masabi(dot)com
 */
public class CsvImporter {
	/** Logging object */
	private static Logger LOG = FrontlineUtils.getLogger(CsvImporter.class); 
	
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\"; 
	
	/**
	 * Import contacts from a CSV file.
	 * @param importFile the file to import from
	 * @param contactDao
	 * @param rowFormat 
	 * @throws IOException If there was a problem accessing the file
	 * @throws CsvParseException If there was a problem with the format of the file
	 */
	public static void importContacts(File importFile, ContactDao contactDao, GroupMembershipDao groupMembershipDao, GroupDao groupDao, CsvRowFormat rowFormat) throws IOException, CsvParseException {
		LOG.trace("ENTER");
		if(LOG.isDebugEnabled()) LOG.debug("File [" + importFile.getAbsolutePath() + "]");
		Utf8FileReader reader = null;
		try {
			reader = new Utf8FileReader(importFile);
			boolean firstLine = true;
			String[] lineValues;
			while((lineValues = CsvUtils.readLine(reader)) != null) {
				if(firstLine) {
					// Ignore the first line of the CSV file as it should be the column titles
					firstLine = false;
				} else {
					String name = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_NAME);
					String number = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_PHONE);
					String email = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_EMAIL);
					String notes = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_NOTES);
					String otherPhoneNumber = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_OTHER_PHONE);
					String groups = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_GROUPS);
					
					String statusString = getString(lineValues, rowFormat, CsvUtils.MARKER_CONTACT_STATUS).toLowerCase();
					boolean active = !"false".equals(statusString) && !"dormant".equals(statusString);
					
					Contact c = new Contact(name, number, otherPhoneNumber, email, notes, active);						
					try {
						contactDao.saveContact(c);
					} catch (DuplicateKeyException e) {
						// FIXME should actually pass details of this back to the user.
						LOG.debug("Contact already exist with this number [" + number + "]", e);
						// If the contact already existed, let's reach the existing one to fill the groupMembership
						c = contactDao.getFromMsisdn(number);
					}
					
					// We make the contact joins its groups
					String[] pathList = groups.split(GROUPS_DELIMITER);
					for (String path : pathList) {
						if (path.length() == 0) continue;
						
						if (!path.startsWith(String.valueOf(Group.PATH_SEPARATOR))) {
							path = Group.PATH_SEPARATOR + path;
						}
						
						Group group = createGroups(groupDao, path);
						groupMembershipDao.addMember(group, c);
					}
				}
			}
		} finally {
			if (reader != null) reader.close();
		}
		LOG.trace("EXIT");
	}

	/**
	 * Import messages from a CSV file.
	 * @param importFile the file to import from
	 * @param messageDao
	 * @param rowFormat 
	 * @throws IOException If there was a problem accessing the file
	 * @throws CsvParseException If there was a problem with the format of the file
	 */
	public static void importMessages(File importFile, MessageDao messageDao, CsvRowFormat rowFormat) throws IOException, CsvParseException {
		LOG.trace("ENTER");
		if(LOG.isDebugEnabled()) LOG.debug("File [" + importFile.getAbsolutePath() + "]");
		Utf8FileReader reader = null;
		try {
			reader = new Utf8FileReader(importFile);
			boolean firstLine = true;
			String[] lineValues;
			LanguageBundle usedLanguageBundle = null;
			while((lineValues = CsvUtils.readLine(reader)) != null) {
				if(firstLine) {
					// Ignore the first line of the CSV file as it should be the column titles
					firstLine = false;
				} else {
					String typeString = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_TYPE);
					String status = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_STATUS);
					String sender = getString(lineValues, rowFormat, CsvUtils.MARKER_SENDER_NUMBER);
					String recipient = getString(lineValues, rowFormat, CsvUtils.MARKER_RECIPIENT_NUMBER);
					String dateString = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_DATE);
					String content = getString(lineValues, rowFormat, CsvUtils.MARKER_MESSAGE_CONTENT);
					
					long date;
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						ParsePosition pos = new ParsePosition(0);
						date = formatter.parse(dateString, pos).getTime();
					} catch (Exception e) {
						date = System.currentTimeMillis();
					}
					
					FrontlineMessage message;
					
					// To avoid checking the language bandle used everytime, we store it 
					if (usedLanguageBundle == null) {
						usedLanguageBundle = getUsedLanguageBundle(typeString);
					}
					Type type = getTypeFromString(typeString, usedLanguageBundle);
					//Status status = getStatusFromString(statusString);
					
					if (content.startsWith("\"") && content.contains("File:")) {
						// Then it's a multimedia message
						message = new FrontlineMultimediaMessage(type, "", content, null);
					} else {
						if (type.equals(Type.OUTBOUND)) {
							message = FrontlineMessage.createOutgoingMessage(date, sender, recipient, content);
						} else {
							message = FrontlineMessage.createIncomingMessage(date, sender, recipient, content);
						}
					}
					
					message.setStatus(Status.valueOf(status.toUpperCase()));
					messageDao.saveMessage(message);
				}
			}
		} finally {
			if (reader != null) reader.close();
		}
		LOG.trace("EXIT");
	}

	public static LanguageBundle getUsedLanguageBundle(String typeString) {
		Collection<FileLanguageBundle> languageBundles = InternationalisationUtils.getLanguageBundles();
		for (FileLanguageBundle languageBundle : languageBundles) {
			if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_SENT, languageBundle))
					|| typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
				return languageBundle;
			}
		}

		return null;
	}
	
	public static Type getTypeFromString(String typeString, LanguageBundle languageBundle) {
		if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_SENT, languageBundle))) {
			return Type.OUTBOUND;
		} else if (typeString.equals(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_RECEIVED, languageBundle))) {
			return Type.RECEIVED;
		}
		
		return Type.UNKNOWN;
	}

	/**
	 * Import contacts from a CSV file.
	 * @param filename the file to import from
	 * @param rowFormat 
	 * @throws IOException If there was a problem accessing the file
	 * @throws CsvParseException If there was a problem with the format of the file
	 */
	public static List<String[]> getValuesFromCsvFile(String filename) throws IOException, CsvParseException {
		LOG.trace("ENTER");
		File importFile = new File(filename);
		List<String[]> valuesList = new ArrayList<String[]>();
		
		if(LOG.isDebugEnabled()) LOG.debug("File [" + importFile.getAbsolutePath() + "]");
		Utf8FileReader reader = null;
		try {
			reader = new Utf8FileReader(importFile);
			boolean firstLine = true;
			String[] lineValues;
			while((lineValues = CsvUtils.readLine(reader)) != null) {
				if(firstLine) {
					firstLine = false;
				} else {
					valuesList.add(lineValues);
				}
			}
		} finally {
			if (reader != null) reader.close();
		}
		
		LOG.trace("EXIT");
		return valuesList;
	}

//> STATIC HELPER METHODS	
	/**
	 * Gets the string from a particular index of an array.  If the array is not long
	 * enough to contain that index, returns an empty string.
	 * @param values
	 * @param index
	 * @return The value in the specified index of the array, or an empty string if the array index is out of bounds.
	 */
	private static String getString(String[] values, int index) {
		assert(index >= 0) : "Supplied array index must be greater than or equal to zero.";
		if(values.length > index) {
			return values[index];
		} else return "";
	}
	
	/**
	 * Gets an optional String from the supplied String array, returning "" if the string is not available.
	 * @param values The values of the row of CSV
	 * @param rowFormat The format of the row we are importing
	 * @param marker The marker we are looking for in the row format
	 * @return The value in the specified index of the array, or an empty string if the array index is out of bounds.
	 */
	private static String getString(String[] values, CsvRowFormat rowFormat, String marker) {
		Integer index = rowFormat.getIndex(marker);
		if(index == null) return "";
		else return getString(values, index);
	}

	/**
	 * Creates the group and all parent groups for a supplied path.
	 * The behaviour of this method is undefined if a group is deleted externally while this method
	 * is executing.
	 * @param groupDao
	 * @param path
	 * @return
	 */
	static Group createGroups(GroupDao groupDao, String path) {
		if (path.length() == 0) {
			return new Group(null, null);
		} else {
			int pos = path.lastIndexOf(Group.PATH_SEPARATOR);
			if (pos == -1) pos = 0;
			
			Group parent = createGroups(groupDao, path.substring(0, pos));
			path = path.substring(pos, path.length());
			if (path.startsWith(String.valueOf(Group.PATH_SEPARATOR))) {
				path = path.substring(1, path.length());
			}
			
			Group group = new Group(parent, path);
			try {
				groupDao.saveGroup(group);
			} catch (DuplicateKeyException ex) {
				// It's not a problem if this group already exists
			}
			
			return group;
		}
	}
}
