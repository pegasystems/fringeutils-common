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

	private JTextField goToLineJTextField;

	private JButton goToFirstJButton;

	private JButton goToLastJButton;

	private JButton okJButton;

	private JButton cancelJButton;

	private JLabel messageJLabel;

	public GoToLineDialog(int startIndex, int endIndex, ImageIcon appIcon, Component parent) throws HeadlessException {

		super();

		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.selectedInteger = null;

		setIconImage(appIcon.getImage());

		setPreferredSize(new Dimension(300, 133));

		setTitle("Go to line");
		// setResizable(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		// setAlwaysOnTop(true);

		setContentPane(getMainJPanel());

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

	protected void setSelectedInteger(Integer aSelectedInteger) {
		selectedInteger = aSelectedInteger;
	}

	private JPanel getMainJPanel() {

		JPanel mainJPanel = new JPanel();

		LayoutManager layout = new BoxLayout(mainJPanel, BoxLayout.Y_AXIS);
		mainJPanel.setLayout(layout);

		JPanel goToLineJPanel = getGoToLineJPanel();

		String text = "Range - First Line: [" + startIndex + "] Last Line: [" + endIndex + "]";

		JLabel goToTextJLabel = new JLabel(text);
		goToTextJLabel.setAlignmentX(CENTER_ALIGNMENT);

		JLabel messageJLabel = getMessageJLabel();

		JPanel buttonsJPanel = getButtonsJPanel();

		// mainJPanel.add(Box.createVerticalGlue());
		mainJPanel.add(Box.createRigidArea(new Dimension(4, 5)));
		mainJPanel.add(goToLineJPanel);
		mainJPanel.add(Box.createRigidArea(new Dimension(4, 4)));
		mainJPanel.add(goToTextJLabel);
		mainJPanel.add(Box.createRigidArea(new Dimension(4, 1)));
		mainJPanel.add(messageJLabel);
		mainJPanel.add(Box.createRigidArea(new Dimension(4, 2)));
		mainJPanel.add(Box.createVerticalGlue());
		mainJPanel.add(buttonsJPanel);
		mainJPanel.add(Box.createRigidArea(new Dimension(4, 4)));
		// mainJPanel.add(Box.createHorizontalGlue());

		return mainJPanel;
	}

	/**
	 * @return the selectedInteger
	 */
	public Integer getSelectedInteger() {
		return selectedInteger;
	}

	/**
	 * @return the goToLineJTextField
	 */
	private JTextField getGoToLineJTextField() {

		if (goToLineJTextField == null) {
			goToLineJTextField = new JTextField();

			goToLineJTextField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent event) {
					if (event.getKeyChar() == KeyEvent.VK_ENTER) {
						executeGoToLine();
					}
				}
			});
		}

		return goToLineJTextField;
	}

	/**
	 * @return the goToFirstJButton
	 */
	private JButton getGoToFirstJButton() {

		if (goToFirstJButton == null) {

			goToFirstJButton = new JButton("First");
			goToFirstJButton.setToolTipText("First Line");

			Dimension size = new Dimension(70, 26);
			goToFirstJButton.setPreferredSize(size);
			goToFirstJButton.setSize(size);

			goToFirstJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedInteger(getStartIndex());
					dispose();
				}
			});
		}

		return goToFirstJButton;
	}

	/**
	 * @return the goToLastJButton
	 */
	private JButton getGoToLastJButton() {

		if (goToLastJButton == null) {

			goToLastJButton = new JButton("Last");
			goToLastJButton.setToolTipText("Last Line");

			Dimension size = new Dimension(70, 26);
			goToLastJButton.setPreferredSize(size);
			goToLastJButton.setSize(size);

			goToLastJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedInteger(getEndIndex());
					dispose();
				}
			});
		}

		return goToLastJButton;
	}

	/**
	 * @return the okJButton
	 */
	private JButton getOkJButton() {

		if (okJButton == null) {

			okJButton = new JButton("OK");
			okJButton.setToolTipText("OK");

			Dimension size = new Dimension(70, 26);
			okJButton.setPreferredSize(size);
			okJButton.setSize(size);

			okJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					executeGoToLine();
				}
			});

		}

		return okJButton;
	}

	/**
	 * @return the cancelJButton
	 */
	private JButton getCancelJButton() {

		if (cancelJButton == null) {

			cancelJButton = new JButton("Cancel");
			cancelJButton.setToolTipText("Cancel");

			Dimension size = new Dimension(70, 26);
			cancelJButton.setPreferredSize(size);
			cancelJButton.setSize(size);

			cancelJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					dispose();
				}
			});
		}

		return cancelJButton;
	}

	private JLabel getMessageJLabel() {

		if (messageJLabel == null) {
			messageJLabel = new JLabel();

			Dimension dim = new Dimension(100, 16);
			messageJLabel.setPreferredSize(dim);
			messageJLabel.setMinimumSize(dim);

			messageJLabel.setAlignmentX(CENTER_ALIGNMENT);
			messageJLabel.setForeground(Color.RED);
		}

		return messageJLabel;
	}

	private JPanel getGoToLineJPanel() {
		JPanel goToLineJPanel = new JPanel();
		LayoutManager layout = new BoxLayout(goToLineJPanel, BoxLayout.X_AXIS);
		goToLineJPanel.setLayout(layout);

		JLabel goToLineJLabel = new JLabel("Go to line:");

		JTextField goToLineJTextField = getGoToLineJTextField();

		Dimension dim = new Dimension(10, 1);
		goToLineJPanel.add(Box.createHorizontalGlue());
		goToLineJPanel.add(Box.createRigidArea(dim));
		goToLineJPanel.add(goToLineJLabel);
		goToLineJPanel.add(Box.createRigidArea(dim));
		goToLineJPanel.add(goToLineJTextField);
		goToLineJPanel.add(Box.createRigidArea(dim));
		goToLineJPanel.add(Box.createHorizontalGlue());

		// goToLineJPanel.setBorder(BorderFactory.createLineBorder(
		// MyColor.LIGHT_GRAY, 1));

		return goToLineJPanel;
	}

	private JPanel getButtonsJPanel() {

		JPanel buttonsJPanel = new JPanel();

		LayoutManager layout = new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS);
		buttonsJPanel.setLayout(layout);

		JButton goToFirstJButton = getGoToFirstJButton();
		JButton goToLastJButton = getGoToLastJButton();
		JButton okJButton = getOkJButton();
		JButton cancelJButton = getCancelJButton();

		Dimension dim = new Dimension(5, 30);
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

			JTextField goToLineJTextField = getGoToLineJTextField();
			String goToLineText = goToLineJTextField.getText();
			goToLineText = goToLineText.trim();
			selectedInteger = Integer.parseInt(goToLineText);

			if ((selectedInteger >= startIndex) && (selectedInteger <= endIndex)) {
				dispose();
			} else {

				messageJLabel = getMessageJLabel();
				messageJLabel.setText(message);
				selectedInteger = null;
			}
		} catch (Exception e) {
			messageJLabel = getMessageJLabel();
			messageJLabel.setText(message);
			selectedInteger = null;
		}

	}
}
