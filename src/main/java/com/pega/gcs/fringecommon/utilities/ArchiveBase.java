/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.io.File;
import java.util.List;

public abstract class ArchiveBase {

    public static final String ZIP = "zip";
    public static final String RAR = "rar";

    private File archiveFile;

    public ArchiveBase(File archiveFile) {
        super();
        this.archiveFile = archiveFile;
    }

    public File getArchiveFile() {
        return archiveFile;
    }

    public void setArchiveFile(File archiveFile) {
        this.archiveFile = archiveFile;
    }

    public abstract List<? extends Object> getArchiveEntries();

}
