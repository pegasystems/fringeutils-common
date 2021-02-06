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
    public int compareTo(EventReadTaskInfo other) {

        if (this.eventCount != other.eventCount) {

            Long thisEventCount = Long.valueOf(getEventCount());
            Long otherEventCount = Long.valueOf(other.getEventCount());

            return thisEventCount.compareTo(otherEventCount);

        } else {
            Long thisLinesRead = Long.valueOf(getLinesRead());
            Long otherLinesRead = Long.valueOf(other.getLinesRead());

            return thisLinesRead.compareTo(otherLinesRead);

        }
    }

}
