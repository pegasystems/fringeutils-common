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

    public void entry(Object... params) {
        if (theLogger.isTraceEnabled()) {
            theLogger.entry(params);
        }
    }

    public void trace(String str, Object... object) {
        if (theLogger.isTraceEnabled()) {
            theLogger.trace(String.format(str, object));
        }
    }

    public void trace(Throwable throwable, String str, Object... object) {
        if (theLogger.isTraceEnabled()) {
            String message = String.format(str, object);
            theLogger.trace(message, throwable);
        }
    }

    public void trace(Object object) {
        if (theLogger.isTraceEnabled()) {
            theLogger.trace(object);
        }
    }

    public void trace(Object object, Throwable throwable) {
        if (theLogger.isTraceEnabled()) {
            theLogger.trace(object, throwable);
        }
    }

    public void debug(String str, Object... object) {
        if (theLogger.isDebugEnabled()) {
            theLogger.debug(String.format(str, object));
        }
    }

    public void debug(Throwable throwable, String str, Object... object) {
        if (theLogger.isDebugEnabled()) {
            theLogger.debug(String.format(str, object), throwable);
        }
    }

    public void debug(Object object) {
        if (theLogger.isDebugEnabled()) {
            theLogger.debug(object);
        }
    }

    public void debug(Object object, Throwable throwable) {
        if (theLogger.isDebugEnabled()) {
            theLogger.debug(object, throwable);
        }
    }

    public void info(String str, Object... object) {
        if (theLogger.isInfoEnabled()) {
            theLogger.info(String.format(str, object));
        }
    }

    public void info(Throwable throwable, String str, Object... object) {
        if (theLogger.isInfoEnabled()) {
            theLogger.info(String.format(str, object), throwable);
        }
    }

    public void info(Object object) {
        if (theLogger.isInfoEnabled()) {
            theLogger.info(object);
        }
    }

    public void info(Object object, Throwable throwable) {
        if (theLogger.isInfoEnabled()) {
            theLogger.info(object, throwable);
        }
    }

    public void warn(String str, Object... object) {
        if (theLogger.isWarnEnabled()) {
            theLogger.warn(String.format(str, object));
        }
    }

    public void warn(Throwable throwable, String str, Object... object) {
        if (theLogger.isWarnEnabled()) {
            String message = String.format(str, object);
            theLogger.warn(message, throwable);
        }
    }

    public void warn(Object object) {
        if (theLogger.isWarnEnabled()) {
            theLogger.warn(object);
        }
    }

    public void warn(Object object, Throwable throwable) {
        if (theLogger.isWarnEnabled()) {
            theLogger.warn(object, throwable);
        }
    }

    public void error(String str, Object... object) {
        theLogger.error(String.format(str, object));
    }

    public void error(Throwable throwable, String str, Object... object) {
        String message = String.format(str, object);
        theLogger.error(message, throwable);
    }

    public void error(Object object) {
        theLogger.error(object);
    }

    public void error(Object object, Throwable throwable) {
        theLogger.error(object, throwable);
    }

    public void fatal(String str, Object... object) {
        theLogger.fatal(String.format(str, object));
    }

    public void fatal(Throwable throwable, String str, Object... object) {
        String message = String.format(str, object);
        theLogger.fatal(message, throwable);
    }

    public void fatal(Object object) {
        theLogger.fatal(object);
    }

    public void fatal(Object object, Throwable throwable) {
        theLogger.fatal(object, throwable);
    }

}
