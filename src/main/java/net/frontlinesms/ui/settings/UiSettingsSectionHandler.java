package net.frontlinesms.ui.settings;

import java.util.List;

import net.frontlinesms.settings.FrontlineValidationMessage;

public interface UiSettingsSectionHandler {
	
	  /** 
	   * @param section 
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
	  public List<FrontlineValidationMessage> validateFields();
	  
//	  /**
//	   * Used to notify the main settings handler that a change has been made in its panel.
//	   */
//	  public void settingsChanged();
}
