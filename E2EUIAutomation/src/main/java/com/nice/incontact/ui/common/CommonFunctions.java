package com.nice.incontact.ui.common;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.util.WaitTool;
import com.nice.incontact.pageObjects.LoginPage;
import com.nice.incontact.components.WebElements;
import com.nice.incontact.core.Driver;

public class CommonFunctions extends WebElements {
	 private static final SeleniumLogger log = new SeleniumLogger(CommonFunctions.class);
	 private static HashMap<String, String> locators = getPageClassLocators().get("Login");
	    
	 private final String protocol;
	 private final String host;
	 private final String port;
	 private final String uri;
	 
	 public CommonFunctions(String protocol, String host, String port, String uri) {
	        this.protocol = protocol;
	        this.host = host;
	        this.port = port;
	        this.uri = uri;
	    }
	 
	 public void login(String user, String password, String authentication) {
		 final int LOGIN_TIMEOUT = 18000; 
		/* System.setProperty("webdriver.chrome.driver", "lib\\drivers\\chromedriver.exe");
		 WebDriver driver = new ChromeDriver();*/
		 System.setProperty("phantomjs.binary.path", "usr/bin/phantomjs");
		 WebDriver driver = new PhantomJSDriver(DesiredCapabilities.phantomjs());
		 Driver.setWebDriver(driver);
	        log.debug("login", "Opening Central");
	        launchApplication(protocol, host, port, uri);
	        if (WaitTool.waitForElementPresent(driver, By.id(locators.get("username")), LOGIN_TIMEOUT) != null) {
	            LoginPage login = new LoginPage();
	            login.doLogin(driver, user, password);
	            //waitForPageLoad();
	            if (WaitTool.waitForElementPresentByXpath(driver, locators.get("coBrandImage")) != null) {
	                log.info("login", "Successfully performed login to Central");
	            }
	        } else {
	            throw new org.openqa.selenium.NoSuchElementException("Failed to find expected login fields within "
	                    + String.valueOf(LOGIN_TIMEOUT / 60) + " minutes.");
	        }
	 }
	 
	 
	 public void launchApplication(){
	        log.debug("login", "Opening Central");
	        launchApplication(protocol, host, port, uri);
	 }
}
