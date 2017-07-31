/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTable;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableTreeModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTreeCellRenderer;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableCellEditor;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableColumn;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableModelAdapter;

public class XMLTreeTable extends AbstractTreeTable {

	private static final long serialVersionUID = 8579465261755437998L;

	public XMLTreeTable(XMLTreeTableTreeModel treeTableModel) {

		super(treeTableModel, 20, 30);

		// setBackground(MyColor.LIGHTEST_GRAY);

	}

	@Override
	protected TreeTableModelAdapter getTreeTableModelAdapter(DefaultTreeTableTree tree) {

		XMLTreeTableModelAdapter xmlTreeTableModelAdapter;
		xmlTreeTableModelAdapter = new XMLTreeTableModelAdapter(tree);

		return xmlTreeTableModelAdapter;
	}

	@Override
	protected DefaultTreeTableTree constructTree(AbstractTreeTableTreeModel abstractTreeTableModel) {

		DefaultTreeTableTreeCellRenderer defaultTreeTableTreeCellRenderer;

		defaultTreeTableTreeCellRenderer = new DefaultTreeTableTreeCellRenderer(this);
		// defaultTreeTableTreeCellRenderer.setOpenIcon(null);
		// defaultTreeTableTreeCellRenderer.setClosedIcon(null);
		// defaultTreeTableTreeCellRenderer.setLeafIcon(null);

		XMLTreeTableTree xmlTreeTableTree = new XMLTreeTableTree(this, (XMLTreeTableTreeModel) abstractTreeTableModel,
				defaultTreeTableTreeCellRenderer);

		// xmlTreeTableTree.setRootVisible(false);
		// xmlTreeTableTree.setShowsRootHandles(true);

		return xmlTreeTableTree;
	}

	@Override
	protected void setTreeTableColumnModel() {

		TreeTableModelAdapter model = (TreeTableModelAdapter) getModel();
		TableColumnModel tableColumnModel = new DefaultTableColumnModel();

		for (int i = 0; i < model.getColumnCount(); i++) {

			TableColumn tableColumn = new TableColumn(i);

			String text = model.getColumnName(i);

			tableColumn.setHeaderValue(text);

			Class<?> columnClass = model.getColumnClass(i);

			TableCellRenderer tcr = null;

			if (TreeTableColumn.TREE_COLUMN_CLASS.equals(columnClass)) {

				DefaultTreeTableTree treeTableTree = getTree();
				tcr = treeTableTree;
				tableColumn.setCellEditor(new TreeTableCellEditor(treeTableTree));
			} else {

				XMLLabelTableCellRenderer ltcr = new XMLLabelTableCellRenderer();
				ltcr.setBorder(new EmptyBorder(1, 3, 1, 1));

				tableColumn.setCellEditor(new TreeTableCellEditor(ltcr));

				tcr = ltcr;
			}

			// ttcr.setHorizontalAlignment(ttmc.getHorizontalAlignment());

			tableColumn.setCellRenderer(tcr);

			// int colWidth = (int) ((ttmc.getPrefColumnWidthPercent() *
			// tableWidth) / 100);
			// tableColumn.setPreferredWidth(colWidth);
			// tableColumn.setMinWidth(colWidth);
			// tableColumn.setWidth(colWidth);
			tableColumn.setResizable(true);

			tableColumnModel.addColumn(tableColumn);
		}

		setColumnModel(tableColumnModel);
	}

	public void setUnescapeHTMLText(boolean unescapeHTMLText) {

		XMLTreeTableTree xmlTreeTableTree = (XMLTreeTableTree) getTree();
		xmlTreeTableTree.setUnescapeHTMLText(unescapeHTMLText);

		updateUI();

	}
}
