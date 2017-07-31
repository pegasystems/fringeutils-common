/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class EventReadTaskInfo implements Comparable<EventReadTaskInfo> {

	private long eventCount;

	private long linesRead;

	public EventReadTaskInfo(long eventCount, long linesRead) {
		super();
		this.eventCount = eventCount;
		this.linesRead = linesRead;
	}

	public long getEventCount() {
		return eventCount;
	}

	public long getLinesRead() {
		return linesRead;
	}

	@Override
	public int compareTo(EventReadTaskInfo o) {

		if (this.eventCount != o.eventCount) {

			Long thisEventCount = new Long(getEventCount());
			Long otherEventCount = new Long(o.getEventCount());

			return thisEventCount.compareTo(otherEventCount);

		} else {
			Long thisLinesRead = new Long(getLinesRead());
			Long otherLinesRead = new Long(o.getLinesRead());

			return thisLinesRead.compareTo(otherLinesRead);

		}
	}

}
