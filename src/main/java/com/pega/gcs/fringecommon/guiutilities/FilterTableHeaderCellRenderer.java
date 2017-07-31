/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.pega.gcs.fringecommon.utilities.FileUtilities;

public class FilterTableHeaderCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -1099340674907918841L;

	private ImageIcon downarrowFilteredImageIcon;

	private ImageIcon downarrowImageIcon;

	private ImageIcon loadingImageIcon;

	private TableCellRenderer origTableCellRenderer;

	/**
	 * @param origTableCellRenderer
	 */
	public FilterTableHeaderCellRenderer(TableCellRenderer origTableCellRenderer) {
		super();

		this.origTableCellRenderer = origTableCellRenderer;

		downarrowFilteredImageIcon = FileUtilities.getImageIcon(this.getClass(), "filterred.png");

		downarrowImageIcon = FileUtilities.getImageIcon(this.getClass(), "filterblack.png");

		loadingImageIcon = FileUtilities.getImageIcon(this.getClass(), "loading.png");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent
	 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel origComponent = (JLabel) origTableCellRenderer.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);

		TableModel tableModel = table.getModel();

		if (tableModel instanceof FilterTableModel) {

			FilterTableModel<?> ftm = (FilterTableModel<?>) tableModel;

			boolean columnFilterEnabled = ftm.isColumnFilterEnabled(column);

			if (columnFilterEnabled) {

				boolean columnFiltered = ftm.isColumnFiltered(column);
				boolean columnLoading = ftm.isColumnLoading(column);

				if (columnLoading) {
					origComponent.setIcon(loadingImageIcon);
				} else if (columnFiltered) {
					origComponent.setIcon(downarrowFilteredImageIcon);
				} else {
					origComponent.setIcon(downarrowImageIcon);
				}

				origComponent.setHorizontalTextPosition(LEFT);

			}

			origComponent.setHorizontalAlignment(CENTER);
		}

		// set header height
		Dimension dim = origComponent.getPreferredSize();
		dim.setSize(dim.getWidth(), 30);
		origComponent.setPreferredSize(dim);

		return origComponent;
	}
}
