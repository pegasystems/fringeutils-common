/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.bookmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;

public class BookmarkContainer<T> implements Serializable {

	private static final long serialVersionUID = 7824515896380123970L;

	private Map<T, List<Marker<T>>> bookmarkMap;

	public BookmarkContainer() {
		super();

		bookmarkMap = new TreeMap<T, List<Marker<T>>>();
	}

	public List<Marker<T>> getBookmarkList(T key) {

		return bookmarkMap.get(key);
	}

	public boolean containsKey(T key) {
		return bookmarkMap.containsKey(key);
	}

	public void addBookmark(Marker<T> bookmark) {

		List<Marker<T>> bookmarkList = getBookmarkList(bookmark.getKey());

		if (bookmarkList == null) {
			bookmarkList = new ArrayList<>();
			bookmarkMap.put(bookmark.getKey(), bookmarkList);
		}

		bookmarkList.add(bookmark);
	}

	public void removeBookmark(T key, int index) {

		List<Marker<T>> bookmarkList = getBookmarkList(key);

		if (bookmarkList != null) {
			bookmarkList.remove(index);

			if (bookmarkList.size() == 0) {
				bookmarkMap.remove(key);
			}
		}

	}

	public void clearBookmarks() {
		bookmarkMap.clear();
	}

	public Iterator<T> getIterator() {
		return bookmarkMap.keySet().iterator();
	}

}
