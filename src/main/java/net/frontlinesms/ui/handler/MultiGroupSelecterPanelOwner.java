/**
 * 
 */
package net.frontlinesms.ui.handler;

import java.util.Collection;

import net.frontlinesms.data.domain.Group;

/**
 * Owner of a {@link GroupSelecterPanel} for simultaneously selecting many groups.
 * @author aga
 */
public interface MultiGroupSelecterPanelOwner extends GroupSelecterPanelOwner {
	/** Method called when the selected groups have changed. */
	void groupSelectionChanged(Collection<Group> selectedGroups);
}
