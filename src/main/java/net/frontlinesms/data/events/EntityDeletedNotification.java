package net.frontlinesms.data.events;

/**
 * This event is sent to the event bus when an entity
 * is deleted from the database
 * @author Dieterich
 *
 * @param <E> the class of the recently deleted entity
 */
public class EntityDeletedNotification<E> extends DatabaseEntityNotification<E> {

	public EntityDeletedNotification(E object) {
		super(object);
	}

}
