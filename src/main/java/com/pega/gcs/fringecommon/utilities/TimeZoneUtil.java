/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class TimeZoneUtil {

    private static final Log4j2Helper LOG = new Log4j2Helper(TimeZoneUtil.class);

    public static final Map<String, List<TimeZone>> TIMEZONE_MAP;

    public static final Map<String, List<TimeZone>> ABBREVIATED_TIMEZONE_MAP;

    static {
        // setup TIMEZONE_MAP
        Map<String, List<TimeZone>> timeZoneMap = new TreeMap<String, List<TimeZone>>();

        for (String tzID : TimeZone.getAvailableIDs()) {

            // LOG.info("TIMEZONE_MAP - tzID: " + tzID);

            TimeZone tz = TimeZone.getTimeZone(tzID);

            String abbrTimeZoneStr = tz.getDisplayName(tz.useDaylightTime(), TimeZone.SHORT);

            List<TimeZone> timeZoneList = timeZoneMap.get(abbrTimeZoneStr);

            if (timeZoneList == null) {
                timeZoneList = new ArrayList<TimeZone>();
                timeZoneMap.put(abbrTimeZoneStr, timeZoneList);
            }

            timeZoneList.add(tz);

        }

        TIMEZONE_MAP = Collections.unmodifiableMap(timeZoneMap);

        // print debug
        // LOG.info("********* TIMEZONE_MAP *********");
        // for (Map.Entry<String, List<TimeZone>> entry :
        // TIMEZONE_MAP.entrySet()) {
        //
        // String abbrTimeZoneStr = entry.getKey();
        // List<TimeZone> timeZoneList = entry.getValue();
        //
        // LOG.info(abbrTimeZoneStr);
        //
        // for (TimeZone timeZone : timeZoneList) {
        // LOG.info("\t" + timeZone);
        // }
        //
        // }

        // setup ABBREVIATED_TIMEZONE_MAP
        Map<Integer, List<TimeZone>> timeZoneOffsetMap = new TreeMap<Integer, List<TimeZone>>();

        for (String tzID : TimeZone.getAvailableIDs()) {

            TimeZone tz = TimeZone.getTimeZone(tzID);
            int offset = tz.getRawOffset();

            List<TimeZone> timeZoneList = timeZoneOffsetMap.get(offset);

            if (timeZoneList == null) {
                timeZoneList = new ArrayList<TimeZone>();
                timeZoneOffsetMap.put(offset, timeZoneList);
            }

            timeZoneList.add(tz);

            // String abbrTimeZoneStr = tz.getDisplayName(tz.useDaylightTime(),
            // TimeZone.SHORT);
            // LOG.info("timeZoneOffsetMap - abbrTimeZoneStr: " +
            // abbrTimeZoneStr + " - " + tz);

        }

        Map<String, List<TimeZone>> abbrTimeZoneMap = new TreeMap<String, List<TimeZone>>();

        for (Integer offset : timeZoneOffsetMap.keySet()) {
            List<TimeZone> tzList = timeZoneOffsetMap.get(offset);

            // split the list into 2
            // with/without DST
            List<TimeZone> tzDSTList = new ArrayList<TimeZone>();
            List<TimeZone> tzNoDSTList = new ArrayList<TimeZone>();

            for (TimeZone tz : tzList) {

                boolean useDaylightTime = tz.useDaylightTime();

                if (useDaylightTime) {
                    tzDSTList.add(tz);
                } else {
                    tzNoDSTList.add(tz);
                }

            }

            if (tzDSTList.size() > 0) {

                TimeZone tz = tzDSTList.get(0);

                String abbrTimeZoneStr = tz.getDisplayName(tz.useDaylightTime(), TimeZone.SHORT);

                if (abbrTimeZoneMap.containsKey(abbrTimeZoneStr)) {
                    LOG.info("DUPLICATE DST Key: " + abbrTimeZoneStr + " " + tz);
                } else {
                    abbrTimeZoneMap.put(abbrTimeZoneStr, tzDSTList);
                }
            }

            if (tzNoDSTList.size() > 0) {

                TimeZone tz = tzNoDSTList.get(0);

                String abbrTimeZoneStr = tz.getDisplayName(tz.useDaylightTime(), TimeZone.SHORT);

                if (abbrTimeZoneMap.containsKey(abbrTimeZoneStr)) {
                    LOG.info("DUPLICATE NO DST Key: " + abbrTimeZoneStr + " " + tz);
                } else {
                    abbrTimeZoneMap.put(abbrTimeZoneStr, tzNoDSTList);
                }
            }

        }

        // LOG.info(abbrTimeZoneMap);
        ABBREVIATED_TIMEZONE_MAP = Collections.unmodifiableMap(abbrTimeZoneMap);

        // print debug
        // LOG.info("********* ABBREVIATED_TIMEZONE_MAP *********");
        // for (Map.Entry<String, List<TimeZone>> entry :
        // ABBREVIATED_TIMEZONE_MAP.entrySet()) {
        //
        // String abbrTimeZoneStr = entry.getKey();
        // List<TimeZone> timeZoneList = entry.getValue();
        //
        // LOG.info(abbrTimeZoneStr);
        //
        // for (TimeZone timeZone : timeZoneList) {
        // LOG.info("\t" + timeZone);
        // }
        //
        // }
    }

    public static TimeZone getTimeZoneFromAbbreviatedString(String abbreviatedTzStr) {

        TimeZone timeZone = null;

        if (abbreviatedTzStr.startsWith("GMT")) {
            timeZone = TimeZone.getTimeZone(abbreviatedTzStr);
        } else {
            List<TimeZone> timeZoneList = ABBREVIATED_TIMEZONE_MAP.get(abbreviatedTzStr);

            if (timeZoneList != null) {
                timeZone = timeZoneList.get(0);
            } else {
                // try in full map
                timeZoneList = TIMEZONE_MAP.get(abbreviatedTzStr);

                if (timeZoneList != null) {
                    timeZone = timeZoneList.get(0);
                }
            }
        }
        return timeZone;
    }

    public static void main(String[] args) {

        LOG.info(TimeZone.getTimeZone("Europe/Madrid").getDisplayName(true, TimeZone.SHORT));

        TimeZone tz = TimeZoneUtil.getTimeZoneFromAbbreviatedString("GMT");
        LOG.info(tz);
        LOG.info(tz.getDisplayName(true, TimeZone.SHORT));

    }
}
