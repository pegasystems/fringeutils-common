/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class ProgressTaskInfo implements Comparable<ProgressTaskInfo> {

	private long total;

	private long count;

	private String note;

	public ProgressTaskInfo(long total, long count) {
		this(total, count, null);
	}

	public ProgressTaskInfo(long total, long count, String note) {
		super();
		this.total = total;
		this.count = count;
		this.note = note;
	}

	public long getTotal() {
		return total;
	}

	public long getCount() {
		return count;
	}

	public String getNote() {
		return note;
	}

	@Override
	public int compareTo(ProgressTaskInfo o) {

		if (this.total != o.total) {

			Long thisTotal = new Long(getTotal());
			Long otherTotal = new Long(o.getTotal());

			return thisTotal.compareTo(otherTotal);

		} else {
			Long thisCount = new Long(getCount());
			Long otherCount = new Long(o.getCount());

			return thisCount.compareTo(otherCount);

		}
	}

}
