/*******************************************************************************
 *  Copyright (c) 2020 Pegasystems Inc. All rights reserved.
 *
 *  Contributors:
 *      Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

public class CustomJTableTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 7570937807354290358L;

    private CustomJTable customJTable;

    public CustomJTableTransferHandler(CustomJTable customJTable) {
        super();
        this.customJTable = customJTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    @Override
    public int getSourceActions(JComponent component) {
        return TransferHandler.COPY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    @Override
    protected Transferable createTransferable(JComponent component) {

        int[] selectedRows = customJTable.getSelectedRows();

        StringBuilder dataSB = new StringBuilder();

        if (selectedRows != null) {

            TableModel tableModel = customJTable.getModel();

            if (tableModel instanceof CustomJTableModel) {

                CustomJTableModel customJTableModel = (CustomJTableModel) tableModel;

                int columnCount = customJTableModel.getColumnCount();

                // build Header row
                for (int column = 0; column < columnCount; column++) {

                    String headerName = customJTableModel.getColumnName(column);

                    dataSB.append(headerName);
                    dataSB.append("\t");
                }

                dataSB.append(System.getProperty("line.separator"));

                // build data rows
                for (int selectedRow : selectedRows) {

                    Object rowObject = customJTableModel.getValueAt(selectedRow, 0);

                    for (int column = 0; column < columnCount; column++) {

                        String columnValue = customJTableModel.getColumnValue(rowObject, column);

                        dataSB.append(columnValue);
                        dataSB.append("\t");
                    }

                    dataSB.append(System.getProperty("line.separator"));
                }
            }
        }

        return new StringSelection(dataSB.toString());
    }

}
