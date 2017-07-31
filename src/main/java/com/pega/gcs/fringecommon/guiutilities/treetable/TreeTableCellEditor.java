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

	private JComponent jComponent;

	/**
	 * @param tree
	 */
	public TreeTableCellEditor(JComponent jComponent) {
		super();

		this.jComponent = jComponent;
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
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return jComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject e) {

		// if (e instanceof MouseEvent) {
		//
		// int colunm1 = 0;
		// MouseEvent me = (MouseEvent) e;
		//
		// MouseEvent newME = new MouseEvent(jComponent, me.getID(),
		// me.getWhen(), me.getModifiers(), me.getX()
		// - table.getCellRect(0, colunm1, true).x, me.getY(),
		// me.getClickCount(), me.isPopupTrigger());
		//
		// jComponent.dispatchEvent(newME);
		// }

		if (e instanceof AWTEvent) {
			jComponent.dispatchEvent((AWTEvent) e);
		}

		return false;
	}

}
