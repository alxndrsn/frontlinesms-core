package net.frontlinesms.encoding;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 *
 */
public class EncodingTool {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES

//> CONSTRUCTORS

//> INSTANCE METHODS

//> ACCESSORS

//> INSTANCE HELPER METHODS

	public static void convertToUnicode(String source) {
		for (char c : source.toCharArray()) {
			System.err.println("'" + c + "' --> \\u" + Integer.toString(c, 16));
		}
	}
//> STATIC FACTORIES

//> STATIC HELPER METHODS
	public static void main (String[] args) {
		EncodingTool.convertToUnicode("руб.");
	}
}
