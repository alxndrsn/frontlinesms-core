/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;

/**
 * Hibernate implementation of {@link GroupDao}.
 * @author Alex   Anderson <alex@frontlinesms.com>
 * @author Morgan Belkadi  <morgan@frontlinesms.com>
 */
public class HibernateGroupDao extends BaseHibernateDao<Group> implements GroupDao {
	/** Create instance of this class */
	public HibernateGroupDao() {
		super(Group.class);
	}

	/** @see GroupDao#deleteGroup(Group, boolean) */
	@Transactional
	public void deleteGroup(Group group, boolean destroyContacts) {
		// Dereference all keywordActions relating to this group
		String keywordActionQuery = "DELETE FROM KeywordAction WHERE group_path=?";
		super.getHibernateTemplate().bulkUpdate(keywordActionQuery, group.getPath());

		Object[] paramValues = getPathParamValues(group);
		
		if (destroyContacts) {
			// If the contacts must also be destroyed, we start by selecting them
			String queryString = "SELECT DISTINCT contact FROM GroupMembership WHERE group_path=? OR group_path LIKE ?";
			List<Contact> contactsList = getList(Contact.class, queryString, paramValues);
			
			// Then we delete all group memberships for the group and its descendants
			String groupMembershipQuery = "DELETE from GroupMembership WHERE group_path=? OR group_path LIKE ?";
			super.getHibernateTemplate().bulkUpdate(groupMembershipQuery, paramValues);
			
			// Then, for each contact...
			for (Contact c : contactsList) {
				// We remove it from each group it's included in
				groupMembershipQuery = "DELETE from GroupMembership WHERE contact=?";
				super.getHibernateTemplate().bulkUpdate(groupMembershipQuery, c);
				
				// And we delete the contact
				String deleteContactQuery = "DELETE FROM Contact WHERE id=?";
				super.getHibernateTemplate().bulkUpdate(deleteContactQuery, c.getId());	
			}
		} else {
			// We just delete all group memberships for the group and its descendants
			String groupMembershipQuery = "DELETE from GroupMembership WHERE group_path=? OR group_path LIKE ?";
			super.getHibernateTemplate().bulkUpdate(groupMembershipQuery, paramValues);
		}
		
		// Finally, we delete all child groups and the group itself
		DetachedCriteria criteria = super.getCriterion();
		Criterion equals = Restrictions.eq(Group.Field.PATH.getFieldName(), paramValues[0]);
		Criterion like = Restrictions.like(Group.Field.PATH.getFieldName(), paramValues[1].toString(), MatchMode.START);  
		criteria.add(Restrictions.or(equals, like));
		List<Group> groups = getList(criteria);
		for (Group deletedGroup : groups) {
			this.delete(deletedGroup);
		}
	}
	
	/** @return params for matching this group and its children */
	private Object[] getPathParamValues(Group group) {
		String groupPath = group.getPath();
		String childPath = groupPath + Group.PATH_SEPARATOR + "%";
		return new Object[]{groupPath, childPath};
	}
	
	/** @see GroupDao#getAllGroups() */
	public List<Group> getAllGroups() {
		DetachedCriteria criteria = super.getSortCriterion(Group.Field.PATH, Order.ASCENDING);
		return super.getList(criteria);
	}
	
	public boolean hasDescendants(Group parent) {
		return super.getCount(getChildCriteria(parent)) > 0;
	}
	
	/** @see GroupDao#getChildGroups(Group) */
	public List<Group> getChildGroups(Group parent) {
		return super.getList(getChildCriteria(parent));
	}

	/** @see GroupDao#getAllGroups(int, int) */
	public List<Group> getAllGroups(int startIndex, int limit) {
		DetachedCriteria criteria = super.getSortCriterion(Group.Field.PATH, Order.ASCENDING);
		return super.getList(criteria, startIndex, limit);
	}

	/** @see GroupDao#getGroupByPath(String) */
	public Group getGroupByPath(String path) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Group.Field.PATH.getFieldName(), path));
		return super.getUnique(criteria);
	}

	/** @see GroupDao#getGroupCount() */
	public int getGroupCount() {
		return super.countAll();
	}

	/** @see GroupDao#saveGroup(Group) */
	public void saveGroup(Group group) throws DuplicateKeyException {
		super.save(group);
	}

	/** @see GroupDao#updateGroup(Group) */
	public void updateGroup(Group group) {
		super.updateWithoutDuplicateHandling(group);
	}

	/** @return criteria for getting the children of a group */
	private DetachedCriteria getChildCriteria(Group parent) {
		DetachedCriteria criteria = super.getSortCriterion(Group.Field.PATH, Order.ASCENDING);
//		criteria.add(Restrictions.like(Group.Field.PATH.getFieldName(), parent.getPath() + Group.PATH_SEPARATOR + "[^" + Group.PATH_SEPARATOR + "]"));
		criteria.add(Restrictions.eq("parentPath", parent.getPath()));
		return criteria;
	}
	
	/**
	 * Gets a list of E matching the supplied HQL query.
	 * @param hqlQuery HQL query
	 * @param values values to insert into the HQL query
	 * @return a list of Es matching the supplied query
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> getList(Class<T> entityClass, String hqlQuery, Object... values) {
		return this.getHibernateTemplate().find(hqlQuery, values);
	}
}
