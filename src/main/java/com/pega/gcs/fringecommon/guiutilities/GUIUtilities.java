/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TimeZone;

import javax.swing.JViewport;

public class GUIUtilities {

	public static void setEnabledComponent(Container container, boolean enable, List<Component> excludeList) {

		if (container != null) {

			for (Component component : container.getComponents()) {

				if ((excludeList == null) || (!excludeList.contains(component))) {
					component.setEnabled(enable);

					if (component instanceof Container) {
						setEnabledComponent((Container) component, enable, excludeList);
					}
				}
			}
		}
	}

	public static AutoCompleteJComboBox<String> getCharsetJComboBox() {

		SortedMap<String, Charset> charsetMap = Charset.availableCharsets();
		List<String> charsetKeyList = new ArrayList<String>(charsetMap.keySet());

		Collections.sort(charsetKeyList);

		String[] charsetArray = charsetKeyList.toArray(new String[charsetKeyList.size()]);

		AutoCompleteJComboBox<String> charsetJComboBox = new AutoCompleteJComboBox<String>(charsetArray);

		return charsetJComboBox;
	}

	public static AutoCompleteJComboBox<Locale> getFileLocaleJComboBox() {

		List<Locale> localeList = Arrays.asList(Locale.getAvailableLocales());

		Collections.sort(localeList, new Comparator<Locale>() {

			@Override
			public int compare(Locale o1, Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		Locale[] localeArray = localeList.toArray(new Locale[localeList.size()]);

		AutoCompleteJComboBox<Locale> fileLocaleJComboBox = new AutoCompleteJComboBox<Locale>(localeArray);

		return fileLocaleJComboBox;
	}

	public static AutoCompleteJComboBox<String> getTimeZoneJComboBox() {

		List<String> timeZoneList = Arrays.asList(TimeZone.getAvailableIDs());

		Collections.sort(timeZoneList);

		String[] timeZoneArray = timeZoneList.toArray(new String[timeZoneList.size()]);

		AutoCompleteJComboBox<String> timeZoneJComboBox = new AutoCompleteJComboBox<String>(timeZoneArray);

		return timeZoneJComboBox;
	}

	public static void scrollRectangleToVisible(JViewport viewport, Rectangle rect) {

		Rectangle viewRect = viewport.getViewRect();

		int x = viewRect.x;
		int y = viewRect.y;

		// Calculate location of rectangle if it were at the center of view
		int centerX = (viewRect.width - rect.width) / 2;
		int centerY = (viewRect.height - rect.height) / 2;

		if (rect.x >= viewRect.x && rect.x <= (viewRect.x + viewRect.width - rect.width)) {
			// do nothing
		} else if (rect.x < viewRect.x) {
			x = rect.x;
		} else if (rect.x > (viewRect.x + viewRect.width - rect.width)) {
			x = rect.x - centerX;
		}

		if (rect.y >= viewRect.y && rect.y <= (viewRect.y + viewRect.height - rect.height)) {
			// do nothing
		} else if (rect.y < viewRect.y) {
			y = rect.y - centerY;
		} else if (rect.y > (viewRect.y + viewRect.height - rect.height)) {
			y = rect.y - centerY;
		}

		viewport.setViewPosition(new Point(x, y));
	}

	public static String getFileHyperlinkText(String filePath) {

		String url = "file://" + filePath;

		return getHyperlinkText(url, filePath);
	}

	public static String getHyperlinkText(String url) {
		return getHyperlinkText(url, url);
	}

	private static String getHyperlinkText(String url, String text) {

		StringBuffer sb = new StringBuffer();

		sb.append("<html><head/><body><div>");
		sb.append("<a href=\"");
		sb.append(url);
		sb.append("\">");
		sb.append(text);
		sb.append("</a>");
		sb.append("</div></body></html>");

		return sb.toString();
	}
}
