package com.nice.incontact.dataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.nice.incontact.dataLoader.XmlParser;
import com.nice.incontact.util.ConfigLoader;
import com.nice.incontact.util.SeleniumLogger;

/**
 * CustomDataLoader Class can be used to load custom data for particular test
 * cases.
 * 
 * @author nmorolia
 * 
 */
public class CustomDataLoader {

    private static final SeleniumLogger log = new SeleniumLogger(CustomDataLoader.class);
    private static ConfigLoader config = null;
    static XmlParser xmlparser = null;
    private static String customDataFileName = null;
    private static ArrayList<Object> parameterList = new ArrayList<>();
    private static HashMap<String, Object> rootNodeData = new HashMap<String, Object>();
    private static final HashMap<String, Object> map = new HashMap<String, Object>();
    private static HashMap<String, Object> customDataValues = null;
    private static ArrayList<Object> paramValues = new ArrayList<>();
    private static HashMap<String, Object> param = new HashMap<String, Object>();
    private static HashMap<String, Object> actualNodeValues = new HashMap<String, Object>();

    /**
     * @purpose :This method is to initialize configuration loader,parse and
     *          load data till the custom data node from test-data Xml.
     * @usage :CustomDataLoader.init() to be called before using custom data.
     */

    public static void init() {
        log.info("init", "Initializing CustomDataLoader...");
        config = new ConfigLoader();
        customDataFileName = System.getProperty("user.dir") + "\\src\\main\\resources\\" + config.locale + "\\" + config.testdatafile;
        xmlparser = new XmlParser(config.cluster);
    }

    /**
     * This method is for obtaining data from specific test nodes. This will
     * return Map containing Name-Value pair where Name is TestNodeName and
     * value is TestDataValue
     * 
     * @param tagXpath
     *            An xpath to the data elements
     * @return resultMap A Hashmap containing the desired values read from XML
     *         nodes
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getdataMap(String tagXpath) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (tagXpath.trim().isEmpty() || tagXpath.trim() == null) {
            throw new IllegalArgumentException("Xpath(Parameter) for data cannot be empty or null");
        } else {
            String[] mydata = null;

            // Parse nodes by using forward slash (preferred)
            if (tagXpath.contains("/")) {
                mydata = tagXpath.split("/");
            } else { // Parse by using back slashes
                mydata = tagXpath.split("'\'");
            }

            if (customDataFileName == null) {
                customDataFileName = System.getProperty("user.dir") + "\\src\\main\\resources\\" + config.locale + "\\"
                        + config.testdatafile;
            }

            log.debug("getdataMap", "Attempting to build hashmap based on file '" + customDataFileName + "' and Node = " + mydata[0]);

            HashMap<String, Object> test = (HashMap<String, Object>) xmlparser.parseXML(customDataFileName, mydata[0]);
            if (test.isEmpty()) {
                throw new IllegalArgumentException("Failed to build the hashmap based on input XML path. Please check the configuration.");
            } else if (mydata.length > 1) {
                for (int i = 1; i < mydata.length; i++) {
                    test = (HashMap<String, Object>) test.get(mydata[i - 1]);
                    if (i == mydata.length - 1) {
                        if (test.get(mydata[i]) == null || test.get(mydata[i]).toString().isEmpty()) {
                            throw new IllegalArgumentException("No Test Data Node found for the given XPath !");
                        } else {
                            resultMap = (HashMap<String, String>) test.get(mydata[i]);
                        }
                    }
                }
            } else {
                resultMap = (HashMap<String, String>) test.get(mydata[0]);
            }
        }
        return resultMap;
    }

    /**
     * This method is for obtaining data from specific test nodes for service
     * request. This will return Map containing Name-Value pair where Name is
     * TestNodeName and value is TestDataValue
     * 
     * @param tagXpath
     *            An xpath to the data elements
     * @return map A Hashmap containing the desired values read from XML nodes
     */
    @SuppressWarnings("unchecked")
    private static HashMap<String, Object> getRootNodeData(String tagXpath) {
        init();
        if (tagXpath.trim().isEmpty() || tagXpath.trim() == null) {
            throw new IllegalArgumentException("Xpath(Parameter) for data cannot be empty or null");
        } else {
            String[] mydata = null;

            // Parse nodes by using forward slash (preferred)
            if (tagXpath.contains("/")) {
                mydata = tagXpath.split("/");
            } else { // Parse by using back slashes
                mydata = tagXpath.split("'\'");
            }

            if (customDataFileName == null) {
                customDataFileName = System.getProperty("user.dir") + "\\src\\main\\resources\\" + config.locale + "\\"
                        + config.testdatafile;
            }

            log.debug("getServiceRequestData", "Attempting to build hashmap based on file '" + customDataFileName + "' and Node = "
                    + mydata[0]);

            rootNodeData = (HashMap<String, Object>) xmlparser.parseXML(customDataFileName, mydata[0]);
            if (rootNodeData.isEmpty()) {
                throw new IllegalArgumentException("Failed to build the hashmap based on input XML path. Please check the configuration.");
            } else if (mydata.length > 1) {
                for (int i = 0; i < mydata.length; i++) {
                    rootNodeData = (HashMap<String, Object>) rootNodeData.get(mydata[i]);
                }
            }
			
			  map.put("rootNodeData", rootNodeData);

        }
        return map;
    }

    /**
     * This method is for obtaining/reading data from root node like option
     * choice and parameters. This will return array of parameter lists.
     * 
     * @param parentNodeName
     *            An xpath to the data elements
     * @param childNodeName
     *            An xpath to the child data elements
     * @return parameterList Array list containing the desired values.
     */
    public static ArrayList<Object> getParameters(String parentNodeName, String childNodeName) {
        if (rootNodeData.isEmpty()) {
            throw new IllegalArgumentException("Failed to build the hashmap based on input XML path. Please check the configuration.");
        } else {
            if (rootNodeData.containsKey(parentNodeName)) {
                HashMap<String, Object> paramMap = (HashMap<String, Object>) rootNodeData.get(parentNodeName);
                Set<String> paramChoiceSet = paramMap.keySet();
                for (String paramKey : paramChoiceSet) {

                    if (paramKey.contains(childNodeName)) {
                        HashMap<String, Object> parameterMap = new HashMap<String, Object>();
                        parameterMap.put(paramKey, paramMap.get(paramKey));
                        parameterList.add(parameterMap);
                    } else {
                        log.info("getParameters", "Child Node is empty or not available");
                    }
                }
            }
        }
        return parameterList;
    }

    /**
     * Return data values from map at a given xPath to a list of string. (This
     * is useful for getting a list of values without each user having to
     * convert on their own within a test case.)
     * 
     * @param tagXpath
     *            An xpath to the data elements
     * @return a list of string containing the values
     */
    public static List<String> getdataList(String tagXpath) {
        List<String> resultList = new ArrayList<String>(getdataMap(tagXpath).values());
        return resultList;
    }

    /**
     * Look inside the XML file to determine whether the path exists or not.
     * 
     * @param tagXpath
     *            An xpath to the data node you want to check
     * @return True if the path exists in the file and false if it does not
     *         exist.
     */
    public static boolean dataPathExists(String tagXpath) {
        boolean bReturn = false;
        try {
            CustomDataLoader.getdataList(tagXpath);
            bReturn = true;
        } catch (IllegalArgumentException e) {
            // don't throw back any exception
        }
        return bReturn;
    }

}
