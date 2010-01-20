package net.frontlinesms.ui.handler;

import net.frontlinesms.data.domain.Group;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class GroupSelecterDialog implements ThinletUiEventHandler, SingleGroupSelecterPanelOwner {
	private static final String XML_LAYOUT_GROUP_SELECTER_DIALOG = "/ui/core/util/dgGroupSelecter.xml";
	private UiGeneratorController ui;
	
	private SingleGroupSelecterDialogOwner owner;
	
	private GroupSelecterPanel selecter;
	private Object dialogComponent;
	
	public GroupSelecterDialog(UiGeneratorController ui, SingleGroupSelecterDialogOwner owner) {
		this.ui = ui;
		this.owner = owner;
	}
	
	public void init(String title, Group...rootGroups) {
		// TODO init
		dialogComponent = ui.loadComponentFromFile(XML_LAYOUT_GROUP_SELECTER_DIALOG, this);
		this.setTitle(title);
		
		this.selecter = new GroupSelecterPanel(ui, this);
		selecter.init(rootGroups);
		selecter.refresh();
		
		Object selecterPanel = selecter.getPanelComponent();
		ui.setColspan(selecterPanel, 2);
		ui.setWeight(selecterPanel, 1, 1);
		ui.add(dialogComponent, selecterPanel, 0);
	}

	private void setTitle(String title) {
		ui.setText(this.dialogComponent, title);
	}
	
	public void show() {
		ui.add(this.dialogComponent);
	}

	public void groupSelectionChanged(Group selectedGroup) {
		// ignore this
	}
	
//> UI EVENT METHODS
	public void done() {
		removeDialog();
		this.owner.groupSelectionCompleted(this.selecter.getSelectedGroup());
	}
	
	public void removeDialog() {
		ui.removeDialog(this.dialogComponent);
	}
}
