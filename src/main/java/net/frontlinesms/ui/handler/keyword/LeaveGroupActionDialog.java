/**
 * 
 */
package net.frontlinesms.ui.handler.keyword;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_AUTO_LEAVE_GROUP;
import static net.frontlinesms.FrontlineSMSConstants.COMMON_KEYWORD;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * @author aga
 */
public class LeaveGroupActionDialog extends BaseGroupActionDialog {
	
	LeaveGroupActionDialog(UiGeneratorController ui, KeywordTabHandler owner) {
		super(ui, owner);
	}
	
	@Override
	protected String getDialogTitle() {
		return InternationalisationUtils.getI18NString(COMMON_KEYWORD)
				+ " \"" + super.getTargetKeyword().getKeyword() + "\" "
				+ InternationalisationUtils.getI18NString(COMMON_AUTO_LEAVE_GROUP) + ":";
	}

	@Override
	public void save() {
		super.save(false);
	}
}
