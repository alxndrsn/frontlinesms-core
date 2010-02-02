/**
 * 
 */
package net.frontlinesms.data.repository.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;

/**
 * Hibernate implementation of {@link GroupDao}.
 * @author Alex
 */
public class HibernateGroupDao extends BaseHibernateDao<Group> implements GroupDao {
	/** Create instance of this class */
	public HibernateGroupDao() {
		super(Group.class);
	}

	/** @see GroupDao#deleteGroup(Group, boolean) */
	public void deleteGroup(Group group, boolean destroyContacts) {
		super.delete(group);
	}
	
	/** @see GroupDao#getAllGroups() */
	public List<Group> getAllGroups() {
		return super.getAll();
	}
	
	public boolean hasDescendants(Group parent) {
		return super.getCount(getChildCriteria(parent)) > 0;
	}
	
	/** @see GroupDao#getChildGroups(Group) */
	public Collection<Group> getChildGroups(Group parent) {
		return super.getList(getChildCriteria(parent));
	}

	/** @see GroupDao#getAllGroups(int, int) */
	public List<Group> getAllGroups(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
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
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.like(Group.Field.PATH.getFieldName(), parent.getPath() + Group.PATH_SEPARATOR + "%"));
		return criteria;
	}
}
