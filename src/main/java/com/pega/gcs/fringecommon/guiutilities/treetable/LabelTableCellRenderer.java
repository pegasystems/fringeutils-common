/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class LabelTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -5126251432557378970L;

    public LabelTableCellRenderer() {

        super();

        setBorder(new EmptyBorder(2, 5, 2, 2));
        setOpaque(true);

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        AbstractTreeTableNode abstractTreeTableNode = (AbstractTreeTableNode) value;

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());

        }

        if (abstractTreeTableNode != null) {
            String text = abstractTreeTableNode.getNodeValue(column);

            setFont(table.getFont());

            setText(text);

            if ((text != null) && (!"".equals(text))) {
                setToolTipText(text);
            }
        }
        return this;

    }

}
