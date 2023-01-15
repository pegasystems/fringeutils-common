/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.markerbar;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.pega.gcs.fringecommon.guiutilities.NavigationController;

public class MarkerBar<T extends Comparable<T>> extends JComponent implements MarkerModelListener {

    private static final long serialVersionUID = 3414304903179140199L;

    private ArrayList<MarkerModel<T>> markerModelList;

    private HashMap<Integer, Integer> positionIndexMap;

    private T selectedKey;

    private NavigationController<T> navigationController;

    public MarkerBar(NavigationController<T> navigationController, MarkerModel<T> markerModel) {

        super();

        this.navigationController = navigationController;
        this.selectedKey = null;
        this.markerModelList = new ArrayList<MarkerModel<T>>();

        if (markerModel != null) {
            markerModelList.add(markerModel);
            markerModel.addMarkerModelListener(this);
        }

        positionIndexMap = new HashMap<>();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

                int ypos = mouseEvent.getY();

                Marker<T> marker = getMarkerForPoint(ypos);

                if (marker != null) {
                    setSelectedKey(marker.getKey());
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    setToolTipText(marker.getText());
                } else {
                    setSelectedKey(null);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    setToolTipText(null);
                }
                /*
                 * // LOG.info("e.getPoint().getY():" + // e.getPoint().getY() + " getHeight():" + getHeight()); double yRatio = e.getPoint().getY()
                 * / getHeight(); Object markerObj = getMarkerForPoint(yRatio);
                 * 
                 * if (markerObj != null) {
                 * 
                 * Marker<T> marker = null;
                 * 
                 * if (markerObj instanceof List) { marker = ((List<Marker<T>>) markerObj).get(0); } else { marker = (Marker<T>) markerObj; }
                 * 
                 * setSelectedKey(marker.getKey()); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                 * 
                 * setToolTipText(marker.getText());
                 * 
                 * } else { setSelectedKey(null); setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); setToolTipText(null); }
                 */
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

                T selectedKey = getSelectedKey();

                if (selectedKey != null) {
                    getNavigationController().scrollToKey(selectedKey);
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                setSelectedKey(null);
            }
        });

        setOpaque(false);

        setMinimumSize(new Dimension(16, 80));
        setPreferredSize(new Dimension(16, 80));

        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }

    protected T getSelectedKey() {
        return selectedKey;
    }

    protected void setSelectedKey(T selectedKey) {
        this.selectedKey = selectedKey;
    }

    protected NavigationController<T> getNavigationController() {
        return navigationController;
    }

    protected Marker<T> getMarkerForPoint(Integer position) {

        Marker<T> markerForPoint = null;

        Integer markerIndex = positionIndexMap.get(position);

        if (markerIndex != null) {
            // override the markerForPoint value so that the last one is used.
            // as we paint the last one as well.
            for (MarkerModel<T> markerModel : markerModelList) {

                T key = markerModel.getKey(markerIndex);

                if (key != null) {
                    List<Marker<T>> markerList = markerModel.getMarkers(key);

                    markerForPoint = markerList.get(0);
                }
            }
        }

        return markerForPoint;
    }
    /*
     * protected Object getMarkerForPoint(double yRatio) {
     * 
     * T key = null; Object marker = null; MarkerModel<T> markerModel = null;
     * 
     * for (MarkerModel<T> mm : markerModelList) {
     * 
     * double pointLine = mm.getTotalEntryCount() * yRatio;
     * 
     * int lineNumber = (int) Math.floor(pointLine); key = mm.getKey(lineNumber);
     * 
     * // LOG.info("lineNumber: " + lineNumber);
     * 
     * if (key != null) {
     * 
     * markerModel = mm; break; } else {
     * 
     * lineNumber = (int) Math.ceil(pointLine); // LOG.info("lineNumber 2: " + lineNumber); key = mm.getKey(lineNumber);
     * 
     * if (key != null) { markerModel = mm; break; }
     * 
     * }
     * 
     * }
     * 
     * if (key != null) { marker = markerModel.getMarker(key); }
     * 
     * return marker; }
     */

    public void addMarkerModel(MarkerModel<T> markerModel) {

        if (markerModel != null) {

            markerModelList.add(markerModel);
            markerModel.addMarkerModelListener(this);

            modelDataChanged(new EventObject(markerModel));
        }
    }

    public void removeMarkerModel(MarkerModel<T> markerModel) {

        if (markerModel != null) {

            boolean success = markerModelList.remove(markerModel);

            if (success) {
                markerModel.removeMarkerModelListener(this);
            }

            modelDataChanged(new EventObject(markerModel));
        }
    }

    public void clearMarkerModelList() {

        markerModelList.clear();

        modelDataChanged(new EventObject(this));

    }

    @Override
    public void modelDataChanged(EventObject eventObject) {
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {

        if (markerModelList.size() > 0) {

            Graphics2D g2 = (Graphics2D) graphics;

            Composite composite = g2.getComposite();

            g2.setColor(getBackground());

            if (!isOpaque()) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST));
            }

            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setComposite(composite);

            // rebuilt whenever the bar is painted
            positionIndexMap.clear();

            for (MarkerModel<T> markerModel : markerModelList) {
                paintMarkerModel(g2, markerModel, positionIndexMap);
            }
        } else {
            super.paintComponent(graphics);
        }
    }

    private void paintMarkerModel(Graphics2D g2, MarkerModel<T> markerModel,
            HashMap<Integer, Integer> positionIndexMap) {

        double totalHeight = getHeight();
        double totalCount = markerModel.getTotalEntryCount();
        double markerScale = totalHeight / totalCount;
        int markerHeight = Math.max(1, (int) markerScale);

        g2.setColor(markerModel.getMarkerColor());
        int pos;

        Set<Integer> indexKeySet = markerModel.getIndexKeySet();

        for (Integer index : indexKeySet) {
            pos = (int) (index * markerScale);
            positionIndexMap.put(pos, index);

            g2.fillRect(0, pos, getWidth(), markerHeight);
        }
    }

}
