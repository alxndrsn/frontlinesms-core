/**
 * 
 */
package net.frontlinesms.ui.handler.contacts;

import java.util.Collection;

import net.frontlinesms.data.domain.Group;

/**
 * Owner of a {@link GroupSelecterPanel} for simultaneously selecting many groups.
 * @author aga
 * @deprecated if anyone is actually using this, please say as otherwise it may very well get removed
 */
public interface MultiGroupSelecterPanelOwner extends GroupSelecterPanelOwner {
	/** Method called when the selected groups have changed. */
	void groupSelectionChanged(Collection<Group> selectedGroups);
}
