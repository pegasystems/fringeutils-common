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

public abstract class BookmarkAddDialog<T> extends JDialog {

	private static final long serialVersionUID = -2026873336640757377L;

	private BookmarkModel<T> bookmarkModel;

	private List<T> keyList;

	private JLabel keyJLabel;

	private JTextField textJTextField;

	private JButton okJButton;

	private JButton cancelJButton;

	private JLabel messageJLabel;

	public abstract List<Marker<T>> getMarkerList(List<T> keyList, String text);

	public BookmarkAddDialog(ImageIcon appIcon, Component parent, BookmarkModel<T> bookMarkModel, List<T> keyList) {

		super();

		this.bookmarkModel = bookMarkModel;
		this.keyList = keyList;

		setIconImage(appIcon.getImage());

		setPreferredSize(new Dimension(350, 170));

		setTitle("Add Bookmark");

		setModalityType(ModalityType.APPLICATION_MODAL);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		setContentPane(getMainJPanel());

		pack();

		setLocationRelativeTo(parent);

		// setVisible called by caller.
		// setVisible(true);
	}

	private JPanel getMainJPanel() {

		JPanel mainJPanel = new JPanel();
		LayoutManager layout = new BoxLayout(mainJPanel, BoxLayout.Y_AXIS);
		mainJPanel.setLayout(layout);

		JPanel bookmarkJPanel = getBookmarkJPanel();
		JLabel messageJLabel = getMessageJLabel();
		JPanel buttonsJPanel = getButtonsJPanel();

		mainJPanel.add(bookmarkJPanel);
		mainJPanel.add(messageJLabel);
		mainJPanel.add(buttonsJPanel);

		return mainJPanel;
	}

	private JPanel getBookmarkJPanel() {

		JPanel bookmarkJPanel = new JPanel();
		bookmarkJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 0.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(5, 5, 5, 5);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0D;
		gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.insets = new Insets(5, 5, 5, 5);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 1;
		gbc3.weightx = 0.0D;
		gbc3.weighty = 0.0D;
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.anchor = GridBagConstraints.NORTHWEST;
		gbc3.insets = new Insets(5, 5, 5, 5);

		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 1;
		gbc4.gridy = 1;
		gbc4.weightx = 1.0D;
		gbc4.weighty = 0.0D;
		gbc4.fill = GridBagConstraints.BOTH;
		gbc4.anchor = GridBagConstraints.NORTHWEST;
		gbc4.insets = new Insets(5, 5, 5, 5);

		JLabel label = new JLabel("Key:");
		JLabel keyJLabel = getKeyJLabel();
		JLabel textLabel = new JLabel("Bookmark:");
		JTextField textJTextField = getTextJTextField();

		bookmarkJPanel.add(label, gbc1);
		bookmarkJPanel.add(keyJLabel, gbc2);
		bookmarkJPanel.add(textLabel, gbc3);
		bookmarkJPanel.add(textJTextField, gbc4);

		bookmarkJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		return bookmarkJPanel;
	}

	private JPanel getButtonsJPanel() {
		JPanel buttonsJPanel = new JPanel();

		LayoutManager layout = new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS);
		buttonsJPanel.setLayout(layout);

		JButton okJButton = getOkJButton();
		JButton cancelJButton = getCancelJButton();

		Dimension dim = new Dimension(5, 30);
		buttonsJPanel.add(Box.createHorizontalGlue());
		buttonsJPanel.add(Box.createRigidArea(dim));
		buttonsJPanel.add(okJButton);
		buttonsJPanel.add(Box.createRigidArea(dim));
		buttonsJPanel.add(cancelJButton);
		buttonsJPanel.add(Box.createRigidArea(dim));
		buttonsJPanel.add(Box.createHorizontalGlue());

		buttonsJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		return buttonsJPanel;
	}

	private JLabel getKeyJLabel() {

		if (keyJLabel == null) {

			StringBuffer sb = new StringBuffer();

			boolean isfirst = true;

			for (T key : keyList) {

				if (!isfirst) {
					sb.append(",");
				}

				isfirst = false;
				sb.append(key.toString());

			}

			keyJLabel = new JLabel(sb.toString());
		}

		return keyJLabel;
	}

	private JTextField getTextJTextField() {

		if (textJTextField == null) {
			textJTextField = new JTextField();

			Dimension size = new Dimension(140, 20);

			textJTextField.setMinimumSize(size);
			textJTextField.setPreferredSize(size);
			textJTextField.setMaximumSize(size);

			textJTextField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent event) {
					if (event.getKeyChar() == KeyEvent.VK_ENTER) {
						performAddBookMark();
					}
				}
			});
		}

		return textJTextField;
	}

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
					performAddBookMark();
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

	protected void performAddBookMark() {

		// Marker<T> existingBookmark;

		// existingBookmark = bookmarkModel.getMarker(key);

		// if (existingBookmark == null) {

		JTextField textJTextField = getTextJTextField();
		String text = textJTextField.getText();
		text = text.trim();

		List<Marker<T>> bookmarkList = getMarkerList(keyList, text);

		for (Marker<T> bookmark : bookmarkList) {
			bookmarkModel.addMarker(bookmark);
		}

		dispose();
		// } else {
		// String text = existingBookmark.getText();
		// text = "A bookmark on this key already exists having text: " + text;
		//
		// JLabel messageJLabel = getMessageJLabel();
		// messageJLabel.setText(text);
		// }
	}
}
