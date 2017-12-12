/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RecentFile implements Serializable {

	private static final long serialVersionUID = 8845107200663083072L;

	public static String KEY_FILE = "file";

	public static String KEY_SHA256 = "sha256";

	public static String KEY_CHARSET = "charset";

	public static String KEY_LOGFILETYPE = "logFileType";

	public static String KEY_TIMEZONE = "timezone";

	public static String KEY_LOCALE = "locale";

	public static String KEY_BOOKMARK = "bookmark";

	public static String KEY_SIZE = "size";

	public Map<String, Object> attibutes;

	private transient PropertyChangeSupport propertyChangeSupport;

	@SuppressWarnings("unused")
	private RecentFile() {
		// constructor for kryo purpose
	}

	public RecentFile(String file, String charset) {

		attibutes = new HashMap<String, Object>();

		attibutes.put(KEY_FILE, file);
		attibutes.put(KEY_CHARSET, charset);

		propertyChangeSupport = getPropertyChangeSupport();
	}

	public Object getAttribute(String key) {
		return attibutes.get(key);
	}

	public void setAttribute(String key, Object value) {
		attibutes.put(key, value);

		PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
		propertyChangeSupport.firePropertyChange("attibutes", null, key + " [" + value + "]");

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		String file = (String) getAttribute(KEY_FILE);

		result = prime * result + ((file == null) ? 0 : file.hashCode());
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
		RecentFile other = (RecentFile) obj;

		String file = (String) getAttribute(KEY_FILE);
		String otherfile = (String) other.getAttribute(KEY_FILE);

		if (file == null) {
			if (otherfile != null) {
				return false;
			}
		} else if (!file.toLowerCase().equals(otherfile.toLowerCase())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		String file = (String) getAttribute(KEY_FILE);
		sb.append(file);

		// String charset = (String) getAttribute(KEY_CHARSET);
		//
		// if ((charset != null) && (!"".equals(charset))) {
		// sb.append(" [");
		// sb.append(charset);
		// sb.append("]");
		// }
		//
		// Long size = (Long) getAttribute(KEY_SIZE);
		//
		// if (size != null) {
		//
		// sb.append(" [");
		// sb.append(GeneralUtilities.humanReadableSize(size.longValue()));
		// sb.append("]");
		// }

		return sb.toString();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {

		PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	private PropertyChangeSupport getPropertyChangeSupport() {

		if (propertyChangeSupport == null) {
			propertyChangeSupport = new PropertyChangeSupport(this);
		}

		return propertyChangeSupport;
	}
}
