package com.nice.incontact.client;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.nice.incontact.core.ConfigureSeleniumHub;
import com.nice.incontact.core.Driver;
import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.ResponseTimer;
import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.ui.common.CommonFunctions;
import com.nice.incontact.ui.common.Screenshot;

@Listeners({ com.nice.incontact.ui.common.Screenshot.class})
public class TestClient {
	private static final SeleniumLogger log = new SeleniumLogger(TestClient.class);
	private static ConfigLoader config = new ConfigLoader();
	ConfigureSeleniumHub seleniumhub = null;
	RemoteWebDriver remoteWebDriver = null;
	protected ResponseTimer responseTimer = null;
	private final String iniFileDir = "c:/Selenium";
	private final String iniFileName = "SeleniumTestCaseMetadata.ini";
	private final long noOfDays = 7;

	@BeforeSuite
	/**
	 * This method is used to configure selenium hub.
	 * After configuration start's hub and register node.
	 * 
	 * @return      don't have return value.
	 */
/*	protected void initHub() throws MalformedURLException {
		seleniumhub = new ConfigureSeleniumHub(config);
		seleniumhub.startHub();
		seleniumhub.registerNode();
		// Setting data for Hub and Nodes

		// delete ini file if exists
		File iniFile = new File(iniFileDir + "/" + iniFileName);
		if (iniFile.exists())
			iniFile.delete();

	}

	@BeforeClass
	*//**
	 * This method is used to initialize the Web driver.
	 * in case, if the @BeforeClass in UT needs the driver instance, this initialization will be required.
	 * This will have the driver initialized even if it is not required e.g. for CLI, REST tests.
	 * It also initializes the Response timer.
	 *//*
	protected void initialize() {
		try {
			setupWebDriver();
		} catch (MalformedURLException e) {
			log.error("initializeDriver", "Driver initialization failed.", e);
		}
		this.responseTimer = new ResponseTimer(config);
	}

	*//**
	 * <p>
	 * Enum which holds types of Browser.
	 *//*
	enum BROWSER {
		Firefox, IE, Chrome
	}

	*//**
	 * Based on browser type specified in the configurations, the BROWSER value
	 * will be returned.
	 * 
	 * @param browser
	 * @return
	 *//*
	private BROWSER setBrowser(String browser) {
		if (browser.equalsIgnoreCase("firefox") || browser.equalsIgnoreCase("ff"))
			return BROWSER.Firefox;
		else if (browser.equalsIgnoreCase("InternetExplorer") || browser.endsWith("ie"))
			return BROWSER.IE;
		else if (browser.equalsIgnoreCase("Chrome") || browser.equalsIgnoreCase("cr"))
			return BROWSER.Chrome;
		else
			return BROWSER.Firefox;
	}


	*//**
	 * Sets up capabilities as per the browser and initializes web driver.
	 * 
	 * @throws MalformedURLException
	 *//*
	private void setupWebDriver() throws MalformedURLException {
		if (remoteWebDriver == null) {
			log.debug("setupWebDriver", "The remoteWebDriver Object is null, so getting Driver Object from Driver Class.");
			if (config == null) {
				log.error("setupWebDriver", "Failed to initialize the configuration details.");
				return;
			}
			DesiredCapabilities capabilities = null;
			try {

				switch (this.setBrowser(config.browser)) {
				case Firefox:
					log.debug("setupWebDriver", "Browser Firefox is requested by user ....");
					capabilities = DesiredCapabilities.firefox();
					break;
				case IE:
					log.debug("setupWebDriver", "Browser IE is requested by user ....");
					System.setProperty("webdriver.ie.driver", config.selenium_iedriver);
					capabilities = DesiredCapabilities.internetExplorer();
					break;
				case Chrome:
					log.debug("setupWebDriver", "Browser Chrome is requested by user ....");
					System.setProperty("webdriver.chrome.driver", config.selenium_chromedriver);
					capabilities = DesiredCapabilities.phantomjs();
					break;
				default:
					log.error("setupWebDriver", "Browser is not specified!!! Firefox will be used ");
					capabilities = DesiredCapabilities.firefox();
				}

				remoteWebDriver = Driver.initializeDriver(new URL("http://" + config.hubhost + ":" + config.hubport),
						capabilities);

			} catch (Exception e) {
				e.printStackTrace();
				log.error("setupWebDriver", "Failed to initialize the Selenium RemoteWebDriver.");
			}
		}

	}*/

	/**
	 * This method will be called in each test to initialize - protocol, host,
	 * port, admin URI.
	 * 
	 * @return {@link CLMCommon} object
	 */
	protected CommonFunctions createCommonFunctions() {
		return new CommonFunctions(config.protocol, config.host, config.port, config.centraluri);
	}

/*	*//**
	 * Quits the web driver session if it exists.
	 *//*
	private void quitWebDriver() {
		log.debug("teardown", "shutting down driver references and quitting application...");
		if (remoteWebDriver != null) {
			log.debug("teardown", "Quiting WebDriver Instance...");
			remoteWebDriver.quit();
		}
	}


	@AfterClass
	protected void tearDown() {
		Driver.deinitializeDriver();
		quitWebDriver();
		responseTimer.closeCSVFile();
		Screenshot.deleteOlderScreenshot(noOfDays, ".jpeg");
	}*/
}

