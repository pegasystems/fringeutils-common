/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import com.pega.gcs.fringecommon.guiutilities.MyColor;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class XMLTreeTableTree extends DefaultTreeTableTree {

	private static final long serialVersionUID = -1702028340648975873L;

	public XMLTreeTableTree(XMLTreeTable xmlTreeTable, XMLTreeTableTreeModel xmlTreeTableTreeModel,
			XMLTreeTableTreeCellRenderer xmlTreeTableTreeCellRenderer) {
		super(xmlTreeTable, xmlTreeTableTreeModel, xmlTreeTableTreeCellRenderer);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		XMLNode xmlNode = (XMLNode) value;

		if (xmlNode != null) {

			super.getTableCellRendererComponent(table, xmlNode, isSelected, hasFocus, row, column);

			if (!table.isRowSelected(row)) {

				Color background = MyColor.LIGHTEST_LIGHT_GRAY;

				// first preference to 'seachfound' color then 'hasmessage'
				// color
				if (GeneralUtilities.any(xmlNode.getSecondarySearchFoundArray())) {
					background = MyColor.LIME;
				} else if (GeneralUtilities.any(xmlNode.getSecondaryParentSearchFoundArray())) {
					background = MyColor.LIGHT_LIME;
				} else if (GeneralUtilities.any(xmlNode.getSearchFoundArray())) {
					background = MyColor.LIGHT_YELLOW;
				} else if (GeneralUtilities.any(xmlNode.getParentSearchFoundArray())) {
					background = MyColor.LIGHTEST_YELLOW;
				} else if (GeneralUtilities.any(xmlNode.getHasMessageArray())) {
					background = Color.ORANGE;
				}

				setBackground(background);
			}

		} else {
			setBackground(Color.WHITE);
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

		return this;

	}

}
