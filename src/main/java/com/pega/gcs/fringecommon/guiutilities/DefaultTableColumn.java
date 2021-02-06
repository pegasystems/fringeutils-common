/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.SwingConstants;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class DefaultTableColumn {

    private static final Log4j2Helper LOG = new Log4j2Helper(DefaultTableColumn.class);

    private String columnId;

    private String displayName;

    private int prefColumnWidth;

    private int horizontalAlignment;

    private boolean visibleColumn;

    private boolean filterable;

    public DefaultTableColumn(String displayName) {
        this(displayName, displayName, -1, SwingConstants.LEFT, true, true);
    }

    public DefaultTableColumn(String displayName, int prefColumnWidth, int horizontalAlignment) {
        this(displayName, displayName, prefColumnWidth, horizontalAlignment, true, true);
    }

    public DefaultTableColumn(String columnId, String displayName, int prefColumnWidth, int horizontalAlignment,
            boolean visibleColumn, boolean filterable) {

        super();

        this.columnId = columnId;
        this.displayName = displayName;
        this.prefColumnWidth = prefColumnWidth;
        this.horizontalAlignment = horizontalAlignment;
        this.visibleColumn = visibleColumn;
        this.filterable = filterable;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPrefColumnWidth() {
        return prefColumnWidth;
    }

    public void setPrefColumnWidth(int prefColumnWidth) {
        this.prefColumnWidth = prefColumnWidth;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public boolean isVisibleColumn() {
        return visibleColumn;
    }

    public void setVisibleColumn(boolean visibleColumn) {
        this.visibleColumn = visibleColumn;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public void setFilterable(boolean filterable) {
        this.filterable = filterable;
    }

    public String getColumnId() {
        return columnId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(columnId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof DefaultTableColumn)) {
            return false;
        }

        DefaultTableColumn other = (DefaultTableColumn) obj;
        return Objects.equals(columnId, other.columnId);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public static <T extends DefaultTableColumn> T getTableColumnByName(String displayName, List<T> columnList) {

        T defaultTableColumn = null;

        for (T dtc : columnList) {

            String name = dtc.getDisplayName();

            if (name.equals(displayName)) {
                defaultTableColumn = dtc;
                break;
            }
        }

        if (defaultTableColumn == null) {
            LOG.error("Unidentified Table Column name: " + displayName);
        }

        return defaultTableColumn;
    }

    public static <T extends DefaultTableColumn> T getTableColumnById(String columnId, List<T> columnList) {

        T defaultTableColumn = null;

        for (T dtc : columnList) {

            String colId = dtc.getColumnId();

            if (colId.equals(columnId)) {
                defaultTableColumn = dtc;
                break;
            }
        }

        if (defaultTableColumn == null) {
            LOG.error("Unidentified Table Column id: " + columnId);
        }

        return defaultTableColumn;
    }

    public static List<String> getColumnNameList(List<? extends DefaultTableColumn> defaultTableColumnList) {

        List<String> columnNameList = new ArrayList<>();

        for (DefaultTableColumn defaultTableColumn : defaultTableColumnList) {

            columnNameList.add(defaultTableColumn.getDisplayName());
        }

        return columnNameList;
    }
}
