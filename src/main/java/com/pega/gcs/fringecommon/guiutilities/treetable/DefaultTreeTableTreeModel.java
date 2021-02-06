/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

public class DefaultTreeTableTreeModel extends AbstractTreeTableTreeModel {

    private static final long serialVersionUID = -8113315733763386280L;

    private TreeTableColumn[] columns;

    public DefaultTreeTableTreeModel(AbstractTreeTableNode root, TreeTableColumn[] columns) {

        super(root);
        this.columns = columns;
    }

    public void setColumns(TreeTableColumn[] columns) {
        this.columns = columns;
        reload();
    }

    private TreeTableColumn[] getColumns() {
        return columns;
    }

    @Override
    public TreeTableColumn getColumn(int column) {
        return getColumns()[column];
    }

    @Override
    public int getColumnCount() {
        return getColumns().length;
    }

    @Override
    public String getColumnName(int column) {
        return getColumns()[column].getDisplayName();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return getColumns()[column].getColumnClass();
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return true;
    }

    @Override
    public Object getValueAt(Object node, int column) {
        // return the xml node back itself as it we need the whole metadata. the
        // rendering of value is taken care by the cell renderer for each
        // columns
        return node;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        // do nothing
    }
}
