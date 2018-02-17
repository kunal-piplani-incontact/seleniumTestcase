package com.incontact.reporting

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.commons.io.FileUtils

/**
  * Created by sandip.bhattacharjee on 9/17/2017.
  */

/** Singleton to copy existing reports and logs to reports folder */
object ReportCopier {

  /**Copy extent report, gatling report and logs to reports folder under target
    */
  def copyReports(): Unit = {

    val reportDir = new File("target\\reports")
    val gatlingReport = new File("target\\gatling")
    val executionReport = new File("target\\executionreport.html")
    val logFile = new File("target\\execution.log")
    if (!reportDir.exists)
      reportDir.mkdir
    if (gatlingReport.exists)
      FileUtils.copyDirectory(gatlingReport, new File("target\\reports\\gatlingreport"))
    if (executionReport.exists)
      FileUtils.copyFileToDirectory(executionReport, new File("target\\reports"), true)
    if (logFile.exists)
      FileUtils.copyFileToDirectory(logFile, new File("target\\reports"), true)
  }
}
