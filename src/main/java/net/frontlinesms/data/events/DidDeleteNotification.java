package net.frontlinesms.data.events;

/**
 * This event is sent to the event bus when an entity
 * is deleted from the database
 * @author Dieterich
 *
 * @param <E> the class of the recently deleted entity
 */
public class DidDeleteNotification<E> extends DatabaseNotification<E> {

	public DidDeleteNotification(E object) {
		super(object);
	}

}
