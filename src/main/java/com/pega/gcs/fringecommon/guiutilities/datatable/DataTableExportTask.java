
package com.pega.gcs.fringecommon.guiutilities.datatable;

import java.io.File;
import java.nio.charset.Charset;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

public class DataTableExportTask extends SwingWorker<Void, Integer> {

    private AbstractDataTableModel<?, ?> abstractDataTableModel;
    private File tsvFile;

    public DataTableExportTask(AbstractDataTableModel<?, ?> abstractDataTableModel, File tsvFile) {

        this.abstractDataTableModel = abstractDataTableModel;
        this.tsvFile = tsvFile;
    }

    @Override
    protected Void doInBackground() throws Exception {

        int rowCount = abstractDataTableModel.getRowCount();
        int colCount = abstractDataTableModel.getColumnCount();

        int batchSize = 4194304; // 4096KB
        StringBuilder outputStrBatch = new StringBuilder();
        boolean append = false;
        Charset charset = abstractDataTableModel.getCharset();

        int row = 0;

        publish(row);

        boolean firstColumn = true;

        // write headers
        for (int col = 0; col < colCount; col++) {

            String columnName = abstractDataTableModel.getColumnList().get(col).getDisplayName();

            if (firstColumn) {
                firstColumn = false;
            } else {
                outputStrBatch.append("\t");
            }

            outputStrBatch.append(columnName);
        }

        outputStrBatch.append(System.getProperty("line.separator"));

        for (row = 0; row < rowCount; row++) {

            if (!isCancelled()) {

                firstColumn = true;

                Object rowValue = abstractDataTableModel.getValueAt(row, 0);

                for (int col = 0; col < colCount; col++) {

                    String columnValue = abstractDataTableModel.getColumnValue(rowValue, col);

                    if (firstColumn) {
                        firstColumn = false;
                    } else {
                        outputStrBatch.append("\t");
                    }

                    if (columnValue != null) {
                        outputStrBatch.append(columnValue);
                    }
                }

                outputStrBatch.append(System.getProperty("line.separator"));

                int accumulatedSize = outputStrBatch.length();

                if (accumulatedSize > batchSize) {

                    FileUtils.writeStringToFile(tsvFile, outputStrBatch.toString(), charset, append);

                    outputStrBatch = new StringBuilder();

                    if (!append) {
                        append = true;
                    }

                    publish(row);
                }
            }
        }

        if (!isCancelled()) {

            int accumulatedSize = outputStrBatch.length();

            if (accumulatedSize > 0) {
                FileUtils.writeStringToFile(tsvFile, outputStrBatch.toString(), charset, append);
            }

            publish(row);
        }

        return null;
    }
}
