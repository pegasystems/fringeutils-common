/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.markerbar;

import java.util.EventListener;
import java.util.EventObject;

public interface MarkerModelListener extends EventListener {

	public void modelDataChanged(EventObject e);
}
