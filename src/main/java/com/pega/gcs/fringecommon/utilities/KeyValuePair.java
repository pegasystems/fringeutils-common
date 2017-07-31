/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
/**
 * 
 */
package com.pega.gcs.fringecommon.utilities;

public class KeyValuePair<K extends Comparable<K>, T extends Comparable<T>> implements Comparable<KeyValuePair<K, T>> {

	private K key;

	private T value;

	public KeyValuePair(K key, T value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(KeyValuePair<K, T> o) {
		K thisKey = getKey();
		K otherKey = o.getKey();

		T thisValue = getValue();
		T otherValue = o.getValue();

		return thisKey.equals(otherKey) ? thisValue.compareTo(otherValue) : thisKey.compareTo(otherKey);
	}

}
