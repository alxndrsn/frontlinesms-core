/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation implying that this class contains i18n text keys.
 * @author Alex
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TextResourceKeyOwner {
	/** The prefix that all text keys start with in a particular {@link TextResourceKeyOwner}. */
	String[] prefix() default {"I18N_"};
}
