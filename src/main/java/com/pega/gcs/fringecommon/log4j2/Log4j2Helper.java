/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.log4j2;

import java.io.File;
import java.net.URI;

import org.apache.logging.log4j.LogManager;

public class Log4j2Helper extends AbstractLog4j2Helper {

	private static final String LOG4J2XML = "/log4j2.xml";

	public Log4j2Helper(Class<?> loggerClass) {

		super(loggerClass);
	}

	protected void initLoggerContext() {

		if (isInitialized()) {
			return;
		}

		try {
			String pwd = System.getProperty("user.dir");
			File log4j2XmlFile = new File(pwd, LOG4J2XML);

			URI configLocation = null;

			if (log4j2XmlFile.exists()) {

				configLocation = log4j2XmlFile.toURI();

			} else {

				configLocation = getClass().getResource(LOG4J2XML).toURI();
			}

			LogManager.getContext(null, false, configLocation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		setInitialized(true);
	}

}
