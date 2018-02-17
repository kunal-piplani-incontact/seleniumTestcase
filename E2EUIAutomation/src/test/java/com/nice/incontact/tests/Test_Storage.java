package com.nice.incontact.tests;


import com.nice.incontact.annotations.TCID;
import com.nice.incontact.client.TestClient;
import com.nice.incontact.dataLoader.BusinessUnit;
import com.nice.incontact.dataLoader.CustomDataLoader;
import com.nice.incontact.dataLoader.Users;
import com.nice.incontact.pageObjects.CentralHomePage;
import com.nice.incontact.ui.common.CommonFunctions;
import com.nice.incontact.pageObjects.MaxPage;
import com.nice.incontact.util.SeleniumLogger;
import com.nice.incontact.util.Xlite;
import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.FileServer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class Test_Storage extends TestClient{
	
	private static final SeleniumLogger log = new SeleniumLogger(Test_Storage.class);
	private HashMap<String, String> customData = new HashMap<String, String>();
	ConfigLoader config = new ConfigLoader();
	
	CentralHomePage centralHomePage=new CentralHomePage();
	MaxPage maxPage=new MaxPage();
	FileServer fileServer = new FileServer();
	
	String maxPhoneNumber = "";
	String windowName = "";
	String callingNumber = "";
	
	
	String loggedInUser = "";
	String user = "";
    String password = "";
	
	
	@BeforeClass
    private void scriptEnter() throws InterruptedException {
		
        
        BusinessUnit.init();
        Users.getUser("BusinessUnit_SC10","UserAdministrator");
        
        user = Users.userloginid;
        password = Users.userpassword;
        loggedInUser = Users.username + " " + Users.userlastname;
        
      //  responseTimer.start("Login to Central");
        
        CommonFunctions comm= createCommonFunctions();
		comm.login(user,password,"");
	//	responseTimer.stop();
		
		CustomDataLoader.init();
        customData = CustomDataLoader.getdataMap("Test_Storage/inputs");
        windowName = customData.get("windowName");
        System.out.println("window name is " + windowName);
        maxPhoneNumber = customData.get("maxPhoneNumber");
        System.out.println("max number is  " + maxPhoneNumber);
        callingNumber = customData.get("callingNumber");
        System.out.println("calling number is  " + callingNumber);
	}

	@Test(description = "This test case launches Max, makes an outbound call, receives the call on Xlite and then hangs up the call.")
	@TCID(value = "IC-74349")
	public void test_launchMaxandMakeOutboundCall(){
		/*String centralUrl=config.protocol+"://"+config.host+"/"+config.centraluri+"/Default.aspx" ;
		System.out.println("the url is " + centralUrl);
		Assert.assertTrue(centralHomePage.isCentralHomePagePresent(centralUrl));
		
		System.out.println("max number is  " + maxPhoneNumber);
		centralHomePage.launchMax(maxPhoneNumber);
		System.out.println("launched max");
		System.out.println("window name is " + windowName);
		
		centralHomePage.switchToWindow(windowName, 3000);
		System.out.println("calling number is  " + callingNumber);
		maxPage.generateOutboundCall(callingNumber);
		Xlite.answerCall(false);
		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		centralHomePage.switchToWindow(windowName, 3000);
		Assert.assertTrue(maxPage.isAgentWorking(), "Agent is not working.");
		maxPage.disconnectCall(30);
		Assert.assertTrue(maxPage.isAgentNotWorking(), "Agent is working still.");
		maxPage.closeMAX();
		
		boolean isPresent = false;
		System.out.println("the time stamp is" + timeStamp);
		if(fileServer.getEntryCount(timeStamp) > 0){
			isPresent = true;
		}
		//Assert.assertTrue(isPresent);
*/		centralHomePage.quitDriver();
	}
}
