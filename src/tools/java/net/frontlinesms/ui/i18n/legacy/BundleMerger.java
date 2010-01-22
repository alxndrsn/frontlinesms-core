/**
 * 
 */
package net.frontlinesms.ui.i18n.legacy;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import net.frontlinesms.ui.i18n.InternationalisationUtils;

/**
 * Compares two translation files, and reports on translations which are unique to one file or
 * the other, and translations on which the two files disagree.
 * @author aga
 */
public class BundleMerger {
	
//> INSTANCE PROPERTIES
	/** Translations in the primary bundle */
	private final Map<String, String> primary;
	/** Translations in the secondary bundle */
	private final Map<String, String> secondary;

//> CONSTRUCTORS
	/** Creates a new BundleMerger to merge the two supplied bundles. */
	public BundleMerger(Map<String, String> primary, Map<String, String> secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

//> ACCESSORS
	/** @return keys and values that are not disputed between the primary and secondary sources */
	public Map<String, String> getUndisputed() {
		Map<String, String> undisputed = new HashMap<String, String>();
		for(Entry<String, String> entry : this.primary.entrySet()) {
			String key = entry.getKey();
			String primaryValue = entry.getValue();
			if(!this.secondary.containsKey(key)
					|| primaryValue.equals(this.secondary.get(key))) {
				undisputed.put(key, primaryValue);
			}
		}
		return undisputed;
	}

	/** @return keys and values that are attributed different values in the primary and secondary sources */
	public Map<String, StringPair> getDisputed() {
		Map<String, StringPair> disputed = new HashMap<String, StringPair>();
		for(Entry<String, String> entry : this.primary.entrySet()) {
			String key = entry.getKey();
			if(this.secondary.containsKey(key)) {
				String primaryValue = entry.getValue();
				String secondaryValue = this.secondary.get(key);
				if(!primaryValue.equals(secondaryValue)) {
					disputed.put(key, new StringPair(primaryValue, secondaryValue));
				}
			}
		}
		return disputed;
	}

	/** @return entries only found in {@link #secondary} */
	public Map<String, String> getSecondaryOnly() {
		return getLeftOnly(this.secondary, this.primary);
	}
	
	/** @return entries only found in {@link #primary} */
	public Map<String, String> getPrimaryOnly() {
		return getLeftOnly(this.primary, this.secondary);
	}

//> STATIC HELPER METHODS
	/** @return map containing entries whose keys were only found in the lefthand of the two maps supplied */
	private static Map<String, String> getLeftOnly(Map<String, String> left, Map<String, String> right) {
		Map<String, String> filtered = new HashMap<String, String>();
		filtered.putAll(left);
		for(String rightKey : right.keySet()) filtered.remove(rightKey);
		return filtered;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static Map<String, String> loadTextResource(String path) throws IOException {
		return InternationalisationUtils.loadTextResources(path, new FileInputStream(path));
	}
	
//> MAIN METHOD
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String primaryPath = args[0];
		String secondaryPath = args[1];

		Map<String, String> primary = loadTextResource(primaryPath);
		Map<String, String> secondary = loadTextResource(secondaryPath);
		BundleMerger merger = new BundleMerger(primary, secondary);
		
		new MergeReporter(System.out).report(merger);
	}
}

/**
 * Generate reports about a {@link BundleMerger}
 * @author aga
 */
class MergeReporter {
	private final PrintStream out;
	
	public MergeReporter(PrintStream out) {
		super();
		this.out = out;
	}

	public void report(BundleMerger merger) {
		Map<String, String> undisputed = merger.getUndisputed();
		Map<String, StringPair> disputed = merger.getDisputed();

		hr();
		ln("Report begins...");
		hr();
		ln("Undisputed: " + undisputed.size());
		ln("\tPrimary only: " + merger.getPrimaryOnly().size());
		ln("\tSecondary only: " + merger.getSecondaryOnly().size());
		for(Entry<String, String> entry : merger.getSecondaryOnly().entrySet()) {
			ln("\t\t" + entry.getKey() + "=" + entry.getValue());
		}
		hr();
		ln("Disputed: " + disputed.size());
		for(Entry<String, StringPair> entry : disputed.entrySet()) {
			StringPair value = entry.getValue();
			ln(entry.getKey());
			ln("\t" + value.getPrimary());
			ln("\t" + value.getSecondary());
		}
		hr();
		ln("...report ends.");
		hr();
	}
	
	/** Print a string to {@link #out} */
	private void p(Object s) {
		out.print(s.toString());
	}
	/** Print a blank line to {@link #out} */
	private void ln() {
		out.println();
	}
	/** Print a horizontal rule to {@link #out} */
	private void hr() {
		ln("----------");
	}
	/** Print a string to {@link #out}, followed by a line break */
	private void ln(Object s) {
		out.println(s.toString());
	}
}

/**
 * Pair of disputed strings in a {@link BundleMerger}.
 * @author aga
 */
class StringPair {
	private final String primary;
	private final String secondary;
	public StringPair(String primary, String secondary) {
		super();
		this.primary = primary;
		this.secondary = secondary;
	}
	public String getPrimary() {
		return primary;
	}
	public String getSecondary() {
		return secondary;
	}
}