package com.nice.incontact.pageObjects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nice.incontact.components.WebElements;
import com.nice.incontact.util.SeleniumLogger;

public class MaxPage extends WebElements {
	
	private static final SeleniumLogger log = new SeleniumLogger(CentralHomePage.class);
	private static HashMap<String, String> locators = getPageClassLocators().get("MaxPage");
	/**
	 * 
	 */
	public void generateOutboundCall(String phoneNumber){
		try{
			WebElement newButton = getElementByXpath(locators.get("newButton"));
			this.clickOnElement(newButton);
			WebElement phoneNumberField = getElementByXpath(locators.get("phoneNumberField"));
			this.enterKey(phoneNumberField, phoneNumber);
			WebElement callButton = getElementByXpath(locators.get("callButton"));
			this.clickOnElement(callButton);
		}catch(Exception e){
			log.error("Error generating outbound call","generateOutboundCall");
		}
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAgentWorking(){
		boolean isWorking = false;
		try{
			WebDriverWait wait = new WebDriverWait(getRemoteWebDriverInstance(), 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locators.get("callPane"))));
			isWorking = MaxPage.isElementPresent(locators.get("callPane"),3000);
		}catch(Exception e){
			log.error("Error checking agent state","");
		}
		return isWorking;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAgentNotWorking(){
		boolean isNotWorking = false;
		try{
			isNotWorking = !MaxPage.isListOfElementsPresent(locators.get("callPane"),3);
		}catch(Exception e){
			log.error("Error checking agent state","isAgentNotWorking");
		}
		return isNotWorking;
	}
	
	/**
	 * 
	 * @param time
	 */
	public void disconnectCall(int time){
		try{
			Thread.sleep(time*1000);
			WebElement hangUpButton = getElementByXpath(locators.get("hangUpButton"));
			this.clickOnElement(hangUpButton);
			WebElement confirmHangUpButton = getElementByXpath(locators.get("confirmHangUpButton"));
			this.clickOnElement(confirmHangUpButton);
		//	this.wait(3000);
			if(MaxPage.isListOfElementsPresent(locators.get("callPane"),10) == false){
				log.info("disconnectCall", "The call was disconnected");
			}
		}catch(Exception e){
			log.error("Error checking agent state","disconnectCall");
		}
	}
	
	/**
	 * 
	 */
	public void closeMAX(){
		try{
			getRemoteWebDriverInstance().close();
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
		}catch(Exception e){
			log.error("Error closing MAX window","closeMAX");
		}
	}

}
