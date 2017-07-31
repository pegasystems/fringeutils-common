/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.Icon;

public class ShapeIcon implements Icon {

	private Shape shape;

	private Color color;

	private boolean antiAliasing;

	public ShapeIcon(Shape shape, Color color) {
		super();

		this.shape = shape;
		this.color = color;
		this.antiAliasing = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Use Graphics2D so we can do antialiasing

		Graphics2D g2d = (Graphics2D) g.create();

		if (antiAliasing) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// Handle Icon position within the component and Shape translation
		// (ie. X/Y positions in bounding rectangle are ignored)

		Rectangle bounds = shape.getBounds();
		g2d.translate(x - bounds.x, y - bounds.y);

		// Fill the Shape with the specified Color
		g2d.setColor(color);
		g2d.fill(shape);

		g2d.dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		Rectangle bounds = shape.getBounds();
		return bounds.width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		Rectangle bounds = shape.getBounds();
		return bounds.height;
	}

	/**
	 * @return the antiAliasing
	 */
	protected boolean isAntiAliasing() {
		return antiAliasing;
	}

	/**
	 * @param antiAliasing
	 *            the antiAliasing to set
	 */
	protected void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	protected void setColor(Color color) {
		this.color = color;
	}

}
