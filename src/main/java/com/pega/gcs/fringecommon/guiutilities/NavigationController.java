/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public interface NavigationController<T> {

	public void navigateToRow(int startRowIndex, int endRowIndex);

	public void scrollToKey(T key);
}
