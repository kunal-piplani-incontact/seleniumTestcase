package com.nice.incontact.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.GridHubConfiguration;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.web.Hub;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.SeleniumLogger;

/**
 * This class helps to load Hub Grid and registers Node to Grid Holds the Hub,
 * Node and Selenium Remote to be up and running called from TestClient.java.
 * 
 */

public class ConfigureSeleniumHub {
	private static final SeleniumLogger log = new SeleniumLogger(ConfigureSeleniumHub.class);
    private static String hubHost = "";
    private static int hubPort;
    private static String browser = "";
    private static String platform = "";
    private String version = "";
    private int instances = 0;
    private static int timeOut = 0;
    private static int browserTimeout = 0;
    private static int nodeport = 0;
    private static int cleanupcycletimeout = 0;
    private static int sessions = 0;
    private int wdPort = 0;
    static SelfRegisteringRemote remote = null;
    static Hub hub = null;

    public ConfigureSeleniumHub(ConfigLoader config) {
        // TODO Auto-generated constructor stub
        setBrowser(config.browser);
        setHubHost(config.hubhost);
        setHubPort(config.hubport);
        setVersion(config.browserversion);
        setInstances(config.maxinstances);
        setHubTimeout(config.hubtimeout);
        setPlatform(config.platform);
        setNodePort(config.nodeport);
        setCleanupCycletimeout(config.cleanuptimeout);
        setBrowserTimeOut(config.browsertimeout);
        setwdPort(config.wdPort);
    }

    // /Setting Desired Capabilities for Hub
    private void setBrowser(String Browser) {
        if (Browser.toLowerCase().contains("internetexplorer") || Browser.toLowerCase().contains("iexplorer")
                || Browser.toLowerCase().contains("ie"))
            Browser = "internet explorer";
        browser = Browser;
    }

    private void setPlatform(String Platform) {
        platform = Platform;
    }

    private void setVersion(String Version) {
        this.version = Version;
    }

    private void setBrowserTimeOut(int browserTimeOut) {
        browserTimeout = browserTimeOut;
    }

    private void setHubPort(int HubPort) {
        hubPort = HubPort;
    }

    private void setHubHost(String HubHost) {
        hubHost = HubHost;
    }

    private void setInstances(int Instances) {
        this.instances = Instances;
    }

    private void setNodePort(int Port) {
        nodeport = Port;
    }

    private void setHubTimeout(int TimeOut) {
        timeOut = TimeOut;
    }

    private void setCleanupCycletimeout(int cleanuptimeout) {
        cleanupcycletimeout = cleanuptimeout;
    }

    private void setSessions(int sessionCount) {
        sessions = sessionCount;
    }

    private void setwdPort(int wdPortfromConfig) {
        wdPort = wdPortfromConfig;
    }

    GridHubConfiguration gridconfig = new GridHubConfiguration();

    @SuppressWarnings("static-access")
    public void startHub()

    {
        RemoteControlConfiguration con = new RemoteControlConfiguration();
        con.setBrowserTimeoutInMs(30000);
        con.setSingleWindow(true);

        log.debug("StartHub", "Starting Hub with host -" + hubHost + " port -" + hubPort);
        gridconfig.setHost(this.hubHost);
        gridconfig.setPort(this.hubPort);
        gridconfig.setTimeout(this.timeOut);
        gridconfig.setCleanupCycle(this.cleanupcycletimeout);
        gridconfig.setBrowserTimeout(this.browserTimeout);
        String[] cmd = { "-browserName firefox", "-platform ANY" };
        gridconfig.loadFromCommandLine(cmd);
        gridconfig.setNewSessionWaitTimeout(40000);

        gridconfig.loadDefault();
        hub = new Hub(gridconfig);
        try {
            log.debug("StartHub", "Calling hub.start ....");
            hub.start();
            log.debug("StartHub", "Hub started successfully....");
        } catch (Exception e) {
            log.error("StartHub", "Exception occured while starting hub - " + e.getMessage());
        }
    }

    public void registerNode() throws MalformedURLException {
        log.debug("registerNode", "Registering Node to hub.......");

        RegistrationRequest req = new RegistrationRequest();
        log.debug("registerNode", "Setting Grid Role to Node.......");
        req.setRole(GridRole.NODE);
        Map<String, Object> nodeConfiguration = new HashMap<String, Object>();
        nodeConfiguration.put(RegistrationRequest.AUTO_REGISTER, true);
        nodeConfiguration.put(RegistrationRequest.HUB_HOST, hubHost);
        nodeConfiguration.put(RegistrationRequest.HUB_PORT, hubPort);
        nodeConfiguration.put(RegistrationRequest.PORT, nodeport);
        URL remoteURL = new URL("http://" + hubHost + ":" + nodeport);
        nodeConfiguration.put(RegistrationRequest.PROXY_CLASS, "org.openqa.grid.selenium.proxy.DefaultRemoteProxy");
        nodeConfiguration.put(RegistrationRequest.MAX_SESSION, 2);
        nodeConfiguration.put(RegistrationRequest.CLEAN_UP_CYCLE, cleanupcycletimeout);
        nodeConfiguration.put(RegistrationRequest.REMOTE_HOST, remoteURL);
        nodeConfiguration.put(RegistrationRequest.MAX_INSTANCES, 2);
        nodeConfiguration.put(RegistrationRequest.BROWSER, browser);
        nodeConfiguration.put(RegistrationRequest.PLATFORM, platform);
        nodeConfiguration.put(RegistrationRequest.VERSION, "30.0");
        nodeConfiguration.put(RegistrationRequest.NODE_POLLING, 50000);
        nodeConfiguration.put(RegistrationRequest.TIME_OUT, timeOut);
        nodeConfiguration.put(RegistrationRequest.BROWSER_TIME_OUT, 20000);
        nodeConfiguration.put(RegistrationRequest.SELENIUM_PROTOCOL, "org.openqa.selenium.remote.WebDriver");
        // nodeConfiguration.put(RegistrationRequest.MAX_TESTS_BEFORE_CLEAN,1);
        req.setConfiguration(nodeConfiguration);
        remote = new SelfRegisteringRemote(req);

        try {
            SeleniumServer server;
            remote.startRemoteServer();
            remote.startRegistrationProcess();
            RemoteControlConfiguration conf = new RemoteControlConfiguration();
            conf.setPort(wdPort);
            conf.setDebugURL("/wd/hub");
            server = new SeleniumServer(conf);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deregisterNode() {
        if (remote != null) {
            remote.deleteAllBrowsers();
            remote.stopRemoteServer();
        }

    }

    public void stopHub() {
        try {
            hub.stop();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // defining no of valid instances
    private int getInstanceCount() {
        int instancecount = 0;

        if (browser.toLowerCase().equals("firefox") || browser.toLowerCase().equals("ff")) {
            if (instances <= 5 && instances > 0) {
                instancecount = instances;
            } else {
                instancecount = 5;
            }
        } else if (browser.toLowerCase().equals("ie") || browser.toLowerCase().equals("internetexplorer")) {
            if (instances == 1) {
                instancecount = instances;
            } else {
                instancecount = 1;
            }
        } else if (browser.toLowerCase().equals("chrome")) {
            if (instances <= 5 && instances > 0) {
                instancecount = instances;
            } else {
                instancecount = 5;
            }
        }
        return instancecount;
    }

}
