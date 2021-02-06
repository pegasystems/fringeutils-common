/*******************************************************************************
 *  Copyright (c) 2020 Pegasystems Inc. All rights reserved.
 *
 *  Contributors:
 *      Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class ComponentIcon implements Icon {

    private final JComponent component;

    public ComponentIcon(JComponent component) {
        super();
        this.component = component;
    }

    @Override
    public void paintIcon(Component comp, Graphics graphics, int xpos, int ypos) {
        SwingUtilities.paintComponent(graphics, component, (Container) comp, xpos, ypos, getIconWidth(), getIconHeight());
    }

    @Override
    public int getIconWidth() {
        return component.getPreferredSize().width;
    }

    @Override
    public int getIconHeight() {
        return component.getPreferredSize().height;
    }

}