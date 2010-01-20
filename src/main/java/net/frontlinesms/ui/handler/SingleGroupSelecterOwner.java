/**
 * 
 */
package net.frontlinesms.ui.handler;

import net.frontlinesms.data.domain.Group;

/**
 * Owner of a {@link GroupSelecter} which allows selection of a single group.
 * @author aga
 */
public interface SingleGroupSelecterOwner extends GroupSelecterOwner {
	/** Method called when the selected group has changed. */
	void groupSelectionChanged(Group selectedGroup);
}
