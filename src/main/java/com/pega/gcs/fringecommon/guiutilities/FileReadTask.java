/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

/**
 * Read from disk in *MB chunks as pass it to LinkedBlockingQueue.
 */
public class FileReadTask extends SwingWorker<Void, Void> {

    private static final Log4j2Helper LOG = new Log4j2Helper(FileReadTask.class);

    private static int DEFAULT_SCAN_SECS = 5;
    private static int DEFAULT_CHUNK_SIZE = 8;

    private int chunkSize;

    private File file;

    private LinkedBlockingQueue<FileReadByteArray> fileReadQueue;

    private boolean tail;

    private int scanSecs;

    private AtomicLong fileSize;

    private long totalRead;

    // default not to tail
    public FileReadTask(File file, AtomicLong fileSize, LinkedBlockingQueue<FileReadByteArray> fileReadQueue)
            throws IOException {
        this(DEFAULT_CHUNK_SIZE, file, fileSize, fileReadQueue, false, DEFAULT_SCAN_SECS);
    }

    public FileReadTask(File file, AtomicLong fileSize, LinkedBlockingQueue<FileReadByteArray> fileReadQueue,
            boolean tail) throws IOException {
        this(DEFAULT_CHUNK_SIZE, file, fileSize, fileReadQueue, tail, DEFAULT_SCAN_SECS);
    }

    public FileReadTask(int chunkSize, File file, AtomicLong fileSize,
            LinkedBlockingQueue<FileReadByteArray> fileReadQueue, boolean tail, int scanSecs) throws IOException {

        super();

        this.chunkSize = chunkSize;
        this.file = file;
        this.fileSize = fileSize;
        this.fileReadQueue = fileReadQueue;
        this.tail = tail;
        this.scanSecs = scanSecs;
        this.totalRead = 0;
    }

    @Override
    protected Void doInBackground() throws Exception {

        int buffSize = chunkSize * (int) FileUtils.ONE_MB;
        int readLen = 0;

        try {

            do {

                try (FileInputStream fis = new FileInputStream(file)) {

                    FileChannel fileChannel = fis.getChannel();
                    long totalSize = fileChannel.size();

                    byte[] byteBuffer = new byte[buffSize];

                    if (totalSize < totalRead) {
                        LOG.info("FileReadTask - File truncated in background, readjusting total read count");
                        totalRead = totalSize;
                    }

                    if (totalRead < totalSize) {

                        long before = System.currentTimeMillis();

                        long fileSizeDiff = totalSize - totalRead;

                        fis.skip(totalRead);

                        while ((!isCancelled()) && ((readLen = fis.read(byteBuffer)) != -1)) {

                            totalRead = totalRead + readLen;
                            fileSize.set(totalRead);

                            byte[] fileReadBytes = new byte[(int) readLen];

                            System.arraycopy(byteBuffer, 0, fileReadBytes, 0, readLen);
                            FileReadByteArray frba = new FileReadByteArray(fileReadBytes);
                            fileReadQueue.put(frba);

                            byteBuffer = new byte[buffSize];
                        }

                        long diff = System.currentTimeMillis() - before;

                        char infinity = '\u221e'; // infinity character
                        String readSpeedStr = null;

                        int secs = (int) Math.ceil((double) diff / 1E3);

                        if (secs > 0) {
                            long readSpeed = fileSizeDiff / secs;
                            readSpeedStr = GeneralUtilities.humanReadableSize(readSpeed, false);

                        } else {
                            readSpeedStr = String.valueOf(infinity);
                        }

                        String message = String.format(
                                "FileReadTask - Read %s bytes in %d secs. Total Read=%s Read Speed=%s/secs",
                                Long.toString(fileSizeDiff), secs, Long.toString(totalRead), readSpeedStr);

                        LOG.info(message);

                        // insert an empty byte buffer to indicate a iteration
                        // of read occurred. this fixes the issue of parsing the
                        // final log entry of every fetch.
                        byteBuffer = new byte[0];
                        FileReadByteArray frba = new FileReadByteArray(byteBuffer);
                        // LOG.info("FileReadTask - begin put
                        // FileReadByteArray.");
                        fileReadQueue.put(frba);
                        // LOG.info("FileReadTask - end put
                        // FileReadByteArray.");

                    }

                    if ((tail) && (!isCancelled())) {
                        LOG.debug(String.format("FileReadTask - Waiting {%d} secs\n", scanSecs));

                        try {
                            synchronized (this) {
                                wait(scanSecs * 1000);
                            }
                        } catch (InterruptedException ie) {
                            LOG.error("FileReadTask - Thread interrupted", ie);
                        }

                        LOG.debug("FileReadTask - getting out of  wait " + scanSecs + "secs");
                    }

                }

            } while ((tail) && (!isCancelled()));

            LOG.info("FileReadTask - finished tailing.");

        } catch (Exception e) {
            LOG.error("Error in FileReadTask", e);
            throw e;
        } catch (OutOfMemoryError oome) {
            LOG.error("Out of memory error in FileReadTask", oome);
            throw (oome);
        }
        return null;
    }

}
