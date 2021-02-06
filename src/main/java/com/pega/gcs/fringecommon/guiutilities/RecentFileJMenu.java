/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public abstract class RecentFileJMenu extends JMenu implements RecentFileContainerListener {

    private static final long serialVersionUID = 6861799077954827435L;

    private RecentFileContainer recentFileContainer;

    public abstract void onSelect(RecentFile recentFile);

    public RecentFileJMenu(RecentFileContainer recentFileContainer) {

        super("Recent");

        this.recentFileContainer = recentFileContainer;

        this.setMnemonic(KeyEvent.VK_R);

        this.recentFileContainer.addRecentFileContainerListener(this);

        populateJMenuItems();
    }

    private void populateJMenuItems() {

        removeAll();

        int index = 0;

        for (RecentFile recentFile : recentFileContainer.getRecentFileList()) {

            if (recentFile != null) {

                String recentFileStr = recentFile.toString();

                JMenuItem menuItem = new JMenuItem(recentFileStr);
                menuItem.setToolTipText(recentFileStr);
                menuItem.setActionCommand(String.valueOf(index));
                menuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        onSelect(recentFile);
                    }
                });

                add(menuItem);
            }

            index++;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fringe.common.guiutilities.RecentFileContainerListener# recentFileAdded ()
     */
    @Override
    public void recentFileAdded() {
        populateJMenuItems();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fringe.common.guiutilities.RecentFileContainerListener# recentFileDeleted ()
     */
    @Override
    public void recentFileDeleted() {
        populateJMenuItems();
    }

}
