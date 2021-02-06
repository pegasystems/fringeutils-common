/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractTreeTableNode extends DefaultMutableTreeNode
        implements Comparable<AbstractTreeTableNode> {

    private static final long serialVersionUID = -6580106011052007836L;

    public AbstractTreeTableNode() {
        super();
    }

    public AbstractTreeTableNode(Object userObject) {
        super(userObject);
    }

    public abstract String getNodeName();

    public abstract String getNodeValue(int column);

    public abstract Object[] getNodeElements();

}
