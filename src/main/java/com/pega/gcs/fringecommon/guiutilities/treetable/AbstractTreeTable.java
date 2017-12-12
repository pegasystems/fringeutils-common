/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.pega.gcs.fringecommon.guiutilities.CustomJTable;

public abstract class AbstractTreeTable extends CustomJTable {

	private static final long serialVersionUID = 567268590479849300L;

	private DefaultTreeTableTree tree;

	protected abstract DefaultTreeTableTree constructTree(AbstractTreeTableTreeModel abstractTreeTableTreeModel);

	protected TreeTableModelAdapter getTreeTableModelAdapter(DefaultTreeTableTree tree) {

		return new TreeTableModelAdapter(tree);
	}

	public AbstractTreeTable(final AbstractTreeTableTreeModel abstractTreeTableTreeModel, int rowHeight,
			final int headerHeight) {

		tree = constructTree(abstractTreeTableTreeModel);

		TableModel tableModel = getTreeTableModelAdapter(tree);

		// tableModel.addTableModelListener(new TableModelListener() {
		//
		// @Override
		// public void tableChanged(TableModelEvent e) {
		//
		// // only updating the tree when a full model is updated. updating
		// // when performing row inserted need to be handled specifically
		// if (e.getType() == TableModelEvent.UPDATE) {
		//// abstractTreeTableTreeModel.reload();
		// LOG.info("AbstractTreeTable: UPDATE called");
		// }
		// }
		// });

		setAutoCreateColumnsFromModel(false);

		setModel(tableModel);

		setTreeTableColumnModel();

		// ListSelectionModel tableListSelectionModel = getSelectionModel();

		// tableListSelectionModel
		// .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TreeTableSelectionModel selectionModel = new TreeTableSelectionModel(tree);

		// add a selection listener to the tree event for updating the table
		selectionModel.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TreePath treePath = e.getPath();
				// AbstractTreeTableNode node = (AbstractTreeTableNode) treePath
				// .getLastPathComponent();

				// Object userObject = node.getUserObject();

				// LOG.info("TreeSelectionListener: " + e);
			}
		});

		tree.setSelectionModel(selectionModel);

		setSelectionModel(selectionModel.getListSelectionModel());

		setTableHeader(new JTableHeader(getColumnModel()) {

			private static final long serialVersionUID = 4853598300124836115L;

			{
				setReorderingAllowed(false);
				setResizingAllowed(true);
			}

			@Override
			public TableCellRenderer getDefaultRenderer() {
				TableCellRenderer tcr = super.getDefaultRenderer();

				((DefaultTableCellRenderer) tcr).setHorizontalAlignment(SwingConstants.CENTER);
				return tcr;
			}

			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = headerHeight;
				return d;
			}

			@Override
			public Font getFont() {
				Font existingFont = AbstractTreeTable.this.getFont();
				String existingFontName = existingFont.getName();
				int existFontSize = existingFont.getSize();
				Font newFont = new Font(existingFontName, Font.BOLD, existFontSize);

				return newFont;
			}

		});

		setTransferHandler(new TransferHandler() {

			private static final long serialVersionUID = 3100522454592832897L;

			@Override
			protected Transferable createTransferable(JComponent c) {

				int[] selectedRows = AbstractTreeTable.this.getSelectedRows();

				StringBuffer dataSB = new StringBuffer();

				if (selectedRows != null) {

					for (int selectedRow : selectedRows) {

						AbstractTreeTableNode xmlNode = (AbstractTreeTableNode) AbstractTreeTable.this
								.getValueAt(selectedRow, 0);

						dataSB.append(xmlNode.getNodeName());
						dataSB.append("\t");

						int size = xmlNode.getNodeElements().length;

						for (int i = 0; i < size; i++) {

							dataSB.append(xmlNode.getNodeValue(i));
							dataSB.append("\t");
						}

						dataSB.append(System.getProperty("line.separator"));
					}

				}
				return new StringSelection(dataSB.toString());
			}

			@Override
			public int getSourceActions(JComponent c) {
				return TransferHandler.COPY;
			}

		});

		if (rowHeight > 0) {
			setRowHeight(rowHeight);
			tree.setRowHeight(rowHeight);
		}

		// setup the side arrows to expand and collapse tree
		final KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
		final KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);

		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(leftKeyStroke, leftKeyStroke);

		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(rightKeyStroke, rightKeyStroke);

		getActionMap().put(leftKeyStroke, new AbstractAction() {

			private static final long serialVersionUID = 3842190208048171536L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTree tree = getTree();

				InputMap map = tree.getInputMap(WHEN_FOCUSED);
				ActionMap am = tree.getActionMap();

				if (map != null && am != null && isEnabled()) {
					Object binding = map.get(leftKeyStroke);
					Action action = (binding == null) ? null : am.get(binding);
					if (action != null) {

						KeyEvent ke = new KeyEvent(tree, KeyEvent.KEY_PRESSED, e.getWhen(), 0, KeyEvent.VK_LEFT,
								KeyEvent.CHAR_UNDEFINED);

						SwingUtilities.notifyAction(action, leftKeyStroke, ke, tree, e.getModifiers());
					}
				}
			}
		});

		getActionMap().put(rightKeyStroke, new AbstractAction() {

			private static final long serialVersionUID = -5850337082346665414L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTree tree = getTree();

				InputMap map = tree.getInputMap(WHEN_FOCUSED);
				ActionMap am = tree.getActionMap();

				if (map != null && am != null && isEnabled()) {
					Object binding = map.get(rightKeyStroke);
					Action action = (binding == null) ? null : am.get(binding);
					if (action != null) {

						KeyEvent ke = new KeyEvent(tree, KeyEvent.KEY_PRESSED, e.getWhen(), 0, KeyEvent.VK_RIGHT,
								KeyEvent.CHAR_UNDEFINED);

						SwingUtilities.notifyAction(action, rightKeyStroke, ke, tree, e.getModifiers());
					}
				}
			}
		});

	}

	public DefaultTreeTableTree getTree() {
		return tree;
	}

	protected void setTreeTableColumnModel() {

		TreeTableModelAdapter model = (TreeTableModelAdapter) getModel();
		TableColumnModel tableColumnModel = new DefaultTableColumnModel();

		for (int i = 0; i < model.getColumnCount(); i++) {

			TableColumn tableColumn = new TableColumn(i);

			TreeTableColumn treeTableColumn = model.getColumn(i);

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

				LabelTableCellRenderer ltcr = new LabelTableCellRenderer();
				ltcr.setBorder(new EmptyBorder(1, 3, 1, 1));
				ltcr.setHorizontalAlignment(treeTableColumn.getHorizontalAlignment());

				tableColumn.setCellEditor(new TreeTableCellEditor(ltcr));

				tcr = ltcr;
			}

			tableColumn.setCellRenderer(tcr);

			tableColumn.setResizable(true);

			tableColumnModel.addColumn(tableColumn);
		}

		setColumnModel(tableColumnModel);
	}

	public void expandAll(boolean expand) {

		DefaultTreeTableTree treeTableTree = getTree();

		Object rootNode = treeTableTree.getModel().getRoot();

		TreePath rootPath = new TreePath(rootNode);

		expandAll(rootPath, expand, 0);
	}

	@SuppressWarnings("rawtypes")
	public void expandAll(TreePath parent, boolean expand, int level) {

		DefaultTreeTableTree treeTableTree = getTree();

		AbstractTreeTableNode node = (AbstractTreeTableNode) parent.getLastPathComponent();

		if (node.getChildCount() > 0) {

			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode childNode = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(childNode);

				expandAll(path, expand, level + 1);
			}

			// restricting expand/collapse to 1 level onwards
			if (level > 0) {
				// Expansion or collapse must be done bottom-up
				if (expand) {
					treeTableTree.expandPath(parent);
				} else {
					treeTableTree.collapsePath(parent);
				}
			}
		}
	}

	public void selectTreeNodeTableRow(AbstractTreeTableNode abstractTreeTableNode) {

		TreeTableModelAdapter treeTableModelAdapter;

		treeTableModelAdapter = (TreeTableModelAdapter) getModel();

		int rowIndex = -1;
		int rowCount = treeTableModelAdapter.getRowCount();

		for (int i = 0; i < rowCount; i++) {

			Object obj = treeTableModelAdapter.getValueAt(i, 0);

			if (obj != null) {

				if (abstractTreeTableNode.equals(obj)) {
					rowIndex = i;
					break;
				}
			}
		}

		if (rowIndex != -1) {
			setRowSelectionInterval(rowIndex, rowIndex);
			scrollRowToVisible(rowIndex);
		}
	}

	public void scrollNodeToVisible(AbstractTreeTableNode abstractTreeTableNode) {

		DefaultTreeTableTree treeTableTree = getTree();

		TreePath treePath = new TreePath(abstractTreeTableNode.getPath());

		treeTableTree.setSelectionPath(treePath);
		// treeTableTree.scrollPathToVisible(treePath);
		selectTreeNodeTableRow(abstractTreeTableNode);
	}

}
