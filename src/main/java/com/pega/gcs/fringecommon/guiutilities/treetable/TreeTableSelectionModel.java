/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.treetable;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

public class TreeTableSelectionModel extends DefaultTreeSelectionModel {

    private static final long serialVersionUID = -7982987488981239448L;

    private boolean updatingListSelectionModel;

    private JTree tree;

    public TreeTableSelectionModel(JTree tree) {

        super();

        this.tree = tree;

        setSelectionMode(SINGLE_TREE_SELECTION);

        getListSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                updateSelectedPathsFromSelectedRows();
            }
        });
    }

    public ListSelectionModel getListSelectionModel() {
        return listSelectionModel;
    }

    @Override
    public void resetRowSelection() {
        if (!updatingListSelectionModel) {
            updatingListSelectionModel = true;
            try {
                super.resetRowSelection();
            } finally {
                updatingListSelectionModel = false;
            }
        }
    }

    /**
     * If updatingListSelectionModel is false, this will reset the selected paths from the selected rows in the list selection model.
     */
    protected void updateSelectedPathsFromSelectedRows() {
        if (!updatingListSelectionModel) {
            updatingListSelectionModel = true;
            try {
                // This is way expensive, ListSelectionModel needs an
                // enumerator for iterating.
                int min = listSelectionModel.getMinSelectionIndex();
                int max = listSelectionModel.getMaxSelectionIndex();

                clearSelection();
                if (min != -1 && max != -1) {
                    for (int counter = min; counter <= max; counter++) {
                        if (listSelectionModel.isSelectedIndex(counter)) {
                            TreePath selPath = tree.getPathForRow(counter);

                            if (selPath != null) {
                                addSelectionPath(selPath);
                            }
                        }
                    }
                }
            } finally {
                updatingListSelectionModel = false;
            }
        }
    }
}
