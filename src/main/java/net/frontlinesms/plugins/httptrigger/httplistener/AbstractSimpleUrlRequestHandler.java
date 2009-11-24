/**
 * 
 */
package net.frontlinesms.plugins.httptrigger.httplistener;

/**
 * @author Alex
 */
public abstract class AbstractSimpleUrlRequestHandler implements SimpleUrlRequestHandler {
//> STATIC CONSTANTS

//> INSTANCE PROPERTIES
	private final String requestStart;

//> CONSTRUCTORS
	public AbstractSimpleUrlRequestHandler(String requestStart) {
		this.requestStart = requestStart;
	}

//> ACCESSORS

//> INSTANCE METHODS
	/** @see net.frontlinesms.plugins.httptrigger.httplistener.SimpleUrlRequestHandler#shouldHandle(java.lang.String) */
	public boolean shouldHandle(String requestUri) {
		return requestUri.startsWith(this.requestStart);
	}
	
	public boolean handle(String requestUri) {
		assert shouldHandle(requestUri) : "This URI should not be handled here.";
		return this.handle(this.getRequestParts(requestUri));
	}
	
	public abstract boolean handle(String[] requestParts);
	
	/**
	 * Splits a request into it's separate "directories", removing {@link #requestStart}.
	 * @param requestUri
	 * @return
	 */
	private String[] getRequestParts(String requestUri) {
		return requestUri.substring(this.requestStart.length()).split("\\/");
	}

//> STATIC FACTORIES

//> STATIC HELPER METHODS
}
