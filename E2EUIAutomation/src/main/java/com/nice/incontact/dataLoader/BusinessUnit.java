package com.nice.incontact.dataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.SeleniumLogger;

public class BusinessUnit {
	 public static String testdatafile = null;
	    private static ConfigLoader config = null;
	    static XmlParser xmlparser = null;
	    static HashMap<String, Object> budata = null;
	    public static String buKey = "";
	    public static String buName = "";
	    public static Users users;
	    private static HashMap<String, Object> busMap = null;
	    private static final SeleniumLogger log = new SeleniumLogger(BusinessUnit.class);

	    /**
	     * @purpose :This method is to initialize configuration loader,parse and
	     *          load BU data from test-data Xml
	     * @usage :BusinessUnit.init() to be called before using BU data
	     */
	    public static void init() {
	        config = new ConfigLoader();
	        testdatafile = System.getProperty("user.dir") + "\\src\\main\\resources\\" + config.locale + "\\" + config.testdatafile;
	        xmlparser = new XmlParser(config.cluster);
	        budata = (HashMap<String, Object>) xmlparser.parseXML(testdatafile.toString(), "BusinessUnit_DATA");
	        busMap = getBusinessUnits(budata);
	    }

	    /**
	     * @purpose :This api will load data for specific BU
	     * @param businessUnit
	     *            : name of BU for which data is to be retrieved
	     * @usage :BusinessUnit.loadBusinessUnit("myBU")
	     */
	    public static void loadBusinessUnit(String businessUnit) {
	        HashMap<String, Object> singleBUMap = null;
	        singleBUMap = getBUData(businessUnit);
	    }

	    /*
	     * @purpose : This api will fetch all BUs from node BusinessUnit_DATA node
	     */
	    @SuppressWarnings({ "unchecked" })
	    private static HashMap<String, Object> getBusinessUnits(HashMap<String, Object> budata) {
	        HashMap<String, Object> businessUnitsMap = new HashMap<String, Object>();
	        HashMap<String, Object> businessUnits = null;
	        businessUnits = (HashMap<String, Object>) budata.get("BusinessUnit_DATA");
	        if (businessUnits.isEmpty()) {
	            log.info("getBusinessUnitss", "Data not not laoded for business units.");
	        } else {
	            Set<String> keys = businessUnits.keySet();
	            for (String key : keys) {
	                HashMap<String, Object> testData = new HashMap<String, Object>();
	                testData = (HashMap<String, Object>) businessUnits.get(key);
	                businessUnitsMap.put(key, testData);
	            }
	        }
	        return businessUnitsMap;
	    }

	    @SuppressWarnings("unchecked")
	    public static HashMap<String, Object> getBUData(String BUName) {
	        HashMap<String, Object> filteredBU = null;
	        try {
	            HashMap<String, Object> myBU = busMap;
	            filteredBU = new HashMap<>();
	            Set<String> keys = myBU.keySet();
	            for (String key : keys) {
	                if (key.toString().equalsIgnoreCase(BUName)) {
	                	filteredBU = (HashMap<String, Object>) myBU.get(key);
	                    break;
	                }
	            }
	        } catch (Exception ex) {
	            ex.getStackTrace();
	        }
	        return filteredBU;
	    }

	    @SuppressWarnings("unchecked")
	    public static ArrayList<Object> getBUList() {
	        ArrayList<Object> buList = new ArrayList<>();

	        HashMap<String, Object> busniessUnits = null;
	        busniessUnits = (HashMap<String, Object>) budata.get("BusinessUnit_DATA");
	        if (busniessUnits.isEmpty()) {
	            log.info("getBUList", "Data not not laoded for BUs.");
	        } else {
	            Set<String> keys = busniessUnits.keySet();
	            for (String key : keys) {
	                HashMap<String, Object> busMap = new HashMap<String, Object>();
	                HashMap<String, Object> testData = new HashMap<String, Object>();
	                testData = (HashMap<String, Object>) busniessUnits.get(key);

	                busMap.put(key, testData);
	                buList.add(busMap);

	            }
	        }
	        return buList;

	    }

	    @SuppressWarnings("unchecked")
	    public static HashMap<String, Object> getBUData1(String BUName) {
	        ArrayList<Object> buList = getBUList();
	        HashMap<String, Object> filteredbu = null;
	        try {
	            for (Object bu : buList) {
	                HashMap<String, Object> buObj = (HashMap<String, Object>) bu;
	                if (buObj.containsKey(BUName)) {
	                	filteredbu = (HashMap<String, Object>) buObj.get(BUName);
	                    break;
	                }
	            }

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        return filteredbu;
	    }

	    @SuppressWarnings("unchecked")
	    public static void loadBU(Object BUMap) {

	        HashMap<String, Object> singleBUMap = null;

	        singleBUMap = (HashMap<String, Object>) BUMap;

	        Set<String> keys = singleBUMap.keySet();
	        for (String key : keys) {
	            HashMap<String, Object> BU = (HashMap<String, Object>) singleBUMap.get(key);
	            buKey = (String) key;
	            buName = (String) BU.get("BUName");
	        }

	    }

}
