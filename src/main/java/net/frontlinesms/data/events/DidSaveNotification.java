package net.frontlinesms.data.events;

/**
 * A notification that is fired immediately after an object is saved. Generics are used
 * so that this notification can be used for any class
 * @author Dieterich
 *
 * @param <E> the Class of the object that was saved
 */
public class DidSaveNotification<E> extends DatabaseNotification<E> {
	
	public DidSaveNotification(E object) {
		super(object);
	}

}
