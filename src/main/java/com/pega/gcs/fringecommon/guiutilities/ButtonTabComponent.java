/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Component to be used as tabComponent; Contains a JLabel to show the text and a JButton to close the tab it belongs to.
 */
public class ButtonTabComponent extends JPanel {

    private static final long serialVersionUID = -8658986828578174335L;

    private String label;

    private JTabbedPane tabbedPane;

    private JLabel tabLabel;

    private JButton tabButton;

    public ButtonTabComponent(String label, JTabbedPane tabbedPane) {

        super();

        this.label = label;
        this.tabbedPane = tabbedPane;

        setOpaque(false);

        LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);

        setLayout(layout);

        JLabel tabLabel = getTabLabel();
        JButton tabButton = getTabButton();

        Dimension spacer = new Dimension(5, 20);

        add(tabLabel);
        add(Box.createRigidArea(spacer));
        add(tabButton);
    }

    protected JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    private JLabel getTabLabel() {

        if (tabLabel == null) {
            tabLabel = new JLabel(label);
        }

        return tabLabel;
    }

    private JButton getTabButton() {

        if (tabButton == null) {

            tabButton = new JButton() {

                private static final long serialVersionUID = -2794775022954503406L;

                /*
                 * (non-Javadoc)
                 * 
                 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
                 */
                @Override
                protected void paintComponent(Graphics graphics) {

                    super.paintComponent(graphics);

                    Graphics2D g2 = (Graphics2D) graphics.create();

                    if (getModel().isPressed()) {
                        g2.translate(1, 1);
                    }

                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.BLACK);

                    if (getModel().isRollover()) {
                        g2.setColor(MyColor.RED_ON);
                    }

                    int delta = 6;

                    g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);

                    g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);

                    g2.dispose();
                }

            };

            Dimension size = new Dimension(17, 17);

            tabButton.setPreferredSize(size);
            tabButton.setSize(size);
            tabButton.setMaximumSize(size);

            tabButton.setToolTipText("Close");
            tabButton.setContentAreaFilled(false);
            tabButton.setFocusable(false);
            tabButton.setBorder(BorderFactory.createEtchedBorder());
            tabButton.setBorderPainted(false);
            tabButton.setRolloverEnabled(true);

            MouseListener mouseListener = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                    Component component = mouseEvent.getComponent();

                    if (component instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) component;
                        button.setBorderPainted(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                    Component component = mouseEvent.getComponent();

                    if (component instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) component;
                        button.setBorderPainted(false);
                    }
                }
            };

            tabButton.addMouseListener(mouseListener);

            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    JTabbedPane tabbedPane = getTabbedPane();

                    int index = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);

                    if (index != -1) {
                        tabbedPane.remove(index);
                    }
                }
            };

            tabButton.addActionListener(actionListener);
        }

        return tabButton;
    }

}
