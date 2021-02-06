/*******************************************************************************
 *  Copyright (c) 2020 Pegasystems Inc. All rights reserved.
 *
 *  Contributors:
 *      Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class BlinkLabel extends JLabel implements ActionListener {

    int blinkTime = 500;
    Timer timer = new Timer(blinkTime, this);
    boolean hide = true;
    Color saveForeground = getForeground();
    Icon saveIcon = createIcon();
    Icon blankIcon = getBlankIcon(saveIcon);

    {
        setIcon(saveIcon);
        timer.start();
    }

    public BlinkLabel(String text) {
        super(text);
    }

    @Override
    public void setForeground(Color foreground) {
        saveForeground = foreground;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (hide) {
            super.setForeground(getBackground());
            setIcon(blankIcon);
        } else {
            super.setForeground(saveForeground);
            setIcon(saveIcon);
        }
        hide = !hide;
    }

    private Icon getBlankIcon(Icon icon) {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(image);
    }

    public void setTime(int time) {
        timer.setDelay(time);
    }

    // for testing without having to load a file
    private Icon createIcon() {
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.RED);
        g2.fillOval(2, 2, 28, 28);
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(4.0f));
        g2.drawOval(8, 8, 16, 16);
        return new ImageIcon(image);
    }

}
