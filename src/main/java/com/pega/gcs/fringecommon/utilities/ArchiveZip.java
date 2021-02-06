/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ArchiveZip extends ArchiveBase {

    ZipFile zipFile;

    public ArchiveZip(File archiveFile) throws ZipException, IOException {
        super(archiveFile);

        zipFile = new ZipFile(archiveFile);
    }

    @Override
    public List<ZipEntry> getArchiveEntries() {

        List<ZipEntry> zipEntryList = new ArrayList<ZipEntry>();

        Enumeration<?> zipFileEnum = zipFile.entries();

        while (zipFileEnum.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) zipFileEnum.nextElement();

            zipEntryList.add(zipEntry);

        }

        return zipEntryList;
    }

    public byte[] getData(ZipEntry zipEntry) {

        byte[] data = null;

        return data;
    }
}
