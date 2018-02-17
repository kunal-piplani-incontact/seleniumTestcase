package com.incontact.reporting

import java.io.{BufferedReader, File, FileReader}
import java.text.SimpleDateFormat
import java.util.Date

import com.relevantcodes.extentreports.{ExtentReports, LogStatus}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sandip.bhattacharjee on 8/21/2017.
  */

/**Singleton to generate extent report from gatling logs
  *
  */
object ReportGenerator {
  /**Updates result from gatling logs to report container
    *
    */
  def updateResults(): Unit = {
    val location = new File("target\\gatling")
    val directories = location.listFiles()
    println(directories(0).getAbsolutePath)
    val fr = new FileReader(directories(0).getAbsolutePath+"\\simulation.log");
    val br = new BufferedReader(fr)
    val lines = br.lines().toArray()
    for(i <- 0 until  lines.length){
      val entry = lines(i).toString.split("\t")
      if(entry.length==9){
        val testCaseResultContainer = ResultContainer.getResultContainer()
        var isAdded = false
        var testCaseIndex = -1
        for(testCaseCounter <- testCaseResultContainer.indices){
          if(testCaseResultContainer(testCaseCounter).testCaseName.equals(entry(1))) {
            isAdded = true
            testCaseIndex = testCaseCounter
          }
        }
        if(isAdded){
          testCaseResultContainer(testCaseIndex).addRequest(entry(4))
          testCaseResultContainer(testCaseIndex).addStartTime(entry(5))
          testCaseResultContainer(testCaseIndex).addEndTime(entry(6))
          testCaseResultContainer(testCaseIndex).addStatus(entry(7))
          testCaseResultContainer(testCaseIndex).addDescription(entry(8))
        }
        else{
          val testCaseResult = new TestCaseResult(entry(1), new ArrayBuffer[String]() += entry(4),new ArrayBuffer[String]() += entry(5),new ArrayBuffer[String]() += entry(6), new ArrayBuffer[String]() += entry(7), new ArrayBuffer[String]() += entry(8))
          ResultContainer.addTestCaseResult(testCaseResult)
        }
      }
    }
    for(testCaseResult <- ResultContainer.getResultContainer())
      testCaseResult.updateStatus()
  }

  /** Generates ISO timestamp from epoch time in gatling logs
    *
    * @param epochTime timestamp from gatling logs in epoch format
    * @return timestamp in ISO format
    */
  def getTimeStamp(epochTime : String) : String = {
    val date = new Date(epochTime.toLong)
    val format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS")
    format.format(date)
  }

  /**
    * Generate extent report from gatling logs
    */
  def generateReport(): Unit ={
    val extentReport = new ExtentReports("target\\executionreport.html")
    for(testCaseResult <- ResultContainer.getResultContainer()){
      val testCase = extentReport.startTest(testCaseResult.testCaseName)
      for(stepCounter <- testCaseResult.requests.indices){
        if(testCaseResult.status(stepCounter).equals("OK")) {
          testCase.log(LogStatus.PASS, testCaseResult.requests(stepCounter) ,"Start Time : "+getTimeStamp(testCaseResult.startTime(stepCounter))+" ; End Time : "+getTimeStamp(testCaseResult.startTime(stepCounter))+" ; "+ "All Validations Passed")
        }
        else{
          testCase.log(LogStatus.FAIL, testCaseResult.requests(stepCounter),"Start Time : "+testCaseResult.startTime(stepCounter)+" ; End Time : "+getTimeStamp(testCaseResult.startTime(stepCounter))+" ; "+testCaseResult.description(stepCounter))
        }
      }
      extentReport.endTest(testCase)
    }
    extentReport.flush()
    extentReport.close()
  }
}