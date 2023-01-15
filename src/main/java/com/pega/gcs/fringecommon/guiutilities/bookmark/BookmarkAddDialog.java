/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.bookmark;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;

public abstract class BookmarkAddDialog<T extends Comparable<T>> extends JDialog {

    private static final long serialVersionUID = -2026873336640757377L;

    private BookmarkModel<T> bookmarkModel;

    private List<T> keyList;

    private JLabel keyLabel;

    private JTextField valueTextField;

    private JButton okButton;

    private JButton cancelButton;

    public abstract List<Marker<T>> getMarkerList(List<T> keyList, String text);

    public BookmarkAddDialog(String defaultText, BookmarkModel<T> bookmarkModel, List<T> keyList, ImageIcon appIcon,
            Component parent) {

        super();

        this.bookmarkModel = bookmarkModel;
        this.keyList = keyList;

        setIconImage(appIcon.getImage());

        setPreferredSize(new Dimension(350, 170));

        setTitle("Add Bookmark");

        setModalityType(ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setContentPane(getMainPanel());

        pack();

        setLocationRelativeTo(parent);

        populateDefaultText(defaultText);

        // setVisible called by caller.
        // setVisible(true);
    }

    private JPanel getMainPanel() {

        JPanel mainPanel = new JPanel();
        LayoutManager layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);

        JPanel bookmarkPanel = getBookmarkPanel();
        JPanel buttonsPanel = getButtonsPanel();

        mainPanel.add(bookmarkPanel);
        mainPanel.add(buttonsPanel);

        return mainPanel;
    }

    private JPanel getBookmarkPanel() {

        JPanel bookmarkPanel = new JPanel();

        bookmarkPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.weightx = 0.0D;
        gbc1.weighty = 0.0D;
        gbc1.fill = GridBagConstraints.BOTH;
        gbc1.anchor = GridBagConstraints.NORTHWEST;
        gbc1.insets = new Insets(10, 10, 3, 2);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.weightx = 1.0D;
        gbc2.weighty = 0.0D;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.anchor = GridBagConstraints.NORTHWEST;
        gbc2.insets = new Insets(10, 2, 3, 10);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        gbc3.weightx = 0.0D;
        gbc3.weighty = 0.0D;
        gbc3.fill = GridBagConstraints.BOTH;
        gbc3.anchor = GridBagConstraints.NORTHWEST;
        gbc3.insets = new Insets(3, 10, 10, 2);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        gbc4.weightx = 1.0D;
        gbc4.weighty = 0.0D;
        gbc4.fill = GridBagConstraints.BOTH;
        gbc4.anchor = GridBagConstraints.NORTHWEST;
        gbc4.insets = new Insets(3, 2, 10, 10);

        JLabel label = new JLabel("Key:");
        JLabel keyJLabel = getKeyLabel();
        JLabel textLabel = new JLabel("Bookmark:");
        JTextField textJTextField = getValueTextField();

        bookmarkPanel.add(label, gbc1);
        bookmarkPanel.add(keyJLabel, gbc2);
        bookmarkPanel.add(textLabel, gbc3);
        bookmarkPanel.add(textJTextField, gbc4);

        bookmarkPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        return bookmarkPanel;
    }

    private JPanel getButtonsPanel() {

        JPanel buttonsPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonsPanel, BoxLayout.X_AXIS);
        buttonsPanel.setLayout(layout);

        JButton okButton = getOkButton();
        JButton cancelButton = getCancelButton();

        Dimension dim = new Dimension(20, 40);

        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(Box.createRigidArea(dim));
        buttonsPanel.add(okButton);
        buttonsPanel.add(Box.createRigidArea(dim));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(dim));
        buttonsPanel.add(Box.createHorizontalGlue());

        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        return buttonsPanel;
    }

    private JLabel getKeyLabel() {

        if (keyLabel == null) {

            StringBuilder sb = new StringBuilder();

            boolean isfirst = true;

            for (T key : keyList) {

                if (!isfirst) {
                    sb.append(",");
                }

                isfirst = false;
                sb.append(key.toString());

            }

            keyLabel = new JLabel(sb.toString());
        }

        return keyLabel;
    }

    private JTextField getValueTextField() {

        if (valueTextField == null) {

            valueTextField = new JTextField();

            valueTextField.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent event) {
                    if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                        performAddBookMark();
                    }
                }
            });
        }

        return valueTextField;
    }

    private JButton getOkButton() {

        if (okButton == null) {

            okButton = new JButton("OK");
            okButton.setToolTipText("OK");

            Dimension size = new Dimension(70, 26);
            okButton.setPreferredSize(size);

            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    performAddBookMark();
                }
            });
        }

        return okButton;
    }

    private JButton getCancelButton() {

        if (cancelButton == null) {

            cancelButton = new JButton("Cancel");
            cancelButton.setToolTipText("Cancel");

            Dimension size = new Dimension(70, 26);
            cancelButton.setPreferredSize(size);

            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    dispose();
                }
            });
        }

        return cancelButton;
    }

    private void performAddBookMark() {

        JTextField textJTextField = getValueTextField();
        String text = textJTextField.getText();
        text = text.trim();

        List<Marker<T>> bookmarkList = getMarkerList(keyList, text);

        for (Marker<T> bookmark : bookmarkList) {
            bookmarkModel.addMarker(bookmark);
        }

        dispose();
    }

    private void populateDefaultText(String defaultText) {

        if ((defaultText != null) && (!"".equals(defaultText))) {

            JTextField textJTextField = getValueTextField();

            textJTextField.setText(defaultText);
        }
    }
}
