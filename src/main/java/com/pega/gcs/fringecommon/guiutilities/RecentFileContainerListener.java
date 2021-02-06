/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

public interface RecentFileContainerListener {

    public abstract void recentFileAdded();

    public abstract void recentFileDeleted();
}
