/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.SwingConstants;

import com.pega.gcs.fringecommon.guiutilities.DefaultTableColumn;

public class TreeTableColumn extends DefaultTableColumn {

    public static final Class<DefaultTreeTableTree> TREE_COLUMN_CLASS = DefaultTreeTableTree.class;

    public static final TreeTableColumn NAME_COLUMN = new TreeTableColumn("Name", TREE_COLUMN_CLASS);

    public static final TreeTableColumn VALUE_COLUMN = new TreeTableColumn("Value", String.class);

    private Class<?> columnClass;

    public TreeTableColumn(String columnName, Class<?> columnClass) {

        this(columnName, 100, SwingConstants.LEFT, columnClass);
    }

    public TreeTableColumn(String displayName, int prefColumnWidth, int horizontalAlignment, Class<?> columnClass) {

        super(displayName, prefColumnWidth, horizontalAlignment);

        this.columnClass = columnClass;
    }

    public Class<?> getColumnClass() {
        return columnClass;
    }

}
