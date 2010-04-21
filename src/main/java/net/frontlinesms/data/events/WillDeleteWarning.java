package net.frontlinesms.data.events;

/**
 * A notification that is fired immediately before an object is deleted from the database. 
 * Generics are used so that this notification can be used for any type
 * @author Dieterich
 *
 * @param <E> the class of the object that will be deleted
 */
public class WillDeleteWarning<E> extends DatabaseNotification<E> {

	public WillDeleteWarning(E object){
		super(object);
	}
	
}
