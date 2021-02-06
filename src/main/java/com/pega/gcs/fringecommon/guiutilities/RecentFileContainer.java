/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;
import com.pega.gcs.fringecommon.utilities.kyro.KryoSerializer;

public class RecentFileContainer implements PropertyChangeListener {

    private static final Log4j2Helper LOG = new Log4j2Helper(RecentFileContainer.class);

    private static final String PREF_RECENT_FILES = "recent_files";

    private Class<?> preferenceClass;

    // 0 newest ... n oldest
    private ArrayList<RecentFile> recentFileList;

    private int capacity;

    private List<RecentFileContainerListener> recentFileContainerListenerList;

    public RecentFileContainer(Class<?> preferenceClass, int capacity) throws Exception {

        this.preferenceClass = preferenceClass;
        this.capacity = capacity;

        initialize();
    }

    @SuppressWarnings("unchecked")
    private void initialize() throws Exception {

        if (capacity < 0) {
            capacity = 1;
        }

        recentFileContainerListenerList = new ArrayList<RecentFileContainerListener>();

        byte[] byteArray = GeneralUtilities.getPreferenceByteArray(preferenceClass, PREF_RECENT_FILES);

        if (byteArray != null) {

            try {
                recentFileList = KryoSerializer.decompress(byteArray, ArrayList.class);

                for (RecentFile recentFile : recentFileList) {
                    recentFile.addPropertyChangeListener(this);
                }

            } catch (Exception e) {
                recentFileList = null;
                LOG.error("Error decompressing recent files list", e);
            }
        }

        if (recentFileList == null) {
            recentFileList = new ArrayList<RecentFile>();
        }

    }

    public void setSize(int size) {
        this.capacity = size;
    }

    public List<RecentFile> getRecentFileList() {
        return recentFileList;
    }

    public void addRecentFile(RecentFile recentFile) {

        if (recentFile != null) {

            int listSize = recentFileList.size();

            if (recentFileList.contains(recentFile)) {
                // just reorganise the list
                recentFileList.remove(recentFile);
                Collections.reverse(recentFileList);
                recentFileList.add(recentFile);
                Collections.reverse(recentFileList);

            } else {
                if (listSize < capacity) {
                    Collections.reverse(recentFileList);
                    recentFileList.add(recentFile);
                    Collections.reverse(recentFileList);
                } else {
                    // reached capacity, remove the oldest entry
                    Collections.reverse(recentFileList);
                    recentFileList.remove(0);
                    recentFileList.add(recentFile);
                    Collections.reverse(recentFileList);
                }

                recentFile.addPropertyChangeListener(this);
            }

            saveRecentFilesPreferrence();

            for (RecentFileContainerListener rfcl : recentFileContainerListenerList) {
                rfcl.recentFileAdded();
            }
        }

    }

    public void deleteRecentFile(RecentFile recentFile) {

        if (recentFile != null) {

            recentFileList.remove(recentFile);

            saveRecentFilesPreferrence();

            for (RecentFileContainerListener rfcl : recentFileContainerListenerList) {
                rfcl.recentFileDeleted();
            }
        }
    }

    public void addRecentFileContainerListener(RecentFileContainerListener rfcl) {
        recentFileContainerListenerList.add(rfcl);
    }

    public void deleteRecentFileContainerListener(RecentFileContainerListener rfcl) {
        recentFileContainerListenerList.remove(rfcl);
    }

    public void saveRecentFilesPreferrence() {

        try {

            // remove entries with persist=false
            List<RecentFile> entriesToPersist = new ArrayList<>(recentFileList);
            entriesToPersist.removeIf(entry -> !entry.isPersist());

            byte[] byteArray = KryoSerializer.compress(entriesToPersist);

            GeneralUtilities.setPreferenceByteArray(preferenceClass, PREF_RECENT_FILES, byteArray);
        } catch (Exception e) {
            LOG.error("Error setting recent files preferences", e);
        }
    }

    public void clearRecentFilesPreferrence() {

        recentFileList.clear();

        saveRecentFilesPreferrence();

        for (RecentFileContainerListener rfcl : recentFileContainerListenerList) {
            rfcl.recentFileDeleted();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();

        if ("attibutes".equals(propertyName)) {
            LOG.info("Updated recent file property: " + evt.getNewValue());
            saveRecentFilesPreferrence();
        }

    }

    public RecentFile getRecentFile(File selectedFile, String charset) {
        return getRecentFile(selectedFile, charset, null);
    }

    public RecentFile getRecentFile(File selectedFile, String charset, Map<String, Object> defaultAttribsIfNew) {

        return getRecentFile(selectedFile.getPath(), charset, defaultAttribsIfNew);
    }

    public RecentFile getRecentFile(String selectedFilePath, String charset, Map<String, Object> defaultAttribsIfNew) {

        return getRecentFile(selectedFilePath, charset, true, defaultAttribsIfNew);
    }

    public RecentFile getRecentFile(String selectedFilePath, String charset, boolean persist,
            Map<String, Object> defaultAttribsIfNew) {

        RecentFile recentFile = null;

        // identify the recent file
        for (RecentFile rf : getRecentFileList()) {

            String path = rf.getPath();

            if ((path != null) && (path.toLowerCase().equals(selectedFilePath.toLowerCase()))) {
                // found in recent files
                recentFile = rf;
                break;
            }
        }

        if (recentFile == null) {

            recentFile = new RecentFile(selectedFilePath, charset, persist);

            if (defaultAttribsIfNew != null) {

                for (Map.Entry<String, Object> entry : defaultAttribsIfNew.entrySet()) {
                    recentFile.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }

        File selectedFile = new File(selectedFilePath);

        if (selectedFile.exists()) {

            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                long totalSize = fis.getChannel().size();
                recentFile.setAttribute(RecentFile.KEY_SIZE, totalSize);
            } catch (Exception e) {
                LOG.error("Error getting filesize :" + selectedFilePath, e);
            }
        }

        // save and bring it to front
        addRecentFile(recentFile);

        return recentFile;
    }
}
