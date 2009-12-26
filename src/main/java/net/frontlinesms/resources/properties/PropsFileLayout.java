/**
 * 
 */
package net.frontlinesms.resources.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This assumes that all files are encoded using UTF-8.
 * @author alexanderson
 */
public class PropsFileLayout {
	private final List<PropsFileLine> lines;

	private PropsFileLayout(List<PropsFileLine> lines) {
		super();
		this.lines = lines;
	}
	
	/**
	 * Format given values in this layout.
	 * @param out the stream to print the formatted properties to
	 * @param values this is not modified
	 */
	public void format(PrintWriter out, Map<String, String> values, boolean printUnrequested) {
		Map<String, String> tempValues = new HashMap<String, String>();
		tempValues.putAll(values);
		values = tempValues;
		
		for(PropsFileLine line : this.lines) {
			if(line instanceof PropsFileEmptyLine) {
				out.println();
			} else if(line instanceof PropsFileCommentLine) {
				out.println(((PropsFileCommentLine) line).getLineContent());
			} else if(line instanceof PropsFileValueLine) {
				String key = ((PropsFileValueLine) line).getKey();
				String value = values.remove(key);
				String printVal;
				if(value != null) {
					values.remove(key);
					printVal = key + "=" + value;
				} else {
					printVal = "# " + key + "=";
				}
				out.println(printVal);
			} else throw new IllegalStateException("Unknown line type: " + line.getClass());
		}
	
		if(printUnrequested && values.size() > 0) {
			// Now print out any values that were NOT requested
			out.println();
			out.println();
			out.println("#####");
			for(Entry<String, String> entry : values.entrySet()) {
				out.println(entry.getKey() + "=" + entry.getValue());
			}
		}
	}
	
//> STATIC FACTORIES
	public static PropsFileLayout create(File file) throws IOException {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			reader = new BufferedReader(isr);
			String line;
			
			LinkedList<PropsFileLine> lines = new LinkedList<PropsFileLine>();
			while((line = reader.readLine()) != null) {
				lines.add(createPropsFileLine(line));
			}
			return new PropsFileLayout(lines);
		} finally {
			if(reader != null) try { reader.close(); } catch(IOException ex) {}
			if(isr != null) try { isr.close(); } catch(IOException ex) {}
			if(fis != null) try { fis.close(); } catch(IOException ex) {}
		}
	}

	private static PropsFileLine createPropsFileLine(String originalLine) {
		String trimmedLine = originalLine.trim();
		if(trimmedLine.length() == 0) {
			return new PropsFileEmptyLine();
		} else if(trimmedLine.charAt(0) == '#') {
			return new PropsFileCommentLine(originalLine);
		} else if(trimmedLine.indexOf('=') > 0) {
			return new PropsFileValueLine(trimmedLine.substring(0, trimmedLine.indexOf('=')));
		} else throw new IllegalStateException("Could not process line: " + originalLine);
	}
}
