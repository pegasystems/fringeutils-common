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
	public int compareTo(FileReadTaskInfo o) {

		if (this.fileSize != o.fileSize) {

			Long thisFileSize = new Long(getFileSize());
			Long otherFileSize = new Long(o.getFileSize());

			return thisFileSize.compareTo(otherFileSize);

		} else {
			Long thisFileRead = new Long(getFileRead());
			Long otherFileRead = new Long(o.getFileRead());

			return thisFileRead.compareTo(otherFileRead);

		}
	}

}
