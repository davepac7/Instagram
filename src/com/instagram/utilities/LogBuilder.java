package com.instagram.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogBuilder { // Initialize Log4j logs

	Logger Log = LogManager.getLogger(LogBuilder.class.getName());
	// This is to print log for the beginning of the test case
	public void startTestCase(String sTestCaseName) {
		Log.info(Thread.currentThread().getId());
		Log.info("*******************************************************************************");
		Log.info("*******************************************************************************");
		Log.info("$$$$$$$$$$$$$$$$$$$$$		" + sTestCaseName + "		$$$$$$$$$$$$$$$$$$$$$$$$$");
		Log.info("********************************************************************************");
		Log.info("********************************************************************************");
	}
	// This is to print log for the ending of the test case
	public void endTestCase() {
		Log.info("*******************************************************************************");
		Log.info("$$$$$$$$$$$$$$$$$$$$$		" + "--E--N--D--" + "		$$$$$$$$$$$$$$$$$$$$$$$$$");
		Log.info("*******************************************************************************");
	}
	// Need to create these methods, so that they can be called
	public void info(String message) {
	//	Log.info(message);
	}
	public void warn(String message) {
		Log.warn(message);
	}
	public void error(String message) {
		Log.error(message);
	}
	public void fatal(String message) {
		Log.fatal(message);
	}
	public void debug(String message) {
		Log.debug(message);
	}

}
