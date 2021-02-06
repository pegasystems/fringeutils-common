/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.jtree;

import javax.swing.tree.DefaultMutableTreeNode;

public interface Filter {

    public String getKey();

    public boolean pass(DefaultMutableTreeNode node);

    public String filterRepresentation(Object node);

}
