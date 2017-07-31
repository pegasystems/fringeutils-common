/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class ReadCounterTaskInfo implements Comparable<ReadCounterTaskInfo> {

	private FileReadTaskInfo fileReadTaskInfo;

	private EventReadTaskInfo eventReadTaskInfo;

	public ReadCounterTaskInfo(FileReadTaskInfo fileReadTaskInfo) {
		super();
		this.fileReadTaskInfo = fileReadTaskInfo;
	}

	public EventReadTaskInfo getEventReadTaskInfo() {
		return eventReadTaskInfo;
	}

	public void setEventReadTaskInfo(EventReadTaskInfo eventReadTaskInfo) {
		this.eventReadTaskInfo = eventReadTaskInfo;
	}

	public FileReadTaskInfo getFileReadTaskInfo() {
		return fileReadTaskInfo;
	}

	@Override
	public int compareTo(ReadCounterTaskInfo o) {
		return getFileReadTaskInfo().compareTo(o.getFileReadTaskInfo());
	}

}
