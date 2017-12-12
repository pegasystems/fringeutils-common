/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.diff.EditCommand;
import com.pega.gcs.fringecommon.utilities.diff.Matcher;
import com.pega.gcs.fringecommon.utilities.diff.MyersDifferenceAlgorithm;

public class XMLNode extends AbstractTreeTableNode {

	private static final long serialVersionUID = 3799173551035007030L;

	private static final Log4j2Helper LOG = new Log4j2Helper(XMLNode.class);

	private Integer id;

	private Element[] nodeArray;

	private String[] nodeValue;

	private boolean[] hasMessage;

	private String[] dataField;

	private boolean[] searchFound;

	private String[] searchStr;

	private boolean[] parentSearchFound;

	private boolean[] secondarySearchFound;

	private boolean[] secondaryParentSearchFound;

	// 0 based indexes.
	private Map<Integer, Integer> compareIndexMap;

	public XMLNode(Element nodeArray, String searchStr) {

		this(new Element[] { nodeArray }, new String[] { searchStr });
	}

	public XMLNode(Element[] nodeArray, String[] searchStr) {
		super();

		this.nodeArray = nodeArray;
		this.searchStr = searchStr;

		this.compareIndexMap = new HashMap<>();

		int length = nodeArray.length;

		// for 2 entries compare them
		if (length == 2) {
			compareIndexMap.put(0, 1);
		}
		// by default comparison done on 0=>1 and 2=>3 and so on.
		else if (length > 2) {

			if (length % 2 != 0) {
				// update nodeArray to make it even number array.
				Element[] tmpNodeArray = new Element[length + 1];

				System.arraycopy(nodeArray, 0, tmpNodeArray, 0, length);

				nodeArray = tmpNodeArray;
				length = nodeArray.length;
			}

			for (int i = 0; i < length; i = i + 2) {
				compareIndexMap.put(i, i + 1);
			}

		}

		initialize();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getNodeName() {
		return getNodeValue(0);
	}

	@Override
	public String getNodeValue(int column) {
		return nodeValue[column];
	}

	@Override
	public Element[] getNodeElements() {
		return nodeArray;
	}

	public boolean[] getHasMessageArray() {
		return hasMessage;
	}

	public void setHasMessageArray(boolean[] hasMessage) {
		this.hasMessage = hasMessage;
	}

	public boolean isHasMessage(int column) {
		return hasMessage[column];
	}

	public String getDataField(int column) {
		return dataField[column];
	}

	public boolean[] getSearchFoundArray() {
		return searchFound;
	}

	public void setSearchFoundArray(boolean[] searchFound) {
		this.searchFound = searchFound;
	}

	public boolean isSearchFound(int column) {
		return searchFound[column];
	}

	public boolean[] getParentSearchFoundArray() {
		return parentSearchFound;
	}

	public void setParentSearchFoundArray(boolean[] searchFound) {
		this.parentSearchFound = searchFound;
	}

	public boolean isParentSearchFound(int column) {
		return parentSearchFound[column];
	}

	public boolean[] getSecondarySearchFoundArray() {
		return secondarySearchFound;
	}

	public void setSecondarySearchFoundArray(boolean[] secondarySearchFound) {
		this.secondarySearchFound = secondarySearchFound;
	}

	// check the tree column as well as the trace data column
	public boolean isSecondarySearchFound(int column) {

		boolean secondarySearch = false;

		if ((column != 0) && (secondarySearchFound.length > 0)) {
			secondarySearch = secondarySearchFound[0];
		}

		secondarySearch = secondarySearch || secondarySearchFound[column];

		return secondarySearch;
	}

	public boolean[] getSecondaryParentSearchFoundArray() {
		return secondaryParentSearchFound;
	}

	public void setSecondaryParentSearchFoundArray(boolean[] secondaryParentSearchFound) {
		this.secondaryParentSearchFound = secondaryParentSearchFound;
	}

	// check the tree column as well as the trace data column
	public boolean isSecondaryParentSearchFound(int column) {

		boolean secondaryParentSearch = false;

		if ((column != 0) && (secondaryParentSearchFound.length > 0)) {
			secondaryParentSearch = secondaryParentSearchFound[0];
		}

		secondaryParentSearch = secondaryParentSearch || secondaryParentSearchFound[column];

		return secondaryParentSearch;
	}

	private void initialize() {

		if (nodeArray != null) {

			int size = nodeArray.length;

			// xml tree table is node list + 1 columns
			// 1st column for name, remaining columns for actual node data.
			nodeValue = new String[size + 1];
			hasMessage = new boolean[size + 1];
			dataField = new String[size + 1];
			searchFound = new boolean[size + 1];
			parentSearchFound = new boolean[size + 1];
			secondarySearchFound = new boolean[size + 1];
			secondaryParentSearchFound = new boolean[size + 1];
			// specialCharsInValue = new boolean[size + 1];

			// column one is only node name
			// column 2 onwards has column data

			for (int i = 0; i < size; i++) {

				hasMessage[i + 1] = false;
				searchFound[i + 1] = false;
				parentSearchFound[i + 1] = false;
				secondarySearchFound[i + 1] = false;
				secondaryParentSearchFound[i + 1] = false;
				// specialCharsInValue[i + 1] = false;

				Element node = nodeArray[i];
				String search = searchStr[i];

				String nodeName = "";

				if (node != null) {

					nodeName = getElementName(node);
					nodeValue[0] = nodeName;

					String nodeValueText = node.getText().trim();
					nodeValue[i + 1] = nodeValueText;

					if (("PZ__".equals(nodeName) || ("PZ__ERROR".equals(nodeName)))
							|| (("pzStatus".equals(nodeName)) && ("false".equalsIgnoreCase(nodeValueText)))) {

						hasMessage[i + 1] = true;

						if ("PZ__ERROR".equals(nodeName)) {

							Attribute attribute = node.attribute("DATAFLD");

							if ((attribute != null) && (attribute.getText() != null)
									&& (!"".equals(attribute.getText()))) {
								dataField[i + 1] = attribute.getText();
							}
						}
					}

					if ((search != null) && (!"".equals(search))) {

						String lSearchStr = search.toLowerCase();
						String lName = nodeName.toLowerCase();
						String lValue = nodeValueText.toLowerCase();

						if (lName.indexOf(lSearchStr) != -1) {
							searchFound[0] = true;
						}

						if (lValue.indexOf(lSearchStr) != -1) {
							searchFound[i + 1] = true;
						}
					}
				}
			}
		}
	}

	protected String getElementName(Element element) {

		String elementName = null;

		Attribute attribute = element.attribute("name");

		if ((attribute != null) && (attribute.getText() != null) && (!"".equals(attribute.getText()))) {
			elementName = attribute.getText();
		} else {

			attribute = element.attribute("eventType");

			if ((attribute != null) && (attribute.getText() != null) && (!"".equals(attribute.getText()))) {
				elementName = attribute.getText();
			} else {

				StringBuffer elementNameSB = new StringBuffer();
				elementNameSB.append(element.getName());

				boolean added = false;

				added = addAttribute(element, "REPEATINGINDEX", elementNameSB);

				if (!added) {
					added = addAttribute(element, "REPEATINGTYPE", elementNameSB);
				}

				if (!added) {
					added = addAttribute(element, "referenceTo", elementNameSB);
				}

				elementName = elementNameSB.toString();
			}
		}

		return elementName;
	}

	private boolean addAttribute(Element element, String attribName, StringBuffer elementNameSB) {

		boolean added = false;

		String attribValue = element.attributeValue(attribName);

		if ((attribValue != null) && (!"".equals(attribValue))) {

			elementNameSB.append(" (");
			elementNameSB.append(attribValue);
			elementNameSB.append(")");
		}

		return added;
	}

	// only support 2 values column
	@SuppressWarnings("unchecked")
	public List<XMLNode> getChildNodeList() {

		List<XMLNode> childNodeList = new ArrayList<XMLNode>();

		if ((nodeArray != null)) {

			Comparator<Element> elementComparator = new Comparator<Element>() {

				@Override
				public int compare(Element o1, Element o2) {

					String o1AttribStr = o1.attributeValue("REPEATINGINDEX");
					String o2AttribStr = o2.attributeValue("REPEATINGINDEX");

					if ((o1AttribStr != null) && (o2AttribStr != null)) {

						try {
							Integer o1AttribInt = Integer.parseInt(o1AttribStr);
							Integer o2AttribInt = Integer.parseInt(o2AttribStr);

							return o1AttribInt.compareTo(o2AttribInt);

						} catch (NumberFormatException nfe) {
							// repeating index can be strings
							return o1AttribStr.compareTo(o2AttribStr);
						}
					}

					String o1ElementName = getElementName(o1).trim();
					String o2ElementName = getElementName(o2).trim();

					return o1ElementName.compareTo(o2ElementName);
				}
			};

			int size = nodeArray.length;

			if (size > 1) {

				try {

					Map<Integer, List<Element>> childElemListMap = new HashMap<Integer, List<Element>>();

					for (int i = 0; i < size; i++) {

						Integer key = new Integer(i);

						Element node = nodeArray[i];

						List<Element> childElemList;

						if (node != null) {
							childElemList = new ArrayList<Element>(node.elements());
						} else {
							childElemList = new ArrayList<Element>();
						}

						Collections.sort(childElemList, elementComparator);

						childElemListMap.put(key, childElemList);
					}

					Map<Integer, List<Element>> childElementListMap = new HashMap<>();

					for (Integer compareIndex : compareIndexMap.keySet()) {

						List<Element> firstList = childElemListMap.get(compareIndex);
						List<Element> secondList = childElemListMap.get(compareIndexMap.get(compareIndex));

						Matcher<Element> matcher = new Matcher<Element>() {

							@Override
							public boolean match(Element o1, Element o2) {

								String o1ElementName = getElementName(o1).trim();
								String o2ElementName = getElementName(o2).trim();

								return o1ElementName.equals(o2ElementName);
							}
						};

						List<EditCommand> editScript = MyersDifferenceAlgorithm.diffGreedyLCS(null, firstList,
								secondList, matcher);

						int indexFirst = 0;
						int indexSecond = 0;

						Element firstElement = null;
						Element secondElement = null;

						Integer index = 0;

						for (EditCommand ec : editScript) {

							switch (ec) {
							case DELETE:

								firstElement = firstList.get(indexFirst);
								secondElement = null;

								indexFirst++;
								break;
							case INSERT:

								firstElement = null;
								secondElement = secondList.get(indexSecond);

								indexSecond++;
								break;
							case SNAKE:
								firstElement = firstList.get(indexFirst);
								secondElement = secondList.get(indexSecond);

								indexFirst++;
								indexSecond++;
								break;
							default:
								break;

							}

							List<Element> childElementList = childElementListMap.get(index);

							if (childElementList == null) {
								childElementList = new ArrayList<>();
								childElementListMap.put(index, childElementList);
							}

							childElementList.add(firstElement);
							childElementList.add(secondElement);

							index++;
						}
					}

					for (Integer index : childElementListMap.keySet()) {

						List<Element> childElementList = childElementListMap.get(index);

						Element[] childElements = childElementList.toArray(new Element[childElementList.size()]);

						Element[] childElementArray = new Element[nodeArray.length];

						System.arraycopy(childElements, 0, childElementArray, 0, childElements.length);

						childNodeList.add(new XMLNode(childElementArray, searchStr));
					}

				} catch (Exception e) {
					LOG.error("Error creating child xml nodes", e);
				}

			} else {

				Element node = nodeArray[0];
				String search = searchStr[0];

				List<Element> childElemList = new ArrayList<Element>(node.elements());

				Collections.sort(childElemList, elementComparator);

				for (Element childElem : childElemList) {
					childNodeList.add(new XMLNode(childElem, search));
				}

			}

			// Collections.sort(childNodeList);

		}

		return childNodeList;
	}

	private static Element getDummyElement() {

		Element element = null;

		String elementName = "DUMMY";
		DocumentFactory factory = DocumentFactory.getInstance();
		element = factory.createElement(elementName);
		element.addAttribute("name", elementName);

		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNodeName();
	}

	@Override
	public int compareTo(AbstractTreeTableNode o) {
		return getNodeName().toLowerCase().compareTo(o.getNodeName().toLowerCase());
	}

	public static String getElementsAsXML(List<Element> elementList) {
		String xmlStr = "";
		try {

			for (Element element : elementList) {

				StringWriter sw = new StringWriter();
				OutputFormat format = new OutputFormat();
				format.setIndentSize(2);
				format.setNewlines(true);
				// format.setTrimText(true);
				format.setPadText(true);

				XMLWriter writer = new XMLWriter(sw, format);

				writer.write(element);
				writer.flush();

				xmlStr = xmlStr + sw.toString();
				xmlStr = xmlStr + "\n";
			}

			return xmlStr;

		} catch (IOException e) {
			throw new RuntimeException("IOException while generating " + "textual representation: " + e.getMessage());
		}
	}

	public boolean[] secondarySearch(String searchStr) {

		for (int i = 0; i < secondarySearchFound.length; i++) {
			secondarySearchFound[i] = false;
		}

		if ((searchStr != null) && (!"".equals(searchStr))) {

			int size = nodeValue.length;

			for (int i = 0; i < size; i++) {

				String lSearchStr = searchStr.toLowerCase();

				String lValue = nodeValue[i];

				if (lValue != null) {

					lValue = lValue.toLowerCase();

					if (lValue.indexOf(lSearchStr) != -1) {
						secondarySearchFound[i] = true;
					}
					
					// double searching to handle escaped data
					if(!secondarySearchFound[i]) {
						lValue = StringEscapeUtils.unescapeHtml4(lValue);

						if (lValue.indexOf(lSearchStr) != -1) {
							secondarySearchFound[i] = true;
						}

					}
				}
			}
		}

		return secondarySearchFound;
	}
}
