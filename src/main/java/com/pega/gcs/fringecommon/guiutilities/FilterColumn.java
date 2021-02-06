/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.io.Serializable;
import java.util.Objects;

public class FilterColumn implements Comparable<FilterColumn>, Serializable {

    private static final long serialVersionUID = 2204061808458324810L;

    private int index;

    private boolean columnFilterEnabled;

    private boolean columnFiltered;

    private boolean columnLoading;

    public FilterColumn(int index) {
        super();
        this.index = index;
        this.columnFilterEnabled = false;
        this.columnFiltered = false;
        this.columnLoading = false;
    }

    public boolean isColumnFilterEnabled() {
        return columnFilterEnabled;
    }

    public void setColumnFilterEnabled(boolean columnFilterEnabled) {
        this.columnFilterEnabled = columnFilterEnabled;
    }

    public boolean isColumnFiltered() {
        return columnFiltered;
    }

    public void setColumnFiltered(boolean columnFiltered) {
        this.columnFiltered = columnFiltered;
    }

    public boolean isColumnLoading() {
        return columnLoading;
    }

    public void setColumnLoading(boolean columnLoading) {
        this.columnLoading = columnLoading;
    }

    public int getIndex() {
        return index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(index);
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

        if (!(obj instanceof FilterColumn)) {
            return false;
        }

        FilterColumn other = (FilterColumn) obj;

        return index == other.index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FilterColumn [index=" + index + ", columnFilterEnabled=" + columnFilterEnabled + ", columnFiltered="
                + columnFiltered + ", columnLoading=" + columnLoading + "]";
    }

    @Override
    public int compareTo(FilterColumn other) {
        return Integer.valueOf(index).compareTo(Integer.valueOf(other.index));
    }

}
