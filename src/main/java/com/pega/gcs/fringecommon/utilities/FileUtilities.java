/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class FileUtilities {

    private static final Log4j2Helper LOG = new Log4j2Helper(FileUtilities.class);

    /**
     * Get the parent hierarchy of file. uses 1 based indexing.
     * 
     * @param file         - leaf file name as start
     * @param fileLevelMap - output map, index 1 is leaf file name
     * @param lvl          - recursive levels
     */
    public static void getFileLevels(File file, HashMap<Integer, File> fileLevelMap, int lvl) {

        int localLvl = lvl;
        fileLevelMap.put(localLvl, file);

        File parent = file.getParentFile();

        if (parent != null) {
            getFileLevels(parent, fileLevelMap, ++localLvl);
        }
    }

    public static ImageIcon getImageIcon(Class<?> clazz, String iconName) {

        String iconPath = "/images/" + iconName;

        URL resourceURL = clazz.getResource(iconPath);

        if (resourceURL == null) {
            LOG.info("error loading iconName " + iconName + " " + resourceURL);
            return null;
        }

        ImageIcon icon = new ImageIcon(resourceURL);

        return icon;
    }

    public static StyleSheet getStyleSheet(Class<?> clazz, String cssName) {

        StyleSheet css = null;
        String csspath = "/styles/" + cssName;

        try (InputStream is = clazz.getResourceAsStream(csspath);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"))) {

            css = new StyleSheet();
            css.loadRules(bufferedReader, null);
            bufferedReader.close();

        } catch (Throwable e) {
            LOG.info("error loading stylesheet " + csspath);
        }

        return css;
    }

    @SuppressWarnings("unused")
    private static byte[] compressObject(Object object) throws Exception {

        byte[] byteArray = null;

        if (object != null) {
            // serialize to a byte array
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(baos);
                    GZIPOutputStream zipped = new GZIPOutputStream(bos) {
                        {
                            def.setLevel(Deflater.BEST_SPEED);
                        }
                    };
                    ObjectOutputStream out = new ObjectOutputStream(zipped)) {

                out.writeObject(object);
                out.close();

                byteArray = baos.toByteArray();

            } catch (Exception e) {
                LOG.error("Error compressing object", e);
                throw e;
            }

        } else {
            LOG.info("compressObject is null");
        }

        // LOG.info("Compressed to: " + byteArray.length);
        return byteArray;
    }

    /**
     * Utility to check the user.dir for any overridden resources
     * 
     * @param clazz        class for loading resource
     * @param resourceName name should start with '\'.
     * @return {@link InputStream} of the resource or null
     */
    public static InputStream getResourceAsStreamFromUserDir(Class<?> clazz, String resourceName) {

        InputStream inputStream = null;

        String pwd = System.getProperty("user.dir");

        File file = new File(pwd, resourceName);

        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                LOG.error("File not found: " + file, e);
            }
        } else {
            inputStream = clazz.getResourceAsStream(resourceName);
        }

        return inputStream;
    }

    /**
     * Utility to Zip a file.
     * 
     * @param sourceFile source file to Zip
     * @param targetFile target zip file
     * @return whether zip was successful
     * @throws IOException - error
     */
    public static boolean zipFile(final File sourceFile, final File targetFile) throws IOException {

        LOG.info("Start - zipFile - sourceFile: " + sourceFile + " targetFile: " + targetFile);

        boolean success = false;

        if (sourceFile.exists()) {

            try (FileInputStream sourceFis = new FileInputStream(sourceFile);
                    ZipOutputStream targetZos = new ZipOutputStream(new FileOutputStream(targetFile))) {

                ZipEntry zipEntry = new ZipEntry(sourceFile.getName());

                targetZos.putNextEntry(zipEntry);

                byte[] byteBuffer = new byte[(int) FileUtils.ONE_MB];
                int readLen = -1;

                while ((readLen = sourceFis.read(byteBuffer)) != -1) {
                    targetZos.write(byteBuffer, 0, readLen);
                }
            }

            success = true;

        } else {
            success = false;
        }

        LOG.info("End - zipFile success:" + success);

        return success;
    }

    public static String getFilePath(File file) {

        String filePath;

        try {
            filePath = file.getCanonicalPath();
        } catch (IOException ioe) {
            filePath = file.getAbsolutePath();
            LOG.error("Exception getting canonical path:" + file + " : " + ioe.getMessage());
        }

        return filePath;
    }

    public static String getFileBaseName(File file) {

        String filePath = getFilePath(file);

        String fileBaseName = FilenameUtils.getBaseName(filePath);

        return fileBaseName;
    }

    public static String getFileExtension(File file) {

        String filePath = getFilePath(file);

        String fileExtension = FilenameUtils.getExtension(filePath);

        return fileExtension;
    }
}
