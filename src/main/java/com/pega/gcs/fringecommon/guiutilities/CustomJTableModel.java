/*******************************************************************************
 *  Copyright (c) 2020 Pegasystems Inc. All rights reserved.
 *
 *  Contributors:
 *      Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public abstract class CustomJTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 88359841085481238L;

    public abstract String getColumnValue(Object valueAtObject, int columnIndex);

    public abstract TableColumnModel getTableColumnModel();
}
