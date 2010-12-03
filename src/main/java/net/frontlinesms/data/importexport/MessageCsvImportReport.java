/**
 * 
 */
package net.frontlinesms.data.importexport;

import net.frontlinesms.csv.CsvImportReport;

/**
 * @author aga
 *
 */
public class MessageCsvImportReport extends CsvImportReport {
	private int multimediaMessageCount;

	public MessageCsvImportReport(int multimediaMessageCount) {
		this.multimediaMessageCount = multimediaMessageCount;
	}

	public int getMultimediaMessageCount() {
		return this.multimediaMessageCount;
	}

}
