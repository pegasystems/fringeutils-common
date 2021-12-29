package com.pega.gcs.fringecommon.guiutilities.datatable;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.pega.gcs.fringecommon.guiutilities.DefaultTableColumn;
import com.pega.gcs.fringecommon.guiutilities.FilterTable;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

public class DataTable<T, V extends DefaultTableColumn> extends FilterTable<Integer> {

    private static final long serialVersionUID = 7882619873210948802L;

    public DataTable(AbstractDataTableModel<T, V> abstractDataTableModel, int colAutoResize) {

        super(abstractDataTableModel);

        setAutoResizeMode(colAutoResize);

        setTransferHandler(new TransferHandler() {

            private static final long serialVersionUID = 5316455238872189390L;

            @Override
            protected Transferable createTransferable(JComponent component) {

                int[] selectedRows = getSelectedRows();

                StringBuilder dataSB = new StringBuilder();

                if (selectedRows != null) {

                    @SuppressWarnings("unchecked")
                    AbstractDataTableModel<T, V> abstractDataTableModel = (AbstractDataTableModel<T, V>) getModel();

                    List<V> columnList = abstractDataTableModel.getColumnList();

                    List<String> columnNameList = new ArrayList<>();

                    for (V tableColumn : columnList) {
                        columnNameList.add(tableColumn.getDisplayName());
                    }

                    String scanResultHotfixInfoColumnListCSV = GeneralUtilities
                            .getCollectionAsSeperatedValues(columnNameList, null, true);

                    dataSB.append(scanResultHotfixInfoColumnListCSV);
                    dataSB.append(System.getProperty("line.separator"));

                    for (int selectedRow : selectedRows) {

                        @SuppressWarnings("unchecked")
                        T data = (T) abstractDataTableModel.getValueAt(selectedRow, 0);

                        List<String> recordDataList = new ArrayList<>();

                        for (V tableColumn : columnList) {
                            String columnValue = abstractDataTableModel.getColumnData(data, tableColumn);
                            recordDataList.add(columnValue);
                        }

                        String recordDataListCSV = GeneralUtilities.getCollectionAsSeperatedValues(recordDataList, null,
                                true);

                        dataSB.append(recordDataListCSV);
                        dataSB.append(System.getProperty("line.separator"));
                    }

                }

                return new StringSelection(dataSB.toString());
            }

            @Override
            public int getSourceActions(JComponent component) {
                return TransferHandler.COPY;
            }
        });

        setRowHeight(26);

    }

}
