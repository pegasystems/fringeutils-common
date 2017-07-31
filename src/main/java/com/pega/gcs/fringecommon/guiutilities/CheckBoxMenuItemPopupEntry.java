/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckBoxMenuItemPopupEntry<T> implements Comparable<CheckBoxMenuItemPopupEntry<T>> {

	private Object userObject;

	private List<T> rowIndexList;

	private boolean visible;

	private boolean selected;

	// show updated counts in case of multiple filters are applied
	private int filteredCount;

	/**
	 * @param traceEventStr
	 */
	public CheckBoxMenuItemPopupEntry(Object userObject) {

		super();

		this.userObject = userObject;
		this.rowIndexList = new ArrayList<T>();
		this.visible = true;
		this.selected = false;
		this.filteredCount = -1;
	}

	public Object getUserObject() {
		return userObject;
	}

	public String getText() {
		return userObject.toString();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getFilteredCount() {
		return filteredCount;
	}

	public void setFilteredCount(int filteredCount) {
		this.filteredCount = filteredCount;
	}

	public List<T> getRowIndexList() {
		return Collections.unmodifiableList(rowIndexList);
	}

	public void addRowIndex(T rowindex) {
		rowIndexList.add(rowindex);

		filteredCount = rowIndexList.size();
	}

	public boolean containsRowIndex(T rowindex) {
		return rowIndexList.contains(rowindex);
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
		result = prime * result + ((getText() == null) ? 0 : getText().hashCode());
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
		@SuppressWarnings("unchecked")
		CheckBoxMenuItemPopupEntry<T> other = (CheckBoxMenuItemPopupEntry<T>) obj;
		if (getText() == null) {
			if (other.getText() != null)
				return false;
		} else if (!getText().equals(other.getText()))
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
		return getText();
	}

	@Override
	public int compareTo(CheckBoxMenuItemPopupEntry<T> o) {
		return getText().compareTo(o.getText());
	}

}
