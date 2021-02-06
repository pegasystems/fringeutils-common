/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;
import javax.swing.table.JTableHeader;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class FilterColumnUpdateTask<T extends Comparable<? super T>> extends SwingWorker<Void, Void> {

    private static final Log4j2Helper LOG = new Log4j2Helper(FilterColumnUpdateTask.class);

    private List<T> ftmEntryIndexList;

    private FilterColumn filterColumn;

    private List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList;

    private JTableHeader tableHeader;

    private long startTimeMillis;

    public FilterColumnUpdateTask(List<T> ftmEntryIndexList, FilterColumn filterColumn,
            List<CheckBoxMenuItemPopupEntry<T>> columnFilterEntryList, JTableHeader tableHeader) {

        super();
        this.ftmEntryIndexList = ftmEntryIndexList;
        this.filterColumn = filterColumn;
        this.columnFilterEntryList = columnFilterEntryList;
        this.tableHeader = tableHeader;
    }

    @Override
    protected Void doInBackground() throws Exception {

        startTimeMillis = System.currentTimeMillis();

        LOG.debug("FilterColumnUpdateTask " + filterColumn + " Start.");

        for (CheckBoxMenuItemPopupEntry<T> ttcfe : columnFilterEntryList) {

            List<T> ttcfeTEIdList = ttcfe.getRowIndexList();

            ttcfe.setVisible(false);

            // List<T> tmpList = new ArrayList<>(ftmEntryIndexList);
            //
            // tmpList.retainAll(ttcfeTEIdList);
            //
            // int filteredCount = tmpList.size();

            // alternate method
            Set<T> ttcfeIdSet = new HashSet<>(ttcfe.getRowIndexList());
            Set<T> ftmEntryIndexSet = new HashSet<>(ftmEntryIndexList);

            int filteredCount = getIntersectionSize(ttcfeIdSet, ftmEntryIndexSet);

            if (filteredCount > 0) {
                ttcfe.setVisible(true);
            }

            ttcfe.setFilteredCount(filteredCount);

            LOG.debug("FilterColumnUpdateTask ftmEntryIndexListSize: " + ftmEntryIndexList.size()
                    + " ttcfeTEIdListSize: " + ttcfeTEIdList.size() + " filteredCount: " + filteredCount);
        }

        return null;
    }

    private int getIntersectionSize(Set<T> set1, Set<T> set2) {

        Set<T> aset;
        Set<T> bset;

        if (set1.size() <= set2.size()) {
            aset = set1;
            bset = set2;
        } else {
            aset = set2;
            bset = set1;
        }

        int count = 0;

        for (T entry : aset) {
            if (bset.contains(entry)) {
                count++;
            }
        }

        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#done()
     */
    @Override
    protected void done() {

        filterColumn.setColumnLoading(false);

        tableHeader.repaint();

        long endTimeMillis = System.currentTimeMillis();

        int secs = (int) Math.ceil((double) (endTimeMillis - startTimeMillis) / 1E3);

        LOG.debug("FilterColumnUpdateTask " + filterColumn + " done in " + secs + " secs.");

    }

}
