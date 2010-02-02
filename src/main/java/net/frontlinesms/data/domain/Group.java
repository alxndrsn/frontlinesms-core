/*
 * FrontlineSMS <http://www.frontlinesms.com>
 * Copyright 2007, 2008 kiwanja
 * 
 * This file is part of FrontlineSMS.
 * 
 * FrontlineSMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 * 
 * FrontlineSMS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FrontlineSMS. If not, see <http://www.gnu.org/licenses/>.
 */
package net.frontlinesms.data.domain;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;

/**
 * Object representing a named group of contacts.  A group can contain sub-groups
 * whose membership is entirely independent of the main group.
 * @author Alex
 */
@Entity(name="frontline_group")
public class Group {

//> DATABASE COLUMN NAMES
	/** Database column name for property: {@link #name} */
	static final String COLUMN_PATH = "path";
	public static final char PATH_SEPARATOR = '/';

//> ENTITY FIELDS
	/** Details of the fields that this class has. */
	public enum Field implements EntityField<Group> {
		/** Represents {@link #path} */
		PATH(COLUMN_PATH);
		
		/** name of a field */
		private final String fieldName;
		/**
		 * Creates a new {@link Field}
		 * @param fieldName name of the field
		 */
		Field(String fieldName) { this.fieldName = fieldName; }
		/** @see EntityField#getFieldName() */
		public String getFieldName() { return this.fieldName; }
	}
	
//> PROPERTIES
	/** The path of this group. */
	@Column(name=COLUMN_PATH, unique=true, updatable=false, nullable=false)
	private String path;
	
//> CONSTRUCTORS
	/** Empty constructor for hibernate */
	Group() {}
	
	private Group(String path) {
		assert(path.startsWith("" + PATH_SEPARATOR)) : "Path must start with the seprator character '" + PATH_SEPARATOR + "'";
		assert(path.indexOf(',') == -1) : "Comma illegal in group name.";
	}
	
	/**
	 * Creates a group with the specified parent and name.
	 * @param parent The parent of the group to be created.
	 * @param name The name of the new group.
	 */
	public Group(Group parent, String name) {
		if(name.indexOf(PATH_SEPARATOR) != -1) 
			throw new IllegalArgumentException("Group names cannot contain the path separator character '" + PATH_SEPARATOR + "'");
		if(name.indexOf(',') != -1)
			throw new IllegalArgumentException("Comma character not valid in group name.");
		
		this.path = parent.getPath() + PATH_SEPARATOR + name;
	}
	
//> ACCESSOR METHODS
	/** @return the name of this group */
	public String getName() {
		return this.path.substring(this.path.lastIndexOf(PATH_SEPARATOR));
	}

	/** @return the path of this group */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Returns the parent of this group. 
	 * @return {@link #parent}
	 */
	public Group getParent() {
		if(this.isRoot()) {
			return null;
		} else {
			return new Group(this.path.substring(0, this.path.lastIndexOf(PATH_SEPARATOR)));
		}
	}
	
	/** @return <code>true</code> if this is the root group */
	private boolean isRoot() {
		return this.path.equals("" + PATH_SEPARATOR);
	}

//> GENERATED CODE
	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}
	
	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}	
	
}
