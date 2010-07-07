/**
 * 
 */
package net.frontlinesms.ui.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A properties file, with values accessible, but retaining the structure in the file. 
 * @author Alex
 */
public class VerbatimPropertiesFile implements Cloneable {
	private final List<String> lines;
	
	private VerbatimPropertiesFile(List<String> lines) {
		this.lines = lines;
	}
	
	protected VerbatimPropertiesFile clone() {
		return new VerbatimPropertiesFile(new ArrayList<String>(this.lines));
	}

	public Set<String> getKeys() {
		HashSet<String> keys = new HashSet<String>();
		for(String line : lines) {
			line = line.trim();
			if(hasValue(line)) {
				keys.add(getKey(line));
			}
		}
		return keys;
	}
	
	private String getLine(String key) {
		for(String line : lines) {
			if(hasValue(line) && key.equals(getKey(line))) {
				return line;
			}
		}
		throw new KeyNotFoundException(key);
	}

	public void setValue(String key, String value) {
		String line = getLine(key);
		this.lines.set(lines.indexOf(line), key + "=" + value);
	}

	public void commentOut(String key) {
		String line = getLine(key);
		this.lines.set(lines.indexOf(line), "#" + line);
	}
	
//> STATIC UTILITY METHODS
	private static boolean hasValue(String line) {
		line = line.trim();
		return line.length() > 0 && line.charAt(0) != '#';
	}
	
	private static String getValue(String line) {
		return line.substring(line.indexOf('='));
	}
	
	private static String getKey(String line) {
		return line.substring(0, line.indexOf('='));
	}
	
//> IO METHODS
	public static VerbatimPropertiesFile create(InputStream is) {
		BufferedReader in = null;
		LinkedList<String> lines = new LinkedList<String>();
		try {
			in = new BufferedReader(new InputStreamReader(is, InternationalisationUtils.CHARSET_UTF8));
			String line;
			while((line = in.readLine()) != null) {
				lines.add(line);
			}
			return new VerbatimPropertiesFile(lines);
		} catch (IOException ex) {
			throw new IllegalStateException("Unhandled problem reading stream.", ex);
		} finally {
			if(in != null) try { in.close(); } catch(IOException ex) {}
		}
	}

	public static VerbatimPropertiesFile create(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return create(fis);
		} catch (FileNotFoundException ex) {
			throw new IllegalStateException("Unhandled problem reading stream.", ex);
		} finally {
			if(fis != null) try { fis.close(); } catch(IOException ex) {}
		}
	}
	
	public static void saveToFile(File file, VerbatimPropertiesFile vpf) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		PrintWriter out = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, InternationalisationUtils.CHARSET_UTF8);
			out = new PrintWriter(osw);
			for(String line : vpf.lines) {
				out.write(line + "\n");
			}
		} finally {
			if(out != null) out.close();
			if(osw != null) try { osw.close(); } catch(IOException ex) {}
			if(fos != null) try { fos.close(); } catch(IOException ex) {}
		}		
	}
}
