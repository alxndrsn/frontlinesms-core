/**
 * 
 */
package net.frontlinesms.data.repository;

import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;

/**
 * @author Alex
 */
public interface GroupMembershipDao {
	/**
	 * @param group
	 * @return all members of a group and its descendants
	 */
	public List<Contact> getMembers(Group group);
	public int getMemberCount(Group selectedGroup);
	public List<Contact> getMembers(Group selectedGroup, int startIndex, int limit);

	/**
	 * @param contact
	 * @return all groups this contact is a direct member of
	 */
	public List<Group> getGroups(Contact contact);

	/** Add a contact to a group */
	public void addMembership(Group g, Contact contact);

	/** Remove a contact from a group */
	public void removeMembership(Group g, Contact contact);

}
