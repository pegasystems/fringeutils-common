/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DefaultTreeTableTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 3344835502597758233L;

	private AbstractTreeTable treeTable;

	public DefaultTreeTableTreeCellRenderer(AbstractTreeTable treeTable) {
		super();
		this.treeTable = treeTable;
	}

	@Override
	public Color getBackgroundSelectionColor() {

		Color color = super.getBackgroundSelectionColor();

		if (treeTable != null) {
			color = treeTable.getSelectionBackground();

		}

		return color;
	}

	@Override
	public Color getBackgroundNonSelectionColor() {

		// Color color = super.getBackgroundNonSelectionColor();
		//
		// if (treeTable != null) {
		// color = treeTable.getBackground();
		//
		// }

		return null;
	}

	@Override
	public Color getBackground() {

		// Color color = super.getBackground();
		//
		// if (treeTable != null) {
		// color = treeTable.getBackground();
		//
		// }

		return null;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		final Component ret = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		// setBorder(BorderFactory.createLineBorder(Color.RED, 1));

		AbstractTreeTableNode abstractTreeTableNode = (AbstractTreeTableNode) value;
		String text = abstractTreeTableNode.getNodeName();

		setText(text);

		// setSize(getWidth(), treeTable.getRowHeight(row));
		// LOG.info("MyCellRenderer ret" + ret.getClass());
		return ret;
	}

}
