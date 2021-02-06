/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.tree.DefaultTreeModel;

public abstract class AbstractTreeTableTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = -2393329881143075328L;

    public AbstractTreeTableTreeModel(AbstractTreeTableNode root) {
        super(root);
    }

    public abstract int getColumnCount();

    public abstract String getColumnName(int column);

    public abstract Class<?> getColumnClass(int column);

    public abstract Object getValueAt(Object node, int column);

    public abstract boolean isCellEditable(Object node, int column);

    public abstract void setValueAt(Object value, Object node, int column);

    public abstract TreeTableColumn getColumn(int column);
}
