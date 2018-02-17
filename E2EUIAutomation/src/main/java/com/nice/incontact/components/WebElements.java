package com.nice.incontact.components;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.util.WaitTool;

import com.nice.incontact.core.Driver;

public class WebElements extends Driver{
	private static Map<String, String> locators = getPageClassLocators().get("Common");
	private static final SeleniumLogger log = new SeleniumLogger(WebElements.class);

	public WebElements(String... params) {
		super(params);
	}

	/**
	 * Waits for webelement presence on UI till mintimeout with locator option
	 * specified.
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return webelement if not null else throws NoSuchElementException
	 */
	public WebElement getElementByXpath(String xpath) {
		WebElement element = WaitTool.waitForElementPresent(getRemoteWebDriverInstance(), By.xpath(xpath), config.mintimeout);
		if (element == null)
			throw new NoSuchElementException(xpath);
		return element;
	}

	/**Waits for webelement presence on UI till mintimeout with locator option
	 * specified.
	 * 
	 * @param id
	 * @return webelement if not null else throws NoSuchElementException
	 */
	public WebElement getElementById(String id) {
		WebElement element = WaitTool.waitForElementPresent(getRemoteWebDriverInstance(), By.id(id), config.mintimeout);
		if (element == null)
			throw new NoSuchElementException(id);
		return element;
	}

	/**
	 * Waits for multiple webelements presence on UI till mintimeout with
	 * locator option specified.
	 * 
	 * @param xpath
	 *            xpath expression
	 * @return List of WebElement if not null else throws NoSuchElementException
	 */
	public List<WebElement> getElementsByXpath(String xpath) {
		List<WebElement> list = WaitTool.waitForListElementsPresent(getRemoteWebDriverInstance(), By.xpath(xpath), config.mintimeout);
		if (list == null)
			throw new NoSuchElementException(xpath);
		return list;
	}

	/**
	 * Checks for presence of element with specified xpath on UI. Driver doesn't
	 * wait for expected element on screen.
	 * 
	 * @param xpath
	 *            -xpath expression of element to be searched on UI.
	 * @return true if element is present false otherwise
	 */
	public static boolean isElementPresent(String xpath) {
		try {
			WebDriver driver = getRemoteWebDriverInstance();
			// nullify implicitlyWait
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			List<WebElement> elements = driver.findElements(By.xpath(xpath));
			if (elements.size() > 0)
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks for presence of element with specified xpath. It will search for
	 * element and waits for it until specified timeout.
	 * 
	 * @param xpath
	 *            -xpath expression of element to be searched on UI.
	 * @param timeout
	 *            - timeout to wait for element
	 * @return true if element is present false otherwise
	 */
	public static boolean isElementPresent(String xpath, int timeout) {
		try {
			WebDriver driver = getRemoteWebDriverInstance();
			// nullify implicitlyWait
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
			if (elements.size() > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks for presence of List of elements with specified xpath on UI.
	 * Driver doesn't wait for expected elements on screen.
	 * 
	 * @param xpath
	 *            -xpath expression of elements to be searched on UI.
	 * @return true if elements are present false otherwise
	 */
	public static boolean isListOfElementsPresent(String xpath) {
		WebDriver driver = getRemoteWebDriverInstance();
		// nullify implicitlyWait
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> list = driver.findElements(By.xpath(xpath));
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks for presence of elements with specified xpath. It will search for
	 * elements and waits for them until specified timeout.
	 * 
	 * @param xpath
	 *            -xpath expression of elements to be searched on UI.
	 * @param timeout
	 *            - timeout to wait for element
	 * @return
	 */
	public static boolean isListOfElementsPresent(String xpath, int timeout) {
		try {
			WebDriver driver = getRemoteWebDriverInstance();
			// nullify implicitlyWait
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Check for the webelements is enabled or not
	 * 
	 * @param
	 * @return boolean If element is enable return true else false.
	 */
	public boolean isEnabled() {
		return getElementByXpath(getXPathLocator()).isEnabled();
	}

	/**
	 * Get the attribute of the webelements.
	 * 
	 * @param String
	 *            The name of the attribute.
	 * @return String The value of the attribute.
	 * 
	 */
	public String getAttribute(String Attribute) {
		return getElementByXpath(getXPathLocator()).getAttribute(Attribute);
	}

	/**
	 * Waits for webelements presence on UI till mintimeout with locator option
	 * specified.
	 * 
	 * @param
	 * @return boolean If element is displayed on UI return true else false.
	 * 
	 */
	public boolean isDisplayed() {
		boolean isElementDisplayed = false;
		try {
			isElementDisplayed = getElementByXpath(getXPathLocator()).isDisplayed();
		} catch (NoSuchElementException ex) {
			isElementDisplayed = false;
		}
		return isElementDisplayed;
	}

	/**
	 * Set Focus on the WebElement
	 * 
	 * @param
	 * 
	 * @return void
	 */
	public void setFocus(String setFocusOn) {
		new Actions(getRemoteWebDriverInstance()).moveToElement(getElementByXpath(getXPathLocator() + setFocusOn)).perform();
	}

	/**
	 * Hover the mouse on the defined webElement
	 * 
	 */
	public void mouseHover() {
		Locatable coordinates = (Locatable) getElementByXpath(getXPathLocator());
		int offset = 15;
		int width = getElementByXpath(getXPathLocator()).getSize().getWidth() / 2;
		int hight = getElementByXpath(getXPathLocator()).getSize().getHeight() / 2;

		Robot robot;
		try {
			robot = new Robot();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			org.openqa.selenium.Dimension browserSize = getRemoteWebDriverInstance().manage().window().getSize();
			int elementOffset = (screenSize.height - browserSize.height) * 2 + offset;
			robot.mouseMove(coordinates.getCoordinates().onPage().x + width, coordinates.getCoordinates().onPage().y + elementOffset
					+ hight);
			WaitTool.sleep(1);
		} catch (AWTException e) {

			log.error("mouseHover", "fail to perform mouseHover operation on element");
			// e.printStackTrace();
		}
	}

	/**
	 * Used to hover on an element of the page
	 * 
	 * @param we
	 *            = The WebElement to hover upon
	 * 
	 */
	public void hoverOnElement(WebElement we) {
		new Actions(getRemoteWebDriverInstance()).moveToElement(we).perform();
	}

	/**
	 * Get the tooltip of the webElement.
	 * 
	 * @return String The tooltip text of the webelement.
	 */
	public String getToolTip() {
		String toolTip = null;
		toolTip = this.getAttribute("title");
		if (toolTip.equals(null)) {
			toolTip = getElementByXpath(getXPathLocator()).getText();
		}
		return toolTip;
	}

	/**
	 * Execute Java Script.
	 * 
	 * @param String
	 *            Java Script that we want to execute on webElement.
	 * @return Object
	 */
	public Object executeJavascript(String script) {
		JavascriptExecutor js = (JavascriptExecutor)getRemoteWebDriverInstance();
		Object output = js.executeScript(script, getElementByXpath(getXPathLocator()));
		return output;

	}

	/**
	 * Execute Java Script.
	 * 
	 * @param String
	 *            Java Script that we want to execute on webElement.
	 * @param Object
	 *            List of Object
	 * @return Object
	 */
	public Object executeJavascript(String script, Object... args) {
		JavascriptExecutor js = (JavascriptExecutor)getRemoteWebDriverInstance();
		Object output = js.executeScript(script, getElementByXpath(getXPathLocator()), args);
		return output;
	}

	/**
	 * Highlight webElement
	 * 
	 * @param
	 * 
	 * @return void
	 */
	public void highlightElement() {
		String originalStyle = getElementByXpath(getXPathLocator()).getAttribute("style");
		executeJavascript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');");
		WaitTool.sleep(2);
		executeJavascript("arguments[0].setAttribute('style', '" + originalStyle + "');");

	}

	/**
	 * Simple method to scroll page using the PAGE_DOWN key press.
	 * 
	 * @param iSleepTime
	 *            - Number of seconds to sleep after scrolling down.
	 * 
	 */
	public void scrollPageDown(int iSleepTime) {
		getElementByXpath("//body").sendKeys(Keys.PAGE_DOWN);
		WaitTool.sleep(iSleepTime);
	}

	/**
	 * Simple method to scroll page using the PAGE_UP key press.
	 * 
	 * @param iSleepTime
	 *            - Number of seconds to sleep after scrolling down.
	 */
	public void scrollPageUp(int iSleepTime) {
		getElementByXpath("//body").sendKeys(Keys.PAGE_UP);
		WaitTool.sleep(iSleepTime);
	}

	/**
	 * Simple method to scroll page down one screen full at a time. Uses a
	 * default wait time of 2 seconds.
	 */
	public void scrollPageDown() {
		this.scrollPageDown(2);
	}

	/**
	 * Simple method to scroll page up one screen full at a time. Uses a default
	 * wait time of 2 seconds.
	 */
	public void scrollPageUp() {
		this.scrollPageUp(2);
	}

	/**
	 * Method that can be called to help keep UI from hitting session timeouts.
	     use this call periodically to help keep your UI from timing out.
	 * 
	 * @param xPath
	 *            - xPath string to a known element in context
	 */
	public void keepUiAlive(String xPath) {
		if (isElementPresent(xPath, 1)) {
			log.info("keepUiAlive", "Clicking on element to keep UI active: " + xPath);
			getElementByXpath(xPath).click();
		} else {
			log.warn("keepUiAlive", "Clicking on element failed: " + xPath);
			this.keepUiAlive();
		}
	}

	/**
	 * Method that can be called to help keep UI from hitting session timeouts.
	 * use this call periodically to help keep your UI from timing out.
	 */
	public void keepUiAlive() {
		log.info("keepUiAlive", "Clicking on known safe element to keep UI active...");
		getElementByXpath("//*[@id='banner_upper']/div/span").click();
	}

	/**
	 * Sleep for milliseconds (instead of whole seconds).
	 * 
	 * @param milliseconds
	 *            - Number of milliseconds to sleep
	 */
	private void sleepMsec(long milliseconds) {
		try {
			Thread.sleep((milliseconds >= 0) ? (milliseconds) : (-milliseconds));
		} catch (InterruptedException ignored) {
		}
	}

	/**
	 * Method used to help wait for the UI to be stable. Especially needed for
	 * some tests executed on Chrome browser. This will wait the min time
	 * configured in config.properties file.
	 * 
	 * @param locator
	 *            Xpath locator to find a WebElement
	 * @return A stable WebElement matching the identified locator.
	 */
	protected WebElement waitForFreezeCoordinates(final String locator) {
		return waitForFreezeCoordinates(locator, config.mintimeout);
	}

	/**
	 * Method used to help wait for the UI to be stable. Especially needed for
	 * some tests executed on Chrome browser.
	 * 
	 * @param locator
	 *            Xpath locator to find a WebElement
	 * @param timeoutSeconds
	 *            Number of whole seconds to wait.
	 * @return A stable WebElement matching the identified locator.
	 */
	private WebElement waitForFreezeCoordinates(final String locator, long timeoutSeconds) {
		long beginMsec = System.currentTimeMillis();
		log.info("waitForFreezeCoordinates", "timeoutSeconds=[" + timeoutSeconds + "]");
		Point currentPos = getElementByXpath(locator).getLocation();
		Point previousPos = getElementByXpath(locator).getLocation();
		boolean isMoving = true;
		long timeDelta = System.currentTimeMillis() - beginMsec;
		do {
			sleepMsec(timeDelta < (1000 * timeoutSeconds - timeDelta) ? timeDelta : (1000 * timeoutSeconds - timeDelta));
			timeDelta = System.currentTimeMillis() - beginMsec;
			previousPos = currentPos;
			currentPos = getElementByXpath(locator).getLocation();
			isMoving = !((previousPos.getX() == currentPos.getX()) && (previousPos.getY() == currentPos.getY()));
			log.info("waitForFreezeCoordinates", String.format("isMoving=[%s] previousPos[%s]->currentPos[%s],timeDelta=[%s],timeOut=[%s]",
					isMoving, previousPos, currentPos, timeDelta, 1000 * timeoutSeconds));
		} while (isMoving && timeDelta < 1000 * timeoutSeconds);

		long endMsec = System.currentTimeMillis();
		log.info("waitForFreezeCoordinates", " done in [" + (endMsec - beginMsec) + "] milliseconds");
		return getElementByXpath(locator);
	}

	/**
	 * Wait for element to appear and be in editable state
	 * 
	 * @param locator
	 *            locator of element to wait for
	 * @return returns webElement object on success

	 */
	protected WebElement waitForClickable(final String locator) {
		return waitForClickable(locator, config.mintimeout);
	}

	/**
	 * Wait for element to appear and be in editable state
	 * 
	 * @param locator
	 *            locator of element to wait for
	 * @param timeOutInSeconds
	 *            timeout to wait element in seconds
	 * @return returns webElement object on success
	 */
	protected WebElement waitForClickable(final String locator, long timeOutInSeconds) {
		/*
		 * Sometimes in google chrome the element that is being rendered is
		 * reported as clickable while being animated even before element
		 * coordinates freezes on the page. When attempting to clicking such
		 * element the following exception may appear:
		 * "Element is not clickable at point"
		 * 
		 * details here
		 * https://groups.google.com/forum/#!topic/selenium-developer
		 * -activity/-MiCZEWNZ_s
		 * 
		 * In order to provide workaround we have to monitor element coordinates
		 * and consider element as clickable only after coordinates stop
		 * changing.
		 */
		new WebDriverWait(getRemoteWebDriverInstance(), timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
		return waitForFreezeCoordinates(locator, timeOutInSeconds);
	}

	/**Method to wait for page with the title passed in the argument.
	 * 
	 * @param pageTitle - title of the page for which the method has to wait to appear
	 * @param timeout - timeout in seconds
	 */
	public void waitForPageWithTitle(String pageTitle, int timeout){
		try{
			WebDriverWait wait = new WebDriverWait(getRemoteWebDriverInstance(), timeout);
			wait.until(ExpectedConditions.titleContains(pageTitle));
			log.info("Waiting for page ", "waitForPageWithTitle");
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while waiting for page ", "waitForPageWithTitle");
		}
	}

	/**Method to get title of the page.
	 * 
	 * @return returns title of the page which is a string
	 */
	public String getPageTitle(){
		String pageTitle = null;
		try{
			pageTitle = getRemoteWebDriverInstance().getTitle();
			System.out.println(pageTitle);
			log.info("Current page title is ","getPageTitle");
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while getting current URL","getPageTitle");
		}
		return pageTitle;
	}

	/**
	 * 
	 * @param element
	 * @param key
	 */
	public void enterKey(WebElement element,String key){
		try{
			if(element==null)
				throw new NoSuchElementException("Element not located");
			element.sendKeys(key);
			log.info("Entered text <"+key+"> on WebElement "+ element, "enterKey");
		}catch(NoSuchElementException e){

			log.error("Error while performing click. Unable to click on NULL WebElement","enterKey");
		}catch(ElementNotVisibleException e){

			log.error("Error while typing text on WebElement "+element+". Element not visile", "enterKey");
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while typing text on WebElement " + element, "enterKey");
		}	
	}

	/**
	 * 
	 * @param element
	 */
	public void clickOnElement(WebElement element){
		try{
			if(element==null)
				throw new NoSuchElementException("Element not located");
			WebDriverWait wait = new WebDriverWait(getRemoteWebDriverInstance(), 60);
			wait.until(ExpectedConditions.visibilityOf(element));
			element.click();
			log.info("Click performed on WebElement "+ element, "clickOnElement");
		}catch(NoSuchElementException e){
			e.printStackTrace();
			log.error("Error while performing click. Unable to click on NULL WebElement" , "clickOnElement");
		}catch(ElementNotVisibleException e){
			e.printStackTrace();
			log.error("Error while performing click on WebElement "+element+". Element not visile", "clickOnElement");
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error encountered while performing click" , "clickOnElement");
		}	
	}
	/**
	 * 
	 * @param windowTitle
	 * @param timeout
	 * @return
	 */
	public boolean switchToWindow(String windowTitle, int timeout){
		boolean result = false;
		try{
			int counter=1;
			OUTER :while(counter<timeout){
				for(String winHandle : getRemoteWebDriverInstance().getWindowHandles()){
					getRemoteWebDriverInstance().switchTo().window(winHandle);
					if(getRemoteWebDriverInstance().getTitle().equals(windowTitle)){
						System.out.println("Window title is " + windowTitle);
						log.info("Window found with title "+windowTitle, "switchToWindow");
						result = true;
						break OUTER;
					}
				}
				counter++;
				Thread.sleep(counter*1000);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("No Window found matching "+windowTitle+" within timeout "+timeout+" seconds", "switchToWindow");
		}
		return result;
	}

	public void quitDriver(){
		WebDriver driver = getRemoteWebDriverInstance();
		try {
			if(driver!=null){
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	   
	}

}
