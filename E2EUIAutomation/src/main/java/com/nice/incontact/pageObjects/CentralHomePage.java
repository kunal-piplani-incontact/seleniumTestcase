package com.nice.incontact.pageObjects;

import java.util.HashMap;

import org.openqa.selenium.WebElement;

import com.nice.incontact.components.WebElements;
import com.nice.incontact.core.Driver;
import com.nice.incontact.util.SeleniumLogger;


public class CentralHomePage extends WebElements {
	private static final SeleniumLogger log = new SeleniumLogger(CentralHomePage.class);
	private static HashMap<String, String> locators = getPageClassLocators().get("CentralHomePage");
	
	//private String CENTRAL_HOME_PAGE_URL = "https://home-sc10.ucnlabext.com/inContact/Default.aspx";
	
	public boolean isCentralHomePagePresent(String CENTRAL_HOME_PAGE_URL){
		try {
			if(Driver.getPageURL().equals(CENTRAL_HOME_PAGE_URL)){
				return true;
			}
		} catch (Exception e) {	
			e.printStackTrace();
		}
		return false;
	}
	
	public void launchMax(String phoneNumber){
		try {
			this.setFocus(locators.get("sideBar"));
			WebElement maxIcon = getElementByXpath(getXPathLocator() + locators.get("maxIcon"));
			this.clickOnElement(maxIcon);
			//Thread.sleep(50000);
			//maxIcon.click();
			this.waitForPageWithTitle("Agent", 30);
			if(this.getPageTitle().equals("Agent")){
				this.enterKey(this.getElementById(locators.get("phoneNumberField")), phoneNumber);
			}
			WebElement continueButton = getElementById(locators.get("agentContinueButton"));
			this.clickOnElement(continueButton);
			//continueButton.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
