/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public abstract class AbstractLog4j2Helper {

	private static boolean initialized = false;

	private Logger theLogger;

	protected abstract void initLoggerContext();

	public AbstractLog4j2Helper(Class<?> loggerClass) {

		if (!isInitialized()) {
			initLoggerContext();
		}

		try {
			theLogger = (Logger) LogManager.getLogger(loggerClass.getName());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	protected static boolean isInitialized() {
		return initialized;
	}

	protected static void setInitialized(boolean initialized) {
		AbstractLog4j2Helper.initialized = initialized;
	}

	public void entry() {
		if (theLogger.isTraceEnabled()) {
			theLogger.entry();
		}
	}

	public void entry(Object... params) {
		if (theLogger.isTraceEnabled()) {
			theLogger.entry(params);
		}
	}

	public void exit() {
		if (theLogger.isTraceEnabled()) {
			theLogger.exit();
		}
	}

	public <R> R exit(final R result) {
		if (theLogger.isTraceEnabled()) {
			theLogger.exit(result);
		}

		return result;
	}

	public void trace(String f, Object... o) {
		if (theLogger.isTraceEnabled()) {
			theLogger.trace(String.format(f, o));
		}
	}

	public void trace(Throwable t, String f, Object... o) {
		if (theLogger.isTraceEnabled()) {
			String message = String.format(f, o);
			theLogger.trace(message, t);
		}
	}

	public void trace(Object o) {
		if (theLogger.isTraceEnabled()) {
			theLogger.trace(o);
		}
	}

	public void trace(Object o, Throwable t) {
		if (theLogger.isTraceEnabled()) {
			theLogger.trace(o, t);
		}
	}

	public void debug(String f, Object... o) {
		if (theLogger.isDebugEnabled()) {
			theLogger.debug(String.format(f, o));
		}
	}

	public void debug(Throwable t, String f, Object... o) {
		if (theLogger.isDebugEnabled()) {
			theLogger.debug(String.format(f, o), t);
		}
	}

	public void debug(Object o) {
		if (theLogger.isDebugEnabled()) {
			theLogger.debug(o);
		}
	}

	public void debug(Object o, Throwable t) {
		if (theLogger.isDebugEnabled()) {
			theLogger.debug(o, t);
		}
	}

	public void info(String f, Object... o) {
		if (theLogger.isInfoEnabled()) {
			theLogger.info(String.format(f, o));
		}
	}

	public void info(Throwable t, String f, Object... o) {
		if (theLogger.isInfoEnabled()) {
			theLogger.info(String.format(f, o), t);
		}
	}

	public void info(Object o) {
		if (theLogger.isInfoEnabled()) {
			theLogger.info(o);
		}
	}

	public void info(Object o, Throwable t) {
		if (theLogger.isInfoEnabled()) {
			theLogger.info(o, t);
		}
	}

	public void warn(String f, Object... o) {
		if (theLogger.isWarnEnabled()) {
			theLogger.warn(String.format(f, o));
		}
	}

	public void warn(Throwable t, String f, Object... o) {
		if (theLogger.isWarnEnabled()) {
			String message = String.format(f, o);
			theLogger.warn(message, t);
		}
	}

	public void warn(Object o) {
		if (theLogger.isWarnEnabled()) {
			theLogger.warn(o);
		}
	}

	public void warn(Object o, Throwable t) {
		if (theLogger.isWarnEnabled()) {
			theLogger.warn(o, t);
		}
	}

	public void error(String f, Object... o) {
		theLogger.error(String.format(f, o));
	}

	public void error(Throwable t, String f, Object... o) {
		String message = String.format(f, o);
		theLogger.error(message, t);
	}

	public void error(Object o) {
		theLogger.error(o);
	}

	public void error(Object o, Throwable t) {
		theLogger.error(o, t);
	}

	public void fatal(String f, Object... o) {
		theLogger.fatal(String.format(f, o));
	}

	public void fatal(Throwable t, String f, Object... o) {
		String message = String.format(f, o);
		theLogger.fatal(message, t);
	}

	public void fatal(Object o) {
		theLogger.fatal(o);
	}

	public void fatal(Object o, Throwable t) {
		theLogger.fatal(o, t);
	}

}
