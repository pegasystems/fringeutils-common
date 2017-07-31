/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.utilities;

import java.nio.ByteBuffer;

public class ArchiveMemoryFile {

	private final String path;

	private final ByteBuffer data;

	public ArchiveMemoryFile(String path, ByteBuffer data) {
		// normalize folder separator
		this.path = path.replace('\\', '/');
		this.data = data;
	}

	public String getName() {
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public String getPath() {
		return path;
	}

	public ByteBuffer getData() {
		return data.duplicate();
	}

	@Override
	public String toString() {
		return path;
	}

}
