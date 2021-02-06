/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;

import com.pega.gcs.fringecommon.log4j2.Log4j2Helper;

public class MyColor extends Color {

    private static final long serialVersionUID = -4553711211542461086L;

    private static final Log4j2Helper LOG = new Log4j2Helper(MyColor.class);

    public static final MyColor PAPAYAWHIP = new MyColor(255, 239, 213);
    public static final MyColor DBTRACE_COLOR = new MyColor(221, 239, 255);

    public static final MyColor LIGHT_PINK = new MyColor(255, 182, 193);
    public static final MyColor GOLD = new MyColor(0xffd700);
    public static final MyColor TOMATO = new MyColor(0xff6347);
    public static final MyColor LIGHT_SLATE_GRAY = new MyColor(0x778899);
    public static final MyColor LIGHTEST_GRAY = new MyColor(225, 225, 225);
    public static final MyColor LIGHTEST_LIGHT_GRAY = new MyColor(240, 240, 240);
    public static final MyColor CREAM = new MyColor(255, 236, 216);
    public static final MyColor LIGHT_YELLOW = new MyColor(255, 255, 64);
    public static final MyColor LIGHTEST_YELLOW = new MyColor(255, 255, 190);
    public static final MyColor LIGHTEST_RED = new MyColor(255, 192, 192);
    public static final MyColor LIME = new MyColor(220, 240, 64);
    public static final MyColor LIGHT_LIME = new MyColor(220, 255, 128);
    public static final MyColor LIGHTEST_LIME = new MyColor(240, 255, 192);
    public static final MyColor LIGHT_GREEN = new MyColor(102, 255, 102);
    public static final MyColor LIGHT_FADED_GREEN = new MyColor(164, 240, 164);
    public static final MyColor LIGHTEST_GREEN = new MyColor(192, 255, 192);
    public static final MyColor LIGHT_BLUE = new MyColor(32, 32, 255);

    // traffic light system
    public static final Color GREEN_ON = Color.GREEN;
    public static final MyColor GREEN_OFF = new MyColor(0, 64, 0);

    public static final Color RED_ON = Color.RED;
    public static final MyColor RED_OFF = new MyColor(128, 0, 0);

    public static final MyColor TRACE = new MyColor(182, 182, 182);
    public static final MyColor DEBUG = new MyColor(127, 255, 212);
    public static final MyColor INFO = new MyColor(155, 255, 155);
    public static final MyColor WARN = new MyColor(255, 165, 0);
    public static final MyColor ALERT = new MyColor(255, 69, 0);
    public static final MyColor ERROR = new MyColor(255, 51, 51);
    public static final MyColor FATAL = new MyColor(255, 0, 0);

    public MyColor(int rgb) {
        super(rgb);
    }

    private MyColor(int red, int green, int blue) {
        super(red, green, blue);
    }

    public static String getHex(Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return hex;
    }

    public static Color getColor(String hexColorStr) {

        int red = Integer.valueOf(hexColorStr.substring(1, 3), 16);
        int green = Integer.valueOf(hexColorStr.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColorStr.substring(5, 7), 16);

        Color color = new Color(red, green, blue);
        return color;
    }

    public static void test() {

        Color color = Color.WHITE;
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        float hue = hsb[0];
        float saturation = hsb[1];
        float brightness = hsb[2];

        hue = (hue + 180) % 360;
        saturation = 1.0f - saturation;
        brightness = 1.0f - brightness;

        LOG.info(new Color(Color.HSBtoRGB(hue, saturation, brightness)));
    }

    public static void main(String[] args) {

        // Color.getColor(nm)
        LOG.info(MyColor.LIGHT_PINK);
        test();
    }
}
