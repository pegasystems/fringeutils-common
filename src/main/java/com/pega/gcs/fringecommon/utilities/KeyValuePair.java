/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.util.Objects;

public class KeyValuePair<K extends Comparable<K>, T extends Comparable<T>> implements Comparable<KeyValuePair<K, T>> {

    private K key;

    private T value;

    public KeyValuePair(K key, T value) {
        super();
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof KeyValuePair)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        KeyValuePair<K, T> other = (KeyValuePair<K, T>) obj;

        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "KeyValuePair [key=" + key + ", value=" + value + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(KeyValuePair<K, T> other) {
        K thisKey = getKey();
        K otherKey = other.getKey();

        int compare = thisKey.compareTo(otherKey);

        if (compare == 0) {

            T thisValue = getValue();
            T otherValue = other.getValue();

            compare = thisValue.compareTo(otherValue);
        }

        return compare;
    }

}
