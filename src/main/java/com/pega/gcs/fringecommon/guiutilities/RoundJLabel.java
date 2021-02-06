/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class RoundJLabel extends JLabel {

    private static final long serialVersionUID = -408982237970624737L;

    public RoundJLabel() {
        super();

        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(graphics);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintBorder(java.awt.Graphics)
     */
    @Override
    protected void paintBorder(Graphics graphics) {
        graphics.setColor(getForeground());
        graphics.drawOval(0, 0, getSize().width - 1, getSize().height - 1);

    }

}
