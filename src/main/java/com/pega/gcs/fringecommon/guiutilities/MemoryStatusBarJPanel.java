/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class MemoryStatusBarJPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -1765095396507743943L;

    private JProgressBar memoryJProgressBar;

    private Timer timer;

    public MemoryStatusBarJPanel() {
        super();

        LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        setLayout(layout);

        JLabel memoryJLabel = new JLabel("Memory ");

        JProgressBar memoryJProgressBar = getMemoryJProgressBar();

        JLabel totalMemoryJLabel = new JLabel();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        String maxMemoryStr = GeneralUtilities.humanReadableSize(maxMemory, false);
        totalMemoryJLabel.setText("Max: " + maxMemoryStr);

        Dimension endSpacer = new Dimension(5, 20);
        Dimension spacer = new Dimension(10, 20);

        add(Box.createRigidArea(endSpacer));
        add(memoryJLabel);
        add(Box.createRigidArea(spacer));
        add(memoryJProgressBar);
        add(Box.createRigidArea(spacer));
        add(totalMemoryJLabel);
        add(Box.createRigidArea(endSpacer));

        timer = new Timer(100, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double usedfactor = (double) usedMemory / totalMemory;
        int usedPercent = (int) (usedfactor * 100);

        String usedmemoryStr = GeneralUtilities.humanReadableSize(usedMemory, false);
        String totalMemoryStr = GeneralUtilities.humanReadableSize(totalMemory, false);
        Color foreground = getForegroundColor(usedfactor);

        JProgressBar memoryJProgressBar = getMemoryJProgressBar();

        memoryJProgressBar.setForeground(foreground);
        memoryJProgressBar.setValue(usedPercent);
        memoryJProgressBar.setString(usedPercent + "% (" + usedmemoryStr + " / " + totalMemoryStr + ")");
    }

    private JProgressBar getMemoryJProgressBar() {

        if (memoryJProgressBar == null) {
            memoryJProgressBar = new JProgressBar();

            memoryJProgressBar.setStringPainted(true);
        }

        return memoryJProgressBar;
    }

    private Color getForegroundColor(double usedfactor) {

        Color foregroundColor = Color.BLACK;

        if ((usedfactor > 0) && (usedfactor < 1)) {
            int red = (int) (255 * usedfactor);
            int green = (int) (255 * (1 - usedfactor));
            foregroundColor = new Color(red, green, 0);
        }

        return foregroundColor;
    }

    public void destroyPanel() {
        timer.stop();
    }
}
