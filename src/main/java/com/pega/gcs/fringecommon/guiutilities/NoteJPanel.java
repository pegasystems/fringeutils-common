/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.pega.gcs.fringecommon.utilities.FileUtilities;

public class NoteJPanel extends JPanel {

	private static final long serialVersionUID = -2038802623485568367L;

	public NoteJPanel(String noteText, int rows) {

		super();

		setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 0.3D;
		gbc1.weighty = 1.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(5, 5, 5, 5);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 0.7D;
		gbc2.weighty = 1.0D;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.insets = new Insets(5, 1, 5, 5);

		JPanel noteImageJPanel = getNoteImageJPanel();
		JTextArea noteTextArea = getNoteTextArea(noteText, rows);
		
		add(noteImageJPanel, gbc1);
		add(noteTextArea, gbc2);

		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
	}

	private JPanel getNoteImageJPanel() {

		ImageIcon noteImage = FileUtilities.getImageIcon(this.getClass(), "loading.png");

		JPanel noteImageJPanel = new ImageJPanel(noteImage.getImage(), false);

		return noteImageJPanel;
	}

	private JTextArea getNoteTextArea(String noteText, int rows) {

		JTextArea noteTextArea = new JTextArea(noteText);

		noteTextArea.setEditable(false);
		noteTextArea.setBackground(null);
		noteTextArea.setLineWrap(true);
		noteTextArea.setWrapStyleWord(true);
		noteTextArea.setFont(this.getFont());
		noteTextArea.setRows(rows);

		return noteTextArea;

	}
}
