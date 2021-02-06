/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TimeZone;

import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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

        int xpos = viewRect.x;
        int ypos = viewRect.y;

        // Calculate location of rectangle if it were at the center of view
        int centerX = (viewRect.width - rect.width) / 2;
        int centerY = (viewRect.height - rect.height) / 2;

        if (rect.x >= viewRect.x && rect.x <= (viewRect.x + viewRect.width - rect.width)) {
            // do nothing
        } else if (rect.x < viewRect.x) {
            xpos = rect.x;
        } else if (rect.x > (viewRect.x + viewRect.width - rect.width)) {
            xpos = rect.x - centerX;
        }

        if (rect.y >= viewRect.y && rect.y <= (viewRect.y + viewRect.height - rect.height)) {
            // do nothing
        } else if (rect.y < viewRect.y) {
            ypos = rect.y - centerY;
        } else if (rect.y > (viewRect.y + viewRect.height - rect.height)) {
            ypos = rect.y - centerY;
        }

        viewport.setViewPosition(new Point(xpos, ypos));
    }

    public static String getHref(String url, String text) {

        StringBuilder sb = new StringBuilder();

        sb.append("<a");

        if ((url != null) && (!"".equals(url))) {
            sb.append(" href=\"");
            sb.append(url);
            sb.append("\"");
        }

        sb.append(">");
        sb.append(text);
        sb.append("</a>");

        return sb.toString();

    }

    public static String getFileHyperlinkText(String filePath) {

        String url = "file://" + filePath;

        return getHyperlinkText(url, filePath);
    }

    public static String getHyperlinkText(String url) {
        return getHyperlinkText(url, url);
    }

    // applies styles: "html body div p-implied a"
    public static String getHyperlinkText(String url, String text) {

        StringBuilder sb = new StringBuilder();

        sb.append("<html><head/><body><div>");
        sb.append(getHref(url, text));
        sb.append("</div></body></html>");

        return sb.toString();
    }

    public static void setMessage(JTextField statusBar, Message message) {

        Color color = Color.BLUE;
        String messageText = null;

        if (message != null) {

            if (message.getMessageType().equals(Message.MessageType.ERROR)) {
                color = Color.RED;
            }

            messageText = message.getText();
        }

        statusBar.setForeground(color);
        statusBar.setText(messageText);
    }

    public static void centerFrameOnScreen(final Window frame) {
        positionFrameOnScreen(frame, 0.5, 0.5);
    }

    /**
     * Positions the specified frame at a relative position in the screen, where 50% is considered to be the center of the screen.
     *
     * @param frame             the frame.
     * @param horizontalPercent the relative horizontal position of the frame (0.0 to 1.0, where 0.5 is the center of the screen).
     * @param verticalPercent   the relative vertical position of the frame (0.0 to 1.0, where 0.5 is the center of the screen).
     */
    public static void positionFrameOnScreen(final Window frame, final double horizontalPercent,
            final double verticalPercent) {

        final Rectangle s = frame.getGraphicsConfiguration().getBounds();
        final Dimension f = frame.getSize();
        final int w = Math.max(s.width - f.width, 0);
        final int h = Math.max(s.height - f.height, 0);
        final int x = (int) (horizontalPercent * w) + s.x;
        final int y = (int) (verticalPercent * h) + s.y;
        frame.setBounds(x, y, f.width, f.height);
    }

    public static JTextField getNonEditableTextField() {

        JTextField nonEditableTextField = new JTextField();
        nonEditableTextField.setEditable(false);
        nonEditableTextField.setHorizontalAlignment(JTextField.CENTER);
        nonEditableTextField.setBackground(null);
        nonEditableTextField.setBorder(null);

        return nonEditableTextField;
    }

    public static void applyTableColumnResize(TableColumnModel tableColumnModel, Rectangle oldBounds,
            Rectangle newBounds) {

        double oldWidth = oldBounds.getWidth();
        double newWidth = newBounds.getWidth();

        double widthAspectRatio = newWidth / oldWidth;

        for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {

            TableColumn column = tableColumnModel.getColumn(i);

            double newValue = (column.getWidth() * widthAspectRatio);
            int columnWidth = (int) Math.round(newValue);

            column.setPreferredWidth(columnWidth);
        }
    }
}
