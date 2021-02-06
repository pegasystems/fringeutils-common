/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities.markerbar;

import java.io.Serializable;
import java.util.Objects;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Marker)) {
            return false;
        }

        Marker<?> other = (Marker<?>) obj;

        return Objects.equals(key, other.key);
    }

    @Override
    public String toString() {
        return key + "-" + text;
    }

}
