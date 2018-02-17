package com.nice.incontact.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class SeleniumLogger {
	 Logger logger;

	    public SeleniumLogger(Class<?> clazz) {
	        logger = Logger.getLogger(clazz);
	    }

	    public void info(String method, String message) {
	        logger.info(method + " - " + message);
	    }

	    public void info(String method, String message, Throwable exception) {
	        logger.info(method + " - " + message, exception);
	    }

	    public boolean isInfoEnabled() {
	        return logger.isInfoEnabled();
	    }

	    public void debug(String method, String message) {
	        logger.debug(method + " - " + message);
	    }

	    public void debug(String method, String message, Throwable exception) {
	        logger.debug(method + " - " + message, exception);
	    }

	    public boolean isDebugEnabled() {
	        return logger.isDebugEnabled();
	    }

	    public void error(String method, String message) {
	        logger.error(method + " - " + message);
	    }

	    public void error(String method, String message, Throwable exception) {
	        logger.error(method + " - " + message, exception);
	    }

	    public void warn(String method, String message) {
	        logger.warn(method + " - " + message);
	    }

	    public void warn(String method, String message, Throwable exception) {
	        logger.warn(method + " - " + message, exception);
	    }

	    public void fatal(String method, String message) {
	        logger.fatal(method + " - " + message);
	    }

	    public void fatal(String method, String message, Throwable exception) {
	        logger.fatal(method + "- " + message, exception);
	    }

	    public boolean isEnabledFor(Priority level) {
	        return logger.isEnabledFor(level);
	    }
}
