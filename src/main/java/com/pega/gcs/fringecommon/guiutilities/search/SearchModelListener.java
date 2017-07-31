/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.search;

import java.util.EventListener;

public interface SearchModelListener<T> extends EventListener {

	public void searchResultChanged(SearchModelEvent<T> searchModelEvent);
}
