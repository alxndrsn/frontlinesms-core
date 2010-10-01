package net.frontlinesms.ui;

import net.frontlinesms.settings.FrontlineValidationMessage;

public interface UiSettingsSectionHandler {
	
	  /** 
	   * @return The Thinlet panel for a section 
	   **/
	  public Object getPanel();
	
	  /**
	   * Called for each {@link UiSettingsSectionHandler} when the settings are saved 
	   **/
	  public void save();
	  
	  /**
	   * @return <code>null</code> if every field in the current panel has been validated,
	   * otherwise an internationalized validation message.
	   */
	  public FrontlineValidationMessage validateFields();
}
