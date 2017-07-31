/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class FilterTable<T extends Comparable<? super T>> extends CustomJTable {

	private static final long serialVersionUID = -3587453063266930735L;

	public FilterTable(FilterTableModel<T> filterTableModel) {

		super(filterTableModel);

		setAutoCreateColumnsFromModel(false);

		setRowHeight(22);

		setRowSelectionAllowed(true);

		setFillsViewportHeight(true);

		TableColumnModel columnModel = filterTableModel.getTableColumnModel();

		setColumnModel(columnModel);

		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		setTraceTableHeader();
	}

	private void setTraceTableHeader() {

		JTableHeader tableHeader = getTableHeader();

		tableHeader.setReorderingAllowed(false);

		TableCellRenderer origTableCellRenderer = tableHeader.getDefaultRenderer();
		FilterTableHeaderCellRenderer tthcr = new FilterTableHeaderCellRenderer(origTableCellRenderer);
		tableHeader.setDefaultRenderer(tthcr);

		// bold the header
		Font existingFont = tableHeader.getFont();
		String existingFontName = existingFont.getName();
		int existFontSize = existingFont.getSize();
		Font newFont = new Font(existingFontName, Font.BOLD, existFontSize);
		tableHeader.setFont(newFont);

		// set column header filters action
		tableHeader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (SwingUtilities.isLeftMouseButton(e)) {

					JTableHeader target = (JTableHeader) e.getSource();
					JTable table = target.getTable();
					TableModel tableModel = table.getModel();

					if (tableModel instanceof FilterTableModel) {

						@SuppressWarnings("unchecked")
						FilterTableModel<T> ftm = (FilterTableModel<T>) tableModel;

						final int columnIndex = target.columnAtPoint(e.getPoint());

						boolean columnFilterEnabled = ftm.isColumnFilterEnabled(columnIndex);

						boolean columnLoading = ftm.isColumnLoading(columnIndex);

						if ((columnFilterEnabled) && (!columnLoading)) {

							Set<CheckBoxMenuItemPopupEntry<T>> filterTableHeaderColumnEntrySet;
							filterTableHeaderColumnEntrySet = ftm.getColumnFilterEntrySet(columnIndex);

							JPopupMenu filterTableHeaderPopupMenu = new FilterTableHeaderPopupMenu<T>(
									filterTableHeaderColumnEntrySet) {

								private static final long serialVersionUID = 69036102746514349L;

								@Override
								public void applyJButtonAction() {
									applyColumnHeaderFilter(columnIndex, false);
								}
							};

							// limit the size of popup
							filterTableHeaderPopupMenu.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

							int width = filterTableHeaderPopupMenu.getPreferredSize().width;
							int height = filterTableHeaderPopupMenu.getPreferredSize().height;

							if ((width > 300) || (height > 400)) {

								width = 300;
								height = ((height > 400) ? 400 : height);

								filterTableHeaderPopupMenu.setPreferredSize(new Dimension(width, height));
							}

							Rectangle rect = target.getHeaderRect(columnIndex);
							filterTableHeaderPopupMenu.show(target, rect.x, rect.height);

						}
					}
				}

			}

		});

		setTableHeader(tableHeader);
	}

	public void applyColumnHeaderFilter(int columnIndex, boolean clearAll) {

		TableModel tableModel = getModel();

		if (tableModel instanceof FilterTableModel) {

			@SuppressWarnings("unchecked")
			FilterTableModel<T> ftm = (FilterTableModel<T>) tableModel;

			Set<CheckBoxMenuItemPopupEntry<T>> columnFilterEntrySet = null;

			if (!clearAll) {

				columnFilterEntrySet = new HashSet<CheckBoxMenuItemPopupEntry<T>>();

				Set<CheckBoxMenuItemPopupEntry<T>> ttcfeSet = ftm.getColumnFilterEntrySet(columnIndex);

				for (CheckBoxMenuItemPopupEntry<T> ttcfe : ttcfeSet) {

					if (ttcfe.isSelected()) {
						columnFilterEntrySet.add(ttcfe);
					} else {
						ttcfe.setVisible(false);
					}
				}

			}

			JTableHeader tableHeader = getTableHeader();

			ftm.applyColumnFilter(columnFilterEntrySet, columnIndex, tableHeader);

			// repaint occurs separately in the FilterColumnUpdateTask now
			// tableHeader.repaint();
		}
	}

}
