/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.table.JTableHeader;

import com.pega.gcs.fringecommon.guiutilities.bookmark.BookmarkModel;
import com.pega.gcs.fringecommon.guiutilities.search.SearchModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public abstract class FilterTableModel<T extends Comparable<T>> extends CustomJTableModel {

    private static final long serialVersionUID = -8731407475816420561L;

    private static final Log4j2Helper LOG = new Log4j2Helper(FilterTableModel.class);

    public static final String NULL_STR = "<NULL>";

    public static final String EMPTY_STR = "<EMPTY>";

    protected abstract int getModelColumnIndex(int column);

    // performing one by one search because of showing progress in the monitor
    // also when cancelling the task we should keep the old search results
    // hence not search result is stored unless the task is completed
    protected abstract boolean search(T key, Object searchStrObj);

    protected abstract FilterTableModelNavigation<T> getNavigationRowIndex(List<T> resultList, int currSelectedRowIndex,
            boolean forward, boolean first, boolean last, boolean wrap);

    // working list
    public abstract List<T> getFtmEntryKeyList();

    protected abstract HashMap<T, Integer> getKeyIndexMap();

    public abstract void resetModel();

    // public abstract int getIndexOfKey(T key);

    public abstract Object getEventForKey(T key);

    public abstract AbstractTreeTableNode getTreeNodeForKey(T key);

    public abstract void clearSearchResults(boolean clearResults);

    public abstract SearchModel<T> getSearchModel();

    private BookmarkModel<T> bookmarkModel;

    private Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap;

    private RecentFile recentFile;

    private Message message;

    private PropertyChangeSupport propertyChangeSupport;

    public FilterTableModel(RecentFile recentFile) {

        this.recentFile = recentFile;
        this.bookmarkModel = null;

        propertyChangeSupport = new PropertyChangeSupport(this);

    }

    @Override
    public int getRowCount() {

        int rowCount = 0;

        List<T> ftmEntryKeyList = getFtmEntryKeyList();

        if (ftmEntryKeyList != null) {
            rowCount = ftmEntryKeyList.size();
        }

        return rowCount;
    }

    public RecentFile getRecentFile() {
        return recentFile;
    }

    public void setRecentFile(RecentFile recentFile) {
        this.recentFile = recentFile;
        resetModel();
    }

    public String getFilePath() {

        String filePath = null;

        RecentFile recentFile = getRecentFile();

        if (recentFile != null) {
            filePath = recentFile.getPath();
        }

        return filePath;
    }

    public String getModelName() {

        String modelName = "";

        RecentFile recentFile = getRecentFile();

        if (recentFile != null) {
            String filePath = recentFile.getPath();

            File file = new File(filePath);
            modelName = file.getName();
        }

        return modelName;
    }

    public Charset getCharset() {

        RecentFile recentFile = getRecentFile();

        return recentFile.getCharset();
    }

    public Locale getLocale() {

        RecentFile recentFile = getRecentFile();

        return recentFile.getLocale();
    }

    public Long getFileSize() {

        RecentFile recentFile = getRecentFile();

        return recentFile.getFileSize();
    }

    public String getFileSHA() {

        RecentFile recentFile = getRecentFile();

        return recentFile.getFileSHA();
    }

    protected PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    private void setFtmEntryKeyList(List<T> newftmEntryIndexList) {

        List<T> ftmEntryIndexList = getFtmEntryKeyList();

        ftmEntryIndexList.clear();

        ftmEntryIndexList.addAll(newftmEntryIndexList);

        Collections.sort(ftmEntryIndexList);

        updateKeyIndexMap();
    }

    public BookmarkModel<T> getBookmarkModel() {
        return bookmarkModel;
    }

    public void setBookmarkModel(BookmarkModel<T> bookmarkModel) {
        this.bookmarkModel = bookmarkModel;
    }

    // storing table header column filter sets. keyed on model column indexes
    protected Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> getColumnFilterMap() {

        if (columnFilterMap == null) {
            columnFilterMap = new TreeMap<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>>();
        }

        return columnFilterMap;
    }

    private FilterColumn getFilterColumn(int column) {

        int modelColumnIndex = getModelColumnIndex(column);

        FilterColumn filterColumn = null;

        Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap = getColumnFilterMap();

        Iterator<FilterColumn> fcIterator = columnFilterMap.keySet().iterator();

        while (fcIterator.hasNext()) {

            FilterColumn filterCol = fcIterator.next();

            if (modelColumnIndex == filterCol.getIndex()) {
                filterColumn = filterCol;
                break;
            }
        }

        return filterColumn;
    }

    public Set<CheckBoxMenuItemPopupEntry<T>> getColumnFilterEntrySet(int column) {

        Set<CheckBoxMenuItemPopupEntry<T>> columnFilterEntrySet = new TreeSet<CheckBoxMenuItemPopupEntry<T>>();

        FilterColumn filterColumn = getFilterColumn(column);

        if (filterColumn != null) {

            Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap;
            columnFilterMap = getColumnFilterMap();
            List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList = columnFilterMap.get(filterColumn);

            if (columnFilterEntryList != null) {
                columnFilterEntrySet = new TreeSet<CheckBoxMenuItemPopupEntry<T>>(columnFilterEntryList);
            }
        }

        return columnFilterEntrySet;

    }

    public boolean isColumnFilterEnabled(int column) {
        boolean columnFilterEnabled = false;

        FilterColumn filterColumn = getFilterColumn(column);

        if (filterColumn != null) {
            columnFilterEnabled = filterColumn.isColumnFilterEnabled();
        }

        return columnFilterEnabled;
    }

    public boolean isAnyColumnFiltered() {
        boolean anyColumnFiltered = false;

        Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap;
        columnFilterMap = getColumnFilterMap();

        Iterator<FilterColumn> fcIterator = columnFilterMap.keySet().iterator();

        while (fcIterator.hasNext()) {

            FilterColumn filterColumn = fcIterator.next();

            anyColumnFiltered = filterColumn.isColumnFiltered();

            if (anyColumnFiltered) {
                break;
            }
        }

        return anyColumnFiltered;
    }

    public boolean isColumnFiltered(int column) {
        boolean columnFiltered = false;

        FilterColumn filterColumn = getFilterColumn(column);

        if (filterColumn != null) {
            columnFiltered = filterColumn.isColumnFiltered();
        }

        return columnFiltered;
    }

    public boolean isColumnLoading(int column) {
        boolean columnLoading = false;

        FilterColumn filterColumn = getFilterColumn(column);

        if (filterColumn != null) {
            columnLoading = filterColumn.isColumnLoading();
        }

        return columnLoading;
    }

    public void applyColumnFilter(Set<CheckBoxMenuItemPopupEntry<T>> columnFilterEntrySet, int columnIndex,
            JTableHeader tableHeader) {

        LOG.info("Apply Column Filter column:" + columnIndex + " " + columnFilterEntrySet);

        FilterColumn filterColumn = getFilterColumn(columnIndex);

        List<T> columnFilteredTEList = new ArrayList<T>();

        if ((columnFilterEntrySet != null) && (columnFilterEntrySet.size() > 0)) {

            filterColumn.setColumnFiltered(true);

            for (CheckBoxMenuItemPopupEntry<T> ttcfe : columnFilterEntrySet) {

                columnFilteredTEList.addAll(ttcfe.getRowIndexList());
            }
        } else {

            filterColumn.setColumnFiltered(false);

            Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap;
            columnFilterMap = getColumnFilterMap();
            List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList = columnFilterMap.get(filterColumn);

            for (CheckBoxMenuItemPopupEntry<T> ttcfe : columnFilterEntryList) {

                ttcfe.setSelected(false);
                ttcfe.setVisible(true);

                columnFilteredTEList.addAll(ttcfe.getRowIndexList());
            }

        }

        applyColumnFilterToMap(columnFilteredTEList, filterColumn, tableHeader);

        // search model can be null in case of simple filtered tables.

        SearchModel<T> searchModel = getSearchModel();

        if (searchModel != null) {
            searchModel.resetResults(true);
        }

        fireTableDataChanged();
    }

    private void applyColumnFilterToMap(List<T> columnFilteredTEList, FilterColumn excludeFilterColumn,
            JTableHeader tableHeader) {

        Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<T>>> columnFilterMap;
        columnFilterMap = getColumnFilterMap();

        for (FilterColumn filterColumn : columnFilterMap.keySet()) {

            if ((!filterColumn.equals(excludeFilterColumn)) && (filterColumn.isColumnFiltered())) {

                List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList = columnFilterMap.get(filterColumn);

                Set<T> ttcfeTEIdSet = new HashSet<T>();

                for (CheckBoxMenuItemPopupEntry<T> ttcfe : columnFilterEntryList) {

                    if (ttcfe.isSelected()) {

                        List<T> ttcfeTEIdList = ttcfe.getRowIndexList();
                        ttcfeTEIdSet.addAll(ttcfeTEIdList);
                    }
                }

                columnFilteredTEList.retainAll(ttcfeTEIdSet);
            }

        }

        setFtmEntryKeyList(columnFilteredTEList);

        for (FilterColumn filterColumn : columnFilterMap.keySet()) {
            // bug: un-checking the CheckBoxMenuItemPopupEntry should only show
            // entries from current selection, in case there is a filter
            // already applied on a different column. hence run the update task
            // on all columns. the update task also updates the current count on
            // each entry

            // if (filterColumn != excludeFilterColumn) {

            List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList = columnFilterMap.get(filterColumn);

            filterColumn.setColumnLoading(true);
            tableHeader.repaint();

            FilterColumnUpdateTask<T> filterColumnUpdateTask = new FilterColumnUpdateTask<T>(getFtmEntryKeyList(),
                    filterColumn, columnFilterEntryList, tableHeader);

            filterColumnUpdateTask.execute();
            // }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {

        this.message = message;

        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        propertyChangeSupport.firePropertyChange("message", null, message);
    }

    /**
     * Get EntryKey for selected rows.
     * 
     * @param selectedRows - null for entire list
     * @return a list of EntryKey
     */
    public List<T> getEntryKeyForRowsList(int[] selectedRows) {

        List<T> entryKeyList = new ArrayList<>();

        if (selectedRows == null) {

            entryKeyList = getFtmEntryKeyList();

        } else {

            for (int selectedRow : selectedRows) {

                T entryKey = getFtmEntryKeyList().get(selectedRow);

                entryKeyList.add(entryKey);

            }
        }

        return entryKeyList;
    }

    protected void updateKeyIndexMap() {

        HashMap<T, Integer> keyIndexMap = getKeyIndexMap();

        if (keyIndexMap != null) {

            keyIndexMap.clear();

            List<T> ftmEntryIndexList = getFtmEntryKeyList();

            for (int index = 0; index < ftmEntryIndexList.size(); index++) {

                T key = ftmEntryIndexList.get(index);

                keyIndexMap.put(key, index);
            }
        }
    }

    public int getIndexOfKey(T key) {

        int index = -1;

        HashMap<T, Integer> keyIndexMap = getKeyIndexMap();

        if (keyIndexMap != null) {
            Integer value = keyIndexMap.get(key);
            index = value != null ? value.intValue() : -1;
        }

        return index;
    }

}
