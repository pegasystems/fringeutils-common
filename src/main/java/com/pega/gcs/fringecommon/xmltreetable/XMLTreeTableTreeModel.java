/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.util.concurrent.atomic.AtomicInteger;

import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTreeModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableColumn;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class XMLTreeTableTreeModel extends DefaultTreeTableTreeModel {

	private static final Log4j2Helper LOG = new Log4j2Helper(XMLTreeTableTreeModel.class);

	private long totalNodeCount;

	private static final long serialVersionUID = 4775143068906869999L;

	public XMLTreeTableTreeModel(XMLNode root, TreeTableColumn[] columns) {

		super(root, columns);

		initialise(root);
	}

	private void initialise(XMLNode root) {

		this.totalNodeCount = 0;

		try {

			AtomicInteger ai = new AtomicInteger(0);

			recursiveInitialize(root, 0, ai);

			this.totalNodeCount = ai.longValue();

			LOG.debug("Total node Count: " + ai);

		} catch (Exception e) {
			LOG.error("Error initialising XMLNode", e);
		}

	}

	public void setUnescapeHTMLText(boolean unescapeHTMLText) {
		XMLNode root = (XMLNode) getRoot();
		root.setUnescapeHTMLText(unescapeHTMLText);
		root.removeAllChildren();
		initialise(root);

		// reload causes collapse of tree structure, using nodeChanged instead
		// reload();
		nodeChanged(root);
	}

	// causes preloading of all xml node because of walk through
	private void recursiveInitialize(XMLNode xmlNode, int level, AtomicInteger ai) throws Exception {

		Integer id = ai.incrementAndGet();
		xmlNode.setId(id);

		for (XMLNode childNode : xmlNode.getChildNodeList()) {

			recursiveInitialize(childNode, level + 1, ai);

			xmlNode.add(childNode);

			// only the child node that have these set should update the parent.
			boolean hasMessage = GeneralUtilities.any(childNode.getHasMessageArray());
			boolean searchFound = GeneralUtilities.any(childNode.getSearchFoundArray());
			boolean parentSearchFound = GeneralUtilities.any(childNode.getParentSearchFoundArray());

			if (level > 0) {

				if (hasMessage) {
					((XMLNode) xmlNode).setHasMessageArray(childNode.getHasMessageArray());
				}

				if (searchFound) {
					((XMLNode) xmlNode).setParentSearchFoundArray(childNode.getSearchFoundArray());
				}

				if (parentSearchFound) {
					((XMLNode) xmlNode).setParentSearchFoundArray(childNode.getParentSearchFoundArray());
				}
			}
		}

	}

	/**
	 * @return the totalNodeCount
	 */
	public long getTotalNodeCount() {
		return totalNodeCount;
	}

}
