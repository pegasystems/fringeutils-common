/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.text.StringEscapeUtils;

import com.pega.gcs.fringecommon.guiutilities.MyColor;

public class XMLLabelTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -5126251432557378970L;

    public XMLLabelTableCellRenderer() {

        super();
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

        XMLNode xmlNode = (XMLNode) value;

        String text = xmlNode.getNodeValue(column);

        XMLTreeTable xmlTreeTable = (XMLTreeTable) table;

        if (xmlTreeTable.isUnescapeHtmlText()) {
            text = StringEscapeUtils.unescapeHtml4(text);
        }

        super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);

        if (!isSelected) {

            Color background = MyColor.LIGHTEST_LIGHT_GRAY;

            // first preference to 'seachfound' color then 'hasmessage' color
            if (xmlNode.isSecondarySearchFound(column)) {
                background = MyColor.LIME;
            } else if (xmlNode.isSecondaryParentSearchFound(column)) {
                background = MyColor.LIGHT_LIME;
            } else if (xmlNode.isSearchFound(column)) {
                background = MyColor.LIGHT_YELLOW;
            } else if (xmlNode.isParentSearchFound(column)) {
                background = MyColor.LIGHTEST_YELLOW;
            } else if (xmlNode.isCompareNodeDataDiffFound()) {
                background = MyColor.LIGHTEST_RED;
            } else if (xmlNode.isHasMessage(column)) {
                background = Color.ORANGE;
            } else if (text == null) {
                background = Color.LIGHT_GRAY;
            }

            setBackground(background);
        }

        setBorder(new EmptyBorder(1, 3, 1, 1));

        // boolean specialCharsInValue = xmlNode.isSpecialCharsInValue(column);
        //
        // if (specialCharsInValue) {
        //
        // text = text.replaceAll("\n", "<br>");
        //
        // StringBuilder textSB = new StringBuilder();
        //
        // textSB.append("<html>");
        // textSB.append(text);
        // textSB.append("<html>");
        //
        // text = textSB.toString();
        // }

        setText(text);

        if ((text != null) && (!"".equals(text))) {
            setToolTipText(text);
        }

        return this;

    }
}
