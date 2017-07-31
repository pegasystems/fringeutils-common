/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.io.Serializable;

public class FilterColumn implements Comparable<FilterColumn>, Serializable {

	private static final long serialVersionUID = 2204061808458324810L;

	private int index;

	private boolean columnFilterEnabled;

	private boolean columnFiltered;

	private boolean columnLoading;

	public FilterColumn(int index) {
		super();
		this.index = index;
		this.columnFilterEnabled = false;
		this.columnFiltered = false;
		this.columnLoading = false;
	}

	/**
	 * @return the columnFilterEnabled
	 */
	public boolean isColumnFilterEnabled() {
		return columnFilterEnabled;
	}

	/**
	 * @param columnFilterEnabled
	 *            the columnFilterEnabled to set
	 */
	public void setColumnFilterEnabled(boolean columnFilterEnabled) {
		this.columnFilterEnabled = columnFilterEnabled;
	}

	/**
	 * @return the columnFiltered
	 */
	public boolean isColumnFiltered() {
		return columnFiltered;
	}

	/**
	 * @param columnFiltered
	 *            the columnFiltered to set
	 */
	public void setColumnFiltered(boolean columnFiltered) {
		this.columnFiltered = columnFiltered;
	}

	/**
	 * @return the columnLoading
	 */
	public boolean isColumnLoading() {
		return columnLoading;
	}

	/**
	 * @param columnLoading
	 *            the columnLoading to set
	 */
	public void setColumnLoading(boolean columnLoading) {
		this.columnLoading = columnLoading;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterColumn other = (FilterColumn) obj;
		if (index != other.index)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FilterColumn [index=" + index + ", columnFilterEnabled=" + columnFilterEnabled + ", columnFiltered="
				+ columnFiltered + ", columnLoading=" + columnLoading + "]";
	}

	@Override
	public int compareTo(FilterColumn o) {
		return new Integer(index).compareTo(new Integer(o.index));
	}

}
