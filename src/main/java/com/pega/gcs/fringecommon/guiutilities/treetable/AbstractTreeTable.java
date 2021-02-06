/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.pega.gcs.fringecommon.guiutilities.ClickablePathPanel;
import com.pega.gcs.fringecommon.guiutilities.CustomJTable;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public abstract class AbstractTreeTable extends CustomJTable {

    private static final long serialVersionUID = 567268590479849300L;

    private static final Log4j2Helper LOG = new Log4j2Helper(AbstractTreeTable.class);

    private DefaultTreeTableTree tree;

    protected abstract DefaultTreeTableTree constructTree(AbstractTreeTableTreeModel abstractTreeTableTreeModel);

    protected TreeTableModelAdapter getTreeTableModelAdapter(DefaultTreeTableTree tree) {

        return new TreeTableModelAdapter(tree) {

            private static final long serialVersionUID = -8097577959363303883L;

            @Override
            public DefaultTableCellRenderer getTreeTableCellRenderer() {
                return new LabelTableCellRenderer();
            }

        };
    }

    public AbstractTreeTable(AbstractTreeTableTreeModel abstractTreeTableTreeModel, int rowHeight,
            final int headerHeight) {

        super();

        tree = constructTree(abstractTreeTableTreeModel);

        TableModel tableModel = getTreeTableModelAdapter(tree);

        setAutoCreateColumnsFromModel(false);

        setModel(tableModel);

        setTreeTableColumnModel();

        TreeTableSelectionModel selectionModel = new TreeTableSelectionModel(tree);

        // add a selection listener to the tree event for updating the table
        selectionModel.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                // TreePath treePath = e.getPath();
                // AbstractTreeTableNode node = (AbstractTreeTableNode) treePath
                // .getLastPathComponent();

                // Object userObject = node.getUserObject();

                // LOG.info("TreeSelectionListener: " + e);
            }
        });

        tree.setSelectionModel(selectionModel);

        setSelectionModel(selectionModel.getListSelectionModel());

        setTableHeader(new JTableHeader(getColumnModel()) {

            private static final long serialVersionUID = 4853598300124836115L;

            {
                setReorderingAllowed(false);
                setResizingAllowed(true);
            }

            @Override
            public TableCellRenderer getDefaultRenderer() {
                TableCellRenderer tcr = super.getDefaultRenderer();

                ((DefaultTableCellRenderer) tcr).setHorizontalAlignment(SwingConstants.CENTER);
                return tcr;
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = headerHeight;
                return dim;
            }

            @Override
            public Font getFont() {
                Font existingFont = AbstractTreeTable.this.getFont();
                String existingFontName = existingFont.getName();
                int existFontSize = existingFont.getSize();
                Font newFont = new Font(existingFontName, Font.BOLD, existFontSize);

                return newFont;
            }

        });

        if (rowHeight > 0) {
            setRowHeight(rowHeight);
            tree.setRowHeight(rowHeight);
        }

        // setup the side arrows to expand and collapse tree
        final KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        final KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);

        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(leftKeyStroke, leftKeyStroke);

        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(rightKeyStroke, rightKeyStroke);

        getActionMap().put(leftKeyStroke, new AbstractAction() {

            private static final long serialVersionUID = 3842190208048171536L;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JTree tree = getTree();

                InputMap map = tree.getInputMap(WHEN_FOCUSED);
                ActionMap am = tree.getActionMap();

                if (map != null && am != null && isEnabled()) {
                    Object binding = map.get(leftKeyStroke);
                    Action action = (binding == null) ? null : am.get(binding);
                    if (action != null) {

                        KeyEvent ke = new KeyEvent(tree, KeyEvent.KEY_PRESSED, actionEvent.getWhen(), 0,
                                KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);

                        SwingUtilities.notifyAction(action, leftKeyStroke, ke, tree, actionEvent.getModifiers());
                    }
                }
            }
        });

        getActionMap().put(rightKeyStroke, new AbstractAction() {

            private static final long serialVersionUID = -5850337082346665414L;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JTree tree = getTree();

                InputMap map = tree.getInputMap(WHEN_FOCUSED);
                ActionMap am = tree.getActionMap();

                if (map != null && am != null && isEnabled()) {
                    Object binding = map.get(rightKeyStroke);
                    Action action = (binding == null) ? null : am.get(binding);
                    if (action != null) {

                        KeyEvent ke = new KeyEvent(tree, KeyEvent.KEY_PRESSED, actionEvent.getWhen(), 0,
                                KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);

                        SwingUtilities.notifyAction(action, rightKeyStroke, ke, tree, actionEvent.getModifiers());
                    }
                }
            }
        });

    }

    public DefaultTreeTableTree getTree() {
        return tree;
    }

    protected void setTreeTableColumnModel() {

        TreeTableModelAdapter model = (TreeTableModelAdapter) getModel();

        TableColumnModel tableColumnModel = model.getTableColumnModel();

        setColumnModel(tableColumnModel);
    }

    public void expandAll(boolean expand) {

        DefaultTreeTableTree treeTableTree = getTree();

        Object rootNode = treeTableTree.getModel().getRoot();

        TreePath rootPath = new TreePath(rootNode);

        expandAll(rootPath, expand, 0);
    }

    @SuppressWarnings("rawtypes")
    public void expandAll(TreePath parent, boolean expand, int level) {

        DefaultTreeTableTree treeTableTree = getTree();

        AbstractTreeTableNode node = (AbstractTreeTableNode) parent.getLastPathComponent();

        if (node.getChildCount() > 0) {

            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode childNode = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(childNode);

                expandAll(path, expand, level + 1);
            }

            // restricting expand/collapse to 1 level onwards
            if (level > 0) {
                // Expansion or collapse must be done bottom-up
                if (expand) {
                    treeTableTree.expandPath(parent);
                } else {
                    treeTableTree.collapsePath(parent);
                }
            }
        }
    }

    public void selectTreeNodeTableRow(AbstractTreeTableNode abstractTreeTableNode) {

        TreeTableModelAdapter treeTableModelAdapter;

        treeTableModelAdapter = (TreeTableModelAdapter) getModel();

        int rowIndex = -1;
        int rowCount = treeTableModelAdapter.getRowCount();

        for (int i = 0; i < rowCount; i++) {

            Object obj = treeTableModelAdapter.getValueAt(i, 0);

            if (obj != null) {

                if (abstractTreeTableNode.equals(obj)) {
                    rowIndex = i;
                    break;
                }
            }
        }

        if (rowIndex != -1) {
            setRowSelectionInterval(rowIndex, rowIndex);
            scrollRowToVisible(rowIndex);
        }
    }

    public void scrollNodeToVisible(AbstractTreeTableNode abstractTreeTableNode) {

        DefaultTreeTableTree treeTableTree = getTree();

        TreePath treePath = new TreePath(abstractTreeTableNode.getPath());

        LOG.debug("scrollNodeToVisible - treePath: " + treePath);
        treeTableTree.setSelectionPath(treePath);
        // treeTableTree.scrollPathToVisible(treePath);
        selectTreeNodeTableRow(abstractTreeTableNode);
    }

}
