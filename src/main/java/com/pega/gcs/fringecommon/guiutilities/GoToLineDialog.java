/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GoToLineDialog extends JDialog {

    private static final long serialVersionUID = -4228477923073243444L;

    private static final String message = "Selected value is outside of range.";

    private int startIndex;

    private int endIndex;

    private Integer selectedInteger;

    private JTextField goToLineTextField;

    private JButton goToFirstButton;

    private JButton goToLastButton;

    private JButton okButton;

    private JButton cancelButton;

    private JLabel messageLabel;

    public GoToLineDialog(int startIndex, int endIndex, ImageIcon appIcon, Component parent) throws HeadlessException {

        super();

        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.selectedInteger = null;

        setIconImage(appIcon.getImage());

        setTitle("Go to line");

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        setContentPane(getMainPanel());

        pack();

        setLocationRelativeTo(parent);

        // setVisible called by caller.
        // setVisible(true);
    }

    protected int getStartIndex() {
        return startIndex;
    }

    protected int getEndIndex() {
        return endIndex;
    }

    protected void setSelectedInteger(Integer selectedInteger) {
        this.selectedInteger = selectedInteger;
    }

    public Integer getSelectedInteger() {
        return selectedInteger;
    }

    private JTextField getGoToLineTextField() {

        if (goToLineTextField == null) {
            goToLineTextField = new JTextField();

            goToLineTextField.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                        executeGoToLine();
                    }
                }
            });
        }

        return goToLineTextField;
    }

    private JButton getGoToFirstButton() {

        if (goToFirstButton == null) {

            goToFirstButton = new JButton("First");
            goToFirstButton.setToolTipText("First Line");

            Dimension size = new Dimension(70, 26);
            goToFirstButton.setPreferredSize(size);
            goToFirstButton.setSize(size);

            goToFirstButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setSelectedInteger(getStartIndex());
                    dispose();
                }
            });
        }

        return goToFirstButton;
    }

    private JButton getGoToLastButton() {

        if (goToLastButton == null) {

            goToLastButton = new JButton("Last");
            goToLastButton.setToolTipText("Last Line");

            Dimension size = new Dimension(70, 26);
            goToLastButton.setPreferredSize(size);
            goToLastButton.setSize(size);

            goToLastButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setSelectedInteger(getEndIndex());
                    dispose();
                }
            });
        }

        return goToLastButton;
    }

    private JButton getOkButton() {

        if (okButton == null) {

            okButton = new JButton("OK");
            okButton.setToolTipText("OK");

            Dimension size = new Dimension(70, 26);
            okButton.setPreferredSize(size);
            okButton.setSize(size);

            okButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    executeGoToLine();
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
            cancelButton.setSize(size);

            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
        }

        return cancelButton;
    }

    private JLabel getMessageLabel() {

        if (messageLabel == null) {
            messageLabel = new JLabel();

            Dimension dim = new Dimension(100, 16);
            messageLabel.setPreferredSize(dim);
            messageLabel.setMinimumSize(dim);

            messageLabel.setAlignmentX(CENTER_ALIGNMENT);
            messageLabel.setForeground(Color.RED);
        }

        return messageLabel;
    }

    private JPanel getMainPanel() {

        JPanel mainPanel = new JPanel();

        LayoutManager layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);

        JPanel gotoPanel = getGotoPanel();
        JPanel buttonsJPanel = getButtonsJPanel();

        mainPanel.add(gotoPanel);
        mainPanel.add(buttonsJPanel);

        return mainPanel;
    }

    private JPanel getGotoPanel() {

        JPanel gotoPanel = new JPanel();

        LayoutManager layout = new BoxLayout(gotoPanel, BoxLayout.Y_AXIS);
        gotoPanel.setLayout(layout);

        JPanel goToLinePanel = getGoToLinePanel();

        String text = "Range - First Line: [" + startIndex + "]        Last Line: [" + endIndex + "]";

        JLabel goToTextLabel = new JLabel(text);
        goToTextLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel messageJLabel = getMessageLabel();

        gotoPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        gotoPanel.add(goToLinePanel);
        gotoPanel.add(Box.createRigidArea(new Dimension(10, 4)));
        gotoPanel.add(goToTextLabel);
        gotoPanel.add(Box.createRigidArea(new Dimension(10, 3)));
        gotoPanel.add(messageJLabel);
        gotoPanel.add(Box.createRigidArea(new Dimension(10, 3)));
        gotoPanel.add(Box.createVerticalGlue());

        gotoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        return gotoPanel;
    }

    private JPanel getGoToLinePanel() {

        JPanel goToLineJPanel = new JPanel();

        LayoutManager layout = new BoxLayout(goToLineJPanel, BoxLayout.X_AXIS);
        goToLineJPanel.setLayout(layout);

        JLabel goToLineJLabel = new JLabel("Go to line:");

        JTextField goToLineJTextField = getGoToLineTextField();

        Dimension dim = new Dimension(10, 26);
        goToLineJPanel.add(Box.createHorizontalGlue());
        goToLineJPanel.add(Box.createRigidArea(dim));
        goToLineJPanel.add(goToLineJLabel);
        goToLineJPanel.add(Box.createRigidArea(dim));
        goToLineJPanel.add(goToLineJTextField);
        goToLineJPanel.add(Box.createRigidArea(dim));
        goToLineJPanel.add(Box.createHorizontalGlue());

        return goToLineJPanel;
    }

    private JPanel getButtonsJPanel() {

        JPanel buttonsJPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS);
        buttonsJPanel.setLayout(layout);

        JButton goToFirstJButton = getGoToFirstButton();
        JButton goToLastJButton = getGoToLastButton();
        JButton okJButton = getOkButton();
        JButton cancelJButton = getCancelButton();

        Dimension dim = new Dimension(10, 40);
        buttonsJPanel.add(Box.createHorizontalGlue());
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(goToFirstJButton);
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(goToLastJButton);
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(okJButton);
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(cancelJButton);
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(Box.createHorizontalGlue());

        buttonsJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        return buttonsJPanel;
    }

    protected void executeGoToLine() {

        try {

            JTextField goToLineJTextField = getGoToLineTextField();
            String goToLineText = goToLineJTextField.getText();
            goToLineText = goToLineText.trim();
            selectedInteger = Integer.parseInt(goToLineText);

            if ((selectedInteger >= startIndex) && (selectedInteger <= endIndex)) {
                dispose();
            } else {

                messageLabel = getMessageLabel();
                messageLabel.setText(message);
                selectedInteger = null;
            }
        } catch (Exception e) {
            messageLabel = getMessageLabel();
            messageLabel.setText(message);
            selectedInteger = null;
        }

    }
}
