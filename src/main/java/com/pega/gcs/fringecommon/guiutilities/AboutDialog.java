/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.pega.gcs.fringecommon.utilities.FileUtilities;

public class AboutDialog extends JDialog {

    private static final long serialVersionUID = 7949153479786349366L;

    private Image aboutImage;

    private String appStr;

    public AboutDialog(Image aboutImage, String appStr, Component parent) {

        super();

        this.aboutImage = aboutImage;
        this.appStr = appStr;

        setIconImage(BaseFrame.getAppIcon().getImage());

        setTitle("About Fringe Utilities");

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setContentPane(getMainJPanel());

        pack();

        setLocationRelativeTo(parent);

        setResizable(false);
        setAlwaysOnTop(true);

        // setVisible called by caller.
        // setVisible(true);
    }

    private JPanel getMainJPanel() {

        JPanel mainJPanel = new JPanel();

        LayoutManager layout = new BoxLayout(mainJPanel, BoxLayout.Y_AXIS);
        mainJPanel.setLayout(layout);

        JPanel imageJPanel = new ImageJPanel(aboutImage);

        JLabel line1 = new JLabel("Fringe Utilities");
        JLabel line2 = new JLabel(appStr);
        JLabel line3 = new JLabel("Author: Manu Varghese");

        imageJPanel.setAlignmentX(CENTER_ALIGNMENT);
        line1.setAlignmentX(CENTER_ALIGNMENT);
        line2.setAlignmentX(CENTER_ALIGNMENT);
        line3.setAlignmentX(CENTER_ALIGNMENT);

        mainJPanel.add(Box.createRigidArea(new Dimension(2, 10)));
        mainJPanel.add(imageJPanel);
        mainJPanel.add(Box.createRigidArea(new Dimension(2, 2)));
        mainJPanel.add(line1);
        mainJPanel.add(Box.createRigidArea(new Dimension(2, 2)));
        mainJPanel.add(line2);
        mainJPanel.add(Box.createRigidArea(new Dimension(2, 2)));
        mainJPanel.add(line3);
        mainJPanel.add(Box.createRigidArea(new Dimension(2, 5)));
        return mainJPanel;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ImageIcon ii = FileUtilities.getImageIcon(this.getClass(), "pega300.png");
                AboutDialog aboutDialog = new AboutDialog(ii.getImage(), "Test App", null);
                aboutDialog.setVisible(true);
            }
        });
    }
}
