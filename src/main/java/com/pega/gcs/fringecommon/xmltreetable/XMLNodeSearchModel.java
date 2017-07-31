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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.pega.gcs.fringecommon.guiutilities.search.SearchData;
import com.pega.gcs.fringecommon.guiutilities.search.SearchModel;
import com.pega.gcs.fringecommon.guiutilities.search.SearchModelEvent;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public abstract class XMLNodeSearchModel extends SearchModel<XMLNode> {

	private static final Log4j2Helper LOG = new Log4j2Helper(XMLNodeSearchModel.class);

	private Map<Object, Map<Integer, XMLNode>> searchResultNodeMap;

	public XMLNodeSearchModel(SearchData<XMLNode> searchData) {
		super(searchData);

	}

	@Override
	protected void init() {
		searchResultNodeMap = new HashMap<Object, Map<Integer, XMLNode>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pega.gcs.fringecommon.guiutilities.search.SearchModel#
	 * getSearchItemList()
	 */
	@Override
	public ArrayList<Object> getSearchItemList() {

		ArrayList<Object> searchItemList = new ArrayList<Object>(searchResultNodeMap.keySet());

		return searchItemList;
	}

	private Map<Integer, XMLNode> getSearchResultMap(Object searchStrObj) {

		Map<Integer, XMLNode> searchResulteMap = null;

		if ((searchStrObj != null) && (!"".equals(searchStrObj))) {

			searchResulteMap = searchResultNodeMap.get(searchStrObj);
		}

		return searchResulteMap;
	}

	protected void clearSearchResultNodeMap() {
		searchResultNodeMap.clear();
	}

	@Override
	public List<XMLNode> getSearchResultList(Object searchStrObj) {

		List<XMLNode> searchResultList = null;

		Map<Integer, XMLNode> searchNodeMap = getSearchResultMap(searchStrObj);

		if (searchNodeMap != null) {
			searchResultList = new ArrayList<XMLNode>(searchNodeMap.values());
		}

		return searchResultList;
	}

	public void setSearchResultMap(Object searchStrObj, Map<Integer, XMLNode> searchResultMap) {

		if ((getSearchStrObj() == null) || (!getSearchStrObj().equals(searchStrObj))) {
			setSearchStrObj(searchStrObj);
		}

		if (searchStrObj != null) {
			searchResultNodeMap.put(searchStrObj, searchResultMap);
		}

		SearchModelEvent<XMLNode> searchModelEvent = new SearchModelEvent<XMLNode>(this, SearchModelEvent.RESET_MODEL,
				null);

		fireSearchResultChanged(searchModelEvent);
	}

	public boolean searchXMLNodes(Object searchStrObj, XMLNode rootNode) {

		boolean search = false;

		if ((searchStrObj != null) && (!"".equals(searchStrObj))) {

			List<XMLNode> searchResultlist = getSearchResultList(searchStrObj);

			if (searchResultlist == null) {

				Map<Integer, XMLNode> searchResultMap;
				searchResultMap = search(searchStrObj, rootNode);

				setSearchResultMap(searchStrObj, searchResultMap);
			} else {
				update(searchStrObj, rootNode);
			}
		}

		return search;
	}

	private Map<Integer, XMLNode> search(Object searchStrObj, XMLNode rootNode) {

		Map<Integer, XMLNode> searchNodeMap = new TreeMap<Integer, XMLNode>();

		long before = System.currentTimeMillis();

		try {

			AtomicInteger ai = new AtomicInteger(0);

			recursiveTraverseSearch(searchStrObj, rootNode, searchNodeMap, ai);

		} finally {

			long diff = System.currentTimeMillis() - before;

			int secs = (int) Math.ceil((double) diff / 1E3);

			LOG.info("Search '" + searchStrObj + "' completed in " + secs + " secs. " + searchNodeMap.size()
					+ " results found.");

		}

		return searchNodeMap;
	}

	private void update(Object searchStrObj, XMLNode rootNode) {

		List<XMLNode> searchResultList = getSearchResultList(searchStrObj);

		long before = System.currentTimeMillis();

		try {

			AtomicInteger ai = new AtomicInteger(0);

			Comparator<XMLNode> nodeComparator = new Comparator<XMLNode>() {

				@Override
				public int compare(XMLNode o1, XMLNode o2) {
					return o1.getId().compareTo(o2.getId());
				}
			};

			recursiveTraverseUpdate(searchStrObj, rootNode, searchResultList, ai, nodeComparator);

		} finally {

			long diff = System.currentTimeMillis() - before;

			int secs = (int) Math.ceil((double) diff / 1E3);

			LOG.info("Search updated '" + searchStrObj + "' completed in " + secs + " secs. " + searchResultList.size()
					+ " results found.");
		}
	}

	private boolean[] recursiveTraverseSearch(Object searchStrObj, XMLNode treeNode,
			Map<Integer, XMLNode> searchResultTreeNodeMap, AtomicInteger ai) {

		boolean[] searchFound = new boolean[treeNode.getSearchFoundArray().length];

		boolean[] found = treeNode.secondarySearch(searchStrObj.toString());

		if (GeneralUtilities.any(found)) {
			searchFound = found;
			Integer id = treeNode.getId();
			searchResultTreeNodeMap.put(id, treeNode);
		}

		for (Enumeration<?> e = treeNode.children(); e.hasMoreElements();) {

			XMLNode childNode = (XMLNode) e.nextElement();

			boolean[] childSearchFound;

			childSearchFound = recursiveTraverseSearch(searchStrObj, childNode, searchResultTreeNodeMap, ai);

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

	private boolean[] recursiveTraverseUpdate(Object searchStrObj, XMLNode treeNode, List<XMLNode> searchResultList,
			AtomicInteger ai, Comparator<XMLNode> nodeComparator) {

		int size = treeNode.getSecondaryParentSearchFoundArray().length;
		boolean[] searchFound = new boolean[size];

		int index = Collections.binarySearch(searchResultList, treeNode, nodeComparator);

		if (index >= 0) {
			searchFound = treeNode.secondarySearch(searchStrObj.toString());
		} else {
			// set to false in other cases
			treeNode.setSecondarySearchFoundArray(searchFound);
		}

		for (Enumeration<?> e = treeNode.children(); e.hasMoreElements();) {

			XMLNode childNode = (XMLNode) e.nextElement();

			boolean[] childSearchFound;

			childSearchFound = recursiveTraverseUpdate(searchStrObj, childNode, searchResultList, ai, nodeComparator);

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

}
