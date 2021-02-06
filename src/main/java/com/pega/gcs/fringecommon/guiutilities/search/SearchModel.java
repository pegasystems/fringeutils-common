/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.pega.gcs.fringecommon.guiutilities.Searchable;

public abstract class SearchModel<T> implements Searchable<T> {

    private SearchData<T> searchData;

    private EventListenerList listenerList;

    public SearchModel(SearchData<T> searchData) {

        super();

        this.searchData = searchData;

        listenerList = new EventListenerList();

        init();

        resetResults(true);

    }

    // hook for initialising variables in child classes
    protected void init() {

    }

    public void addSearchModelListener(SearchModelListener<T> sml) {
        listenerList.add(SearchModelListener.class, sml);
    }

    public void removeSearchModelListener(SearchModelListener<T> sml) {
        listenerList.remove(SearchModelListener.class, sml);
    }

    public void fireSearchResultChanged(SearchModelEvent<T> searchModelEvent) {

        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 1; i >= 0; i--) {

            if (listeners[i] == SearchModelListener.class) {
                ((SearchModelListener<T>) listeners[i + 1]).searchResultChanged(searchModelEvent);
            }
        }
    }

    @Override
    public Object getSearchStrObj() {
        return searchData.getSearchStrObj();
    }

    @Override
    public void setSearchStrObj(Object searchStrObj) {
        searchData.setSearchStrObj(searchStrObj);
    }

    public ArrayList<Object> getSearchItemList() {
        return searchData.getSearchItemList();
    }

    public List<T> getSearchResultList(Object searchStrObj) {
        return searchData.getSearchResultList(searchStrObj);
    }

    public void setSearchResultList(Object searchStrObj, List<T> searchResultList) {

        searchData.setSearchResultList(searchStrObj, searchResultList);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.RESET_MODEL, null);
        fireSearchResultChanged(searchModelEvent);
    }

    @Override
    public T first() {

        List<T> searchResultList = getSearchResultList(getSearchStrObj());

        int searchNavIndex = 1;

        searchData.setSearchNavIndex(searchNavIndex);

        T key = searchResultList.get(searchNavIndex - 1);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.NAVIGATION, key);
        fireSearchResultChanged(searchModelEvent);

        return key;
    }

    @Override
    public T previous() {

        List<T> searchResultList = getSearchResultList(getSearchStrObj());
        int searchNavIndex = searchData.getSearchNavIndex();

        if (searchNavIndex > 1) {

            searchNavIndex--;

        } else {

            if (isWrapMode()) {
                searchNavIndex = searchResultList.size();
            }
        }

        searchData.setSearchNavIndex(searchNavIndex);

        T key = searchResultList.get(searchNavIndex - 1);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.NAVIGATION, key);
        fireSearchResultChanged(searchModelEvent);

        return key;
    }

    @Override
    public T next() {

        List<T> searchResultList = getSearchResultList(getSearchStrObj());
        int searchNavIndex = searchData.getSearchNavIndex();

        if (searchNavIndex <= searchResultList.size() - 1) {
            searchNavIndex++;
        } else {
            if (isWrapMode()) {
                searchNavIndex = 1;
            }
        }

        searchData.setSearchNavIndex(searchNavIndex);

        T key = searchResultList.get(searchNavIndex - 1);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.NAVIGATION, key);
        fireSearchResultChanged(searchModelEvent);

        return key;
    }

    @Override
    public T last() {

        List<T> searchResultList = getSearchResultList(getSearchStrObj());

        int searchNavIndex = searchResultList.size();

        searchData.setSearchNavIndex(searchNavIndex);

        T key = searchResultList.get(searchNavIndex - 1);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.NAVIGATION, key);
        fireSearchResultChanged(searchModelEvent);

        return key;
    }

    @Override
    public boolean isWrapMode() {
        return searchData.isWrapMode();
    }

    @Override
    public void setWrapMode(boolean wrapMode) {
        searchData.setWrapMode(wrapMode);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.NAVIGATION, null);
        fireSearchResultChanged(searchModelEvent);
    }

    public void resetSearchResults(boolean clearResults) {

        searchData.resetSearchResults(clearResults);

        SearchModelEvent<T> searchModelEvent = new SearchModelEvent<T>(this, SearchModelEvent.RESET_MODEL, null);
        fireSearchResultChanged(searchModelEvent);
    }

    @Override
    public int getResultCount() {

        List<T> searchResultList = getSearchResultList(getSearchStrObj());

        int currentSearchResultCount = (searchResultList != null) ? searchResultList.size() : 0;

        return currentSearchResultCount;
    }

    @Override
    public int getNavIndex() {
        return searchData.getSearchNavIndex();
    }

    @Override
    public SelectedRowPosition getSelectedRowPosition() {

        SelectedRowPosition selectedRowPosition = SelectedRowPosition.NONE;

        List<T> searchResultList = getSearchResultList(getSearchStrObj());

        if ((searchResultList != null) && (searchResultList.size() > 0)) {

            boolean searchResultsWrap = isWrapMode();

            if (searchResultsWrap) {

                selectedRowPosition = SelectedRowPosition.BETWEEN;

            } else {

                int resultSize = searchResultList.size();

                int searchNavIndex = searchData.getSearchNavIndex();

                if ((searchNavIndex > 1) && (searchNavIndex < resultSize)) {
                    selectedRowPosition = SelectedRowPosition.BETWEEN;
                } else if (searchNavIndex == resultSize) {
                    selectedRowPosition = SelectedRowPosition.LAST;
                } else if (searchNavIndex <= 1) {
                    selectedRowPosition = SelectedRowPosition.FIRST;
                } else {
                    selectedRowPosition = SelectedRowPosition.NONE;
                }
            }
        }

        return selectedRowPosition;

    }

}
