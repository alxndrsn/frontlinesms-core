package net.frontlinesms.data.events;

import net.frontlinesms.events.FrontlineEvent;

/**
 * A superclass for notifications involving database entities
 * @author Dieterich
 *
 * @param <E>
 */
public abstract class DatabaseNotification<E> extends FrontlineEvent{

	/**
	 * The object that this notification is about, which has presumably just been updated
	 */
	protected E databaseEntity;
	
	public DatabaseNotification(E databaseEntity){
		assert(databaseEntity.getClass().getAnnotation(javax.persistence.Entity.class) != null):
			   "Object is not a database entity";
		this.databaseEntity = databaseEntity;
	}
	
	public E getDatabaseEntity(){
		return databaseEntity;
	}
}
