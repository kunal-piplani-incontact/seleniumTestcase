package com.nice.incontact.pageObjects;

import java.util.HashMap;

import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.components.WebElements;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends WebElements {

	private static final SeleniumLogger log = new SeleniumLogger(LoginPage.class);
	private static HashMap<String, String> locators = getPageClassLocators().get("LoginPage");

	public LoginPage() {
		super("xpath=" + locators.get("pageLocator"));
	}

	public boolean isOpen() {
		boolean bIsOpen = false;
		if (isElementPresent(getXPathLocator())) {
			bIsOpen = true;
		}
		return bIsOpen;
	}

	public void doLogin(WebDriver driver, String userName, String password) {
		try {
			log.info("doLogin", "Logging into Central with user: " + userName);
			log.info("doLogin", locators.get("username"));
			log.info("doLogin", driver.findElement(By.xpath(locators.get("username"))).toString());
			WebElement username = driver.findElement(By.xpath(locators.get("username")));
			username.sendKeys(userName);

			WebElement password1 = driver.findElement(By.xpath(locators.get("password")));
			password1.sendKeys(password);

			WebElement button = driver.findElement(By.xpath(locators.get("loginButton")));
			button.click();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
