/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

public class FileReadTaskInfo implements Comparable<FileReadTaskInfo> {

    private long fileSize;

    private long fileRead;

    public FileReadTaskInfo(long fileSize, long fileRead) {
        super();
        this.fileSize = fileSize;
        this.fileRead = fileRead;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getFileRead() {
        return fileRead;
    }

    @Override
    public int compareTo(FileReadTaskInfo other) {

        if (this.fileSize != other.fileSize) {

            Long thisFileSize = Long.valueOf(getFileSize());
            Long otherFileSize = Long.valueOf(other.getFileSize());

            return thisFileSize.compareTo(otherFileSize);

        } else {
            Long thisFileRead = Long.valueOf(getFileRead());
            Long otherFileRead = Long.valueOf(other.getFileRead());

            return thisFileRead.compareTo(otherFileRead);

        }
    }

}
