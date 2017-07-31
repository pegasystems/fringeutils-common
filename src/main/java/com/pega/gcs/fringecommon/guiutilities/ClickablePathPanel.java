/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.FileUtilities;

public class ClickablePathPanel extends JPanel {

	private static final long serialVersionUID = 692503447362579736L;

	private static final Log4j2Helper LOG = new Log4j2Helper(ClickablePathPanel.class);

	private JEditorPane pathJEditorPane;

	public ClickablePathPanel() {

		super();

		setupUI();

	}

	protected void setupUI() {

		setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(0, 0, 0, 0);

		JEditorPane filePathJEditorPane = getPathJEditorPane();
		add(filePathJEditorPane, gbc1);

	}

	protected JEditorPane getPathJEditorPane() {

		if (pathJEditorPane == null) {

			pathJEditorPane = new JEditorPane();
			pathJEditorPane.setSize(Integer.MAX_VALUE, 30);
			pathJEditorPane.setEditable(false);
			pathJEditorPane.setContentType("text/html");
			pathJEditorPane.setOpaque(false);
			pathJEditorPane.setBackground(this.getBackground());

			StyleSheet styleSheet = FileUtilities.getStyleSheet(this.getClass(), "styles.css");

			if (styleSheet != null) {

				HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
				StyleSheet htmlStyleSheet = htmlEditorKit.getStyleSheet();
				htmlStyleSheet.addStyleSheet(styleSheet);

				pathJEditorPane.setEditorKitForContentType("text/html", htmlEditorKit);
			}

			pathJEditorPane.addHyperlinkListener(new HyperlinkListener() {

				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {

					if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {

						if (Desktop.isDesktopSupported()) {

							Desktop desktop = Desktop.getDesktop();
							try {
								desktop.browse(e.getURL().toURI());
							} catch (Exception ex) {
								LOG.error("Error in invoking browser url: " + e.getURL(), ex);
							}
						}
					}

				}
			});
		}

		return pathJEditorPane;
	}

	public void setUrl(String url) {

		String filePathHtml = GUIUtilities.getHyperlinkText(url);

		JEditorPane filePathJEditorPane = getPathJEditorPane();

		filePathJEditorPane.setText(filePathHtml);
	}

}
