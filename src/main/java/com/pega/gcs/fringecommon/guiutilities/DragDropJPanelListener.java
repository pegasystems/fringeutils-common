/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.io.File;
import java.util.List;

public interface DragDropJPanelListener {

    public abstract void filesDropped(List<File> files);

}
