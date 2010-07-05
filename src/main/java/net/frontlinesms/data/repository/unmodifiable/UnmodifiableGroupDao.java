/**
 * 
 */
package net.frontlinesms.data.repository.unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.GroupDao;

/**
 * An implementation of {@link GroupDao} which does not allow modifications to the Groups.
 * This creates a snapshot of the {@link Group} tree at a moment in time which does not allow modification.
 * @author Alex
 */
public class UnmodifiableGroupDao implements GroupDao {
	private final TreeMap<String, Group> groups;
	private final List<Group> groupList;
	
//> CONSTRUCTOR
	/**
	 * Create a new view of the {@link Group} tree.
	 */
	public UnmodifiableGroupDao(GroupDao realGroupDao) {
		groups = new TreeMap<String, Group>();
		for(Group g : realGroupDao.getAllGroups()) {
			groups.put(g.getPath(), g);
		}
		
		groupList = new ArrayList<Group>();
		for(Group g : groups.values()) {
			groupList.add(g);
		}
	}
	
	public boolean hasDescendants(Group group) {
		return getChildGroups(group).size() > 0;
	}
	
	public int getGroupCount() {
		return groups.size();
	}
	
	public Group getGroupByPath(String path) {
		return groups.get(path);
	}
	
	public List<Group> getChildGroups(Group parent) {
		ArrayList<Group> kids = new ArrayList<Group>();
		String pathMatch = parent.getPath() + "/";
		for(Entry<String, Group> e : groups.entrySet()) {
			String path = e.getKey();
			if(path.startsWith(pathMatch)
					&& path.substring(pathMatch.length()).indexOf(Group.PATH_SEPARATOR)==-1)
				kids.add(e.getValue());
		}
		return kids;
	}
	
	public List<Group> getAllGroups(int startIndex, int limit) {
		return groupList.subList(startIndex, Math.min(limit, groupList.size()));
	}
	
	public List<Group> getAllGroups() {
		return groupList;
	}
	
//> ILLEGAL METHODS
	/** @deprecated */
	public void updateGroup(Group group) {
		throw new IllegalStateException();
	}
	/** @deprecated */
	public void saveGroup(Group group) throws DuplicateKeyException {
		throw new IllegalStateException();
	}
	/** @deprecated */
	public void deleteGroup(Group group, boolean destroyContacts) {
		throw new IllegalStateException();
	}
	/** @deprecated */
	public Group createGroupIfAbsent(String path) {
		throw new IllegalStateException();
	}
}
