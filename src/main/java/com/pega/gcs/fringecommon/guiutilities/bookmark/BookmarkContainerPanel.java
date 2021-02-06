/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.bookmark;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.pega.gcs.fringecommon.guiutilities.FilterTableModel;
import com.pega.gcs.fringecommon.guiutilities.NavigationTableController;
import com.pega.gcs.fringecommon.guiutilities.markerbar.Marker;

public abstract class BookmarkContainerPanel<T> extends JPanel {

    private static final long serialVersionUID = -7076048541108098076L;

    public abstract FilterTableModel<? super T> getFilterTableModel();

    private BookmarkModel<T> bookmarkModel;

    private NavigationTableController<T> navigationTableController;

    JList<Marker<T>> bookmarkJList;

    private JButton deleteJButton;

    private JButton deleteAllJButton;

    public BookmarkContainerPanel(BookmarkModel<T> bookmarkModel,
            NavigationTableController<T> navigationTableController) {

        super();

        this.bookmarkModel = bookmarkModel;
        this.navigationTableController = navigationTableController;

        setLayout(new BorderLayout());

        JPanel buttonsJPanel = getButtonsJPanel();
        JPanel bookmarkListPanel = getBookmarkListPanel();

        add(buttonsJPanel, BorderLayout.NORTH);
        add(bookmarkListPanel, BorderLayout.CENTER);
    }

    protected BookmarkModel<T> getBookmarkModel() {
        return bookmarkModel;
    }

    protected NavigationTableController<T> getNavigationTableController() {
        return navigationTableController;
    }

    private JPanel getButtonsJPanel() {

        JPanel buttonsJPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonsJPanel, BoxLayout.X_AXIS);
        buttonsJPanel.setLayout(layout);

        JButton deleteJButton = getDeleteJButton();
        JButton deleteAllJButton = getDeleteAllJButton();

        Dimension dim = new Dimension(1, 30);

        buttonsJPanel.add(Box.createHorizontalGlue());
        buttonsJPanel.add(deleteJButton);
        buttonsJPanel.add(Box.createRigidArea(dim));
        buttonsJPanel.add(Box.createHorizontalGlue());
        buttonsJPanel.add(deleteAllJButton);
        buttonsJPanel.add(Box.createHorizontalGlue());

        return buttonsJPanel;
    }

    private JList<Marker<T>> getBookmarkJList() {

        if (bookmarkJList == null) {

            BookmarkModel<T> bookmarkModel = getBookmarkModel();

            DefaultListModel<Marker<T>> dlm = new DefaultListModel<Marker<T>>();

            BookmarkContainer<T> bookmarkContainer;
            bookmarkContainer = bookmarkModel.getBookmarkContainer();

            Iterator<T> bmIterator = bookmarkContainer.getIterator();

            while (bmIterator.hasNext()) {

                T key = bmIterator.next();

                List<Marker<T>> bookmarkList;
                bookmarkList = bookmarkContainer.getBookmarkList(key);

                if (bookmarkList != null) {
                    for (Marker<T> bookmark : bookmarkList) {
                        dlm.addElement(bookmark);
                    }
                }
            }

            bookmarkJList = new JList<>(dlm);

            DefaultListCellRenderer dlcr = getDefaultListCellRenderer();

            bookmarkJList.setCellRenderer(dlcr);

            bookmarkJList.setFixedCellHeight(20);

            bookmarkJList.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                    if (mouseEvent.getClickCount() == 2) {

                        Marker<T> bookmark = bookmarkJList.getSelectedValue();

                        if (bookmark != null) {
                            T key = bookmark.getKey();

                            getNavigationTableController().scrollToKey(key);
                        }
                    } else {
                        super.mouseClicked(mouseEvent);
                    }
                }

            });

        }
        return bookmarkJList;
    }

    private JButton getDeleteJButton() {

        if (deleteJButton == null) {
            deleteJButton = new JButton("Delete Bookmark");

            Dimension size = new Dimension(150, 20);
            deleteJButton.setPreferredSize(size);
            deleteJButton.setMaximumSize(size);
            // deleteJButton.setHorizontalTextPosition(SwingConstants.LEADING);

            deleteJButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    Marker<T> bookmark = (Marker<T>) bookmarkJList.getSelectedValue();

                    if (bookmark != null) {

                        T key = bookmark.getKey();

                        BookmarkModel<T> bookmarkModel = getBookmarkModel();

                        BookmarkContainer<T> bookmarkContainer;
                        bookmarkContainer = bookmarkModel.getBookmarkContainer();

                        List<Marker<T>> bookmarkList;
                        bookmarkList = bookmarkContainer.getBookmarkList(key);

                        int index = 0;

                        for (Marker<T> bm : bookmarkList) {
                            // remove the first occurrence
                            if (bm.getText().equals(bookmark.getText())) {

                                DefaultListModel<Marker<T>> dlm = (DefaultListModel<Marker<T>>) bookmarkJList
                                        .getModel();

                                dlm.removeElement(bookmark);

                                bookmarkModel.removeMarker(bm.getKey(), index);

                                break;
                            }

                            index++;
                        }

                    }
                }

            });

        }

        return deleteJButton;
    }

    private JButton getDeleteAllJButton() {

        if (deleteAllJButton == null) {
            deleteAllJButton = new JButton("Delete all Bookmarks");

            Dimension size = new Dimension(150, 20);
            deleteAllJButton.setPreferredSize(size);
            deleteAllJButton.setMaximumSize(size);
            // deleteJButton.setHorizontalTextPosition(SwingConstants.LEADING);

            deleteAllJButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    DefaultListModel<Marker<T>> dlm = (DefaultListModel<Marker<T>>) bookmarkJList.getModel();
                    dlm.clear();

                    BookmarkModel<T> bookmarkModel = getBookmarkModel();
                    bookmarkModel.clearMarkers();
                }

            });

        }

        return deleteAllJButton;
    }

    private DefaultListCellRenderer getDefaultListCellRenderer() {

        DefaultListCellRenderer dlcr = new DefaultListCellRenderer() {

            private static final long serialVersionUID = 7578704614877257466L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setBorder(new EmptyBorder(1, 10, 1, 1));

                return this;
            }

        };

        return dlcr;
    }

    private JPanel getBookmarkListPanel() {

        JPanel bookmarkListPanel = new JPanel();

        LayoutManager layout = new BoxLayout(bookmarkListPanel, BoxLayout.Y_AXIS);

        bookmarkListPanel.setLayout(layout);

        String text = "Double click on an entry to select the row on main table.";
        JPanel labelJPanel = getLabelJPanel(text);

        JList<Marker<T>> bookmarkJList = getBookmarkJList();
        JScrollPane bookmarkJListScrollPane = new JScrollPane(bookmarkJList);

        bookmarkListPanel.add(labelJPanel);
        bookmarkListPanel.add(bookmarkJListScrollPane);

        return bookmarkListPanel;
    }

    private JPanel getLabelJPanel(String text) {

        JPanel labelJPanel = new JPanel();

        LayoutManager layout = new BoxLayout(labelJPanel, BoxLayout.LINE_AXIS);
        labelJPanel.setLayout(layout);

        JLabel label = new JLabel(text);

        int height = 30;

        Dimension spacer = new Dimension(10, height);
        labelJPanel.add(Box.createRigidArea(spacer));
        labelJPanel.add(label);
        labelJPanel.add(Box.createHorizontalGlue());

        labelJPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return labelJPanel;

    }

}
