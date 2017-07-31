/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.util.ArrayList;
import java.util.List;

import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTable;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;

public class NavigationTableController<T> implements NavigationController<T> {

	// the same model is referenced/used by tables in the following list.
	private FilterTableModel<? super T> filterTableModel;

	private List<CustomJTable> customJTableList;

	public NavigationTableController(FilterTableModel<? super T> filterTableModel) {
		super();

		this.filterTableModel = filterTableModel;
		this.customJTableList = new ArrayList<CustomJTable>();
	}

	protected FilterTableModel<? super T> getFilterTableModel() {
		return filterTableModel;
	}

	protected List<CustomJTable> getCustomJTableList() {
		return customJTableList;
	}

	public void addCustomJTable(CustomJTable customJTable) {
		customJTableList.add(customJTable);
	}

	@Override
	public void navigateToRow(int startRowIndex, int endRowIndex) {

		for (CustomJTable customJTable : getCustomJTableList()) {

			customJTable.setRowSelectionInterval(startRowIndex, endRowIndex);
			customJTable.scrollRowToVisible(startRowIndex);

			customJTable.updateUI();

		}
	}

	@Override
	public void scrollToKey(T key) {

		FilterTableModel<? super T> filterTableModel = getFilterTableModel();

		int rowNumber = filterTableModel.getIndexOfKey(key);

		for (CustomJTable customJTable : getCustomJTableList()) {

			if (customJTable instanceof AbstractTreeTable) {
				AbstractTreeTableNode treeNode = filterTableModel.getTreeNodeForKey(key);

				if (treeNode != null) {
					((AbstractTreeTable) customJTable).scrollNodeToVisible(treeNode);
				}
			} else {

				if (rowNumber != -1) {
					customJTable.setRowSelectionInterval(rowNumber, rowNumber);
					customJTable.scrollRowToVisible(rowNumber);
				}
			}
		}
	}
}
