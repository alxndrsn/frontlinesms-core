/**
 * 
 */
package net.frontlinesms.data.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Alex
 */
@Entity
//@Table(uniqueConstraints=@UniqueConstraint(columnNames={Contact.COLUMN_ID, Group.COLUMN_PATH}))
public class GroupMembership {
	@SuppressWarnings("unused")
	@Id
	private long id;
	
	@ManyToOne()
	private Group group;
	@ManyToOne()
	private Contact contact;

	public GroupMembership(Group group, Contact contact) {
		this.group = group;
		this.contact = contact;
	}
}
