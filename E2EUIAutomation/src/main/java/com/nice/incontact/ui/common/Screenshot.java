package com.nice.incontact.ui.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.nice.incontact.core.Driver;
import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.SeleniumLogger;

public class Screenshot extends TestListenerAdapter {
	 private static final SeleniumLogger log = new SeleniumLogger(Screenshot.class);
	    public static final String destDir = "screenshots";

	    protected static final ConfigLoader config = new ConfigLoader();

	    
	    @Override
	    public void onConfigurationFailure(ITestResult result) {
	        log.error("onConfigurationFailure", "TEST_FAILURE: ", result.getThrowable());
	    }
	    
	    /*
	     * Invoked each time a test fails. tr - ITestResult containing information
	     * about the run test
	     */
	    @Override
	    public void onTestFailure(ITestResult tr) {
	        // Log error message with cause of failure
	        log.error("onTestFailure", "TEST_FAILURE: ", tr.getThrowable());
	        addScreenshot();
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	        // will be called after test will be skipped
	        Method method = result.getMethod().getConstructorOrMethod().getMethod();
	        String className = method.getDeclaringClass().getName();
	        String methodName = method.getName();

	        log.info("onTestSkipped", "setup - Executing testcase: " + method + " ...");
	        log.warn("onTestSkipped", "SKIPPED_TEST:" + className + "." + methodName);

	    }

	    @Override
	    public void onTestSuccess(ITestResult result) {
	        // will be called after test will pass
	    }

	    /**
	     * This method will capture the screenshot and saves in local temp folder It
	     * will copy the screenshot from temp to the newly created destination
	     * folder
	     * 
	     */
	    private static void addScreenshot() {
	        // Get driver instance.
	        WebDriver driver = Driver.getRemoteWebDriverInstance();
	        /* Screenshot captured */
	        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	        /* get current date time with Date() to create unique file name */
	        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
	        if (new File(destDir).mkdirs()) {
	            log.debug("addScreenshot", "The directory ["+destDir+"] is created where the screenshots will be stored.");
	        }
	        String destFile = dateFormat.format(new Date()) + ".jpeg";

	        try {
	            FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
	        } catch (IOException e) {
	            log.warn("addScreenshot", "Error while creating Screenshot file " + e.getMessage());
	        }

	        Reporter.setEscapeHtml(false);
	        Reporter.log("Saved <a href=../screenshots/" + destFile + ">Screenshot</a>");
	        log.info("addScreenshot", "Screenshot saved successfully: " + destFile);
	    }

	    /**
	     * This method will invoked every time a user requests either in page/test
	     * class
	     */
	    public static void captureScreenshot() {
	        if (config.takeScreenshot) {        	
	            addScreenshot();
	        } else {
	            log.warn("captureScreenshot", "No Screenshot captured");
	        }
	    }

	    /**
	     * This method will demonstrate how to delete files, older than specified
	     * number of days. This function will delete files from specified directory
	     * only, not from child folders. deleteOlderScreenshot() method takes two
	     * parameters first one is number of days and second parameter is file
	     * extension. It deletes only specified extension files.
	     * listFile.lastModified() - Returns the last modified time in milliseconds.
	     * 
	     */
	    public static void deleteOlderScreenshot(long days, String fileExtension) {
	        File folder = new File(destDir);
	        if (folder.exists()) {
	            File[] listFiles = folder.listFiles();
	            long eligibleForDeletion = System.currentTimeMillis() - (days * 24L * 60L * 60L * 1000L);
	            for (File listFile : listFiles) {
	                if (listFile.getName().endsWith(fileExtension) && listFile.lastModified() < eligibleForDeletion) {
	                    if (!listFile.delete()) {
	                        log.debug("deleteOlderScreenshot", "Sorry, unable to delete files!");
	                    }
	                }
	            }
	        } else {
	            log.error("deleteOlderScreenshot", "Files were not deleted, directory does not exist: " + folder);
	        }
	    }

}
