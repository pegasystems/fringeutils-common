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
import javax.swing.table.TableModel;

/**
 * extending JTable for enhancement
 */
public class CustomJTable extends JTable {

	private static final long serialVersionUID = 4931914632130374617L;

	private NavigationTableController navigationTableController;

	public CustomJTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dm
	 */
	public CustomJTable(TableModel dm) {
		super(dm);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#getScrollableTracksViewportWidth()
	 */
	/**
	 * bug report #1027936 Override JTable.getScrollableTracksViewportWidth() to
	 * honor the table's preferred size and show horizontal scrollbars if that
	 * size cannot be honored by the viewport if an auto-resize mode is selected
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

	/*
	 * public void scrollRowToVisible(int rowIndex) { if (!(getParent()
	 * instanceof JViewport)) { return; } JViewport viewport = (JViewport)
	 * getParent();
	 * 
	 * // This rectangle is relative to the table where the // northwest corner
	 * of cell (0,0) is always (0,0). Rectangle rect = getCellRect(rowIndex, 0,
	 * true);
	 * 
	 * // The location of the view relative to the table Rectangle viewRect =
	 * viewport.getViewRect();
	 * 
	 * rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);
	 * 
	 * // Calculate location of rectangle if it were at the center of view int
	 * centerX = (viewRect.width - rect.width) / 2; int centerY =
	 * (viewRect.height - rect.height) / 2;
	 * 
	 * 
	 * Fake the location of the cell so that scrollRectToVisible will move the
	 * cell to the center
	 * 
	 * if (rect.x < centerX) { centerX = -centerX; }
	 * 
	 * if (rect.y < centerY) { centerY = -centerY; }
	 * 
	 * rect.translate(centerX, centerY);
	 * 
	 * // Scroll the area into view viewport.scrollRectToVisible(rect); }
	 */

	public void scrollRowToVisible(int rowIndex) {

		if (!(this.getParent() instanceof JViewport)) {
			return;
		}

		JViewport viewport = (JViewport) getParent();
		Rectangle rect = getCellRect(rowIndex, 0, true);

		GUIUtilities.scrollRectangleToVisible(viewport, rect);

	}

	public NavigationTableController getNavigationTableController() {
		return navigationTableController;
	}

	public void setNavigationController(NavigationTableController navigationTableController) {
		this.navigationTableController = navigationTableController;
		navigationTableController.addCustomJTable(this);
	}
}
