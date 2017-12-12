/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.pega.gcs.fringecommon.guiutilities.ModalProgressMonitor;
import com.pega.gcs.fringecommon.guiutilities.SearchTableModelEvent;
import com.pega.gcs.fringecommon.guiutilities.search.SearchData;
import com.pega.gcs.fringecommon.guiutilities.search.SearchModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableTreeModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableModelAdapter;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class XMLTreeTableModelAdapter extends TreeTableModelAdapter {

	private static final long serialVersionUID = -7970210624374603557L;

	private static final Log4j2Helper LOG = new Log4j2Helper(XMLTreeTableModelAdapter.class);

	// private XMLNodeSearchModel xmlNodeSearchModel;

	// search
	private SearchModel<XMLNode> searchModel;

	Comparator<XMLNode> xmlNodeComparator;

	public XMLTreeTableModelAdapter(DefaultTreeTableTree tree) {

		super(tree);

		xmlNodeComparator = new Comparator<XMLNode>() {

			@Override
			public int compare(XMLNode o1, XMLNode o2) {
				return o1.getId().compareTo(o2.getId());
			}
		};
	}

	public SearchModel<XMLNode> getSearchModel() {

		if (searchModel == null) {

			SearchData<XMLNode> searchData = new SearchData<>(null);

			searchModel = new SearchModel<XMLNode>(searchData) {

				@Override
				public void searchInEvents(Object searchStrObj, ModalProgressMonitor mProgressMonitor) {

					if ((searchStrObj != null) && (!"".equals(searchStrObj))) {

						List<XMLNode> searchResultList = searchXMLTreeTable(searchStrObj);

						if (searchResultList != null) {
							// LOG.info("LogTableSearchTask
							// done "
							// + searchResultList.size() +
							// " entries found");
							// setSearchStrObj(searchStrObj);
							setSearchResultList(searchStrObj, searchResultList);
						}

						// general fire will reload the tree,
						// collapsing the whole tree.
						// hence generating a special to identify
						// search action.
						fireTableChanged(new SearchTableModelEvent(XMLTreeTableModelAdapter.this));
					}
				}

				@Override
				public void resetResults(boolean clearResults) {
					// clears search result on search model and reset the search
					// panel
					resetSearchResults(clearResults);

					// clear search results from within trace events and tree
					// nodes
					XMLNode rootNode = (XMLNode) getRoot();
					//
					recursiveClearSearchResults(rootNode);

					// general fire will reload the tree,
					// collapsing the whole tree.
					// hence generating a special to identify
					// search action.
					fireTableChanged(new SearchTableModelEvent(XMLTreeTableModelAdapter.this));
				}
			};
		}

		return searchModel;
	}

	private List<XMLNode> searchXMLTreeTable(Object searchStrObj) {

		List<XMLNode> searchResultList = getSearchModel().getSearchResultList(searchStrObj);

		XMLNode rootNode = (XMLNode) getRoot();

		if (searchResultList == null) {

			searchResultList = searchXMLNode(searchStrObj, rootNode);

		} else {
			update(searchStrObj, rootNode);
		}

		return searchResultList;
	}

	private List<XMLNode> searchXMLNode(Object searchStrObj, XMLNode rootNode) {

		List<XMLNode> searchResultList = new ArrayList<XMLNode>();

		long before = System.currentTimeMillis();

		try {

			recursiveTraverseSearch(searchStrObj, rootNode, searchResultList);

		} finally {

			long diff = System.currentTimeMillis() - before;

			int secs = (int) Math.ceil((double) diff / 1E3);

			LOG.info("Search '" + searchStrObj + "' completed in " + secs + " secs. " + searchResultList.size()
					+ " results found.");

		}

		Collections.sort(searchResultList, xmlNodeComparator);

		return searchResultList;
	}

	private boolean[] recursiveTraverseSearch(Object searchStrObj, XMLNode treeNode, List<XMLNode> searchResultList) {

		boolean[] searchFound = new boolean[treeNode.getSearchFoundArray().length];

		boolean[] found = treeNode.secondarySearch(searchStrObj.toString());

		if (GeneralUtilities.any(found)) {
			searchFound = found;
			searchResultList.add(treeNode);
		}

		for (Enumeration<?> e = treeNode.children(); e.hasMoreElements();) {

			XMLNode childNode = (XMLNode) e.nextElement();

			boolean[] childSearchFound;

			childSearchFound = recursiveTraverseSearch(searchStrObj, childNode, searchResultList);

			int length = searchFound.length;

			for (int i = 0; i < length; i++) {

				boolean childFound = childSearchFound[i];
				boolean parent = searchFound[i];

				if (!parent && childFound) {
					searchFound[i] = true;
				}

			}
		}

		treeNode.setSecondaryParentSearchFoundArray(searchFound);

		return searchFound;
	}

	private void update(Object searchStrObj, XMLNode rootNode) {

		List<XMLNode> searchResultList = getSearchModel().getSearchResultList(searchStrObj);

		long before = System.currentTimeMillis();

		try {
			recursiveTraverseUpdate(searchStrObj, rootNode, searchResultList);

		} finally {

			long diff = System.currentTimeMillis() - before;

			int secs = (int) Math.ceil((double) diff / 1E3);

			LOG.info("Search updated '" + searchStrObj + "' completed in " + secs + " secs. " + searchResultList.size()
					+ " results found.");
		}
	}

	private boolean[] recursiveTraverseUpdate(Object searchStrObj, XMLNode treeNode, List<XMLNode> searchResultList) {

		int size = treeNode.getSecondaryParentSearchFoundArray().length;
		boolean[] searchFound = new boolean[size];

		int index = Collections.binarySearch(searchResultList, treeNode, xmlNodeComparator);

		if (index >= 0) {
			searchFound = treeNode.secondarySearch(searchStrObj.toString());
		} else {
			// set to false in other cases
			treeNode.setSecondarySearchFoundArray(searchFound);
		}

		for (Enumeration<?> e = treeNode.children(); e.hasMoreElements();) {

			XMLNode childNode = (XMLNode) e.nextElement();

			boolean[] childSearchFound;

			childSearchFound = recursiveTraverseUpdate(searchStrObj, childNode, searchResultList);

			int length = searchFound.length;

			for (int i = 0; i < length; i++) {

				boolean childFound = childSearchFound[i];
				boolean parent = searchFound[i];

				if (!parent && childFound) {
					searchFound[i] = true;
				}
			}
		}

		treeNode.setSecondaryParentSearchFoundArray(searchFound);

		return searchFound;
	}

	protected void recursiveClearSearchResults(XMLNode treeNode) {

		for (Enumeration<?> e = treeNode.children(); e.hasMoreElements();) {

			XMLNode childNode = (XMLNode) e.nextElement();

			recursiveClearSearchResults(childNode);
		}

		boolean[] search = new boolean[treeNode.getSecondarySearchFoundArray().length];
		treeNode.setSecondarySearchFoundArray(search);
		treeNode.setSecondaryParentSearchFoundArray(search);
	}

}
