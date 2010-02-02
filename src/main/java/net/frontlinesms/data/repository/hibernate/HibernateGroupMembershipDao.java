/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.domain.GroupMembership;
import net.frontlinesms.data.repository.GroupMembershipDao;

/**
 * @author aga
 *
 */
public class HibernateGroupMembershipDao extends BaseHibernateDao<GroupMembership> implements GroupMembershipDao {

	HibernateGroupMembershipDao() {
		super(GroupMembership.class);
	}
	
	/**
	 * @see GroupMembershipDao#addMembership(Group, Contact)
	 */
	public boolean addMembership(Group g, Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see GroupMembershipDao#getActiveMembers(Group)
	 */
	public List<Contact> getActiveMembers(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see GroupMembershipDao#getGroups(Contact)
	 */
	public List<Group> getGroups(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see GroupMembershipDao#getMemberCount(Group)
	 */
	public int getMemberCount(Group group) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see GroupMembershipDao#getMembers(Group)
	 */
	public List<Contact> getMembers(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see GroupMembershipDao#getMembers(Group, int, int)
	 */
	public List<Contact> getMembers(Group group, int startIndex, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see GroupMembershipDao#isMember(Group, Contact)
	 */
	public boolean isMember(Group group, Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see GroupMembershipDao#removeMembership(Group, Contact)
	 */
	public boolean removeMembership(Group g, Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}

}
