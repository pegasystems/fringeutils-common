/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.AWTEvent;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1801619475820815900L;

    private JComponent component;

    public TreeTableCellEditor(JComponent component) {
        super();

        this.component = component;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing .JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return component;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
     */
    @Override
    public boolean isCellEditable(EventObject eventObject) {

        if (eventObject instanceof AWTEvent) {
            AWTEvent awtEvent = (AWTEvent) eventObject;
            awtEvent.setSource(component);
            component.dispatchEvent(awtEvent);
        }

        return false;
    }

}
