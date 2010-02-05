/**
 * 
 */
package net.frontlinesms.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Alex
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"contact_contact_id", "group_path"}))
public class GroupMembership {
	@SuppressWarnings("unused")
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true, updatable=false)
	private long id;
	@ManyToOne(optional=false)
	private Group group;
	@ManyToOne(optional=false)
	private Contact contact;

//> CONSTRUCTORS
	/** Empty constructor for Hibernate */
	public GroupMembership() {}
	
	public GroupMembership(Group group, Contact contact) {
		this.group = group;
		this.contact = contact;
	}

//> GENERATED CODE
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupMembership other = (GroupMembership) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		return true;
	}
}
