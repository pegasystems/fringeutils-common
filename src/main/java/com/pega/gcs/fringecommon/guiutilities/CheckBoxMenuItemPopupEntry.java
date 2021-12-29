/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CheckBoxMenuItemPopupEntry<T> implements Comparable<CheckBoxMenuItemPopupEntry<T>>, Serializable {

    private static final long serialVersionUID = 3889706679542807912L;

    private String itemText;

    private List<T> rowIndexList;

    private boolean visible;

    private boolean selected;

    // show updated counts in case of multiple filters are applied
    private int filteredCount;

    @SuppressWarnings("unused")
    private CheckBoxMenuItemPopupEntry() {
        // for kryo
    }

    public CheckBoxMenuItemPopupEntry(String itemText) {

        super();

        this.itemText = itemText;
        this.rowIndexList = new ArrayList<T>();
        this.visible = true;
        this.selected = false;
        this.filteredCount = -1;
    }

    public String getItemText() {
        return itemText;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getFilteredCount() {
        return filteredCount;
    }

    public void setFilteredCount(int filteredCount) {
        this.filteredCount = filteredCount;
    }

    public List<T> getRowIndexList() {
        return Collections.unmodifiableList(rowIndexList);
    }

    public void addRowIndex(T rowindex) {
        rowIndexList.add(rowindex);

        filteredCount = rowIndexList.size();
    }

    public boolean containsRowIndex(T rowindex) {
        return rowIndexList.contains(rowindex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(itemText);
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

        if (!(obj instanceof CheckBoxMenuItemPopupEntry)) {
            return false;
        }

        CheckBoxMenuItemPopupEntry<?> other = (CheckBoxMenuItemPopupEntry<?>) obj;

        return Objects.equals(itemText, other.itemText);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getItemText();
    }

    @Override
    public int compareTo(CheckBoxMenuItemPopupEntry<T> other) {
        return getItemText().compareTo(other.getItemText());
    }

}
