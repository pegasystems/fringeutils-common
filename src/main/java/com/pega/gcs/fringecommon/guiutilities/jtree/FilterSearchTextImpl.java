/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.jtree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;

public class FilterSearchTextImpl extends AbstractFilter {

	private static final String HTML_MATCH_END = "</font><font color=\"black\">";
	private static final String HTML_MATCH_START = "</font><font color=\"red\">";
	private static final String HTML_POSTFIX = "</font></html>";
	private static final String HTML_PREFIX = "<html><font color=\"black\">";

	private String filterString;

	private Matcher matcher;

	// this is special, because this filter should always be unique. as this
	// will applied on text field for each key press
	public FilterSearchTextImpl(String filterString) {

		super("FilterSearchTextImpl");

		this.filterString = null;
		this.matcher = null;

		if ((filterString != null) && (!"".equals(filterString))) {
			this.filterString = filterString;
			this.matcher = Pattern.compile(filterString, Pattern.CASE_INSENSITIVE).matcher("");
		}

	}

	private boolean basicFilter(String nodeStr) {
		// if (filterString.equals("")) {
		// return true;
		// }

		matcher.reset(nodeStr);
		return matcher.find();
		// return
		// node.getUserObject().toString().toLowerCase().contains(filterLowerCase);
	}

	@Override
	public boolean pass(DefaultMutableTreeNode node) {
		return basicFilter(node.toString());
	}

	public String filterRepresentation(String original) {

		String retStr = original;
		int filterStringLength = filterString.length();

		if ((retStr != null) && (filterStringLength > 0) && (basicFilter(retStr))) {

			int staticTextLength = HTML_PREFIX.length() + HTML_MATCH_START.length() + HTML_MATCH_END.length()
					+ HTML_POSTFIX.length();

			StringBuffer sb = new StringBuffer(retStr.length() + staticTextLength);
			sb.append(HTML_PREFIX);
			int from = 0;
			String originalLowerCase = retStr.toLowerCase();
			String filterLowerCase = filterString.toLowerCase();
			int index = originalLowerCase.indexOf(filterLowerCase, from);
			while (index > -1) {
				sb.append(original.substring(from, index));
				sb.append(HTML_MATCH_START);
				sb.append(original.substring(index, index + filterStringLength));
				sb.append(HTML_MATCH_END);
				from = index + filterStringLength;
				index = originalLowerCase.indexOf(filterLowerCase, from);
			}
			if (from < retStr.length()) {
				sb.append(retStr.substring(from, retStr.length()));
			}
			sb.append(HTML_POSTFIX);
			retStr = sb.toString();
		}

		return retStr;
	}

	@Override
	public String filterRepresentation(Object node) {
		String retStr = "";

		if ((node instanceof FilterTreeNode)) {
			retStr = ((FilterTreeNode) node).getUserObject().toString();
			int filterStringLength = filterString.length();

			if ((!((FilterTreeNode) node).isRoot()) && (retStr != null) && (filterStringLength > 0)
					&& (basicFilter(retStr))) {

				int staticTextLength = HTML_PREFIX.length() + HTML_MATCH_START.length() + HTML_MATCH_END.length()
						+ HTML_POSTFIX.length();

				StringBuffer sb = new StringBuffer(retStr.length() + staticTextLength);
				sb.append(HTML_PREFIX);
				int from = 0;
				String originalLowerCase = retStr.toLowerCase();
				String filterLowerCase = filterString.toLowerCase();
				int index = originalLowerCase.indexOf(filterLowerCase, from);
				while (index > -1) {
					sb.append(retStr.substring(from, index));
					sb.append(HTML_MATCH_START);
					sb.append(retStr.substring(index, index + filterStringLength));
					sb.append(HTML_MATCH_END);
					from = index + filterStringLength;
					index = originalLowerCase.indexOf(filterLowerCase, from);
				}
				if (from < retStr.length()) {
					sb.append(retStr.substring(from, retStr.length()));
				}
				sb.append(HTML_POSTFIX);
				retStr = sb.toString();
			}
		}
		return retStr;
	}

	@Override
	public String toString() {
		return "FilterSearchTextImpl [filterString=" + filterString + "]";
	}

}
