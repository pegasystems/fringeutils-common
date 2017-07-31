/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TreeTableModelAdapter extends AbstractTableModel implements TreeModelListener {

	private static final long serialVersionUID = -4711898270514748553L;

	DefaultTreeTableTree tree;

	/**
	 * @param tree
	 * @param abstractTreeTableModel
	 */
	public TreeTableModelAdapter(DefaultTreeTableTree tree) {

		super();

		this.tree = tree;

		AbstractTreeTableTreeModel abstractTreeTableTreeModel;
		abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) tree.getModel();
		abstractTreeTableTreeModel.addTreeModelListener(this);

		tree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				fireTableDataChanged();
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
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
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		DefaultTreeTableTree defaultTreeTableTree = getTree();

		AbstractTreeTableTreeModel abstractTreeTableTreeModel;
		abstractTreeTableTreeModel = (AbstractTreeTableTreeModel) defaultTreeTableTree.getModel();

		abstractTreeTableTreeModel.setValueAt(aValue, nodeForRow(rowIndex), columnIndex);
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
	public void treeNodesChanged(TreeModelEvent e) {
		fireTableDataChanged();

	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		fireTableDataChanged();

	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		fireTableDataChanged();

	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {

		fireTableDataChanged();

	}
}
