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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;

public class BookmarkDeleteDialog<T extends Comparable<? super T>> extends JDialog {

	private static final long serialVersionUID = -2026873336640757377L;

	private BookmarkModel<T> bookMarkModel;

	private List<T> keyList;

	private JComboBox<T> keyJComboBox;

	private JComboBox<Object> bookmarkJComboBox;

	private JButton okJButton;

	private JButton cancelJButton;

	public BookmarkDeleteDialog(ImageIcon appIcon, Component parent, BookmarkModel<T> bookMarkModel, List<T> keyList) {

		super();

		this.bookMarkModel = bookMarkModel;
		this.keyList = keyList;

		setIconImage(appIcon.getImage());

		setPreferredSize(new Dimension(350, 170));

		setTitle("Delete Bookmark");

		setModalityType(ModalityType.APPLICATION_MODAL);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		setContentPane(getMainJPanel());

		pack();

		setLocationRelativeTo(parent);
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
		JComboBox<Object> bookmarkJComboBox = getBookmarkJComboBox();

		bookmarkJPanel.add(label, gbc1);
		bookmarkJPanel.add(keyJComboBox, gbc2);
		bookmarkJPanel.add(textLabel, gbc3);
		bookmarkJPanel.add(bookmarkJComboBox, gbc4);

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

	private JComboBox<T> getKeyJComboBox() {

		if (keyJComboBox == null) {

			keyJComboBox = new JComboBox<>();

			for (T key : keyList) {
				keyJComboBox.addItem(key);
			}

			Dimension size = new Dimension(140, 20);

			keyJComboBox.setPreferredSize(size);

			keyJComboBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JComboBox<T> keyJComboBox = (JComboBox<T>) e.getSource();
					T key = (T) keyJComboBox.getSelectedItem();

					JComboBox<Object> bookmarkJComboBox = getBookmarkJComboBox();
					bookmarkJComboBox.removeAllItems();

					List<Marker<T>> bookmarkList = bookMarkModel.getMarkers(key);

					for (Marker<T> bookmark : bookmarkList) {
						bookmarkJComboBox.addItem(makeObj(bookmark.getText()));
					}

				}
			});

			keyJComboBox.setSelectedIndex(0);
		}

		return keyJComboBox;
	}

	private JComboBox<Object> getBookmarkJComboBox() {

		if (bookmarkJComboBox == null) {
			bookmarkJComboBox = new JComboBox<>();

			Dimension size = new Dimension(140, 20);
			bookmarkJComboBox.setPreferredSize(size);
		}

		return bookmarkJComboBox;
	}

	private Object makeObj(final String item) {
		return new Object() {
			public String toString() {
				return item;
			}
		};
	}

	private JButton getOkJButton() {

		if (okJButton == null) {

			okJButton = new JButton("Delete");
			okJButton.setToolTipText("Delete");

			Dimension size = new Dimension(70, 26);
			okJButton.setPreferredSize(size);
			okJButton.setSize(size);

			okJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					performDeleteBookMark();
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

	protected void performDeleteBookMark() {

		JComboBox<T> keyJComboBox = getKeyJComboBox();
		T key = (T) keyJComboBox.getSelectedItem();

		JComboBox<Object> bookmarkJComboBox = getBookmarkJComboBox();

		String selectedBookmark = bookmarkJComboBox.getSelectedItem().toString();

		List<Marker<T>> bookmarkList = bookMarkModel.getMarkers(key);

		int index = 0;

		for (Marker<T> bookmark : bookmarkList) {

			if (selectedBookmark.equals(bookmark.getText())) {
				break;
			}

			index++;
		}

		bookMarkModel.removeMarker(key, index);

		dispose();

	}
}
