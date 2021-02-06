/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.xmltreetable;

import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTable;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableTreeModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableModelAdapter;

public class XMLTreeTable extends AbstractTreeTable {

    private static final long serialVersionUID = 8579465261755437998L;

    private boolean unescapeHtmlText;

    public XMLTreeTable(XMLTreeTableTreeModel treeTableModel) {
        super(treeTableModel, 20, 30);

        this.unescapeHtmlText = false;
    }

    public boolean isUnescapeHtmlText() {
        return unescapeHtmlText;
    }

    public void setUnescapeHtmlText(boolean unescapeHtmlText) {
        this.unescapeHtmlText = unescapeHtmlText;

        updateUI();
    }

    @Override
    protected TreeTableModelAdapter getTreeTableModelAdapter(DefaultTreeTableTree tree) {

        XMLTreeTableModelAdapter xmlTreeTableModelAdapter;
        xmlTreeTableModelAdapter = new XMLTreeTableModelAdapter(tree);

        return xmlTreeTableModelAdapter;
    }

    @Override
    protected DefaultTreeTableTree constructTree(AbstractTreeTableTreeModel abstractTreeTableModel) {

        XMLTreeTableTreeCellRenderer xmlTreeTableTreeCellRenderer;
        xmlTreeTableTreeCellRenderer = new XMLTreeTableTreeCellRenderer(this);

        XMLTreeTableTree xmlTreeTableTree = new XMLTreeTableTree(this, (XMLTreeTableTreeModel) abstractTreeTableModel,
                xmlTreeTableTreeCellRenderer);

        return xmlTreeTableTree;
    }

}
