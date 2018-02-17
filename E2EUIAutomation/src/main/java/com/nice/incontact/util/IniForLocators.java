package com.nice.incontact.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

/**
 * This utility class loads INI file data. This class is used internally.
 * 
 */

public class IniForLocators {
	 private String iniFile = "";
	    public File file = null;

	    /*
	     * loads locator file @runtime
	     */
	    static IniForLocators iniDataObj = null;

	    public IniForLocators(String locatorINIfile) {
	        if (locatorINIfile.trim().endsWith(".ini"))
	            iniFile = locatorINIfile.trim();
	        else
	            iniFile = locatorINIfile.trim() + ".ini";

	        file = new File(System.getProperty("user.dir") + "/src/main/resources/" + iniFile);
	    }

	    Ini ini;

	    /**
	     * Internal api that returns map with Locators called from initLocators.
	     * 
	     * @param pageclassSection
	     * @param isCommonSectionIncluded
	     * @return
	     * @throws InvalidFileFormatException
	     */
	    @SuppressWarnings({ "rawtypes" })
	    private HashMap initLocators(String pageclassSection, Boolean isCommonSectionIncluded) throws InvalidFileFormatException {
	        HashMap<String, String> locatorMap = new HashMap<String, String>();
	        // Map<String,String> ElementKeyValue=new TreeMap();
	        ini = new Ini();

	        try {
	            ini.load(new FileReader(file));
	            for (String sectionName : ini.keySet()) {
	                if (sectionName.equalsIgnoreCase(pageclassSection) || (isCommonSectionIncluded && sectionName.equalsIgnoreCase("common"))) {
	                    Section section = ini.get(sectionName);
	                    Set NameValueSet = section.entrySet();
	                    Iterator i = NameValueSet.iterator();
	                    while (i.hasNext()) {
	                        Map.Entry me = (Map.Entry) i.next();
	                        String val = (String) me.getValue();
	                        locatorMap.put(me.getKey().toString(), val.substring(val.indexOf('/'), val.length()));

	                    }
	                    if (section.childrenNames().length > 0) {
	                        String[] childSections = section.childrenNames();
	                        for (String sec : childSections) {
	                            if (sec.contains(pageclassSection)) {
	                                // ///TODO To be added in case we have child
	                                // sections.
	                                System.out.println("Child elements found , this section is to be used for later reference");
	                            }
	                        }
	                    } else {
	                        // TODO: Remove this if not needed.
	                    }

	                }

	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return locatorMap;
	    }

	    /**
	     * Get ini Locators based on PageClassSection,Boolean flag for loading
	     * common section.
	     * 
	     * @param iniLocatorFile
	     * @param pageclassSection
	     * @param isCommonSectionIncluded
	     * @return
	     * @throws InvalidFileFormatException
	     */
	    @SuppressWarnings("unchecked")
	    public static HashMap<String, String> getlocatorMap(String iniLocatorFile, String pageclassSection, Boolean isCommonSectionIncluded)
	            throws InvalidFileFormatException {
	        if (iniLocatorFile.isEmpty() || pageclassSection.isEmpty()) {
	            return null;
	        } else {
	            IniForLocators iniObj = new IniForLocators(iniLocatorFile);
	            return iniObj.initLocators(pageclassSection, isCommonSectionIncluded);
	        }
	    }

	    /**
	     * This loads all locator data from file passed.
	     * 
	     * @param iniLocatorFile
	     * @return
	     * @throws InvalidFileFormatException
	     */
	    public static HashMap<String, String> getlocatorMap(String iniLocatorFile) throws InvalidFileFormatException {
	        HashMap<String, String> locators = null;
	        iniDataObj = new IniForLocators(iniLocatorFile);
	        locators = iniDataObj.initLocators();
	        return locators;

	    }

	    /**
	     * This api lodas all data from ini file and return it into HashMap. The
	     * locators in returned hashmap are used throughout PageClass.
	     * 
	     * @return
	     * @throws InvalidFileFormatException
	     */
	    @SuppressWarnings("rawtypes")
	    private HashMap<String, String> initLocators() throws InvalidFileFormatException {
	        HashMap<String, String> locatorMap = new HashMap<String, String>();
	        // Map<String,String> ElementKeyValue=new TreeMap();
	        ini = new Ini();

	        try {
	            ini.load(new FileReader(file));
	            for (String sectionName : ini.keySet()) {

	                Section section = ini.get(sectionName);
	                // System.out.print(section.childrenNames().length);
	                Set<Entry<String, String>> NameValueSet = section.entrySet();

	                Iterator<Entry<String, String>> i = NameValueSet.iterator();

	                while (i.hasNext()) {
	                    Map.Entry me = (Map.Entry) i.next();
	                    String val = (String) me.getValue();
	                    if (val.contains("/"))
	                        locatorMap.put(me.getKey().toString(), val.substring(val.indexOf('/'), val.length()).trim());
	                    else
	                        locatorMap.put(me.getKey().toString(), val.trim());
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return locatorMap;
	    }

	    /**
	     * This api loads locators based on PageClass Sections User needs to call
	     * this class from api exposed from driver.
	     * 
	     * @param iniLocatorFile
	     * @return
	     * @throws InvalidFileFormatException
	     */
	    public static HashMap<String, HashMap<String, String>> getPageClasslocatorMap(String iniLocatorFile) throws InvalidFileFormatException {
	        HashMap<String, HashMap<String, String>> locators = null;
	        iniDataObj = new IniForLocators(iniLocatorFile);
	        locators = iniDataObj.initPageClassLocators();
	        return locators;
	    }


	    /**
	     * This api will be called internally.
	     * 
	     * @return HashMap - Having PageClass name as Key and Locator map as values
	     * @throws InvalidFileFormatException
	     */
	    @SuppressWarnings({ "rawtypes" })
	    private HashMap<String, HashMap<String, String>> initPageClassLocators() throws InvalidFileFormatException {

	        HashMap<String, HashMap<String, String>> locatorMap = new HashMap<String, HashMap<String, String>>();
	        // Map<String,String> ElementKeyValue=new TreeMap();
	        ini = new Ini();

	        try {
	            ini.load(new FileReader(file));
	            for (String sectionName : ini.keySet()) {
	                HashMap<String, String> sectionMap = new HashMap<String, String>();
	                Section section = ini.get(sectionName);
	                // System.out.print(section.childrenNames().length);
	                Set<Entry<String, String>> NameValueSet = section.entrySet();

	                Iterator<Entry<String, String>> i = NameValueSet.iterator();

	                while (i.hasNext()) {
	                    Map.Entry me = (Map.Entry) i.next();
	                    String val = (String) me.getValue();
	                    if (val.contains("/"))
	                        sectionMap.put(me.getKey().toString(), val.substring(val.indexOf('/'), val.length()).trim());
	                    else
	                        sectionMap.put(me.getKey().toString(), val.trim());
	                }
	                locatorMap.put(sectionName, sectionMap);
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return locatorMap;

	    }

}
