package net.frontlinesms.data.events;

/**
 * This notification is sent to the event bus when an object in the database
 * is updated
 * @author Dieterich
 *
 * @param <E> the class of the object that was updated
 */
public class EntityUpdatedNotification<E> extends DatabaseEntityNotification<E> {

	public EntityUpdatedNotification(E object) {
		super(object);
	}

}
