/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.SystemUtils;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class ClickableFilePathPanel extends ClickablePathPanel {

	private static final long serialVersionUID = 692503447362579736L;

	private static final Log4j2Helper LOG = new Log4j2Helper(ClickableFilePathPanel.class);

	private JButton openFolderJButton;

	private JLabel fileSizeJLabel;

	private boolean showFileSize;

	public ClickableFilePathPanel(boolean showFileSize) {

		this.showFileSize = showFileSize;

		setupUI();
	}

	@Override
	protected void setupUI() {

		setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(2, 2, 2, 2);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 0.0D;
		gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.insets = new Insets(2, 2, 2, 2);

		JEditorPane filePathJEditorPane = getPathJEditorPane();
		JButton openFolderJButton = getOpenFolderJButton();

		add(filePathJEditorPane, gbc1);
		add(openFolderJButton, gbc2);

		if (showFileSize) {

			GridBagConstraints gbc3 = new GridBagConstraints();
			gbc3.gridx = 2;
			gbc3.gridy = 0;
			gbc3.weightx = 0.0D;
			gbc3.weighty = 0.0D;
			gbc3.fill = GridBagConstraints.BOTH;
			gbc3.anchor = GridBagConstraints.NORTHWEST;
			gbc3.insets = new Insets(2, 2, 2, 2);

			JLabel fileSizeJLabel = getFileSizeJLabel();
			add(fileSizeJLabel, gbc3);
		}
	}

	private JButton getOpenFolderJButton() {

		if (openFolderJButton == null) {

			openFolderJButton = new JButton("Open Folder");

			openFolderJButton.setEnabled(false);

			openFolderJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					String filePath = e.getActionCommand();

					Runtime runtime = Runtime.getRuntime();
					try {
						if (SystemUtils.IS_OS_MAC) {
							runtime.exec("open -R " + filePath);
						} else if (SystemUtils.IS_OS_UNIX) {
							runtime.exec("xdg-open " + filePath);
						} else {
							runtime.exec("explorer.exe /select," + filePath);
						}

					} catch (Exception ex) {
						LOG.error("Error opening file explorer", ex);
					}
				}
			});

		}
		return openFolderJButton;
	}

	private JLabel getFileSizeJLabel() {

		if (fileSizeJLabel == null) {

			fileSizeJLabel = new JLabel();

			Dimension dim = new Dimension(60, 20);
			fileSizeJLabel.setPreferredSize(dim);
			fileSizeJLabel.setMinimumSize(dim);

			fileSizeJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		}

		return fileSizeJLabel;
	}

	public void setFile(File aFile) {

		JEditorPane filePathJEditorPane = getPathJEditorPane();
		JButton openFolderJButton = getOpenFolderJButton();

		if ((aFile != null) && (aFile.exists())) {

			String filePath = aFile.getAbsolutePath();
			String filePathHtml = GUIUtilities.getFileHyperlinkText(filePath);

			filePathJEditorPane.setText(filePathHtml);

			openFolderJButton.setActionCommand(filePath);
			openFolderJButton.setEnabled(true);

			if (showFileSize) {

				JLabel fileSizeJLabel = getFileSizeJLabel();
				String fileSizeStr = null;
				long fileSize = -1;

				try (FileInputStream fis = new FileInputStream(aFile)) {

					FileChannel fileChannel = fis.getChannel();
					fileSize = fileChannel.size();
					fileSizeStr = GeneralUtilities.humanReadableSize(fileSize, false);

					fileSizeJLabel.setText(fileSizeStr);

				} catch (Exception e) {
					LOG.error("Error parsing filesize:" + fileSize, e);

					fileSizeJLabel.setText(fileSizeStr);
				}
			}
		} else {

			filePathJEditorPane.setText(null);
			openFolderJButton.setActionCommand(null);
			openFolderJButton.setEnabled(false);

			if (showFileSize) {
				JLabel fileSizeJLabel = getFileSizeJLabel();
				fileSizeJLabel.setText(null);
			}
		}
	}
}
