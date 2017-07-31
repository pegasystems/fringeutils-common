/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.markerbar;

import java.io.Serializable;

public class Marker<T> implements Serializable {

	private static final long serialVersionUID = 6455371017273413320L;

	private T key;

	private String text;

	protected Marker() {
		// for kryo
	}

	public Marker(T key, String text) {
		super();
		this.key = key;
		this.text = text;
	}

	public T getKey() {
		return key;
	}

	public String getText() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		Marker<?> other = (Marker<?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return key + "-" + text;
	}

}
