/**
 * 
 */
package net.frontlinesms.data.repository.unmodifiable;

import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupMembershipDao;

/**
 * @author aga
 *
 */
public class UnmodifiableGroupMembershipDao implements GroupMembershipDao {

	public UnmodifiableGroupMembershipDao(GroupMembershipDao groupMembershipDao) {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#addMember(net.frontlinesms.data.domain.Group, net.frontlinesms.data.domain.Contact)
	 */
	public boolean addMember(Group group, Contact contact) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#getActiveMembers(net.frontlinesms.data.domain.Group)
	 */
	public List<Contact> getActiveMembers(Group group) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#getGroups(net.frontlinesms.data.domain.Contact)
	 */
	public List<Group> getGroups(Contact contact) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#getMemberCount(net.frontlinesms.data.domain.Group)
	 */
	public int getMemberCount(Group group) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#getMembers(net.frontlinesms.data.domain.Group)
	 */
	public List<Contact> getMembers(Group group) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#getMembers(net.frontlinesms.data.domain.Group, int, int)
	 */
	public List<Contact> getMembers(Group group, int startIndex, int limit) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#isMember(net.frontlinesms.data.domain.Group, net.frontlinesms.data.domain.Contact)
	 */
	public boolean isMember(Group group, Contact contact) {
		throw new IllegalArgumentException();
	}

	/** 
	 * @see net.frontlinesms.data.repository.GroupMembershipDao#removeMember(net.frontlinesms.data.domain.Group, net.frontlinesms.data.domain.Contact)
	 */
	public boolean removeMember(Group group, Contact contact) {
		throw new IllegalArgumentException();
	}

}
