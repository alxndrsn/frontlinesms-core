/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

/**
 * Exception thrown when there was a problem validating a dialog field.
 * @author aga
 */
@SuppressWarnings("serial")
class DialogValidationException extends Exception {
	/** The warning to display to the user if the field could not be validated. */
	private final String userMessage;
	
	DialogValidationException(String message, String userMessage) {
		super(message);
		this.userMessage = userMessage;
	}
	
	DialogValidationException(String message, Exception cause, String userMessage) {
		super(message, cause);
		this.userMessage = userMessage;
	}
	
	public String getUserMessage() {
		return userMessage;
	}
}
