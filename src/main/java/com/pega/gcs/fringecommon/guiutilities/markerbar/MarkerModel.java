/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.markerbar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.pega.gcs.fringecommon.guiutilities.FilterTableModel;

public abstract class MarkerModel<T> implements TableModelListener {

    private EventListenerList listenerList;

    private TreeMap<Integer, T> filteredMarkerMap;

    private Color markerColor;

    private FilterTableModel<? super T> filterTableModel;

    protected abstract void resetFilteredMarkerMap();

    // it may be a list or just marker item
    public abstract List<Marker<T>> getMarkers(T key);

    public abstract void addMarker(Marker<T> marker);

    // in case there are multiple markers on the same key.
    public abstract void removeMarker(T key, int index);

    public abstract void clearMarkers();

    public MarkerModel(Color markerColor, FilterTableModel<? super T> filterTableModel) {
        super();

        this.markerColor = markerColor;
        this.filterTableModel = filterTableModel;
        filterTableModel.addTableModelListener(this);

        listenerList = new EventListenerList();
        filteredMarkerMap = new TreeMap<Integer, T>();

    }

    @Override
    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getType() == TableModelEvent.UPDATE) {
            resetFilteredMarkerMap();
            fireModelDataChanged(null);
        }
    }

    public Color getMarkerColor() {
        return markerColor;
    }

    protected FilterTableModel<? super T> getFilterTableModel() {
        return filterTableModel;
    }

    protected void addToFilteredMarkerMap(T key) {

        int index = filterTableModel.getIndexOfKey(key);

        if (index != -1) {
            filteredMarkerMap.put(index, key);
        }
    }

    protected void removeFromFilteredMarkerMap(T key) {

        List<Integer> deleteList = new ArrayList<Integer>();

        for (Integer index : filteredMarkerMap.keySet()) {

            T bookmarkkey = filteredMarkerMap.get(index);

            if (key.equals(bookmarkkey)) {
                deleteList.add(index);
            }
        }

        for (Integer index : deleteList) {
            filteredMarkerMap.remove(index);
        }
    }

    protected void clearFilteredMarkerMap() {
        filteredMarkerMap.clear();
    }

    public void addMarkerModelListener(MarkerModelListener mml) {
        listenerList.add(MarkerModelListener.class, mml);
    }

    public void removeMarkerModelListener(MarkerModelListener mml) {
        listenerList.remove(MarkerModelListener.class, mml);
    }

    public int getTotalEntryCount() {
        return filterTableModel.getRowCount();
    }

    public int getMarkerCount() {
        return filteredMarkerMap.size();
    }

    protected void fireModelDataChanged(EventObject eventObject) {

        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 1; i >= 0; i--) {

            if (listeners[i] == MarkerModelListener.class) {
                ((MarkerModelListener) listeners[i + 1]).modelDataChanged(eventObject);
            }
        }
    }

    public T getKey(Integer index) {

        T key = filteredMarkerMap.get(index);

        return key;
    }

    public Set<Integer> getIndexKeySet() {
        return filteredMarkerMap.keySet();
    }
}
