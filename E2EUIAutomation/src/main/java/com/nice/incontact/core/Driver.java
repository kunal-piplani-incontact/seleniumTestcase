package com.nice.incontact.core;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.ini4j.InvalidFileFormatException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nice.incontact.util.IniForLocators;
import com.nice.incontact.core.Driver;
import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.SeleniumLogger;

public abstract class Driver {
	private static final SeleniumLogger log = new SeleniumLogger(Driver.class);
	private static RemoteWebDriver driver = null;
	protected HashMap<String, String> parameters = new HashMap<String, String>();
	private String rootXPath = "";
	protected static ConfigLoader config = null;
	static HashMap<String,String> locatorMap=null;
	static HashMap<String, HashMap<String, String>> pageClasslocatorMap=null;
	//Create Map to store current driver session.
	private static ConcurrentHashMap<Long, RemoteWebDriver> driverMap = new ConcurrentHashMap<Long, RemoteWebDriver>();
	public Driver() {

	}
	
	public Driver(String... params) {
		for (String parameter : params) {
			String name = parameter.substring(0, parameter.indexOf("="));
			String value = parameter.substring(parameter.indexOf("=") + 1);
			parameters.put(name, value);
		}
		// can set the rootxpath here based on if it is xpath/id/class etc
		if (isParameterDefined("xpath")) {
			this.rootXPath = getParameterValue("xpath");
		}
		if (isParameterDefined("id")) {
			this.rootXPath = "//*[@id='" + getParameterValue("id") + "']";
		}
		if (isParameterDefined("class")) {
			this.rootXPath = "//*[@class='" + getParameterValue("class") + "']";
		}
		if (config == null) {
			config = new ConfigLoader();
		}
	}
	
	/**
	 * Prepares the server URL and posts to the browser
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 * @param uri
	 * @return
	 */
    protected void launchApplication(String protocol, String host, String port, String uri) {
        if (driver != null) {
            
            log.info("launchApplication", "Launching " + config.browser + " with url.........." + protocol + "://" + host + "/" + uri);
            driver.manage().deleteAllCookies();
            driver.get(protocol + "://" + host + ":" + port + "/" + uri);

            // Following piece of code is for handling any Alert Message Box
            // that may appear before Login
            try {
                WebDriverWait wait = new WebDriverWait(driver, 2);

               // Alert alt = driver.switchTo().alert();
               // log.info("openApplication", "Alert Message Box is detected: " + alt.getText());
               // alt.dismiss();
            } catch (NoAlertPresentException noe) {
                // No alert found on page, proceed with test.
                log.info("launchApplication", "Alert Message Box is NOT present");

            }
            driver.manage().window().maximize();
        } else {
            log.error("launchApplication", "Driver not initialized. Look for earlier warning / errors in the log.");
        }
	}
	
	
	/**
	 * Returns the instance of Remote web driver
	 * 
	 * @return driver
	 */
	public static RemoteWebDriver getRemoteWebDriverInstance() {
		driver = driverMap.get(Thread.currentThread().getId());
		return driver;
	}
	
	/**
	 * Returns true of key with name is defined else returns false
	 * 
	 * @param paramName
	 * @return boolean
	 */
	protected boolean isParameterDefined(String paramName) {
		if (parameters.containsKey(paramName)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns value of key with name if defined else returns null
	 * 
	 * @param paramName
	 * @return paramValue
	 */
	protected String getParameterValue(String paramName) {
		if (isParameterDefined(paramName))
			return parameters.get(paramName);

		return null;
	}
	
	/**
	 * Returns rootXPath
	 * 
	 * @return rootXPath
	 */
	public String getXPathLocator() {
		return rootXPath;
	}
	

	/**
	 * Initializes driver object with the capabilities set
	 * 
	 * @param hubUrl
	 * @param capabilities
	 * @return driver
	 */
	public static RemoteWebDriver initializeDriver(URL hubUrl, DesiredCapabilities capabilities) {
		 System.out.println("RemoteWebDriver");
		driver =  new RemoteWebDriver(hubUrl, capabilities);
		System.out.println(driver);
		//Put the current browser instance in map
		driverMap.put(Thread.currentThread().getId(), driver);
		return driver;
	}

	/**
	 * Method should remove the entry from map.
	 * 
	 */
	public static void deinitializeDriver()
	{
		driverMap.remove(Thread.currentThread().getId());
	}


	/**
	 * @usage: This api will load locators for Locators.ini
	 * Description: This will return map containing entire end user portal
	 * map containing locator lists
	 */
	protected static HashMap<String,String> getEUPortalLocators()
	{

		try {
			if(locatorMap==null)
				locatorMap=IniForLocators.getlocatorMap("Locators");
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		}
		return locatorMap;

	}

	/**
	 * @usage: This api will load locators
	 * Description: This will return map where name is pageclass name and Values are
	 * map containing locator lists
	 */
	protected static HashMap<String,HashMap<String,String>> getPageClassLocators()
	{

		try {
			if(pageClasslocatorMap==null)
				pageClasslocatorMap=IniForLocators.getPageClasslocatorMap("Locators");
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		}
		return pageClasslocatorMap;

	}

	/**
	 * return config Data Object
	 */
	public static ConfigLoader getconfigData()
	{
		return config;
	}

	/**
	 * This method returns the URL of the page which is loaded.
	 * @return Page URL of the current page
	 */
	public static String getPageURL() {
        if (driver != null) {
            // Do special code for getting current URL on IE browser....
            if (driver.getCapabilities().getBrowserName().trim().equals("internet explorer")) {
                return (String) driver.executeScript("return document.location.href");
            } else {
                return driver.getCurrentUrl();
            }
		} else {
			log.warn("getPageURL", "Driver not initialized. Cannot determine current page URL");
		}
		return null;
	}


}
