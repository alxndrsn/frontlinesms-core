/**
 * 
 */
package net.frontlinesms.ui.handler;

import java.util.Collection;

import net.frontlinesms.data.domain.Group;

/**
 * Owner of a {@link GroupSelecter} for simultaneously selecting many groups.
 * @author aga
 */
public interface MultiGroupSelecterOwner extends GroupSelecterOwner {
	/** Method called when the selected groups have changed. */
	void groupSelectionChanged(Collection<Group> selectedGroups);
}
