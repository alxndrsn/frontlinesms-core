/*
 * Copyright (C) 2008 Andrea Zito
 * 
 * This file is part of jMmsLib.
 *
 * jMmsLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or  (at your option) any later version.
 *
 * jMmsLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jMmsLib.  If not, see <http://www.gnu.org/licenses/>.
 */ 
package net.sourceforge.jmmslib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Defines a part of the MMS message.<br>
 * <p>Actually part can contain only text and images.</p>
 * <p>
 * 	Here a brief example on how to use the class.
 * 	<pre>
 * 	  MmsMessage mms = new MmsMessage();
 * 	  //fill the fields...
 * 
 * 	  MmsPart p = new MmsPart();
 * 	  p.setPartContent(new String("Hello World").getBytes("US-ASCII"));
 *    p.setPartContentType(MmsMessage.CTYPE_TEXT_PLAIN);
 *
 *    mms.addPart(p);
 * 	</pre>
 * </p>
 * @author Andrea Zito
 *
 */
public class MmsPart {
	/** Identifier of the part*/
	private String partId;
	
	/** Part data */
	private byte[] partContent;
	
	/** Part content type*/
	private String partType;
	
	/** Charset of the text component */
	private String partCharset;
	
	
	/** 
	 * Creates an MmsPart object 
	 */
	public MmsPart(){
		this.partId = null;
		this.partContent = null;
		this.partType = MmsMessage.CTYPE_UNKNOWN;
		this.partCharset = MmsMessage.CHARSET_US_ASCII;
	}
	
	/**
	 * Sets the part identifier.
	 * @param id part identifier
	 */
	public void setPartId(String id){
		this.partId = id;
	}
	
	/**
	 * Sets the part data.
	 * @param buffer part data buffer
	 */
	public void setPartContent(byte buffer[]){
		partContent = buffer;
	}
	
	/**
	 * Sets the part data from a file.
	 * @param f part data file
	 * @throws FileNotFoundException 
	 * @throws MmsContentException part data too big
	 */
	public void setPartContent(File f) throws FileNotFoundException, MmsContentException{
		RandomAccessFile file = new RandomAccessFile(f, "r");
		
		byte buf[] = null;
		try{
			long fileSize = file.length();
		
			if (fileSize > Integer.MAX_VALUE)
				throw new MmsContentException("MMS Part content file ("+ f +") too big.");
		
			buf = new byte[(int)fileSize];
			file.read(buf);
		}catch(IOException e){}
		
		setPartContent(buf);
	}
	
	/**
	 * Sets the part data from a file name.
	 * This implementation calls setPartContent(new File(filePath));
	 * @param filePath path of the part data file
	 * @throws FileNotFoundException
	 * @throws MmsContentException
	 */
	public void setPartContent(String filePath) throws FileNotFoundException, MmsContentException{
		setPartContent(new File(filePath));
	}
	
	/**
	 * Sets the part content type.
	 * @param contentType part content type
	 */
	public void setPartContentType(String contentType){
		this.partType = contentType;
	}
	
	/**
	 * Returns the part identifier.
	 * @return part identifier
	 */
	public String getPartId(){
		if (this.partId == null) return "";
		else return this.partId;
	}
	
	/**
	 * Returns the buffer containing the part data.
	 * @return part data buffer
	 */
	public byte[] getPartContent(){
		return this.partContent;
	}
	
	/**
	 * Returns the part content type.
	 * @return part content type
	 */
	public String getPartContentType(){
		return this.partType;
	}
	
	/**
	 * Returns the charset for the content.
	 * Valid only in case of text/ content type.
	 * @return charset
	 */
	public String getPartCharset(){
		return this.partCharset;
	}
	
	/**
	 * Sets the charset for the content.
	 * Valid only in case of text/ content type.
	 * @param charset charset
	 */
	public void setPartCharset(String charset){
		this.partCharset = charset;
	}
}
