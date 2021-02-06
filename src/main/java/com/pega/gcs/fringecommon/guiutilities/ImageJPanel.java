/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class ImageJPanel extends JPanel {

    private static final long serialVersionUID = 1576961618259416647L;

    private Image image;

    private Image scaledImage;

    private int imageWidth;

    private int imageHeight;

    private boolean scaling;

    private boolean drawBorder;

    private Color borderColor;

    public ImageJPanel(Image image) {
        this(image, true);
    }

    public ImageJPanel(Image image, boolean scaling) {
        this(image, scaling, false, null);
    }

    public ImageJPanel(Image image, boolean scaling, boolean drawBorder, Color borderColor) {
        super();

        this.image = image;
        this.scaling = scaling;
        this.drawBorder = drawBorder;
        this.borderColor = borderColor;

        if (borderColor == null) {
            borderColor = Color.BLACK;
        }

        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);

        Dimension preferredSize = new Dimension(imageWidth, imageHeight);

        setPreferredSize(preferredSize);

        addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent componentEvent) {
                // do nothing
            }

            @Override
            public void componentResized(ComponentEvent componentEvent) {
                setScaledImage();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {
                // do nothing
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {
                // do nothing
            }
        });

    }

    @Override
    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);

        if (scaledImage != null) {

            int xpos = 0;
            int ypos = 0;
            int scaledWidth = scaledImage.getWidth(this);
            int scaledHeight = scaledImage.getHeight(this);
            int panelWidth = this.getWidth();
            int panelHeight = this.getHeight();

            // centre image in panel
            if (panelWidth > scaledWidth) {
                xpos = (panelWidth - scaledWidth) / 2;
            }
            if (panelHeight > scaledHeight) {
                ypos = (panelHeight - scaledHeight) / 2;
            }

            graphics.drawImage(scaledImage, xpos, ypos, this);

            if (drawBorder) {
                graphics.setColor(borderColor);
                // top line
                graphics.drawLine(xpos, ypos, (xpos + scaledWidth), ypos);
                // bottom line
                graphics.drawLine(xpos, (ypos + scaledHeight), (xpos + scaledWidth), (ypos + scaledHeight));
                // left line
                graphics.drawLine(xpos, ypos, xpos, (ypos + scaledHeight));
                // right line
                graphics.drawLine((xpos + scaledWidth), ypos, (xpos + scaledWidth), (ypos + scaledHeight));
            }
        } else {
            graphics.setColor(Color.BLACK);
            graphics.drawString("No Image", this.getWidth() / 3, this.getHeight() / 3);
        }
    }

    protected void setScaledImage() {

        if (image != null) {

            if (scaling) {
                // use floats so division below won't round
                float iw = imageWidth;
                float ih = imageHeight;
                float pw = this.getWidth(); // panel width
                float ph = this.getHeight(); // panel height

                if (pw < iw || ph < ih) {

                    if ((pw / ph) > (iw / ih)) {
                        iw = -1;
                        ih = ph;
                    } else {
                        iw = pw;
                        ih = -1;
                    }

                    // prevent errors if panel is 0 wide or high
                    if (iw == 0) {
                        iw = -1;
                    }
                    if (ih == 0) {
                        ih = -1;
                    }

                    scaledImage = image.getScaledInstance(Float.valueOf(iw).intValue(), Float.valueOf(ih).intValue(),
                            Image.SCALE_DEFAULT);

                } else {
                    scaledImage = image;
                }
            } else {
                scaledImage = image;
            }

        }

        revalidate();
        repaint();
    }

}
