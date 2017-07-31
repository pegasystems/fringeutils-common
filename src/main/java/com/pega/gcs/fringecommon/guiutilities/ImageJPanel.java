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
			public void componentShown(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentResized(ComponentEvent e) {
				setScaledImage();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// do nothing
			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (scaledImage != null) {

			int x = 0;
			int y = 0;
			int scaledWidth = scaledImage.getWidth(this);
			int scaledHeight = scaledImage.getHeight(this);
			int panelWidth = this.getWidth();
			int panelHeight = this.getHeight();

			// centre image in panel
			if (panelWidth > scaledWidth) {
				x = (panelWidth - scaledWidth) / 2;
			}
			if (panelHeight > scaledHeight) {
				y = (panelHeight - scaledHeight) / 2;
			}

			g.drawImage(scaledImage, x, y, this);

			if (drawBorder) {
				g.setColor(borderColor);
				// top line
				g.drawLine(x, y, (x + scaledWidth), y);
				// bottom line
				g.drawLine(x, (y + scaledHeight), (x + scaledWidth), (y + scaledHeight));
				// left line
				g.drawLine(x, y, x, (y + scaledHeight));
				// right line
				g.drawLine((x + scaledWidth), y, (x + scaledWidth), (y + scaledHeight));
			}
		} else {
			g.setColor(Color.BLACK);
			g.drawString("No Image", this.getWidth() / 3, this.getHeight() / 3);
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

					scaledImage = image.getScaledInstance(new Float(iw).intValue(), new Float(ih).intValue(),
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
