/**
 * 
 */
package net.frontlinesms.plugins.translation;

import java.util.HashSet;
import java.util.Set;

import net.frontlinesms.ui.i18n.LanguageBundle;

/**
 * @author Alex alex@frontlinesms.com
 *
 */
public class LanguageBundleComparison {
	private final LanguageBundle bundle1;
	private final LanguageBundle bundle2;
	private Set<String> keysIn1Only;
	private Set<String> keysIn2Only;
	
	LanguageBundleComparison(LanguageBundle one, LanguageBundle two) {
		this.bundle1 = one;
		this.bundle2 = two;
	}

	synchronized Set<String> getKeysIn1Only() {
		if(this.keysIn1Only == null) {
			// Init set
			this.keysIn1Only = getLeftOnly(bundle1.getProperties().keySet(), bundle2.getProperties().keySet());
		}
		return this.keysIn1Only;
	}
	
	synchronized Set<String> getKeysIn2Only() {
		if(this.keysIn2Only == null) {
			// Init set
			this.keysIn2Only = getLeftOnly(bundle2.getProperties().keySet(), bundle1.getProperties().keySet());
		}
		return this.keysIn2Only;
	}

	private <T> Set<T> getLeftOnly(Set<T> left, Set<T> right) {
		final Set<T> ret = new HashSet<T>();
		for(T val : left) {
			if(!right.contains(val)) {
				ret.add(val);
			}
		}
		return ret;
	}

	String get1(String key) {
		return this.bundle1.getValue(key);
	}
	
	String get2(String key) {
		return this.bundle2.getValue(key);
	}
}
