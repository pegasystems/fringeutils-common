/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.util.List;

import javax.swing.SwingConstants;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public abstract class DefaultTableColumn {

	private static final Log4j2Helper LOG = new Log4j2Helper(DefaultTableColumn.class);

	private String columnId;

	private String displayName;

	private int prefColumnWidth;

	private int horizontalAlignment;

	private boolean visibleColumn;

	private boolean filterable;

	public DefaultTableColumn(String displayName) {
		this(displayName, displayName, -1, SwingConstants.LEFT, true, true);
	}

	public DefaultTableColumn(String displayName, int prefColumnWidth, int horizontalAlignment) {
		this(displayName, displayName, prefColumnWidth, horizontalAlignment, true, true);
	}

	public DefaultTableColumn(String columnId, String displayName, int prefColumnWidth, int horizontalAlignment,
			boolean visibleColumn, boolean filterable) {

		super();

		this.columnId = columnId;
		this.displayName = displayName;
		this.prefColumnWidth = prefColumnWidth;
		this.horizontalAlignment = horizontalAlignment;
		this.visibleColumn = visibleColumn;
		this.filterable = filterable;
	}

	public String getColumnId() {
		return columnId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getPrefColumnWidth() {
		return prefColumnWidth;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public boolean isVisibleColumn() {
		return visibleColumn;
	}

	public boolean isFilterable() {
		return filterable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnId == null) ? 0 : columnId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultTableColumn other = (DefaultTableColumn) obj;
		if (columnId == null) {
			if (other.columnId != null)
				return false;
		} else if (!columnId.equals(other.columnId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	public static <T extends DefaultTableColumn> T getTableColumnByName(String displayName,
			List<? extends DefaultTableColumn> columnList) {

		DefaultTableColumn defaultTableColumn = null;

		for (DefaultTableColumn dtc : columnList) {

			String dName = dtc.getDisplayName();

			if (dName.equals(displayName)) {
				defaultTableColumn = dtc;
				break;
			}
		}

		if (defaultTableColumn == null) {
			LOG.error("Unidentified Table Column name: " + displayName);
		}

		return (T) defaultTableColumn;
	}

	public static <T extends DefaultTableColumn> T getTableColumnById(String columnId,
			List<? extends DefaultTableColumn> columnList) {

		DefaultTableColumn defaultTableColumn = null;

		for (DefaultTableColumn dtc : columnList) {

			String cId = dtc.getColumnId();

			if (cId.equals(columnId)) {
				defaultTableColumn = dtc;
				break;
			}
		}

		if (defaultTableColumn == null) {
			LOG.error("Unidentified Table Column id: " + columnId);
		}

		return (T) defaultTableColumn;
	}
}
