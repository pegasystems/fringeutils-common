/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.bookmark;

import java.awt.Color;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import com.pega.gcs.fringecommon.guiutilities.FilterTableModel;
import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;
import com.pega.gcs.fringecommon.guiutilities.markerbar.MarkerModel;

public class BookmarkModel<T> extends MarkerModel<T> {

    private BookmarkContainer<T> bookmarkContainer;

    public BookmarkModel(BookmarkContainer<T> bookmarkContainer, FilterTableModel<? super T> filterTableModel) {

        super(Color.BLACK, filterTableModel);

        this.bookmarkContainer = bookmarkContainer;

        resetFilteredMarkerMap();

    }

    public BookmarkContainer<T> getBookmarkContainer() {
        return bookmarkContainer;
    }

    @Override
    public void resetFilteredMarkerMap() {

        clearFilteredMarkerMap();

        Iterator<T> iterator = bookmarkContainer.getIterator();

        while (iterator.hasNext()) {

            T key = iterator.next();

            addToFilteredMarkerMap(key);
        }
    }

    @Override
    public List<Marker<T>> getMarkers(T key) {
        return bookmarkContainer.getBookmarkList(key);
    }

    @Override
    public void addMarker(Marker<T> marker) {

        bookmarkContainer.addBookmark(marker);

        addToFilteredMarkerMap(marker.getKey());

        fireModelDataChanged(new EventObject(this));

    }

    @Override
    public void removeMarker(T key, int index) {

        bookmarkContainer.removeBookmark(key, index);

        removeFromFilteredMarkerMap(key);

        fireModelDataChanged(new EventObject(this));
    }

    @Override
    public void clearMarkers() {
        bookmarkContainer.clearBookmarks();

        clearFilteredMarkerMap();

        fireModelDataChanged(new EventObject(this));

    }
}
