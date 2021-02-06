/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.search;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pega.gcs.fringecommon.guiutilities.FilterTableModel;
import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;
import com.pega.gcs.fringecommon.guiutilities.markerbar.MarkerModel;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class SearchMarkerModel<T> extends MarkerModel<T> {

    private static final Log4j2Helper LOG = new Log4j2Helper(SearchMarkerModel.class);

    public SearchMarkerModel(FilterTableModel<? super T> filterTableModel) {

        super(Color.YELLOW, filterTableModel);

        resetFilteredMarkerMap();

    }

    @Override
    protected void resetFilteredMarkerMap() {

        clearFilteredMarkerMap();

        FilterTableModel<? super T> filterTableModel;
        filterTableModel = getFilterTableModel();

        @SuppressWarnings("unchecked")
        SearchModel<T> searchModel = (SearchModel<T>) filterTableModel.getSearchModel();

        Object searchStrObj = searchModel.getSearchStrObj();

        List<? super T> searchResultList = searchModel.getSearchResultList(searchStrObj);

        if (searchResultList != null) {
            Iterator<? super T> iterator = searchResultList.iterator();

            while (iterator.hasNext()) {

                @SuppressWarnings("unchecked")
                T key = (T) iterator.next();

                addToFilteredMarkerMap(key);
            }
        }
    }

    @Override
    public List<Marker<T>> getMarkers(T key) {

        Marker<T> marker = new Marker<T>(key, key.toString());

        List<Marker<T>> markerList = new ArrayList<>();
        markerList.add(marker);

        return markerList;
    }

    @Override
    public void addMarker(Marker<T> marker) {
        LOG.info("Error: SearchMarkerModel doesnt explictly add marker.");
    }

    @Override
    public void removeMarker(T key, int index) {
        LOG.info("Error: SearchMarkerModel doesnt explictly remove marker.");
    }

    @Override
    public void clearMarkers() {
        LOG.info("Error: SearchMarkerModel doesnt explictly clear markers.");

    }

}
