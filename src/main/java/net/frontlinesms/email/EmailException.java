/**
 * 
 */
package net.frontlinesms.email;

/**
 * Exception thrown by methods in net.frontlinesms.email to wrap third-party exceptions.
 * @author Alex Anderson <alex@frontlinesms.com>
 */
@SuppressWarnings("serial")
public class EmailException extends Exception {
	public EmailException(Throwable cause) {
		super(cause);
	}
}
