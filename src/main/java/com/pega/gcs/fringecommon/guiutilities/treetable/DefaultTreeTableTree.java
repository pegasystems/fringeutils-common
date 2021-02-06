/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;

public class DefaultTreeTableTree extends JTree implements TableCellRenderer {

    private static final long serialVersionUID = 1153459282854879452L;

    protected int visibleRow;

    private AbstractTreeTable treeTable;

    public DefaultTreeTableTree(AbstractTreeTable treeTable, AbstractTreeTableTreeModel model,
            DefaultTreeTableTreeCellRenderer defaultTreeTableTreeCellRenderer) {

        super(model);

        this.treeTable = treeTable;

        setCellRenderer(defaultTreeTableTreeCellRenderer);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics graphics) {
        graphics.translate(0, -visibleRow * getRowHeight());
        super.paint(graphics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Component#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int xpos, int ypos, int width, int height) {
        super.setBounds(xpos, ypos, width, treeTable.getHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax .swing.JTable, java.lang.Object, boolean, boolean,
     * int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }

        visibleRow = row;
        return this;
    }

}
