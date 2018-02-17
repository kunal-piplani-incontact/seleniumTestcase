package com.nice.incontact.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import com.nice.incontact.util.SeleniumLogger;

/**
 * Writes Key value pairs to INI file
 * 
 */

public class WriteKeyValuePairsINIFile {

	    private static final SeleniumLogger log = new SeleniumLogger(WriteKeyValuePairsINIFile.class);

	    /**
	     * Writes the key-value pairs to specified INI file at given path.
	     * 
	     * @param path
	     *            Path at which INI file exists
	     * @param fileName
	     *            INI file name
	     * @param hm
	     */
	    public void writeKeyValue(String path, String fileName, HashMap<String, String> hm) {
	        try {
	            File newFile = this.createINIFile(path, fileName);
	            Properties props = new Properties();
	            // Get a set of the entries
	            Set<Entry<String, String>> set = hm.entrySet();
	            Iterator<Entry<String, String>> i = set.iterator();
	            // Display elements
	            while (i.hasNext()) {
	                Entry<String, String> me = i.next();
	                props.setProperty(me.getKey().toString(), me.getValue().toString());
	            }
	            props.store(new FileOutputStream(newFile.getAbsolutePath(), true), "");
	            props.clear();
	        } catch (Exception ex) {
	            log.warn("writeKeyValue", ex.getMessage());
	        }
	    }

	    /**
	     * Create INI file at provided path with given file name.
	     * 
	     * @param path
	     *            Path at which INI file to be created
	     * @param fileName
	     *            INI file name
	     * @return
	     */
	    public File createINIFile(String path, String fileName) {
	        File dir = new File(path);
	        String newPath = path + "/";
	        if (dir.exists()) {
	            log.debug("createINIFile", "DIRECTORY EXISTS");
	        } else {
	            dir.mkdir();
	        }
	        newPath = newPath + "/";
	        File newFile = new File(newPath + fileName);
	        if (!newFile.exists()) {
	            newFile.setWritable(true);
	            try {
	                newFile.createNewFile();
	            } catch (IOException e) {
	                log.warn("writeKeyValue", e.getMessage());
	            }
	        }
	        return newFile;
	    }
}
