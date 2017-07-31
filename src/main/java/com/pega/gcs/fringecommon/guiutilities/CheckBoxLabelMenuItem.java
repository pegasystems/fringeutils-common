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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * shows an entry like <code>[x] text (123)<code>
 */
public class CheckBoxLabelMenuItem<T> extends JPanel {

	private static final long serialVersionUID = 1915104163331004973L;

	private JCheckBox itemJCheckBox;

	private JLabel countJLabel;

	private CheckBoxMenuItemPopupEntry<T> checkBoxMenuItemPopupEntry;

	private boolean showCount;

	/**
	 * @param isSelected
	 * @param objectMenuItem
	 */
	public CheckBoxLabelMenuItem(CheckBoxMenuItemPopupEntry<T> checkBoxMenuItemPopupEntry, boolean showCount) {

		super();

		this.checkBoxMenuItemPopupEntry = checkBoxMenuItemPopupEntry;
		this.showCount = showCount;

		JCheckBox checkBox = getItemJCheckBox();
		JLabel countJLabel = getCountJLabel();

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 1.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.WEST;

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0D;
		gbc2.weighty = 1.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.EAST;
		gbc2.insets = new Insets(0, 0, 0, 5);

		setLayout(new GridBagLayout());

		add(checkBox, gbc1);
		add(countJLabel, gbc2);

		setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
	}

	protected CheckBoxMenuItemPopupEntry<T> getCheckBoxMenuItemPopupEntry() {
		return checkBoxMenuItemPopupEntry;
	}

	protected boolean isShowCount() {
		return showCount;
	}

	private JCheckBox getItemJCheckBox() {

		if (itemJCheckBox == null) {
			itemJCheckBox = new JCheckBox();

			itemJCheckBox.setText(checkBoxMenuItemPopupEntry.getText());
			itemJCheckBox.setSelected(checkBoxMenuItemPopupEntry.isSelected());

			// not using the item listener because we want to set the
			// checkBoxMenuItemPopupEntry only when the user selects apply.

			// itemJCheckBox.addItemListener(new ItemListener() {
			//
			// @Override
			// public void itemStateChanged(ItemEvent e) {
			//
			// if (e.getStateChange() == ItemEvent.SELECTED) {
			//
			// checkBoxMenuItemPopupEntry.setSelected(true);
			// checkBoxMenuItemPopupEntry.setVisible(true);
			// } else {
			// checkBoxMenuItemPopupEntry.setSelected(false);
			// checkBoxMenuItemPopupEntry.setVisible(false);
			// }
			//
			// }
			// });
		}

		return itemJCheckBox;
	}

	private JLabel getCountJLabel() {

		if (countJLabel == null) {

			countJLabel = new JLabel() {

				private static final long serialVersionUID = -6727122479133920624L;

				@Override
				public String getText() {

					String text = null;

					if (isShowCount()) {

						String count = String.valueOf(getCheckBoxMenuItemPopupEntry().getFilteredCount());

						text = "(" + count + ")";

					} else {

						text = null;

					}

					return text;
				}

			};

			countJLabel.setHorizontalAlignment(SwingConstants.TRAILING);

		}

		return countJLabel;
	}

	public CheckBoxMenuItemPopupEntry<T> getFilterTableHeaderPopupEntry() {
		return checkBoxMenuItemPopupEntry;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return itemJCheckBox.isSelected();
	}
	//
	// /**
	// * @param isSelected
	// * the isSelected to set
	// */
	// public void setSelected(boolean isSelected) {
	// checkBox.setSelected(isSelected);
	// }

	// public void setLabelText(int traceEventTypeCount) {
	// label.setText("(" + traceEventTypeCount + ")");
	// }

}
