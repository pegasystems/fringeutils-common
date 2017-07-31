/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.TreePath;

import org.dom4j.Element;

import com.pega.gcs.fringecommon.guiutilities.NavigationTableController;
import com.pega.gcs.fringecommon.guiutilities.RightClickMenuItem;
import com.pega.gcs.fringecommon.guiutilities.search.SearchPanel;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;
import com.pega.gcs.fringecommon.guiutilities.treetable.DefaultTreeTableTree;
import com.pega.gcs.fringecommon.guiutilities.treetable.TreeTableColumn;

public class XMLTreeTableDetailJDialog extends JFrame {

	private static final long serialVersionUID = 6379382564364772240L;

	private XMLTreeTable xmlTreeTable;

	private XMLTreeTableTreeModel xmlTreeTableModel;

	private SearchPanel<XMLNode> searchPanel;

	private JButton expandAllJButton;

	private JCheckBox unescapeJCheckBox;

	private XMLElementTextDetailsPanel xmlElementTextDetailsPanel;

	private String[] searchStr;

	// contains xml element names that need to be displayed as table on details
	// window.
	private Map<String, XMLElementType> xmlElementTypeMap;

	public XMLTreeTableDetailJDialog(String title, Element[] traceEventPropertyElement, String[] searchStr,
			TreeTableColumn[] treeTableColumns, Dimension dimension, Map<String, XMLElementType> xmlElementTypeMap,
			ImageIcon appIcon, Component parent) {

		super();

		this.searchStr = searchStr;
		this.xmlElementTypeMap = xmlElementTypeMap;

		setIconImage(appIcon.getImage());

		XMLNode rootXMLNode = new XMLNode(traceEventPropertyElement, searchStr, false);

		xmlTreeTableModel = new XMLTreeTableTreeModel(rootXMLNode, treeTableColumns);

		setPreferredSize(dimension);
		setTitle(title);
		// setResizable(true);
		// setModalityType(ModalityType.MODELESS);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		// setAlwaysOnTop(true);

		setContentPane(getMainJPanel());

		pack();

		setLocationRelativeTo(parent);

		// visible should be the last step
		setVisible(true);
	}

	protected String[] getSearchStr() {
		return searchStr;
	}

	/**
	 * @return the expandAllJButton
	 */
	protected JButton getExpandAllJButton() {

		if (expandAllJButton == null) {
			expandAllJButton = new JButton("Expand All Nodes");

			Dimension size = new Dimension(200, 20);
			Dimension minSize = new Dimension(100, 20);

			expandAllJButton.setPreferredSize(size);
			expandAllJButton.setMinimumSize(minSize);
			expandAllJButton.setMaximumSize(size);
			expandAllJButton.setActionCommand("ExpandAll");

			long totalNodeCount = xmlTreeTableModel.getTotalNodeCount();

			// disable the expand button in case there are large number of nodes
			if (totalNodeCount > 100000) {
				expandAllJButton.setEnabled(false);
			}

			expandAllJButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JButton expandAllJButton = getExpandAllJButton();

					String actionCommand = e.getActionCommand();

					if ("ExpandAll".equals(actionCommand)) {
						getXMLTreeTable().expandAll(true);
						expandAllJButton.setText("Collapse All Nodes");
						expandAllJButton.setActionCommand("CollapseAll");
					} else {
						getXMLTreeTable().expandAll(false);
						expandAllJButton.setText("Expand All Nodes");
						expandAllJButton.setActionCommand("ExpandAll");
					}
				}
			});
		}

		return expandAllJButton;
	}

	protected JCheckBox getUnescapeJCheckBox() {

		if (unescapeJCheckBox == null) {

			unescapeJCheckBox = new JCheckBox("Unescape HTML", false);

			unescapeJCheckBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					XMLTreeTable xmlTreeTable = getXMLTreeTable();

					JCheckBox unescapeJCheckBox = getUnescapeJCheckBox();

					if (unescapeJCheckBox.isSelected()) {
						xmlTreeTable.setUnescapeHTMLText(true);
					} else {
						xmlTreeTable.setUnescapeHTMLText(false);
					}
				}
			});
		}

		return unescapeJCheckBox;
	}

	private SearchPanel<XMLNode> getSearchPanel() {

		if (searchPanel == null) {

			final XMLTreeTable xmlTreeTable = getXMLTreeTable();
			final XMLTreeTableModelAdapter xmlTreeTableModelAdapter;
			xmlTreeTableModelAdapter = (XMLTreeTableModelAdapter) xmlTreeTable.getModel();

			NavigationTableController<XMLNode> navigationTableController = new NavigationTableController<XMLNode>(
					null) {

				@Override
				public void scrollToKey(XMLNode key) {

					DefaultTreeTableTree treeTableTree = xmlTreeTable.getTree();

					TreePath treePath = new TreePath(key.getPath());

					treeTableTree.setSelectionPath(treePath);
					// treeTableTree.scrollPathToVisible(treePath);
					xmlTreeTable.selectTreeNodeTableRow(key);

				}
			};

			searchPanel = new SearchPanel<XMLNode>(navigationTableController,
					xmlTreeTableModelAdapter.getXMLNodeSearchModel());

		}

		return searchPanel;
	}

	private JPanel getMainJPanel() {

		JPanel mainJPanel = new JPanel();
		mainJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0D;
		gbc1.weighty = 1.0D;
		gbc1.fill = GridBagConstraints.BOTH;
		gbc1.anchor = GridBagConstraints.NORTHWEST;

		JPanel topJPanel = new JPanel();

		topJPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0D;
		gbc2.weighty = 0.0D;
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.anchor = GridBagConstraints.CENTER;
		// gbc2.insets = new Insets(5, 5, 5, 5);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 1;
		gbc3.weightx = 1.0D;
		gbc3.weighty = 1.0D;
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.anchor = GridBagConstraints.NORTHWEST;

		JPanel utilityPanel = getUtilityPanel();
		JComponent tableJComponent = getTableJComponent();

		topJPanel.add(utilityPanel, gbc2);
		topJPanel.add(tableJComponent, gbc3);

		JPanel xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();

		JSplitPane mainJComponent = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topJPanel, xmlElementTextDetailsPanel);

		mainJComponent.setContinuousLayout(true);

		mainJComponent.setDividerLocation(0.8);
		mainJComponent.setResizeWeight(0.5);

		mainJPanel.add(mainJComponent, gbc1);
		mainJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		return mainJPanel;
	}

	private JPanel getUtilityPanel() {

		JPanel utilityPanel = new JPanel();

		LayoutManager layout = new BoxLayout(utilityPanel, BoxLayout.LINE_AXIS);

		utilityPanel.setLayout(layout);

		JPanel searchPanel = getSearchPanel();
		JPanel expandAllButtonPanel = getExpandAllButtonPanel();

		utilityPanel.add(searchPanel);
		utilityPanel.add(expandAllButtonPanel);

		return utilityPanel;
	}

	private JPanel getExpandAllButtonPanel() {

		JPanel expandAllButtonPanel = new JPanel();

		LayoutManager layout = new BoxLayout(expandAllButtonPanel, BoxLayout.LINE_AXIS);

		expandAllButtonPanel.setLayout(layout);

		JButton expandAllJButton = getExpandAllJButton();
		JCheckBox unescapeJCheckBox = getUnescapeJCheckBox();

		Dimension spacer = new Dimension(5, 30);

		expandAllButtonPanel.add(Box.createHorizontalGlue());
		expandAllButtonPanel.add(Box.createRigidArea(spacer));
		expandAllButtonPanel.add(expandAllJButton);
		expandAllButtonPanel.add(Box.createRigidArea(spacer));
		expandAllButtonPanel.add(unescapeJCheckBox);
		expandAllButtonPanel.add(Box.createRigidArea(spacer));
		expandAllButtonPanel.add(Box.createHorizontalGlue());

		expandAllButtonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		return expandAllButtonPanel;
	}

	private JComponent getTableJComponent() {

		XMLTreeTable xmlTreeTable = getXMLTreeTable();

		JScrollPane tableJComponent = new JScrollPane(xmlTreeTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tableJComponent.setPreferredSize(xmlTreeTable.getPreferredSize());
		tableJComponent.setMaximumSize(xmlTreeTable.getMaximumSize());

		return tableJComponent;
	}

	protected XMLTreeTable getXMLTreeTable() {

		if (xmlTreeTable == null) {

			xmlTreeTable = new XMLTreeTable(xmlTreeTableModel);

			xmlTreeTable.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {

					if (SwingUtilities.isRightMouseButton(e)) {

						final XMLTreeTable target = (XMLTreeTable) e.getSource();
						final JTree tree = target.getTree();

						int selRow = tree.getRowForLocation(e.getX(), e.getY());

						if (selRow != -1) {
							target.setRowSelectionInterval(selRow, selRow);
						}

						final int[] selectedRows = target.getSelectedRows();

						if ((selectedRows != null) && (selectedRows.length > 0)) {

							final JPopupMenu popupMenu = new JPopupMenu();

							final RightClickMenuItem copyAsXML = new RightClickMenuItem("Copy as XML");

							copyAsXML.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

									List<Element> elementList = new ArrayList<Element>();

									XMLTreeTable xmlTreeTable = getXMLTreeTable();

									for (int selectedRow : selectedRows) {

										XMLNode xmlNode = (XMLNode) xmlTreeTable.getValueAt(selectedRow, 0);

										// TODO: sort it out later
										elementList.add(xmlNode.getNodeElements()[0]);

									}

									String data = XMLNode.getElementsAsXML(elementList);

									clipboard.setContents(new StringSelection(data), copyAsXML);

									popupMenu.setVisible(false);

								}
							});

							popupMenu.add(copyAsXML);

							if (selectedRows.length == 1) {

								final TreePath treePath = tree.getPathForRow(selectedRows[0]);

								AbstractTreeTableNode treeNode = (AbstractTreeTableNode) treePath
										.getLastPathComponent();

								// XMLNode xmlNode = (XMLNode) xmlTreeTable
								// .getValueAt(selectedRows[0], 0);
								//
								// final TreePath treePath = new
								// TreePath(xmlNode
								// .getPath());

								boolean expanded = tree.isExpanded(treePath);

								if (!treeNode.isLeaf() && !expanded) {

									RightClickMenuItem expand = new RightClickMenuItem("Expand");

									expand.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent e) {

											tree.expandPath(treePath);

											popupMenu.setVisible(false);

											tree.setSelectionRow(selectedRows[0]);

										}
									});

									RightClickMenuItem expandAllChildren = new RightClickMenuItem(
											"Expand All Children");

									expandAllChildren.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent e) {

											target.expandAll(treePath, true, 1);
											popupMenu.setVisible(false);

										}
									});

									popupMenu.add(expand);
									popupMenu.add(expandAllChildren);

								} else if (!treeNode.isLeaf() && expanded) {
									RightClickMenuItem collapse = new RightClickMenuItem("Collapse");

									collapse.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent e) {

											tree.collapsePath(treePath);

											popupMenu.setVisible(false);

										}
									});

									RightClickMenuItem collapseAllChildren = new RightClickMenuItem(
											"Collapse All Children");

									collapseAllChildren.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent e) {

											// collapse only children
											target.expandAll(treePath, false, 0);
											popupMenu.setVisible(false);

										}
									});

									popupMenu.add(collapse);
									popupMenu.add(collapseAllChildren);

								}
							}

							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					} else {
						super.mouseClicked(e);
					}
				}

			});

			XMLElementTextDetailsPanel xmlElementTextDetailsPanel;
			xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();

			xmlTreeTable.getSelectionModel().addListSelectionListener(xmlElementTextDetailsPanel);
		}

		return xmlTreeTable;
	}

	protected XMLElementTextDetailsPanel getXMLElementTextDetailsPanel() {

		if (xmlElementTextDetailsPanel == null) {

			xmlElementTextDetailsPanel = new XMLElementTextDetailsPanel(xmlElementTypeMap) {

				private static final long serialVersionUID = 7338119945658969215L;

				@Override
				public void valueChanged(ListSelectionEvent e) {

					XMLTreeTable xmlTreeTable = getXMLTreeTable();

					int selectedRow = xmlTreeTable.getSelectedRow();

					if (selectedRow >= 0) {

						XMLNode xmlNode = (XMLNode) xmlTreeTable.getValueAt(selectedRow, 0);

						XMLTreeTableModelAdapter xmlTreeTableModelAdapter;
						xmlTreeTableModelAdapter = (XMLTreeTableModelAdapter) xmlTreeTable.getModel();
						Object secondarySearchStrObj = xmlTreeTableModelAdapter.getXMLNodeSearchModel()
								.getSearchStrObj();
						String secondarySearchStr = (secondarySearchStrObj != null) ? secondarySearchStrObj.toString()
								: null;

						XMLElementTextDetailsPanel xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();
						xmlElementTextDetailsPanel.populatePanel(xmlNode, getSearchStr(), secondarySearchStr,
								xmlTreeTable.getFont());

					}

				}
			};
		}
		return xmlElementTextDetailsPanel;
	}

}
