/**
 * 
 */
package net.frontlinesms.ui.handler.contacts;

import net.frontlinesms.data.domain.Group;

/**
 * Owner of a {@link GroupSelecterPanel} which allows selection of a single group.
 * @author aga
 */
public interface SingleGroupSelecterPanelOwner extends GroupSelecterPanelOwner {
	/** Method called when the selected group has changed. */
	void groupSelectionChanged(Group selectedGroup);
}
