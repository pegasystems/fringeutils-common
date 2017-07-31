/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.jtree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public final class FilterTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = -7934948725975983276L;

	public FilterTreeModel(FilterTreeNode node) {
		super(node);
	}

	public void addFilter(Filter filter) {

		if ((root != null) && (root instanceof FilterTreeNode)) {

			FilterTreeNode filterTreeNode = (FilterTreeNode) root;

			filterTreeNode.addFilter(filter);

			Object[] path = { root };

			fireTreeStructureChanged(this, path, null, null);

		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof FilterTreeNode) {
			return (((FilterTreeNode) parent).getChildCount());
		}
		return 0;
	}

	public int getChildCount() {
		return (((FilterTreeNode) root).getChildCount());
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof FilterTreeNode) {
			return (((FilterTreeNode) parent).getChildAt(index));
		}
		return null;
	}

	/**
	 * @return the currentFilter
	 */
	// public Filter getCurrentFilter() {
	// return currentFilter;
	// }

	public TreePath getTreePathForObjectPath(List<Object> path) {
		List<FilterTreeNode> resultList = new ArrayList<FilterTreeNode>();
		FilterTreeNode current = (FilterTreeNode) root;
		resultList.add(current);
		for (int i = 1; (i < path.size()) && (current != null); i++) {
			// logger.info("Looking in " + current.getUserObject().toString() +
			// " for " + path[i].toString());
			current = current.getChildForObject(path.get(i));
			if (current != null) {
				resultList.add(current);
			}
		}
		if (current != null) {
			Object[] nodeArray = resultList.toArray();
			return (new TreePath(nodeArray));
		}
		return null;
	}
}
