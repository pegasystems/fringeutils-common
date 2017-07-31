/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.util.ArrayList;
import java.util.List;

public class XMLElementType {

	String elementName;

	// Separator for every level, for ex, may have semicolon separated on first,
	// then '=' for the second`
	List<String> seperatorListforLevel;

	boolean decodeHTML;

	public XMLElementType(String elementName, String firstLevelSeperator, boolean decodeHTML) {
		super();
		this.elementName = elementName;

		this.seperatorListforLevel = new ArrayList<String>();
		this.seperatorListforLevel.add(firstLevelSeperator);

		this.decodeHTML = decodeHTML;
	}

	public String getElementName() {
		return elementName;
	}

	// 0 based
	public String getSeperatorforLevel(int level) {
		return seperatorListforLevel.get(level);
	}

	public void setSeperatorforLevel(int level, String seperator) {
		seperatorListforLevel.set(level, seperator);
	}

	public boolean isDecodeHTML() {
		return decodeHTML;
	}

}
