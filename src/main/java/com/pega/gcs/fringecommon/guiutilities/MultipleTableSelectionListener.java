/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MultipleTableSelectionListener extends NavigationTableController<Integer>
		implements ListSelectionListener {

	private boolean valueChanging;

	public MultipleTableSelectionListener(FilterTableModel<Integer> filterTableModel,
			NavigationTableController<Integer> navigationTableController) {

		super(filterTableModel);

		List<CustomJTable> customJTableList = navigationTableController.getCustomJTableList();

		for (CustomJTable customJTable : customJTableList) {
			addCustomJTable(customJTable);
		}
	}

	public void clearCustomJTables() {

		List<CustomJTable> customJTableList = getCustomJTableList();

		for (CustomJTable customJTable : customJTableList) {
			customJTable.getSelectionModel().removeListSelectionListener(this);
		}
	}

	@Override
	public void addCustomJTable(CustomJTable customJTable) {
		super.addCustomJTable(customJTable);

		customJTable.getSelectionModel().addListSelectionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (!e.getValueIsAdjusting() && !valueChanging) {

			valueChanging = true;

			try {

				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				JTable table = getTableFromModel(lsm);

				int selectedRow = table.getSelectedRow();

				List<CustomJTable> customJTableList = getCustomJTableList();
				for (CustomJTable customJTable : customJTableList) {

					if (customJTable != table) {

						customJTable.changeSelection(selectedRow, 0, false, false);
					}
				}

			} finally {
				valueChanging = false;
			}
		}

	}

	private JTable getTableFromModel(ListSelectionModel model) {

		JTable tableFromModel = null;

		List<CustomJTable> customJTableList = getCustomJTableList();

		for (CustomJTable customJTable : customJTableList) {

			if (customJTable.getSelectionModel() == model) {
				tableFromModel = customJTable;
			}
		}

		return tableFromModel;

	}
}
