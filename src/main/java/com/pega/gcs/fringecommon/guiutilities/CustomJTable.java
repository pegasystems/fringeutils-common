/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Extending JTable for enhancement.
 */
public class CustomJTable extends JTable {

    private static final long serialVersionUID = 4931914632130374617L;

    public CustomJTable() {

        super();

        setTransferHandler(new CustomJTableTransferHandler(this));
    }

    public CustomJTable(CustomJTableModel customJTableModel) {

        super(customJTableModel);

        setTransferHandler(new CustomJTableTransferHandler(this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JTable#getScrollableTracksViewportWidth()
     */
    /**
     * Bug report #1027936 Override JTable.getScrollableTracksViewportWidth() to honor the table's preferred size and show horizontal
     * scrollbars if that size cannot be honored by the viewport if an auto-resize mode is selected
     */
    @Override
    public boolean getScrollableTracksViewportWidth() {
        // return getPreferredSize().width < getParent().getWidth();

        if (autoResizeMode != AUTO_RESIZE_OFF) {
            if (getParent() instanceof JViewport) {
                return (((JViewport) getParent()).getWidth() > getPreferredSize().width);
            }

            return true;
        }

        return false;
    }

    public void scrollRowToVisible(int rowIndex) {

        if (!(this.getParent() instanceof JViewport)) {
            return;
        }

        JViewport viewport = (JViewport) getParent();
        Rectangle rect = getCellRect(rowIndex, 0, true);

        GUIUtilities.scrollRectangleToVisible(viewport, rect);

    }
}
