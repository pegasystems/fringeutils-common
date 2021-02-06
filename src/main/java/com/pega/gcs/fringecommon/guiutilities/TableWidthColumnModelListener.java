/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;

public class TableWidthColumnModelListener implements TableColumnModelListener {

    private List<JTable> tableList;

    public TableWidthColumnModelListener() {
        super();

        tableList = new ArrayList<JTable>();

    }

    @Override
    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
        // do nothing
    }

    @Override
    public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
        // do nothing
    }

    @Override
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
        // do nothing
    }

    @Override
    public void columnMarginChanged(ChangeEvent changeEvent) {

        TableColumnModel tableColumnModel = (TableColumnModel) changeEvent.getSource();

        for (JTable table : tableList) {

            TableColumnModel tcm = table.getColumnModel();

            // exclude the source
            if (tableColumnModel != tcm) {

                // disable the listener temporarily while the width is changed
                tcm.removeColumnModelListener(this);

                for (int i = 0; i < tcm.getColumnCount(); i++) {
                    tcm.getColumn(i).setPreferredWidth(tableColumnModel.getColumn(i).getWidth());
                }

                tcm.addColumnModelListener(this);
            }

        }
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        // do nothing
    }

    public void addTable(JTable table) {

        tableList.add(table);

    }

}
