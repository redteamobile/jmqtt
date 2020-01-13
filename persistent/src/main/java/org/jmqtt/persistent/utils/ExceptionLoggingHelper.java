package org.jmqtt.persistent.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionLoggingHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLoggingHelper.class);

    /**
     * Log the throwable with the given logger in 'ERROR' level without stacktrace.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void log(Logger logger, Throwable throwable) {
        logger.error("#EXCEPTION# " + throwable.toString());
    }

    /**
     * Log the throwable with the given logger in 'ERROR' level with stacktrace.
     *
     * @param logger    the logger
     * @param throwable the throwable
     */
    public static void logWithStackTrace(Logger logger, Throwable throwable) {
        StringWriter sr = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sr));
        logger.error("#STACKTRACE# " + sr.toString());
    }

    /**
     * Unexpected error is logged by the logger of {@link ExceptionLoggingHelper}.
     *
     * @param message Error message which should be given manually
     * @param throwable The unexpected error
     */
    public static void logUnexpected(String message, Throwable throwable) {
        LOGGER.error("#UNEXPECTED#" + message);
        logWithStackTrace(LOGGER, throwable);
    }


}
