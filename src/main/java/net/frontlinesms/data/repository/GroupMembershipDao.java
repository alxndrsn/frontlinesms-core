/**
 * 
 */
package net.frontlinesms.data.repository;

import java.util.List;

import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.Contact.Field;

/**
 * @author Alex
 */
public interface GroupMembershipDao {
	/** @return all members of a group and its descendants */
	public List<Contact> getMembers(Group group);
	
	/** @return active members of a group */
	public List<Contact> getActiveMembers(Group group);
	
	/** @return count of all members of a group and its subgroups */
	public int getMemberCount(Group group);
	
	/** @return all members of a group and its subgroup, paged. */
	public List<Contact> getMembers(Group group, int startIndex, int limit);

	/** @return all groups this contact is a <b>direct</b> member of */
	public List<Group> getGroups(Contact contact);
	
	/** @return Count of all members of the supplied group and its subgroups whose name or
	 * phone number matches the <code>filterString</code> */
	public int getFilteredMemberCount(final Group group, String contactFilterString);
	
	/** @return all members of the supplied group and its subgroups whose name or
	 * phone number matches the <code>filterString</code> */
	public List<Contact> getFilteredMembers(Group group, String filterString, int startIndex, int limit);

	/** @return Count of all members of the supplied group and its subgroups whose name or
	 * phone number matches the <code>filterString</code>, sorted by the order asked */
	public List<Contact> getFilteredMembersSorted(final Group group, String contactFilterString, Field sortBy, Order order, int startIndex, int limit);
	
	/** 
	 * Add a contact to a group
	 * @return <code>true</code> if the contact was added to the group, <code>false</code> if he was already a member
	 */
	public boolean addMember(Group group, Contact contact);

	/**
	 * Remove a contact from a group
	 * @return <code>true</code> if the contact was removed from the group, <code>false</code> if he was not a member
	 */
	public boolean removeMember(Group group, Contact contact);

	/** @return <code>true</code> if the contact is a member of the group, <code>false</code> otherwise */
	public boolean isMember(Group group, Contact contact);
}
