/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class DragDropJPanel extends JPanel implements DropTargetListener {

    private static final long serialVersionUID = 1548046368672009855L;

    private static final Log4j2Helper LOG = new Log4j2Helper(DragDropJPanel.class);

    private DragDropJPanelListener dragDropJPanelListener;

    private Border normalBorder;

    public DragDropJPanel(DragDropJPanelListener dragDropJPanelListener) {
        super();

        this.dragDropJPanelListener = dragDropJPanelListener;
        this.normalBorder = this.getBorder();

        try {
            DropTarget dt = new DropTarget();
            dt.addDropTargetListener(this);
            setDropTarget(dt);
        } catch (TooManyListenersException e) {
            LOG.error("Error adding drop target listner", e);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (isDragOk(dtde)) {

            setBorder(BorderFactory.createLineBorder(Color.RED));

            dtde.acceptDrag(DnDConstants.ACTION_NONE);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        if (isDragOk(dtde)) {
            dtde.acceptDrag(DnDConstants.ACTION_NONE);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        setBorder(normalBorder);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        try {
            Transferable tr = dtde.getTransferable();

            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                // Get a useful list
                @SuppressWarnings("unchecked")
                List<File> fileList = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);

                // Alert listener to drop.
                dragDropJPanelListener.filesDropped(fileList);

                // Mark that drop is completed.
                dtde.getDropTargetContext().dropComplete(true);

            }

        } catch (Exception e) {
            LOG.error("Error performing drop operation", e);
        } finally {
            // reset border
            setBorder(normalBorder);
        }

    }

    private boolean isDragOk(final DropTargetDragEvent evt) {

        boolean retValue = false;

        // Get data flavors being dragged
        DataFlavor[] dataFlavorArray = evt.getCurrentDataFlavors();

        for (int i = 0; i < dataFlavorArray.length; i++) {

            final DataFlavor dataFlavor = dataFlavorArray[i];

            if (dataFlavor.equals(DataFlavor.javaFileListFlavor) || dataFlavor.isRepresentationClassReader()) {
                retValue = true;
                break;
            }
        }

        LOG.info(retValue);
        return retValue;
    }

}
