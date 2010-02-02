/**
 * 
 */
package net.frontlinesms.data.domain;

import javax.persistence.OneToMany;

/**
 * @author Alex
 */
public class GroupMembership {
	@OneToMany(mappedBy=Group.COLUMN_PATH)
	private Group group;
	@OneToMany(mappedBy=Contact.COLUMN_ID)
	private Contact contact;
}
