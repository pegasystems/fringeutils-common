/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class TableCompareEntry {

	private int startEntry;

	private int endEntry;

	public TableCompareEntry(int startEntry, int endEntry) {

		super();

		this.startEntry = startEntry;
		this.endEntry = endEntry;
	}

	public int getStartEntry() {
		return startEntry;
	}

	public int getEndEntry() {
		return endEntry;
	}

}
