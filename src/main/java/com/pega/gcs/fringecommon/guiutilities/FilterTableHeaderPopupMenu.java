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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.pega.gcs.fringecommon.utilities.FileUtilities;

public abstract class FilterTableHeaderPopupMenu<T> extends JPopupMenu {

	private static final long serialVersionUID = 3225142996534047809L;

	private static final String MATCH_CASE_ON_ACTION = "Match case on";

	private static final String MATCH_CASE_OFF_ACTION = "Match case Off";

	public abstract void applyJButtonAction();

	// original copy
	private final Set<CheckBoxMenuItemPopupEntry<T>> filterTableHeaderColumnEntrySet;

	// display copy
	private ArrayList<CheckBoxMenuItemPopupEntry<T>> filteredTableHeaderColumnEntrySet;

	private List<CheckBoxLabelMenuItem<T>> checkBoxLabelMenuItemList;

	private JPanel checkBoxLabelMenuItemListJPanel;

	private JTextField searchJTextField;

	private boolean caseSensitive;

	private ImageIcon matchCaseOnIcon;

	private ImageIcon matchCaseOffIcon;

	public FilterTableHeaderPopupMenu(Set<CheckBoxMenuItemPopupEntry<T>> filterTableHeaderColumnEntrySet) {

		super();

		// original copy
		this.filterTableHeaderColumnEntrySet = filterTableHeaderColumnEntrySet;
		// display copy
		this.filteredTableHeaderColumnEntrySet = new ArrayList<CheckBoxMenuItemPopupEntry<T>>(
				filterTableHeaderColumnEntrySet);

		this.checkBoxLabelMenuItemList = new ArrayList<CheckBoxLabelMenuItem<T>>();
		this.checkBoxLabelMenuItemListJPanel = null;
		this.caseSensitive = false;

		matchCaseOnIcon = FileUtilities.getImageIcon(this.getClass(), "matchcaseon.png");

		matchCaseOffIcon = FileUtilities.getImageIcon(this.getClass(), "matchcaseoff.png");

		setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(2, 0, 2, 0);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.weightx = 1.0D;
		gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.insets = new Insets(2, 0, 2, 0);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 2;
		gbc3.weightx = 1.0D;
		gbc3.weighty = 1.0D;
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.anchor = GridBagConstraints.NORTHWEST;
		gbc3.insets = new Insets(2, 0, 2, 0);

		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.gridx = 0;
		gbc4.gridy = 3;
		gbc4.weightx = 1.0D;
		gbc4.weighty = 0.0D;
		gbc4.fill = GridBagConstraints.BOTH;
		gbc4.anchor = GridBagConstraints.CENTER;
		gbc4.insets = new Insets(2, 0, 2, 0);

		JPanel clearAllButtonJPanel = getClearAllButtonJPanel();
		JPanel searchJPanel = getSearchJPanel();
		JComponent checkBoxLabelMenuItemListJComponent = getCheckBoxLabelMenuItemListJComponent();
		JPanel applyCancelButtonJPanel = getApplyCancelButtonJPanel();

		add(clearAllButtonJPanel, gbc1);
		add(searchJPanel, gbc2);
		add(checkBoxLabelMenuItemListJComponent, gbc3);
		add(applyCancelButtonJPanel, gbc4);

	}

	protected boolean isCaseSensitive() {
		return caseSensitive;
	}

	protected void setCaseSensitive(boolean aCaseSensitive) {
		caseSensitive = aCaseSensitive;
	}

	protected ImageIcon getMatchCaseOnIcon() {
		return matchCaseOnIcon;
	}

	protected ImageIcon getMatchCaseOffIcon() {
		return matchCaseOffIcon;
	}

	private JPanel getClearAllButtonJPanel() {

		JPanel clearAllButtonJPanel = new JPanel();

		clearAllButtonJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		// gbc1.weightx = 0.0D;
		// gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.CENTER;
		gbc1.insets = new Insets(0, 0, 0, 3);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		// gbc2.weightx = 0.0D;
		// gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.insets = new Insets(0, 3, 0, 0);

		JButton selectAllJButton = getSelectAllJButton();
		JButton clearAllJButton = getClearAllJButton();

		clearAllButtonJPanel.add(selectAllJButton, gbc1);
		clearAllButtonJPanel.add(clearAllJButton, gbc2);

		return clearAllButtonJPanel;
	}

	private JButton getSelectAllJButton() {
		JButton selectAllJButton = new JButton("Select All");

		Dimension size = new Dimension(80, 20);
		selectAllJButton.setPreferredSize(size);
		selectAllJButton.setMinimumSize(size);
		selectAllJButton.setMaximumSize(size);

		selectAllJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				applySelectAll();
			}
		});

		return selectAllJButton;
	}

	private JButton getClearAllJButton() {
		JButton clearAllJButton = new JButton("Clear All");

		Dimension size = new Dimension(80, 20);
		clearAllJButton.setPreferredSize(size);
		clearAllJButton.setMinimumSize(size);
		clearAllJButton.setMaximumSize(size);

		clearAllJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// applyColumnHeaderFilter(columnIndex, true);
				// clearAllJButtonAction();
				//
				// setVisible(false);
				applyClearAll();
			}
		});

		return clearAllJButton;
	}

	private JPanel getSearchJPanel() {

		JPanel searchJPanel = new JPanel();
		searchJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 0.0D;
		gbc1.weighty = 1.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		gbc1.insets = new Insets(0, 2, 0, 1);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0D;
		gbc2.weighty = 1.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		gbc2.insets = new Insets(0, 1, 0, 2);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 2;
		gbc3.gridy = 0;
		gbc3.weightx = 0.0D;
		gbc3.weighty = 1.0D;
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.anchor = GridBagConstraints.NORTHWEST;
		gbc3.insets = new Insets(0, 1, 0, 2);

		JLabel searchJLabel = new JLabel("Search");
		JTextField searchJTextField = getSearchJTextField();
		JButton caseSensitiveJButton = getCaseSensitiveJButton();

		searchJPanel.add(searchJLabel, gbc1);
		searchJPanel.add(searchJTextField, gbc2);
		searchJPanel.add(caseSensitiveJButton, gbc3);

		return searchJPanel;
	}

	private JTextField getSearchJTextField() {

		if (searchJTextField == null) {

			searchJTextField = new JTextField();
			searchJTextField.setEditable(true);

			// Dimension dim = new Dimension(150, 22);
			// searchJTextField.setPreferredSize(dim);
			// searchJTextField.setMaximumSize(dim);

			searchJTextField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// do nothing
				}

				@Override
				public void keyReleased(KeyEvent e) {

					if (e.getSource() instanceof JTextField) {

						applySearchText();

						// revalidate();
						// repaint();
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// do nothing
				}
			});
		}

		return searchJTextField;
	}

	private JButton getCaseSensitiveJButton() {

		final JButton caseSensitiveJButton = new JButton(matchCaseOnIcon);
		caseSensitiveJButton.setActionCommand(MATCH_CASE_ON_ACTION);
		caseSensitiveJButton.setToolTipText(MATCH_CASE_ON_ACTION);

		Dimension size = new Dimension(25, 20);
		caseSensitiveJButton.setPreferredSize(size);

		caseSensitiveJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (MATCH_CASE_ON_ACTION.equals(e.getActionCommand())) {

					setCaseSensitive(true);

					caseSensitiveJButton.setActionCommand(MATCH_CASE_OFF_ACTION);
					caseSensitiveJButton.setIcon(getMatchCaseOffIcon());
					caseSensitiveJButton.setToolTipText(MATCH_CASE_OFF_ACTION);

				} else {

					setCaseSensitive(false);

					caseSensitiveJButton.setActionCommand(MATCH_CASE_ON_ACTION);
					caseSensitiveJButton.setIcon(getMatchCaseOnIcon());
					caseSensitiveJButton.setToolTipText(MATCH_CASE_ON_ACTION);
				}

				applySearchText();
			}
		});

		return caseSensitiveJButton;
	}

	public JComponent getCheckBoxLabelMenuItemListJComponent() {

		JPanel checkBoxLabelMenuItemListJPanel = getCheckBoxLabelMenuItemListJPanel();

		JScrollPane jScrollPane = new JScrollPane(checkBoxLabelMenuItemListJPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		int vBarWidth = jScrollPane.getVerticalScrollBar().getPreferredSize().width;
		int hBarHeight = jScrollPane.getHorizontalScrollBar().getPreferredSize().height
				+ 3/* border size */;

		int compWidth = checkBoxLabelMenuItemListJPanel.getPreferredSize().width;
		int compHeight = checkBoxLabelMenuItemListJPanel.getPreferredSize().height;

		int newCompWidth = compWidth + vBarWidth;
		int newCompHeight = compHeight + hBarHeight;

		Dimension newDim = new Dimension(newCompWidth, newCompHeight);

		jScrollPane.setPreferredSize(newDim);

		jScrollPane.getVerticalScrollBar().setUnitIncrement(14);

		return jScrollPane;
	}

	private JPanel getCheckBoxLabelMenuItemListJPanel() {

		if (checkBoxLabelMenuItemListJPanel == null) {

			checkBoxLabelMenuItemListJPanel = new JPanel();
			checkBoxLabelMenuItemListJPanel.setLayout(new GridBagLayout());

			populateCheckBoxLabelMenuItemListJPanel();

		}

		return checkBoxLabelMenuItemListJPanel;
	}

	private void populateCheckBoxLabelMenuItemListJPanel() {

		JPanel checkBoxLabelMenuItemListJPanel = getCheckBoxLabelMenuItemListJPanel();

		checkBoxLabelMenuItemList.clear();
		checkBoxLabelMenuItemListJPanel.removeAll();

		int index = 0;

		for (CheckBoxMenuItemPopupEntry<T> filterTableHeaderColumnEntry : filteredTableHeaderColumnEntrySet) {

			if (filterTableHeaderColumnEntry.isVisible()) {

				GridBagConstraints gbc1 = new GridBagConstraints();
				gbc1.gridx = 0;
				gbc1.gridy = index;
				gbc1.weightx = 1.0D;
				gbc1.weighty = 0.0D;
				gbc1.fill = GridBagConstraints.BOTH;
				gbc1.anchor = GridBagConstraints.NORTHWEST;
				gbc1.insets = new Insets(0, 0, 0, 0);

				CheckBoxLabelMenuItem<T> cblmi = new CheckBoxLabelMenuItem<T>(filterTableHeaderColumnEntry, true);

				checkBoxLabelMenuItemList.add(cblmi);
				checkBoxLabelMenuItemListJPanel.add(cblmi, gbc1);

				index++;
			}
		}

		revalidate();
		repaint();
	}

	private JPanel getApplyCancelButtonJPanel() {

		JPanel applyCancelButtonJPanel = new JPanel();

		applyCancelButtonJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 0.0D;
		gbc1.weighty = 0.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.CENTER;
		gbc1.insets = new Insets(0, 10, 0, 10);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 0.0D;
		gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.insets = new Insets(0, 10, 0, 10);

		JButton applyJButton = getApplyJButton();
		JButton cancelJButton = getCancelJButton();

		applyCancelButtonJPanel.add(applyJButton, gbc1);

		applyCancelButtonJPanel.add(cancelJButton, gbc2);

		return applyCancelButtonJPanel;
	}

	private JButton getApplyJButton() {

		JButton applyJButton = new JButton("Apply");

		Dimension size = new Dimension(70, 20);
		applyJButton.setPreferredSize(size);
		applyJButton.setMinimumSize(size);
		applyJButton.setMaximumSize(size);

		applyJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// applyColumnHeaderFilter(columnIndex, false);
				// applyJButtonAction();
				apply();
				setVisible(false);
			}
		});

		return applyJButton;
	}

	private JButton getCancelJButton() {

		JButton cancelJButton = new JButton("Cancel");

		Dimension size = new Dimension(70, 20);
		cancelJButton.setPreferredSize(size);
		cancelJButton.setMinimumSize(size);
		cancelJButton.setMaximumSize(size);

		cancelJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		return cancelJButton;
	}

	protected void applySelectAll() {

		for (CheckBoxMenuItemPopupEntry<T> checkBoxMenuItemPopupEntry : filteredTableHeaderColumnEntrySet) {

			checkBoxMenuItemPopupEntry.setSelected(true);

		}

		populateCheckBoxLabelMenuItemListJPanel();
	}

	protected void applyClearAll() {

		for (CheckBoxMenuItemPopupEntry<T> checkBoxMenuItemPopupEntry : filteredTableHeaderColumnEntrySet) {

			checkBoxMenuItemPopupEntry.setSelected(false);

		}

		populateCheckBoxLabelMenuItemListJPanel();
	}

	protected void applySearchText() {

		filteredTableHeaderColumnEntrySet.clear();

		JTextField searchJTextField = getSearchJTextField();

		String searchText = searchJTextField.getText().trim();

		if ((searchText != null) && (!"".equals(searchText))) {

			for (CheckBoxMenuItemPopupEntry<T> checkBoxMenuItemPopupEntry : filterTableHeaderColumnEntrySet) {

				String entryText = checkBoxMenuItemPopupEntry.getText();

				if (!caseSensitive) {
					searchText = searchText.toUpperCase();
					entryText = entryText.toUpperCase();
				}

				if (entryText.indexOf(searchText) != -1) {
					filteredTableHeaderColumnEntrySet.add(checkBoxMenuItemPopupEntry);
				}

			}

		} else {
			filteredTableHeaderColumnEntrySet.addAll(filterTableHeaderColumnEntrySet);
		}

		populateCheckBoxLabelMenuItemListJPanel();
	}

	protected void apply() {

		// Dissociating the checkbox action with the underlying object. the
		// action with be completed when user confirms
		for (CheckBoxLabelMenuItem<T> cblmi : checkBoxLabelMenuItemList) {
			CheckBoxMenuItemPopupEntry<T> fthcEntry;

			fthcEntry = (CheckBoxMenuItemPopupEntry<T>) cblmi.getFilterTableHeaderPopupEntry();

			boolean selected = cblmi.isSelected();
			fthcEntry.setSelected(selected);
			fthcEntry.setVisible(selected);
		}

		applyJButtonAction();

	}
}
