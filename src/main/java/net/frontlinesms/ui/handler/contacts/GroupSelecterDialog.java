package net.frontlinesms.ui.handler.contacts;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class GroupSelecterDialog implements ThinletUiEventHandler, SingleGroupSelecterPanelOwner {
	private static final String XML_LAYOUT_GROUP_SELECTER_DIALOG = "/ui/core/contacts/dgGroupSelecter.xml";
	private UiGeneratorController ui;
	
	private SingleGroupSelecterDialogOwner owner;
	
	private GroupSelecterPanel selecter;
	private Object dialogComponent;
	
	public GroupSelecterDialog(UiGeneratorController ui, SingleGroupSelecterDialogOwner owner) {
		this.ui = ui;
		this.owner = owner;
	}
	
	/**
	 * Init with default title
	 * @param rootGroups
	 */
	public void init(Group rootGroup) {
		init(InternationalisationUtils.getI18NString(FrontlineSMSConstants.COMMON_GROUP), rootGroup);
	}
	
	public void init(String title, Group rootGroup) {
		// TODO init
		dialogComponent = ui.loadComponentFromFile(XML_LAYOUT_GROUP_SELECTER_DIALOG, this);
		this.setTitle(title);
		
		this.selecter = new GroupSelecterPanel(ui, this);
		selecter.init(rootGroup);
		selecter.refresh();
		
		Object selecterPanel = selecter.getPanelComponent();
		ui.setColspan(selecterPanel, 2);
		ui.setWeight(selecterPanel, 1, 1);
		ui.add(dialogComponent, selecterPanel, 0);
		
		// Disable the DONE button until the user has selected something
		setDoneButtonEnabled(false);
	}

	private void setTitle(String title) {
		ui.setText(this.dialogComponent, title);
	}
	
	public void show() {
		ui.add(this.dialogComponent);
	}

	public void groupSelectionChanged(Group selectedGroup) {
		// Once a group is selected, we want to allow the DONE button to be clicked
		setDoneButtonEnabled(true);
	}

//> UI EVENT METHODS
	public void done() {
		removeDialog();
		this.owner.groupSelectionCompleted(this.selecter.getSelectedGroup());
	}
	
	public void removeDialog() {
		ui.removeDialog(this.dialogComponent);
	}
	
//> UI HELPER METHODS
	/** Enable or disable the DONE button */
	private void setDoneButtonEnabled(boolean enabled) {
		ui.setEnabled(ui.find(this.dialogComponent, "btGroupSelecterDone"), enabled);
	}
}
