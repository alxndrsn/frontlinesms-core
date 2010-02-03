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
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true, updatable=false)
	private long id;
	@ManyToOne(optional=false)
	private Group group;
	@ManyToOne(optional=false)
	private Contact contact;

	public GroupMembership(Group group, Contact contact) {
		this.group = group;
		this.contact = contact;
	}
}
