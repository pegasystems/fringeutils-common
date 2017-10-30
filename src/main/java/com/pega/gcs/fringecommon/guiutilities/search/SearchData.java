/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class SearchData<T> {

	// current search str.
	private Object searchStrObj;

	private LinkedHashSet<Object> defaultSearchKeySet;

	// stores search results trace events id's. this is generated outside of the
	// model because of showing progress while searching.
	// store historic search results. key is Object because some search are
	// special (enum) others are string
	// <Path event type, list<trace event id>>
	private Map<Object, List<T>> searchResultMap;

	// stores the current search traverse pointer (starting from 1)
	private int searchNavIndex;

	// whether to search navigate around and around
	private boolean searchResultsWrap;

	public SearchData(Object[] defaultSearchItems) {
		super();

		searchResultMap = new LinkedHashMap<Object, List<T>>();

		defaultSearchKeySet = new LinkedHashSet<Object>();

		if (defaultSearchItems != null) {

			for (Object item : defaultSearchItems) {
				defaultSearchKeySet.add(item);
				searchResultMap.put(item, null);
			}
		}
	}

	// clear the map leaving the default items only
	// fixing issue - keep the existing search keywords for user to see/use, but
	// clear the results
	protected void clearSearchResultMap() {

		// Set<Object> searchResultKeySet = searchResultMap.keySet();
		// searchResultKeySet.retainAll(defaultSearchKeySet);

		// not removing the keys, but clearing its values.
		for (Map.Entry<Object, List<T>> entry : searchResultMap.entrySet()) {
			entry.setValue(null);
		}

	}

	public Object getSearchStrObj() {
		return searchStrObj;
	}

	public void setSearchStrObj(Object searchStrObj) {
		this.searchStrObj = searchStrObj;
		searchNavIndex = 0;
	}

	public ArrayList<Object> getSearchItemList() {

		ArrayList<Object> searchItemList = new ArrayList<Object>(searchResultMap.keySet());

		return searchItemList;
	}

	public List<T> getSearchResultList(Object searchStrObj) {

		List<T> searchResultList = null;

		if ((searchStrObj != null) && (!"".equals(searchStrObj))) {

			searchResultList = searchResultMap.get(searchStrObj);
		}

		return searchResultList;
	}

	public void setSearchResultList(Object searchStrObj, List<T> searchResultList) {

		if ((this.searchStrObj == null) || (!this.searchStrObj.equals(searchStrObj))) {
			setSearchStrObj(searchStrObj);
		}

		if (searchStrObj != null) {
			searchResultMap.put(searchStrObj, searchResultList);
		}
	}

	public boolean isWrapMode() {
		return searchResultsWrap;
	}

	public void setWrapMode(boolean wrapMode) {
		this.searchResultsWrap = wrapMode;
	}

	public void resetSearchResults(boolean clearResults) {

		setSearchStrObj(null);

		if (clearResults) {
			clearSearchResultMap();
		}
	}

	public int getSearchNavIndex() {
		return searchNavIndex;
	}

	public void setSearchNavIndex(int searchNavIndex) {
		this.searchNavIndex = searchNavIndex;
	}

}
