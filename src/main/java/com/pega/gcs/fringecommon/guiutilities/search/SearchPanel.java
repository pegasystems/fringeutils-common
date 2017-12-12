/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.pega.gcs.fringecommon.guiutilities.ModalProgressMonitor;
import com.pega.gcs.fringecommon.guiutilities.NavigationTableController;
import com.pega.gcs.fringecommon.guiutilities.Searchable.SelectedRowPosition;
import com.pega.gcs.fringecommon.utilities.FileUtilities;

public class SearchPanel<T> extends JPanel implements SearchModelListener<T> {

	private static final long serialVersionUID = -2404000689650703202L;

	public static final String PAGES_FORMAT_STR = "%d of %d";

	private static final String SEARCH_ENABLE_WRAP = "Enable Search Results Wrap";

	private static final String SEARCH_DISABLE_WRAP = "Disable Search Results Wrap";

	private NavigationTableController<T> navigationTableController;

	private SearchModel<T> searchModel;

	private ImageIcon firstImageIcon;

	private ImageIcon lastImageIcon;

	private ImageIcon prevImageIcon;

	private ImageIcon nextImageIcon;

	private ImageIcon enableWrapImageIcon;

	private ImageIcon disableWrapImageIcon;

	private JComboBox<Object> searchJComboBox;

	private JButton searchJButton;

	private JButton searchClearJButton;

	private JButton searchPrevJButton;

	private JButton searchNextJButton;

	private JButton searchFirstJButton;

	private JButton searchLastJButton;

	private JButton searchWrapJButton;

	private JLabel searchNavIndexJLabel;

	public SearchPanel(NavigationTableController<T> navigationTableController, SearchModel<T> searchModel) {

		super();

		this.navigationTableController = navigationTableController;
		this.searchModel = searchModel;

		// search model will fire events when search task is completed. for
		// updating the nav + search results
		searchModel.addSearchModelListener(this);

		initialize();

	}

	protected ImageIcon getEnableWrapImageIcon() {
		return enableWrapImageIcon;
	}

	protected ImageIcon getDisableWrapImageIcon() {
		return disableWrapImageIcon;
	}

	@Override
	public void searchResultChanged(SearchModelEvent<T> searchModelEvent) {

		if (searchModelEvent.getType() == SearchModelEvent.RESET_MODEL) {
			refreshSearchJComboBox();
		}

		updateSearchNavIndexDetails();
	}

	private void initialize() {

		firstImageIcon = FileUtilities.getImageIcon(this.getClass(), "first.png");

		lastImageIcon = FileUtilities.getImageIcon(this.getClass(), "last.png");

		prevImageIcon = FileUtilities.getImageIcon(this.getClass(), "prev.png");

		nextImageIcon = FileUtilities.getImageIcon(this.getClass(), "next.png");

		enableWrapImageIcon = FileUtilities.getImageIcon(this.getClass(), "enablewrap.png");

		disableWrapImageIcon = FileUtilities.getImageIcon(this.getClass(), "disablewrap.png");

		LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);

		JLabel searchJLabel = new JLabel("Search: ");
		JComboBox<Object> searchJComboBox = getSearchJComboBox();
		JButton searchJButton = getSearchJButton();
		JButton searchClearJButton = getSearchClearJButton();
		JButton searchFirstJButton = getSearchFirstJButton();
		JButton searchPrevJButton = getSearchPrevJButton();
		JLabel searchNavIndexJLabel = getSearchNavIndexJLabel();
		JButton searchNextJButton = getSearchNextJButton();
		JButton searchLastJButton = getSearchLastJButton();
		JButton searchWrapJButton = getSearchWrapJButton();

		int height = 30;

		JLabel resultsLabel = new JLabel("Results:");
		Dimension size = new Dimension(40, 20);
		resultsLabel.setPreferredSize(size);
		resultsLabel.setMinimumSize(size);
		resultsLabel.setMaximumSize(size);

		add(Box.createRigidArea(new Dimension(10, height)));
		add(searchJLabel);
		add(Box.createRigidArea(new Dimension(2, height)));
		add(searchJComboBox);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchClearJButton);
		add(Box.createRigidArea(new Dimension(20, height)));
		add(searchFirstJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchPrevJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(resultsLabel);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchNavIndexJLabel);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchNextJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchLastJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		add(searchWrapJButton);
		add(Box.createRigidArea(new Dimension(4, height)));
		// add(Box.createHorizontalGlue());

		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

	}

	protected void refreshSearchJComboBox() {

		Object selectedItem = searchModel.getSearchStrObj();
		List<Object> searchItemList = searchModel.getSearchItemList();

		JComboBox<Object> searchJComboBox = getSearchJComboBox();
		DefaultComboBoxModel<Object> defaultComboBoxModel;

		defaultComboBoxModel = (DefaultComboBoxModel<Object>) searchJComboBox.getModel();

		// clear current elements
		defaultComboBoxModel.removeAllElements();

		defaultComboBoxModel.addElement(null);

		// add custom items

		if (searchItemList != null) {

			Collections.reverse(searchItemList);

			for (Object searchItem : searchItemList) {
				defaultComboBoxModel.addElement(searchItem);
			}
		}

		// // add default search items
		// if ((defaultSearchItems != null) && (defaultSearchItems.length > 0))
		// {
		//
		// for (Object item : defaultSearchItems) {
		// defaultComboBoxModel.addElement(item);
		// }
		// }

		searchJComboBox.setSelectedItem(selectedItem);

	}

	/**
	 * @return the searchJComboBox
	 */
	private JComboBox<Object> getSearchJComboBox() {

		if (searchJComboBox == null) {
			searchJComboBox = new JComboBox<Object>();

			Dimension size = new Dimension(350, 20);
			searchJComboBox.setPreferredSize(size);
			// searchJComboBox.setMinimumSize(size);
			searchJComboBox.setMaximumSize(size);
			searchJComboBox.setEditable(true);
			// searchJComboBox.setMaximumRowCount(10);

			searchJComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent event) {
					if (event.getKeyChar() == KeyEvent.VK_ENTER) {
						searchEvents();
					}
				}
			});

			// searchJComboBox.addActionListener(new ActionListener() {
			//
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// LOG.info(e);
			// seachTraceEvents();
			// }
			// });

			searchJComboBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

			refreshSearchJComboBox();
		}

		return searchJComboBox;
	}

	/**
	 * @return the searchJButton
	 */
	private JButton getSearchJButton() {

		if (searchJButton == null) {

			searchJButton = new JButton("Find");

			Dimension size = new Dimension(45, 20);
			searchJButton.setPreferredSize(size);
			searchJButton.setMinimumSize(size);
			searchJButton.setMaximumSize(size);
			searchJButton.setBorder(BorderFactory.createEmptyBorder());
			searchJButton.setToolTipText("Start search");
			searchJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchEvents();
				}
			});
		}

		return searchJButton;
	}

	/**
	 * @return the searchClearJButton
	 */
	private JButton getSearchClearJButton() {

		if (searchClearJButton == null) {
			searchClearJButton = new JButton("Clear");

			Dimension size = new Dimension(45, 20);
			searchClearJButton.setPreferredSize(size);
			searchClearJButton.setMinimumSize(size);
			searchClearJButton.setMaximumSize(size);
			searchClearJButton.setBorder(BorderFactory.createEmptyBorder());
			searchClearJButton.setToolTipText("Clear search results");
			searchClearJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					resetSearchPageDetails(true);
				}
			});
		}

		return searchClearJButton;
	}

	/**
	 * @return the searchFirstJButton
	 */
	private JButton getSearchFirstJButton() {

		if (searchFirstJButton == null) {

			searchFirstJButton = new JButton(firstImageIcon);

			Dimension size = new Dimension(40, 20);
			Dimension minSize = new Dimension(30, 20);

			searchFirstJButton.setPreferredSize(size);
			searchFirstJButton.setMinimumSize(minSize);
			searchFirstJButton.setMaximumSize(size);
			searchFirstJButton.setBorder(BorderFactory.createEmptyBorder());
			searchFirstJButton.setEnabled(false);
			searchFirstJButton.setToolTipText("First search result");
			searchFirstJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchFirst();
				}
			});
		}

		return searchFirstJButton;

	}

	/**
	 * @return the searchPrevJButton
	 */
	private JButton getSearchPrevJButton() {

		if (searchPrevJButton == null) {

			searchPrevJButton = new JButton(prevImageIcon);

			Dimension size = new Dimension(40, 20);
			Dimension minSize = new Dimension(30, 20);
			searchPrevJButton.setPreferredSize(size);
			searchPrevJButton.setMinimumSize(minSize);
			searchPrevJButton.setMaximumSize(size);
			searchPrevJButton.setBorder(BorderFactory.createEmptyBorder());
			searchPrevJButton.setEnabled(false);
			searchPrevJButton.setToolTipText("Previous search result from current selection");
			searchPrevJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchPrevious();
				}
			});
		}

		return searchPrevJButton;
	}

	/**
	 * @return the searchNextJButton
	 */
	private JButton getSearchNextJButton() {

		if (searchNextJButton == null) {
			searchNextJButton = new JButton(nextImageIcon);

			Dimension size = new Dimension(40, 20);
			Dimension minSize = new Dimension(30, 20);
			searchNextJButton.setPreferredSize(size);
			searchNextJButton.setMinimumSize(minSize);
			searchNextJButton.setMaximumSize(size);
			searchNextJButton.setBorder(BorderFactory.createEmptyBorder());
			searchNextJButton.setEnabled(false);
			searchNextJButton.setToolTipText("Next search result from current selection");
			searchNextJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchNext();
				}
			});
		}

		return searchNextJButton;
	}

	/**
	 * @return the searchLastJButton
	 */
	private JButton getSearchLastJButton() {

		if (searchLastJButton == null) {

			searchLastJButton = new JButton(lastImageIcon);

			Dimension size = new Dimension(40, 20);
			Dimension minSize = new Dimension(30, 20);
			searchLastJButton.setPreferredSize(size);
			searchLastJButton.setMinimumSize(minSize);
			searchLastJButton.setMaximumSize(size);
			searchLastJButton.setBorder(BorderFactory.createEmptyBorder());
			searchLastJButton.setEnabled(false);
			searchLastJButton.setToolTipText("Last search result");
			searchLastJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchLast();
				}
			});
		}

		return searchLastJButton;
	}

	/**
	 * @return the searchWrapJButton
	 */
	protected JButton getSearchWrapJButton() {

		if (searchWrapJButton == null) {

			searchWrapJButton = new JButton(disableWrapImageIcon);

			Dimension size = new Dimension(40, 20);
			Dimension minSize = new Dimension(30, 20);
			searchWrapJButton.setPreferredSize(size);
			searchWrapJButton.setMinimumSize(minSize);
			searchWrapJButton.setMaximumSize(size);
			searchWrapJButton.setEnabled(true);
			searchWrapJButton.setActionCommand(SEARCH_ENABLE_WRAP);
			searchWrapJButton.setToolTipText(SEARCH_ENABLE_WRAP);
			searchWrapJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					boolean searchResultsWrap = false;

					JButton searchWrapJButton = getSearchWrapJButton();

					if (e.getActionCommand().equals(SEARCH_ENABLE_WRAP)) {

						searchWrapJButton.setIcon(getEnableWrapImageIcon());
						searchWrapJButton.setActionCommand(SEARCH_DISABLE_WRAP);
						searchWrapJButton.setToolTipText(SEARCH_DISABLE_WRAP);

						searchResultsWrap = true;

					} else {

						searchWrapJButton.setIcon(getDisableWrapImageIcon());
						searchWrapJButton.setActionCommand(SEARCH_ENABLE_WRAP);
						searchWrapJButton.setToolTipText(SEARCH_ENABLE_WRAP);

						searchResultsWrap = false;

					}

					setSearchResultsWrap(searchResultsWrap);

				}
			});
		}

		return searchWrapJButton;

	}

	/**
	 * @return the searchNavIndexJLabel
	 */
	private JLabel getSearchNavIndexJLabel() {

		if (searchNavIndexJLabel == null) {
			searchNavIndexJLabel = new JLabel();

			Dimension size = new Dimension(100, 20);
			searchNavIndexJLabel.setPreferredSize(size);
			searchNavIndexJLabel.setMinimumSize(size);
			searchNavIndexJLabel.setMaximumSize(size);
			searchNavIndexJLabel.setForeground(Color.BLUE);
		}

		return searchNavIndexJLabel;
	}

	protected void searchEvents() {

		Object searchStrObj = getSearchJComboBox().getSelectedItem();

		if (searchStrObj != null) {

			// updating of nav detail will occur from fire event after search
			// task is completed
			searchInEvents(searchStrObj, this);
		}

	}

	private void searchInEvents(Object searchStrObj, JComponent parent) {

		UIManager.put("ModalProgressMonitor.progressText", "Search");

		ModalProgressMonitor mProgressMonitor = new ModalProgressMonitor(parent, ("Searching '" + searchStrObj + "'"),
				"Searching... (0%)", 0, 100);
		mProgressMonitor.setMillisToDecideToPopup(0);
		mProgressMonitor.setMillisToPopup(0);

		searchModel.searchInEvents(searchStrObj, mProgressMonitor);
	}

	public void updateSearchNavIndexDetails() {

		SelectedRowPosition selectedRowPosition = searchModel.getSelectedRowPosition();

		updateSearchPageButtons(selectedRowPosition);

	}

	private void updateSearchPageButtons(SelectedRowPosition selectedRowPosition) {

		int searchResults = searchModel.getResultCount();
		int searchNavIndex = searchModel.getNavIndex();

		JButton searchFirstJButton = getSearchFirstJButton();
		JButton searchPrevJButton = getSearchPrevJButton();
		JLabel searchNavIndexJLabel = getSearchNavIndexJLabel();
		JButton searchNextJButton = getSearchNextJButton();
		JButton searchLastJButton = getSearchLastJButton();

		switch (selectedRowPosition) {

		case FIRST:
			searchFirstJButton.setEnabled(false);
			searchPrevJButton.setEnabled(false);
			searchNextJButton.setEnabled(true);
			searchLastJButton.setEnabled(true);
			break;

		case LAST:
			searchFirstJButton.setEnabled(true);
			searchPrevJButton.setEnabled(true);
			searchNextJButton.setEnabled(false);
			searchLastJButton.setEnabled(false);
			break;

		case BETWEEN:
			searchFirstJButton.setEnabled(true);
			searchPrevJButton.setEnabled(true);
			searchNextJButton.setEnabled(true);
			searchLastJButton.setEnabled(true);
			break;

		case NONE:
			searchFirstJButton.setEnabled(false);
			searchPrevJButton.setEnabled(false);
			searchNextJButton.setEnabled(false);
			searchLastJButton.setEnabled(false);
			break;

		default:
			break;
		}

		String text = String.format(PAGES_FORMAT_STR, searchNavIndex, searchResults);

		searchNavIndexJLabel.setText(text);

	}

	protected void resetSearchPageDetails(boolean clearResults) {

		searchModel.resetResults(clearResults);

		refreshSearchJComboBox();
		updateSearchNavIndexDetails();
	}

	protected void searchFirst() {

		T navigationKey = (T) searchModel.first();

		navigationTableController.scrollToKey(navigationKey);

		updateSearchNavIndexDetails();
	}

	protected void searchPrevious() {

		T navigationKey = (T) searchModel.previous();

		navigationTableController.scrollToKey(navigationKey);

		updateSearchNavIndexDetails();
	}

	protected void searchNext() {

		T navigationKey = (T) searchModel.next();

		navigationTableController.scrollToKey(navigationKey);

		updateSearchNavIndexDetails();
	}

	protected void searchLast() {

		T navigationKey = (T) searchModel.last();

		navigationTableController.scrollToKey(navigationKey);

		updateSearchNavIndexDetails();
	}

	protected void setSearchResultsWrap(boolean searchResultsWrap) {

		searchModel.setWrapMode(searchResultsWrap);

		updateSearchNavIndexDetails();
	}

}
