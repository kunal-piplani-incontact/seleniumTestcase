package com.nice.incontact.pageObjects;

import java.util.HashMap;

import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.components.WebElements;
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

	public void doLogin(String userName, String password) {
		try {
			log.info("doLogin", "Logging into Central with user: " + userName);
			WebElement username = getElementByXpath(getXPathLocator() + locators.get("username"));
			username.sendKeys(userName);

			WebElement password1 = getElementByXpath(getXPathLocator() + locators.get("password"));
			password1.sendKeys(password);

			WebElement button = getElementByXpath(getXPathLocator() + locators.get("loginButton"));
			button.click();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
