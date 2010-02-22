package org.smslib.util;

public interface GsmCharacterTable {
	/** @return the National Language Identifier for this language */
	int getNli();
	/** @return the character represented by the supplied byte value, or TODO define the default value if no character is defined */
	char getCharacter(int byteValue);
}
