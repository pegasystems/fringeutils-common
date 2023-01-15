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
import java.security.MessageDigest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

/**
 * Read from disk in *MB chunks as pass it to LinkedBlockingQueue.
 */
public class FileReaderThread implements Runnable {

    private static final Log4j2Helper LOG = new Log4j2Helper(FileReaderThread.class);

    private static int DEFAULT_SCAN_SECS = 5;
    private static int DEFAULT_CHUNK_SIZE = 8;

    private int chunkSize;

    private File file;

    private LinkedBlockingQueue<FileReadByteArray> fileReadQueue;

    private boolean tail;

    private int scanSecs;

    private AtomicLong fileSize;

    private long totalRead;

    private AtomicBoolean cancel;

    private boolean calcSHA;

    private MessageDigest messageDigest;

    // default not to tail
    public FileReaderThread(File file, AtomicLong fileSize, LinkedBlockingQueue<FileReadByteArray> fileReadQueue,
            AtomicBoolean cancel) throws IOException {
        this(DEFAULT_CHUNK_SIZE, file, fileSize, fileReadQueue, false, DEFAULT_SCAN_SECS, cancel, true);
    }

    public FileReaderThread(File file, AtomicLong fileSize, LinkedBlockingQueue<FileReadByteArray> fileReadQueue,
            boolean tail, AtomicBoolean cancel) throws IOException {
        this(DEFAULT_CHUNK_SIZE, file, fileSize, fileReadQueue, tail, DEFAULT_SCAN_SECS, cancel, true);
    }

    public FileReaderThread(int chunkSize, File file, AtomicLong fileSize,
            LinkedBlockingQueue<FileReadByteArray> fileReadQueue, boolean tail, int scanSecs, AtomicBoolean cancel,
            boolean calcSHA) throws IOException {

        super();

        this.chunkSize = chunkSize;
        this.file = file;
        this.fileSize = fileSize;
        this.fileReadQueue = fileReadQueue;
        this.tail = tail;
        this.scanSecs = scanSecs;
        this.cancel = cancel;
        this.calcSHA = calcSHA;
        this.totalRead = 0;
        this.messageDigest = null;
    }

    @Override
    public void run() {

        int buffSize = chunkSize * (int) FileUtils.ONE_MB;
        int readLen = 0;

        try {

            if (calcSHA) {
                messageDigest = MessageDigest.getInstance("SHA-256");
            }

            do {

                // only handling UTF-8 BOM
                try (FileInputStream fis = new FileInputStream(file); BOMInputStream bomis = new BOMInputStream(fis)) {

                    FileChannel fileChannel = fis.getChannel();
                    long totalSize = fileChannel.size();
                    long totalSizeNoBOM = -1;

                    byte[] byteBuffer = new byte[buffSize];

                    LOG.debug("FileReaderThread - totalSize: " + totalSize + " totalRead: " + totalRead);

                    if (totalSize < totalRead) {

                        LOG.info("FileReaderThread - File truncated in background, exiting...");

                        totalRead = 0;

                        if (messageDigest != null) {
                            messageDigest.reset();
                        }

                        cancel.set(true);

                    } else {

                        ByteOrderMark bom = bomis.getBOM();

                        // Subtracting 3 bytes from length calculation
                        if (bom != null) {

                            int bomLength = bom.length();
                            totalSizeNoBOM = totalSize - bomLength;

                            LOG.info("FileReaderThread - BOM found in file, bomLength = " + bomLength);

                        } else {
                            totalSizeNoBOM = totalSize;
                        }

                        if ((totalRead == 0) && (bom != null) && (messageDigest != null)) {

                            byte[] bomBytes = bom.getBytes();
                            int bomLength = bom.length();

                            messageDigest.update(bomBytes, 0, bomLength);

                        }

                        if (totalRead < totalSizeNoBOM) {

                            long before = System.currentTimeMillis();

                            // only use totalSize for logging read info. for read tracking use totalSizeNoBOM.
                            long fileSizeDiff = totalSize - totalRead;

                            bomis.skip(totalRead);

                            while ((!isCancelled()) && ((readLen = bomis.read(byteBuffer)) != -1)) {

                                totalRead = totalRead + readLen;
                                fileSize.set(totalRead);

                                LOG.trace("FileReaderThread - Loop readLen: " + readLen + " totalRead: " + totalRead);

                                byte[] fileReadBytes = new byte[(int) readLen];

                                System.arraycopy(byteBuffer, 0, fileReadBytes, 0, readLen);

                                if (messageDigest != null) {
                                    messageDigest.update(fileReadBytes, 0, readLen);
                                }

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
                                    "FileReaderThread - Read %s bytes in %d secs. Total Read=%s Read Speed=%s/secs",
                                    Long.toString(fileSizeDiff), secs, Long.toString(totalRead), readSpeedStr);

                            LOG.info(message);

                            // insert an empty byte buffer to indicate a iteration
                            // of read occurred. this fixes the issue of parsing the
                            // final log entry of every fetch.
                            byteBuffer = new byte[0];
                            FileReadByteArray frba = new FileReadByteArray(byteBuffer);
                            // LOG.info("FileReaderThread - begin put
                            // FileReadByteArray.");
                            fileReadQueue.put(frba);
                            // LOG.info("FileReaderThread - end put
                            // FileReadByteArray.");

                        }

                        if ((tail) && (!isCancelled())) {

                            try {
                                synchronized (this) {
                                    wait(scanSecs * 1000);
                                }
                            } catch (InterruptedException ie) {
                                LOG.error("FileReaderThread - Thread interrupted.", ie);
                            }

                            // LOG.info("FileReaderThread - getting out of wait " +
                            // scanSecs + "secs");
                        }
                    }
                }

            } while ((tail) && (!isCancelled()));

            LOG.info("FileReaderThread - finished tailing.");

        } catch (Exception e) {
            LOG.error("Error in FileReaderThread", e);
            cancel.set(true);
        }
    }

    private boolean isCancelled() {
        return cancel.get();
    }

    public void stopTailing() {
        this.tail = false;
    }

    public String getSHA() {

        String sha = null;

        if (messageDigest != null) {
            byte[] mdbytes = messageDigest.digest();
            sha = GeneralUtilities.byteArrayToHexString(mdbytes);
        }

        return sha;
    }
}
