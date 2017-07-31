/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class FilterTableModelNavigation<T> {

	private int navigationIndex;

	private int navigationRowIndex;

	// will be used to find range of records
	private T navigationKey;

	public FilterTableModelNavigation() {

		navigationIndex = 0;
		navigationRowIndex = 0;
	}

	public int getNavigationIndex() {
		return navigationIndex;
	}

	public void setNavigationIndex(int navigationIndex) {
		this.navigationIndex = navigationIndex;
	}

	public int getNavigationRowIndex() {
		return navigationRowIndex;
	}

	public void setNavigationRowIndex(int navigationRowIndex) {
		this.navigationRowIndex = navigationRowIndex;
	}

	public T getNavigationKey() {
		return navigationKey;
	}

	public void setNavigationKey(T navigationKey) {
		this.navigationKey = navigationKey;
	}

}
