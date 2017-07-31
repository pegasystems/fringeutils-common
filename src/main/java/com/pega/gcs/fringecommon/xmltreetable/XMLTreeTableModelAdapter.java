/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.util.Enumeration;

import com.pega.gcs.fringecommon.guiutilities.ModalProgressMonitor;
import com.pega.gcs.fringecommon.guiutilities.search.SearchData;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableModelAdapter;

public class XMLTreeTableModelAdapter extends TreeTableModelAdapter {

	private static final long serialVersionUID = -7970210624374603557L;

	private XMLNodeSearchModel xmlNodeSearchModel;

	public XMLTreeTableModelAdapter(DefaultTreeTableTree tree) {
		super(tree);

		SearchData<XMLNode> searchData = new SearchData<>(null);

		xmlNodeSearchModel = new XMLNodeSearchModel(searchData) {

			@Override
			public boolean searchInEvents(Object searchStrObj, ModalProgressMonitor mProgressMonitor) {

				boolean search = searchXMLXMLTreeTableNodes(searchStrObj);

				fireTableDataChanged();

				return search;
			}

			@Override
			public void resetResults(boolean clearResults) {

				clearSearchResultNodeMap();

				XMLNode rootNode = (XMLNode) getRoot();

				recursiveClearSearchResults(rootNode);

				fireTableDataChanged();
			}
		};
	}

	public XMLNodeSearchModel getXMLNodeSearchModel() {
		return xmlNodeSearchModel;
	}

	protected boolean searchXMLXMLTreeTableNodes(Object searchStrObj) {

		boolean search = false;

		XMLNode treeNode = (XMLNode) getRoot();

		search = xmlNodeSearchModel.searchXMLNodes(searchStrObj, treeNode);

		return search;
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
