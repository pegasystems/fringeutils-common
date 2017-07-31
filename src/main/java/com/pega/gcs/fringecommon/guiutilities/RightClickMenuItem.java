/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class RightClickMenuItem extends JMenuItem implements ClipboardOwner {

	private static final long serialVersionUID = 480438577628709354L;

	public RightClickMenuItem(final String title) {
		super(title);

		// // setBorderPainted(false);
		// setContentAreaFilled(false);
		// // setFocusPainted(false);
		setOpaque(true);

		addMouseListener(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseAdapter#mouseEntered(java
			 * .awt.event.MouseEvent)
			 */
			@Override
			public void mouseEntered(MouseEvent e) {
				JMenuItem rcmi = (JMenuItem) e.getSource();
				rcmi.setBackground(UIManager.getColor("MenuItem.selectionBackground"));
				rcmi.setForeground(UIManager.getColor("MenuItem.selectionForeground"));
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.event.MouseAdapter#mouseExited(java.
			 * awt.event.MouseEvent)
			 */
			@Override
			public void mouseExited(MouseEvent e) {
				JMenuItem rcmi = (JMenuItem) e.getSource();
				rcmi.setBackground(UIManager.getColor("MenuItem.background"));
				rcmi.setForeground(UIManager.getColor("MenuItem.foreground"));
			}

		});

		setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub

	}

}
