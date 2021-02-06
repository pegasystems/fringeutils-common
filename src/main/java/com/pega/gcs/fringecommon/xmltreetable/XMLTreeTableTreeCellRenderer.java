/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.Component;

import javax.swing.JTree;

import org.apache.commons.text.StringEscapeUtils;

import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTable;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTreeCellRenderer;

public class XMLTreeTableTreeCellRenderer extends DefaultTreeTableTreeCellRenderer {

    private static final long serialVersionUID = -942338087585748532L;

    public XMLTreeTableTreeCellRenderer(AbstractTreeTable treeTable) {
        super(treeTable);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {

        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        AbstractTreeTableNode abstractTreeTableNode = (AbstractTreeTableNode) value;
        String text = abstractTreeTableNode.getNodeName();

        XMLTreeTable xmlTreeTable = (XMLTreeTable) getTreeTable();

        if (xmlTreeTable.isUnescapeHtmlText()) {
            text = StringEscapeUtils.unescapeHtml4(text);
        }

        setText(text);

        return component;

    }

}
