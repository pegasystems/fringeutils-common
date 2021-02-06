/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

public interface Searchable<T> {

    public enum SelectedRowPosition {
        FIRST, BETWEEN, LAST, NONE
    }

    public T first();

    public T previous();

    public T next();

    public T last();

    public Object getSearchStrObj();

    public void setSearchStrObj(Object searchStrObj);

    public boolean isWrapMode();

    public void setWrapMode(boolean wrapMode);

    public int getResultCount();

    public int getNavIndex();

    // based on the current selected row
    public SelectedRowPosition getSelectedRowPosition();

    public void resetResults(boolean clearResults);

    public void searchInEvents(Object searchStrObj, ModalProgressMonitor modalProgressMonitor);
}
