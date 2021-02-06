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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RecentFile implements Serializable {

    private static final long serialVersionUID = 2000931902948933150L;

    public static String KEY_SHA = "sha";

    public static String KEY_CHARSET = "charset";

    public static String KEY_LOGFILETYPE = "logFileType";

    public static String KEY_TIMEZONE = "timezone";

    public static String KEY_LOCALE = "locale";

    public static String KEY_BOOKMARK = "bookmark";

    public static String KEY_SIZE = "size";

    public static String KEY_SEARCH_KEYS = "searchKeys";

    public static String REMOTE_ADDRESS = "remoteAddress";

    private String path;

    private Map<String, Object> attibutes;

    private transient PropertyChangeSupport propertyChangeSupport;

    // for ex for Socket receiver, same ports could be used for different log types.
    private boolean persist;

    @SuppressWarnings("unused")
    private RecentFile() {
        // constructor for kryo purpose
    }

    public RecentFile(String path, String charset, boolean persist) {
        super();
        this.path = path;
        this.persist = persist;

        attibutes = new HashMap<String, Object>();

        attibutes.put(KEY_CHARSET, charset);

        propertyChangeSupport = getPropertyChangeSupport();
    }

    public String getPath() {
        return path;
    }

    public boolean isPersist() {
        return persist;
    }

    public Object getAttribute(String key) {
        return attibutes.get(key);
    }

    public void setAttribute(String key, Object value) {
        attibutes.put(key, value);

        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        propertyChangeSupport.firePropertyChange("attibutes", null, key + " [" + value + "]");

    }

    public Charset getCharset() {

        Charset charset = null;
        String charsetName = (String) getAttribute(RecentFile.KEY_CHARSET);

        if ((charsetName == null) || ("".equals(charsetName))) {
            charset = Charset.defaultCharset();
        } else {
            charset = Charset.forName(charsetName);
        }

        return charset;
    }

    public Locale getLocale() {

        Locale locale = (Locale) getAttribute(RecentFile.KEY_LOCALE);

        if (locale == null) {
            locale = Locale.getDefault();
        }

        return locale;
    }

    public Long getFileSize() {

        Long fileSize = (Long) getAttribute(RecentFile.KEY_SIZE);

        return fileSize;
    }

    public String getFileSHA() {

        String fileSHA = (String) getAttribute(RecentFile.KEY_SHA);

        return fileSHA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(path);
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

        if (!(obj instanceof RecentFile)) {
            return false;
        }

        RecentFile other = (RecentFile) obj;

        return Objects.equals(path.toLowerCase(), other.path.toLowerCase());
    }

    @Override
    public String toString() {
        return path;
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
