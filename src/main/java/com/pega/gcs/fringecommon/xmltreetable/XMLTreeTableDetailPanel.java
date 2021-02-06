/*******************************************************************************
 * Copyright (c) 2018, 2020 Pegasystems Inc.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.xmltreetable;

import java.awt.Color;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
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
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class XMLTreeTableDetailPanel extends JPanel {

    private static final long serialVersionUID = -5244416772056941207L;

    private static final Log4j2Helper LOG = new Log4j2Helper(XMLTreeTableDetailPanel.class);

    private XMLTreeTable xmlTreeTable;

    private XMLTreeTableTreeModel xmlTreeTableModel;

    private SearchPanel<XMLNode> searchPanel;

    private JButton expandAllJButton;

    private JCheckBox unescapeJCheckBox;

    private XMLElementTextDetailsPanel xmlElementTextDetailsPanel;

    private String[] searchStrArray;

    // contains xml element names that need to be displayed as table on details
    // window.
    private Map<String, XMLElementType> xmlElementTypeMap;

    private Charset charset;

    public XMLTreeTableDetailPanel(Element[] traceEventPropertyElementArray, String[] searchStrArray,
            TreeTableColumn[] treeTableColumnArray, Map<String, XMLElementType> xmlElementTypeMap, Charset charset) {

        super();

        this.searchStrArray = searchStrArray;
        this.xmlElementTypeMap = xmlElementTypeMap;
        this.charset = charset;

        XMLNode rootXMLNode = new XMLNode(traceEventPropertyElementArray, searchStrArray);

        xmlTreeTableModel = new XMLTreeTableTreeModel(rootXMLNode, treeTableColumnArray);

        setLayout(new GridBagLayout());

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

        mainJComponent.setDividerLocation(600);
        mainJComponent.setResizeWeight(0.5);

        add(mainJComponent, gbc1);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    }

    private XMLTreeTable getXMLTreeTable() {

        if (xmlTreeTable == null) {

            xmlTreeTable = new XMLTreeTable(xmlTreeTableModel);

            xmlTreeTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                    if (SwingUtilities.isRightMouseButton(mouseEvent)) {

                        final XMLTreeTable target = (XMLTreeTable) mouseEvent.getSource();
                        final JTree tree = target.getTree();

                        int selRow = tree.getRowForLocation(mouseEvent.getX(), mouseEvent.getY());

                        if (selRow != -1) {
                            target.setRowSelectionInterval(selRow, selRow);
                        }

                        final int[] selectedRows = target.getSelectedRows();

                        if ((selectedRows != null) && (selectedRows.length > 0)) {

                            final JPopupMenu popupMenu = new JPopupMenu();

                            final RightClickMenuItem copyAsXML = new RightClickMenuItem("Copy as XML");

                            copyAsXML.addActionListener(new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent actionEvent) {

                                    try {
                                        List<Element> elementList = new ArrayList<Element>();

                                        XMLTreeTable xmlTreeTable = getXMLTreeTable();

                                        for (int selectedRow : selectedRows) {

                                            XMLNode xmlNode = (XMLNode) xmlTreeTable.getValueAt(selectedRow, 0);

                                            // TODO: sort it out later
                                            elementList.add(xmlNode.getNodeElements()[0]);

                                        }

                                        String data = GeneralUtilities.getElementsAsXML(elementList);
                                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                                        clipboard.setContents(new StringSelection(data), copyAsXML);
                                    } catch (Exception ex) {
                                        LOG.error("Error on 'Copy as XML' action ", ex);
                                    } finally {
                                        popupMenu.setVisible(false);
                                    }
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
                                        public void actionPerformed(ActionEvent actionEvent) {

                                            tree.expandPath(treePath);

                                            popupMenu.setVisible(false);

                                            tree.setSelectionRow(selectedRows[0]);

                                        }
                                    });

                                    RightClickMenuItem expandAllChildren = new RightClickMenuItem(
                                            "Expand All Children");

                                    expandAllChildren.addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent actionEvent) {

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
                                        public void actionPerformed(ActionEvent actionEvent) {

                                            tree.collapsePath(treePath);

                                            popupMenu.setVisible(false);

                                        }
                                    });

                                    RightClickMenuItem collapseAllChildren = new RightClickMenuItem(
                                            "Collapse All Children");

                                    collapseAllChildren.addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent actionEvent) {

                                            // collapse only children
                                            target.expandAll(treePath, false, 0);
                                            popupMenu.setVisible(false);

                                        }
                                    });

                                    popupMenu.add(collapse);
                                    popupMenu.add(collapseAllChildren);

                                }
                            }

                            popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                        }
                    } else {
                        super.mouseClicked(mouseEvent);
                    }
                }

            });

            XMLElementTextDetailsPanel xmlElementTextDetailsPanel;
            xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();

            xmlTreeTable.getSelectionModel().addListSelectionListener(xmlElementTextDetailsPanel);
        }

        return xmlTreeTable;
    }

    private XMLElementTextDetailsPanel getXMLElementTextDetailsPanel() {

        if (xmlElementTextDetailsPanel == null) {

            xmlElementTextDetailsPanel = new XMLElementTextDetailsPanel(xmlElementTypeMap, charset) {

                private static final long serialVersionUID = 7338119945658969215L;

                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {

                    XMLTreeTable xmlTreeTable = getXMLTreeTable();

                    int selectedRow = xmlTreeTable.getSelectedRow();

                    if (selectedRow >= 0) {

                        XMLNode xmlNode = (XMLNode) xmlTreeTable.getValueAt(selectedRow, 0);

                        XMLTreeTableModelAdapter xmlTreeTableModelAdapter;
                        xmlTreeTableModelAdapter = (XMLTreeTableModelAdapter) xmlTreeTable.getModel();
                        Object secondarySearchStrObj = xmlTreeTableModelAdapter.getSearchModel().getSearchStrObj();
                        String secondarySearchStr = (secondarySearchStrObj != null) ? secondarySearchStrObj.toString()
                                : null;

                        XMLElementTextDetailsPanel xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();

                        xmlElementTextDetailsPanel.populatePanel(xmlNode, getSearchStr(), secondarySearchStr,
                                xmlTreeTable.isUnescapeHtmlText(), xmlTreeTable.getFont());

                    }

                }
            };
        }
        return xmlElementTextDetailsPanel;
    }

    private String[] getSearchStr() {
        return searchStrArray;
    }

    private JButton getExpandAllJButton() {

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
                public void actionPerformed(ActionEvent actionEvent) {

                    JButton expandAllJButton = getExpandAllJButton();

                    String actionCommand = actionEvent.getActionCommand();

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

    private JCheckBox getUnescapeJCheckBox() {

        if (unescapeJCheckBox == null) {

            unescapeJCheckBox = new JCheckBox("Unescape HTML", false);

            unescapeJCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    XMLTreeTable xmlTreeTable = getXMLTreeTable();

                    JCheckBox unescapeJCheckBox = getUnescapeJCheckBox();

                    if (unescapeJCheckBox.isSelected()) {
                        xmlTreeTable.setUnescapeHtmlText(true);
                    } else {
                        xmlTreeTable.setUnescapeHtmlText(false);
                    }

                    XMLElementTextDetailsPanel xmlElementTextDetailsPanel;
                    xmlElementTextDetailsPanel = getXMLElementTextDetailsPanel();

                    xmlElementTextDetailsPanel.revalidate();
                    xmlElementTextDetailsPanel.repaint();

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
                    xmlTreeTableModelAdapter.getSearchModel());

        }

        return searchPanel;
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
}
