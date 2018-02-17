package com.nice.incontact.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.StopWatch;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Class to create CSV file output with response times measurement. Enable Test
 * Author to record response times of particular step, method and or class.
 * Response times can be measured for multiple steps or multiple methods.
 * 
 */

public class ResponseTimer extends StopWatch{
	private static final SeleniumLogger log = new SeleniumLogger(ResponseTimer.class);
    private ConfigLoader config;
    private CSVWriter csv;
    private String testAction = "";
    private String startTime = "";

    public ResponseTimer(ConfigLoader configProperties) {
        config = configProperties;
        if (config.measureResponseTime)
            createCSVFile();
    }

    /**
     * Creates ResponseTimes folder and CSV file at project folder. Writes the
     * headers in CSV file to record the response time.
     */
    private void createCSVFile() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy");
        String folderName = "response-times";
        boolean isFolderCreated = new File(folderName).mkdirs();
        if (isFolderCreated)
            log.info("createCSVFile", folderName + "folder is created.");
        String fileName = folderName + "/" + "ResponseTimes_" + dateFormat.format(new Date()) + ".csv";
        String[] header = new String[] { "Test Action", "Run Time", "Response Time(Seconds)" };
        String[] server = new String[] { "Server:",
                config.protocol + "://" + config.host + "/" + config.centraluri };
        String[] browser = new String[] { "Browser:", config.browser };

        try {
            FileWriter writer = new FileWriter(fileName, true);
            csv = new CSVWriter(writer);
            csv.writeNext(server);
            csv.writeNext(browser);
            csv.writeNext(header);
        } catch (IOException e) {
            log.error("createCSVFile", "failed to create CSV file!");
        }
    }

    /**
     * Starts the stop watch
     * 
     * @param action
     */
    public void start(String action) {
        if (config.measureResponseTime) {
            this.testAction = action;
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
            this.startTime = dateFormat2.format(new Date());
            super.start();
        }
    }

    /**
     * Stops the stop watch and records the time It converts this response time
     * in seconds and writes to CSV file.
     */
    public void stop() {
        if (config.measureResponseTime && csv != null) {
            super.stop();
            String actualTime = String.format("%3.2f", getTime() / 1000.0);
            try {
                csv.writeNext(new String[] { this.testAction, this.startTime, actualTime });
                csv.flush();
            } catch (IOException e) {
                log.error("stop", "Failed to write to CSV file!");
            }
            super.reset();
        }
    }

    /**
     * Closes CSV file Handler.
     */
    public void closeCSVFile() {
        if (config.measureResponseTime && csv != null) {
            try {
                csv.close();
            } catch (IOException e) {
                log.error("closeCSVFile", "failed to close CSV file!");
            }
        }
    }
}
