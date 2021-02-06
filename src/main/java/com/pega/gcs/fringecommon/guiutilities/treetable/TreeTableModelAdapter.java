/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.pega.gcs.fringecommon.guiutilities.CustomJTableModel;

public abstract class TreeTableModelAdapter extends CustomJTableModel implements TreeModelListener {

    private static final long serialVersionUID = -4711898270514748553L;

    private DefaultTreeTableTree tree;

    public abstract DefaultTableCellRenderer getTreeTableCellRenderer();

    public TreeTableModelAdapter(DefaultTreeTableTree tree) {

        super();

        this.tree = tree;

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) tree.getModel();
        abstractTreeTableTreeModel.addTreeModelListener(this);

        tree.addTreeExpansionListener(new TreeExpansionListener() {

            @Override
            public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
                fireTableDataChanged();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
                fireTableDataChanged();
            }
        });
    }

    public DefaultTreeTableTree getTree() {
        return tree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        return defaultTreeTableTree.getRowCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.getColumnCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.getColumnName(column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.getColumnClass(columnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.isCellEditable(nodeForRow(rowIndex), columnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.getValueAt(nodeForRow(rowIndex), columnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        abstractTreeTableTreeModel.setValueAt(value, nodeForRow(rowIndex), columnIndex);
    }

    public TreeTableColumn getColumn(int column) {

        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return abstractTreeTableTreeModel.getColumn(column);
    }

    private Object nodeForRow(int row) {

        Object node = null;

        if (row >= 0) {

            TreePath treePath = tree.getPathForRow(row);

            if (treePath != null) {
                node = treePath.getLastPathComponent();
            }
        }

        return node;
    }

    public DefaultMutableTreeNode getRoot() {
        DefaultTreeTableTree defaultTreeTableTree = getTree();

        AbstractTreeTableTreeModel abstractTreeTableTreeModel;
        abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

        return (DefaultMutableTreeNode) abstractTreeTableTreeModel.getRoot();
    }

    @Override
    public void treeNodesChanged(TreeModelEvent treeModelEvent) {
        fireTableDataChanged();

    }

    @Override
    public void treeNodesInserted(TreeModelEvent treeModelEvent) {
        fireTableDataChanged();

    }

    @Override
    public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
        fireTableDataChanged();

    }

    @Override
    public void treeStructureChanged(TreeModelEvent treeModelEvent) {

        fireTableDataChanged();

    }

    @Override
    public String getColumnValue(Object valueAtObject, int columnIndex) {

        AbstractTreeTableNode abstractTreeTableNode = (AbstractTreeTableNode) valueAtObject;

        String columnValue = abstractTreeTableNode.getNodeValue(columnIndex);

        return columnValue;
    }

    @Override
    public TableColumnModel getTableColumnModel() {

        TableColumnModel tableColumnModel = new DefaultTableColumnModel();

        for (int i = 0; i < getColumnCount(); i++) {

            TableColumn tableColumn = new TableColumn(i);

            TreeTableColumn treeTableColumn = getColumn(i);

            String text = treeTableColumn.getDisplayName();

            Class<?> columnClass = treeTableColumn.getColumnClass();

            int preferredWidth = treeTableColumn.getPrefColumnWidth();

            tableColumn.setHeaderValue(text);
            tableColumn.setPreferredWidth(preferredWidth);

            TableCellRenderer tcr = null;

            if (TreeTableColumn.TREE_COLUMN_CLASS.equals(columnClass)) {

                DefaultTreeTableTree treeTableTree = getTree();
                tcr = treeTableTree;
                tableColumn.setCellEditor(new TreeTableCellEditor(treeTableTree));
            } else {

                DefaultTableCellRenderer ltcr = getTreeTableCellRenderer();
                ltcr.setBorder(new EmptyBorder(1, 3, 1, 1));
                ltcr.setHorizontalAlignment(treeTableColumn.getHorizontalAlignment());

                tableColumn.setCellEditor(new TreeTableCellEditor(ltcr));

                tcr = ltcr;
            }

            tableColumn.setCellRenderer(tcr);

            tableColumn.setResizable(true);

            tableColumnModel.addColumn(tableColumn);
        }

        return tableColumnModel;
    }
}
