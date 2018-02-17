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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.nice.incontact.core.Driver;
import com.nice.incontact.util.SeleniumLogger;



public class ScreenShotOnFailure extends TestListenerAdapter {
	  private static final SeleniumLogger log = new SeleniumLogger(ScreenShotOnFailure.class);

	    /*
	     * Invoked each time a test fails. tr - ITestResult containing information
	     * about the run test
	     */
	    @Override
	    public void onTestFailure(ITestResult tr) {
	        // Log error message with cause of failure
	        String testMsg = tr.getThrowable().toString();
	        log.error("onTestFailure", "TEST_FAILURE: " + testMsg);

	        // Get driver instance.
	        RemoteWebDriver driver = Driver.getRemoteWebDriverInstance();

	        /* Screenshot captured */
	        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	        /* get current date time with Date() to create unique file name */
	        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
	        String destDir = "screenshots";
	        new File(destDir).mkdirs();
	        String destFile = dateFormat.format(new Date()) + ".jpeg";

	        try {
	            FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
	        } catch (IOException e) {
	            log.warn("onTestFailure", "Error while creating Screenshot file " + e.getMessage());
	        }

	        Reporter.setEscapeHtml(false);
	        Reporter.log("Saved <a href=../screenshots/" + destFile + ">Screenshot</a>");
	        log.debug("onTestFailure", "Screenshot saved successfully on test failure: " + destFile);
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
}
