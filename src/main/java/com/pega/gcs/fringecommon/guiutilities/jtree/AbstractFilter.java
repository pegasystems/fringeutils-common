/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities.jtree;

public abstract class AbstractFilter implements Filter {

	private String key;

	public AbstractFilter(String key) {
		this.key = key;

		assert (key == null);
	}

	@Override
	public String getKey() {
		return key;
	}

}
