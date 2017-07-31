/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.search;

import java.util.EventObject;

public class SearchModelEvent<T> extends EventObject {

	private static final long serialVersionUID = -4764936505735398191L;

	// type of event,
	public static final int RESET_MODEL = -1;

	public static final int NAVIGATION = 0;

	private int type;

	private T navigationKey;

	public SearchModelEvent(SearchModel<T> source, int type, T navigationKey) {
		super(source);
		this.type = type;
		this.navigationKey = navigationKey;
	}

	public int getType() {
		return type;
	}

	public T getNavigationKey() {
		return navigationKey;
	}

}
