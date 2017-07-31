/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 7949153479786349366L;

	private Image aboutImage;

	private String appStr;

	/**
	 * @param aboutImage
	 * @param appStr
	 */
	public AboutDialog(Image aboutImage, String appStr) {
		super();
		this.aboutImage = aboutImage;
		this.appStr = appStr;

		setContentPane(getMainJPanel());
		setPreferredSize(new Dimension(300, 320));
		setTitle("About Fringe Utilities");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		pack();

		setLocationRelativeTo(null);

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

}
