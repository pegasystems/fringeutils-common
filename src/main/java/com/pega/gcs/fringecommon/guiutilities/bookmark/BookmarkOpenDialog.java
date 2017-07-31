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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;

public class BookmarkOpenDialog<T extends Comparable<? super T>> extends JDialog {

	private static final long serialVersionUID = -2026873336640757377L;

	private Map<T, List<Marker<T>>> bookmarkListMap;

	private JComboBox<T> keyJComboBox;

	private JPanel bookmarkListJPanel;

	public BookmarkOpenDialog(ImageIcon appIcon, Component parent, Map<T, List<Marker<T>>> bookmarkListMap) {

		super();

		this.bookmarkListMap = bookmarkListMap;

		setIconImage(appIcon.getImage());

		setPreferredSize(new Dimension(350, 170));

		setTitle("Open Bookmark");

		setModalityType(ModalityType.APPLICATION_MODAL);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		setContentPane(getMainJPanel());

		pack();

		setLocationRelativeTo(parent);
	}

	private JPanel getBookmarkListJPanel() {

		if (bookmarkListJPanel == null) {
			bookmarkListJPanel = new JPanel();
			bookmarkListJPanel.setLayout(new GridBagLayout());

		}

		return bookmarkListJPanel;
	}

	private JPanel getMainJPanel() {

		JPanel mainJPanel = new JPanel();
		LayoutManager layout = new BoxLayout(mainJPanel, BoxLayout.Y_AXIS);
		mainJPanel.setLayout(layout);

		JPanel bookmarkJPanel = getBookmarkJPanel();
		JPanel buttonsJPanel = getButtonsJPanel();

		mainJPanel.add(bookmarkJPanel);
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
		JComboBox<T> keyJComboBox = getKeyJComboBox();
		JLabel textLabel = new JLabel("Bookmark:");
		JPanel bookmarkListJPanel = getBookmarkListJPanel();

		bookmarkJPanel.add(label, gbc1);
		bookmarkJPanel.add(keyJComboBox, gbc2);
		bookmarkJPanel.add(textLabel, gbc3);
		bookmarkJPanel.add(bookmarkListJPanel, gbc4);

		bookmarkJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		return bookmarkJPanel;
	}

	private JComboBox<T> getKeyJComboBox() {

		if (keyJComboBox == null) {

			keyJComboBox = new JComboBox<>();

			for (T key : bookmarkListMap.keySet()) {
				keyJComboBox.addItem(key);
			}

			Dimension size = new Dimension(140, 20);

			keyJComboBox.setPreferredSize(size);

			keyJComboBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JComboBox<T> keyJComboBox = (JComboBox<T>) e.getSource();
					T key = (T) keyJComboBox.getSelectedItem();

					populatebookmarkListJPanel(key);
				}
			});

			keyJComboBox.setSelectedIndex(0);
		}

		return keyJComboBox;
	}

	private void populatebookmarkListJPanel(T key) {

		List<Marker<T>> bookmarkList = bookmarkListMap.get(key);
		JPanel bookmarkListJPanel = getBookmarkListJPanel();

		bookmarkListJPanel.removeAll();
		int yIndex = 0;

		for (Marker<T> bookmark : bookmarkList) {

			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 1;
			gbc1.gridy = yIndex++;
			gbc1.weightx = 1.0D;
			gbc1.weighty = 0.0D;
			gbc1.fill = GridBagConstraints.BOTH;
			gbc1.anchor = GridBagConstraints.NORTHWEST;
			gbc1.insets = new Insets(5, 5, 5, 5);

			JTextField textJTextField = getTextJTextField(bookmark);

			bookmarkListJPanel.add(textJTextField, gbc1);

			bookmarkListJPanel.revalidate();

		}

	}

	private JPanel getSingleBookmarkKeyJPanel(Map.Entry<T, List<Marker<T>>> bookmarkKeyEntry) {

		JPanel singleBookmarkKeyJPanel = new JPanel();

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

		T key = bookmarkKeyEntry.getKey();
		List<Marker<T>> bookmarkList = bookmarkKeyEntry.getValue();

		JLabel label = new JLabel("Key:");
		JLabel keyJLabel = new JLabel(key.toString());
		JLabel textLabel = new JLabel("Bookmark:");

		singleBookmarkKeyJPanel.add(label, gbc1);
		singleBookmarkKeyJPanel.add(keyJLabel, gbc2);
		singleBookmarkKeyJPanel.add(textLabel, gbc3);

		int yIndex = 2;

		for (Marker<T> bookmark : bookmarkList) {

			GridBagConstraints gbc4 = new GridBagConstraints();
			gbc4.gridx = 1;
			gbc4.gridy = yIndex++;
			gbc4.weightx = 1.0D;
			gbc4.weighty = 0.0D;
			gbc4.fill = GridBagConstraints.BOTH;
			gbc4.anchor = GridBagConstraints.NORTHWEST;
			gbc4.insets = new Insets(5, 5, 5, 5);

			JTextField textJTextField = getTextJTextField(bookmark);

			singleBookmarkKeyJPanel.add(textJTextField, gbc4);

		}

		return singleBookmarkKeyJPanel;
	}

	private JPanel getButtonsJPanel() {
		JPanel buttonsJPanel = new JPanel();

		LayoutManager layout = new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS);
		buttonsJPanel.setLayout(layout);

		JButton okJButton = getOkJButton();

		Dimension dim = new Dimension(5, 30);
		buttonsJPanel.add(Box.createHorizontalGlue());
		buttonsJPanel.add(Box.createRigidArea(dim));
		buttonsJPanel.add(okJButton);
		buttonsJPanel.add(Box.createRigidArea(dim));
		buttonsJPanel.add(Box.createHorizontalGlue());

		buttonsJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		return buttonsJPanel;
	}

	private JTextField getTextJTextField(Marker<T> bookmark) {

		JTextField textJTextField = new JTextField(bookmark.toString());

		Dimension size = new Dimension(140, 20);

		textJTextField.setMinimumSize(size);
		textJTextField.setPreferredSize(size);
		textJTextField.setMaximumSize(size);

		textJTextField.setEnabled(false);

		return textJTextField;
	}

	private JButton getOkJButton() {

		JButton okJButton = new JButton("OK");
		okJButton.setToolTipText("OK");

		Dimension size = new Dimension(70, 26);
		okJButton.setPreferredSize(size);
		okJButton.setSize(size);

		okJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});

		return okJButton;
	}

}
