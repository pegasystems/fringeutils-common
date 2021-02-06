/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.pega.gcs.fringecommon.guiutilities.GUIUtilities;
import com.pega.gcs.fringecommon.guiutilities.ModalProgressMonitor;
import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class GeneralUtilities {

    private static final Log4j2Helper LOG = new Log4j2Helper(GeneralUtilities.class);

    private static final String GOTO_URL = "http://goto.pega.com";

    // ( hfix | se | bug | sr | us | hotfix | task | issue)
    private static Pattern PEGA_WORK_ID_PATTERN;
    /*
     * = Pattern.compile( "(hfix[-\\s]?\\d+)|(se[-\\s]?\\d+)|(bug-\\d+)|(sr-[a-z]?\\d+)|((us-\\d+[-]?)\\d+)|(hotfix[-\\s]?\\d+)",
     * Pattern.CASE_INSENSITIVE)
     */

    // @formatter:off
    private static final String SECRET = "[Q]And so, to our first contestant. Good evening! Your name, please!"
            + "[A]Good evening!"
            + "[Q]Your chosen subject was answering questions before you are asked. This time, you've chosen to answer the question"
            + " before last, each time, is that correct?"
            + "[A]Charlie Smithers."
            + "[Q]And your time starts now. What is palaeontology?"
            + "[A]Yes, absolutely correct."
            + "[Q]What is the name of the directory that lists members of the peerage?"
            + "[A]A study of old fossils."
            + "[Q]Correct. Who are Len Murray and Sir Geoffrey Howe?"
            + "[A]Burkes."
            + "[Q]Correct. What is the difference between a donkey and an ass?"
            + "[A]One is a Trade Union leader, the other one is a member of the cabinet."
            + "[Q]Correct. Complete the quotation \"To be or not to be...\""
            + "[A]They are both the same."
            + "[Q]Correct. What is Bernard Manning famous for?"
            + "[A]That is the question."
            + "[Q]Correct. Who is the current Archbishop of Canterbury?"
            + "[A]He is a fat man who tells blue jokes."
            + "[Q]Correct. What do people kneel on in church?"
            + "[A]The Right Reverend Robert Runcey"
            + "[Q]Correct. What do tarantulas pray on?"
            + "[A]Cassocks"
            + "[Q]Correct. What would you use a rip cord to pull open?"
            + "[A]Large flies."
            + "[Q]Correct. What sort of person lives in Bedlam?"
            + "[A]A parachute"
            + "[Q]Correct. What is a jockstrap?"
            + "[A]A nut case."
            + "[Q]For what purpose would a decorator use ethylene chlorides?"
            + "[A]A form of athletic support."
            + "[Q]What did Henri de Toulouse-Lautrec do?"
            + "[A]Paint strippers."
            + "[Q]Who is Dean Martin?"
            + "[A]He is a kind of artist."
            + "[Q]Yes, what sort of artist?"
            + "[A]Err... Pass."
            + "[Q]That's near enough."
            + "[Q]What make of vehicle is the standard London bus"
            + "[A]A singer."
            + "[Q]In 1892 Brandon Thomas wrote a famous long running British farce, what was it?"
            + "[A]British Leyland."
            + "[Q]Correct. Complete the following quota...(a beep is heard)...I started, so I finish! Complete the following"
            + " quotation about Mrs Thatcher: \"Her heart may be in the right place but her ..\""
            + "[A]Charlies aren't."
            + "[Q]Correct!";
    // @formatter:on
    private static final String ENCRYPT_SUFFIX = "#^?";

    private static int getOffset(byte salt) {
        return salt & 0xFF;
    }

    private static String encrypt(String input) throws UnsupportedEncodingException {

        String encoding = "UTF8";

        byte[] bytes = input.getBytes(encoding);
        byte salt = (byte) (Math.random() * 0xFF);
        int offset = getOffset(salt);
        byte[] out = new byte[bytes.length + 1];
        out[0] = salt;

        byte[] mask = SECRET.getBytes(encoding);

        for (int i = 0; i < bytes.length; i++) {
            out[i + 1] = (byte) (bytes[i] ^ mask[(i + offset) % mask.length]);
        }

        String encrypt = Base64.encode(out);

        return encrypt;
    }

    private static String decrypt(String input) throws UnsupportedEncodingException {

        String encoding = "UTF8";

        byte[] bytes = Base64.decode(input);
        byte salt = bytes[0];
        int offset = getOffset(salt);
        byte[] out = new byte[bytes.length - 1];

        byte[] mask = SECRET.getBytes(encoding);

        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) (bytes[i + 1] ^ mask[(i + offset) % mask.length]);
        }

        String decrypt = new String(out, encoding);

        return decrypt;
    }

    public static String encryptText(String text) throws UnsupportedEncodingException {

        String encryptText = text;

        if ((text != null) && (!"".equals(text))) {
            encryptText = encrypt(text) + ENCRYPT_SUFFIX;
        }

        return encryptText;
    }

    public static String decryptText(String text) throws UnsupportedEncodingException {

        String decryptText = text;

        if (isTextEncrypted(decryptText)) {
            decryptText = text.substring(0, (text.length() - ENCRYPT_SUFFIX.length()));
            decryptText = decrypt(decryptText);
        }

        return decryptText;
    }

    public static boolean isTextEncrypted(String text) {
        boolean textEncrypted = false;

        if ((text != null) && (!"".equals(text)) && (text.endsWith(ENCRYPT_SUFFIX))) {
            textEncrypted = true;
        }

        return textEncrypted;
    }

    public static String getConsoleInput(String promptStr, String defaultValue, Charset charset) throws IOException {

        InputStreamReader inputStreamReader = new InputStreamReader(System.in, charset);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        LOG.info(promptStr + "[" + defaultValue + "]: ");

        System.out.flush();

        String value = bufferedReader.readLine();

        if ((value == null) || (Collator.getInstance().compare(value, "") == 0)) {
            value = defaultValue;
        } else {
            value = value.trim();
        }

        return value;
    }

    // fill the array in an expanding binary fashion
    public static void bytefill(byte[] array, byte value) {
        int len = array.length;
        if (len > 0) {
            array[0] = value;
        }

        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }

    public static boolean any(boolean[] booleanArray) {

        boolean found = false;

        if (booleanArray != null) {

            for (int i = 0; i < booleanArray.length; i++) {

                found = booleanArray[i];

                if (found) {
                    break;
                }
            }
        }

        return found;
    }

    public static String getPreferenceString(Class<?> clazz, String prefName) {

        String prefValue = "";

        Preferences preferences = Preferences.userNodeForPackage(clazz);

        prefValue = preferences.get(prefName, prefValue);

        return prefValue;
    }

    public static void setPreferenceString(Class<?> clazz, String prefName, String prefValue) {

        Preferences preferences = Preferences.userNodeForPackage(clazz);

        preferences.put(prefName, prefValue);
    }

    public static byte[] getPreferenceByteArray(Class<?> clazz, String prefName) throws BackingStoreException {

        byte[] prefValue = null;
        Preferences preferences = Preferences.userNodeForPackage(clazz);

        prefValue = preferences.getByteArray(prefName, prefValue);

        if (prefValue == null) {
            // try new method
            Preferences node = preferences.node(prefName);

            String[] keys = node.keys();

            int numPieces = keys.length;

            byte[][] byteArrayPieces = new byte[numPieces][];

            for (int i = 0; i < numPieces; ++i) {
                String key = keys[i];
                byteArrayPieces[i] = node.getByteArray(key, null);
            }

            prefValue = joinByteArray(byteArrayPieces);

        }

        return prefValue;
    }

    public static void setPreferenceByteArray(Class<?> clazz, String prefName, byte[] prefValue)
            throws BackingStoreException {

        byte[][] byteArrayPieces = splitByteArray(prefValue);

        Preferences preferences = Preferences.userNodeForPackage(clazz);

        // remove old style preference storage
        preferences.remove(prefName);

        // start new style storage
        Preferences node = preferences.node(prefName);
        node.clear();

        for (int i = 0; i < byteArrayPieces.length; ++i) {

            String key = String.valueOf(i);

            node.putByteArray(key, byteArrayPieces[i]);
        }

        // preferences.putByteArray(prefName, prefValue);
    }

    public static String byteArrayToHexString(byte[] byteArray) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {

            String str = Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1);
            result.append(str);
        }

        return result.toString();
    }

    // commenting out - use getHostAddress to get IPv4 address
    // public static String getHostName() {
    // String hostName = "";
    //
    // try {
    // hostName = InetAddress.getLocalHost().getHostName();
    // } catch (Exception e) {
    // LOG.error("Error getting hostname", e);
    //
    // if (System.getProperty("os.name").startsWith("Windows")) {
    // hostName = System.getenv("COMPUTERNAME");
    // } else {
    // hostName = System.getenv("HOSTNAME");
    // }
    // }
    //
    // return hostName;
    // }

    private static byte[][] splitByteArray(byte[] byteArray) {

        int indCapacity = 6144;
        int numPieces = (byteArray.length + indCapacity - 1) / indCapacity;

        byte[][] byteArrayPieces = new byte[numPieces][];

        for (int i = 0; i < numPieces; ++i) {

            int startByte = i * indCapacity;

            int endByte = startByte + indCapacity;

            if (endByte > byteArray.length) {
                endByte = byteArray.length;
            }

            int length = endByte - startByte;

            byteArrayPieces[i] = new byte[length];

            System.arraycopy(byteArray, startByte, byteArrayPieces[i], 0, length);
        }

        return byteArrayPieces;
    }

    private static byte[] joinByteArray(byte[][] byteArrayPieces) {

        int length = 0;

        for (int i = 0; i < byteArrayPieces.length; ++i) {
            length += byteArrayPieces[i].length;
        }

        byte[] byteArray = new byte[length];

        int cursor = 0;

        for (int i = 0; i < byteArrayPieces.length; ++i) {
            System.arraycopy(byteArrayPieces[i], 0, byteArray, cursor, byteArrayPieces[i].length);
            cursor += byteArrayPieces[i].length;
        }

        return byteArray;
    }

    public static String humanReadableSize(long bytes, boolean metric) {

        String byteCountStr = "";

        int unit = 0;
        int exp = 0;

        try {
            unit = (metric) ? 1000 : 1024;

            if (bytes < unit) {
                byteCountStr = bytes + " B";
            } else {
                exp = (int) (Math.log(bytes) / Math.log(unit));
                char pre = ("KMGTPE").charAt(exp - 1);
                byteCountStr = String.format("%.1f %sB", (bytes / Math.pow(unit, exp)), pre);
            }
        } catch (Exception e) {
            LOG.error("bytes[" + bytes + "] unit[" + unit + "] exp[" + exp + "]", e);
        }

        return byteCountStr;
    }

    public static String humanReadableTime(long duration) {

        StringBuilder timeStrSB = new StringBuilder();

        try {
            long balMillis = duration % 1000;
            long days = TimeUnit.MILLISECONDS.toDays(duration);
            long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(days);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);

            if (days > 0) {
                timeStrSB.append(days);
                timeStrSB.append(" days ");
            }
            if (hours > 0) {
                timeStrSB.append(hours);
                timeStrSB.append(" hours ");
            }
            if (minutes > 0) {
                timeStrSB.append(minutes);
                timeStrSB.append(" minutes ");
            }
            if (seconds > 0) {
                timeStrSB.append(seconds);
                timeStrSB.append(" seconds ");
            }

            if (balMillis > 0) {
                timeStrSB.append(balMillis);
                timeStrSB.append(" ms");
            }
        } catch (Exception e) {
            LOG.error("duration [" + duration + "] error", e);
        }

        return timeStrSB.toString().trim();
    }

    public static <K, T> List<Map.Entry<K, T>> denormalizeNestedListMap(Map<K, ? extends Collection<T>> nestedListMap) {

        List<Map.Entry<K, T>> denormalizedEntryList = new ArrayList<Map.Entry<K, T>>();

        for (Map.Entry<K, ? extends Collection<T>> entry : nestedListMap.entrySet()) {

            for (T t : entry.getValue()) {
                denormalizedEntryList.add(new AbstractMap.SimpleEntry<K, T>(entry.getKey(), t));
            }
        }

        return denormalizedEntryList;
    }

    public static <K extends Comparable<K>, T extends Comparable<T>> Set<KeyValuePair<K, T>> denormalizeNestedSetMap(
            Map<K, ? extends Collection<T>> nestedListMap, Comparator<KeyValuePair<K, T>> comparator) {

        Set<KeyValuePair<K, T>> denormalizedEntrySet = null;

        if (comparator != null) {
            denormalizedEntrySet = new TreeSet<KeyValuePair<K, T>>(comparator);
        } else {
            denormalizedEntrySet = new TreeSet<KeyValuePair<K, T>>();
        }

        for (Map.Entry<K, ? extends Collection<T>> entry : nestedListMap.entrySet()) {

            for (T t : entry.getValue()) {
                denormalizedEntrySet.add(new KeyValuePair<K, T>(entry.getKey(), t));
            }
        }

        return denormalizedEntrySet;
    }

    public static String getHostAddress() {

        String hostAddress = null;

        try {
            List<Inet4Address> extAddressList = new ArrayList<>();
            List<Inet4Address> intAddressList = new ArrayList<>();

            Enumeration<NetworkInterface> netifs = NetworkInterface.getNetworkInterfaces();

            while (netifs.hasMoreElements()) {

                NetworkInterface networkInterface = netifs.nextElement();

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {

                    InetAddress inetAddress = inetAddresses.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {

                        byte[] address = inetAddress.getAddress();

                        if (((address[0] & 0xff) != 192) && ((address[0] & 0xff) != 168)) {
                            extAddressList.add((Inet4Address) inetAddress);
                        } else {
                            intAddressList.add((Inet4Address) inetAddress);
                        }
                    }
                }
            }

            // get with following preference
            // 1. anything other than 192.*/168.*
            // 2. all 192.*/168.*
            // 3. localhost
            if (extAddressList.size() > 0) {
                hostAddress = extAddressList.get(0).getHostAddress();
            } else if (intAddressList.size() > 0) {
                hostAddress = intAddressList.get(0).getHostAddress();
            } else {
                hostAddress = InetAddress.getLocalHost().getHostName();
            }

        } catch (Exception e) {
            LOG.error("Error getting hostaddress", e);
        }

        return hostAddress;
    }

    public static String[] getAllHostAddresses() {

        Set<String> hostAddressSet = new TreeSet<>();

        try {

            Enumeration<NetworkInterface> netifs = NetworkInterface.getNetworkInterfaces();

            while (netifs.hasMoreElements()) {

                NetworkInterface networkInterface = netifs.nextElement();

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {

                    InetAddress inetAddress = inetAddresses.nextElement();

                    if (inetAddress instanceof Inet4Address) {
                        hostAddressSet.add(inetAddress.getHostAddress());
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("Error getting all host addresses", e);
        }

        return hostAddressSet.toArray(new String[hostAddressSet.size()]);
    }

    /**
     * Convert a Collection into a string of seperated values.
     * 
     * @param objectList - Collection to be converted as string
     * @param seperator  - If null, defaults to ','
     * @param escapeCSV  - make converted string csv compatible.
     * @return - String containing collection entries as separated values
     */
    public static String getCollectionAsSeperatedValues(Collection<?> objectList, String seperator, boolean escapeCSV) {

        StringBuilder sb = new StringBuilder();

        if (objectList != null) {

            if ((seperator == null) || ("".equals(seperator))) {
                seperator = ",";
            }

            for (Object object : objectList) {

                if (sb.length() > 0) {
                    sb.append(seperator);
                }

                if (object != null) {

                    String objStr = object.toString();

                    if (escapeCSV) {
                        objStr = StringEscapeUtils.escapeCsv(objStr);
                    }
                    sb.append(objStr);
                }
            }
        }

        return sb.toString();
    }

    public static String getElementsAsXML(List<Element> elementList) throws IOException {

        StringBuilder xmlStrSB = new StringBuilder();

        for (Element element : elementList) {

            String elemStr = getElementAsXML(element);
            xmlStrSB.append(elemStr);
        }

        return xmlStrSB.toString();

    }

    public static String getElementAsXML(Element element) throws IOException {

        StringBuilder xmlStrSB = new StringBuilder();

        StringWriter sw = new StringWriter();
        OutputFormat format = new OutputFormat();
        format.setIndentSize(2);
        format.setNewlines(true);
        // format.setTrimText(true);
        format.setPadText(true);

        XMLWriter writer = new XMLWriter(sw, format);

        writer.write(element);
        writer.flush();

        xmlStrSB.append(sw.toString());

        return xmlStrSB.toString();
    }

    /**
     * Trimming special characters from beginning and end. however keeping space intact
     * 
     * @param inputString - Input String
     * @return trimmed string
     */
    public static String specialTrim(String inputString) {

        int origLength = inputString.length();
        int length = origLength;
        int startIndex = 0;

        while ((startIndex < length) && (inputString.charAt(startIndex) < ' ')) {
            startIndex++;
        }

        while ((startIndex < length) && (inputString.charAt(length - 1) < ' ')) {
            length--;
        }

        String outputString = ((startIndex > 0) || (length < origLength)) ? inputString.substring(startIndex, length)
                : inputString;

        return outputString;
    }

    public static Pattern getPegaWorkIdPattern() {

        if (PEGA_WORK_ID_PATTERN == null) {

            StringBuilder patternStringBuilder = new StringBuilder();

            // HFIX
            patternStringBuilder.append("(hfix[-\\s]?\\d+)");
            patternStringBuilder.append("|");
            // SE
            patternStringBuilder.append("(se[-\\s]?\\d+)");
            patternStringBuilder.append("|");
            // BUG
            patternStringBuilder.append("(bug-\\d+)");
            patternStringBuilder.append("|");
            // SR
            patternStringBuilder.append("(sr-[a-z]?\\d+)");
            patternStringBuilder.append("|");
            // US
            patternStringBuilder.append("((us-\\d+[-]?)\\d+)");
            patternStringBuilder.append("|");
            // HOTFIX
            patternStringBuilder.append("(hotfix[-\\s]?\\d+)");
            patternStringBuilder.append("|");
            // TASK
            patternStringBuilder.append("(task-\\d+)");
            patternStringBuilder.append("|");
            // ISSUE
            patternStringBuilder.append("(issue-\\d+)");
            patternStringBuilder.append("|");
            // EPIC
            patternStringBuilder.append("(epic-\\d+)");

            String patternString = patternStringBuilder.toString();

            PEGA_WORK_ID_PATTERN = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        }

        return PEGA_WORK_ID_PATTERN;
    }

    public static String getWorkIdHyperlinkText(String workId) {

        StringBuilder sb = new StringBuilder();

        sb.append("<html><head/><body><div>");

        String gotoWorkId = workId.toUpperCase().replaceAll("HFIX[-\\s]?", "SE-");

        StringBuilder idURL = new StringBuilder();
        idURL.append(GOTO_URL);
        idURL.append("/");
        idURL.append(gotoWorkId);

        String href = GUIUtilities.getHref(idURL.toString(), workId);
        sb.append(href);

        sb.append("</div></body></html>");

        String hotfixIDHyperlinkText = sb.toString();

        return hotfixIDHyperlinkText;
    }

    public static String getWorkDescHyperlinkText(String workDesc) {

        StringBuilder sb = new StringBuilder();
        int beginIndex = 0;
        int endIndex = 0;

        sb.append("<html><head/><body><div>");

        Pattern workIdPattern = GeneralUtilities.getPegaWorkIdPattern();

        Matcher matcher = workIdPattern.matcher(workDesc);

        while (matcher.find()) {

            String someId = matcher.group();
            endIndex = matcher.start();

            sb.append(workDesc.substring(beginIndex, endIndex));

            StringBuilder idURL = new StringBuilder();

            String idText = someId.trim().toUpperCase();

            if (idText.startsWith("HFIX")) {
                idText = idText.replaceAll("HFIX[-\\s]?", "SE-");
            }

            idURL.append(GOTO_URL);
            idURL.append("/");
            idURL.append(idText);

            String href = GUIUtilities.getHref(idURL.toString(), someId);

            sb.append(href);

            beginIndex = matcher.end();
        }

        endIndex = workDesc.length();

        sb.append(workDesc.substring(beginIndex, endIndex));
        sb.append("</div></body></html>");

        String workDescHyperlinkText = sb.toString();

        return workDescHyperlinkText;
    }

    /**
     * Get map structure from a csv.
     * 
     * @param reader - Reader object
     * @param progressMonitor - ModalProgressMonitor
     * @return String - data Map from csv. key of -1 is header/column names list
     * @throws Exception  - error
     */
    public static Map<Integer, List<String>> getDataMapFromCSV(Reader reader, ModalProgressMonitor progressMonitor)
            throws Exception {

        Map<Integer, List<String>> dataMap = new HashMap<>();

        CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);

        Iterator<CSVRecord> csvRecordIt = csvParser.iterator();

        boolean headerRecord = true;

        AtomicInteger indexAi = new AtomicInteger(0);

        while (csvRecordIt.hasNext()) {

            CSVRecord csvRecord = csvRecordIt.next();

            List<String> recordDataList = getRecordDataList(csvRecord);

            if (headerRecord) {
                headerRecord = false;
                dataMap.put(-1, recordDataList);

            } else {
                dataMap.put(indexAi.incrementAndGet(), recordDataList);
            }

            if ((progressMonitor != null) && (progressMonitor.isCanceled())) {
                break;
            }
        }

        return dataMap;
    }

    private static List<String> getRecordDataList(CSVRecord csvRecord) {

        List<String> recordDataList = new ArrayList<>();

        Iterator<String> colIt = csvRecord.iterator();

        while (colIt.hasNext()) {

            String col = colIt.next();

            recordDataList.add(col);
        }

        return recordDataList;
    }

    public static void main(String[] args) {
        System.out.println(humanReadableTime(1467032622000L));
        System.out.println(getHostAddress());

        for (String hostAddress : getAllHostAddresses()) {
            System.out.println("\t" + hostAddress);
        }
    }
}
