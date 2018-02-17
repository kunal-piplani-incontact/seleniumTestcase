package com.nice.incontact.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.nice.incontact.util.SeleniumLogger;

public class ConfigLoader {
	public Properties config = null;
    public FileInputStream fis = null;
    public String cluster = "";
    public String host = "";
    public String port = "";
    public String protocol = "";
    public String centraluri = "";
    public String browser = "";
    public String locale = "";
    public String testdatafile = "";
    public String selenium_iedriver = "";
    public String selenium_chromedriver = "";
    public boolean measureResponseTime;
    
    public int mintimeout;
    public int avgtimeout;
    public int maxtimeout;
    public int loadTimeout;
    
    // file Server related
    
    public String fileServerHost = "";
    public String userName = "";
    public String password = "";
    
    // Hub related variables
    public String hubhost = "";
    public int hubport = 0;
    public String platform = "";
    public int maxinstances = 0;
    public int nodeport = 0;
    public int browsertimeout = 0;
    public int cleanuptimeout = 0;
    public String browserversion = "";
    public int hubtimeout = 0;
    public int wdPort = 0;

    
    //Screenshot related
    public Boolean takeScreenshot = false;

    private static final SeleniumLogger logger = new SeleniumLogger(ConfigLoader.class);

    public ConfigLoader() {
        try {
            config = new Properties();
            fis = new FileInputStream(System.getProperty("user.dir") + "/config/config.properties");
            config.load(fis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        populateConfigData();
    }

    /*
     * 
     * @usage :getConfigValue("key")
     * 
     * @description:This method retrieves configuration data from config.ini
     * based on key passed
     * 
     * @return : String value of Key
     * 
     * @throws IllegalArgumentException
     */
    private String getConfigValue(String Key) {
        String sConfigValue = "";
        sConfigValue = config.getProperty(Key);
        if (sConfigValue == null) {
            throw new IllegalArgumentException("Unable to read Key from config.properties file: " + Key);
        }
        return sConfigValue.trim();
    }

    /*
     * 
     * @return : Boolean
     */
    private Boolean getBoolean(String Key) {
        Boolean flag = false;
        flag = Boolean.parseBoolean(this.getConfigValue(Key));
        return flag;
    }

    /*
     * @return : return integer value
     */
    private int getIntValue(String key) {
        return Integer.parseInt(this.getConfigValue(key));
    }

    /* 
     * @return : void
     * 
     * @description:This method helps on holding data from config file to a
     * variable
     */
    public void populateConfigData() {
    	cluster = getConfigValue("cluster");
        host = getConfigValue("host");
        port = getConfigValue("port");
        protocol = getConfigValue("protocol");
        centraluri = getConfigValue("centraluri");
        browser = getConfigValue("browser");
        locale = getConfigValue("locale");
        testdatafile = getConfigValue("testdatafile");
        selenium_iedriver = System.getProperty("user.dir") + getConfigValue("selenium_iedriver");
        selenium_chromedriver = System.getProperty("user.dir") + getConfigValue("selenium_chromedriver");
       
        // Screenshot related
        takeScreenshot = getBoolean("takeScreenshot");
        measureResponseTime = getBoolean("measureResponseTime");

        mintimeout = getIntValue("mintimeout");
        avgtimeout = getIntValue("avgtimeout");
        maxtimeout = getIntValue("maxtimeout");
        loadTimeout = getIntValue("loadTimeout");
        
        // File Server Related
        
        fileServerHost = getConfigValue("fileServerHost");
        userName = getConfigValue("userName");
        password = getConfigValue("password");

     // HUB related
        hubhost = getConfigValue("hubHost");
        hubport = getIntValue("hubPort");
        maxinstances = getIntValue("maxinstances");
        platform = getConfigValue("platform");
        nodeport = getIntValue("nodePort");
        browsertimeout = getIntValue("browserTimeout");
        cleanuptimeout = getIntValue("cleanupcycle");
        browserversion = getConfigValue("browserversion");
        hubtimeout = getIntValue("hubtimeout");
        wdPort = getIntValue("wdPort");
    }
}
