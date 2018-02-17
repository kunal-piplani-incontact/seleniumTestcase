package com.incontact.reporting

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sandip.bhattacharjee on 8/21/2017.
  */

/**
  * Test case result case class to instantiate a new result from log
  * @param _testCaseName name of the test case
  * @param _requests name of the request
  * @param _startTime execution start time
  * @param _endTime execution end time
  * @param _status result (PASS, FAIL)
  * @param _description description of the test case
  */
class TestCaseResult(_testCaseName : String, _requests : ArrayBuffer[String], _startTime : ArrayBuffer[String], _endTime : ArrayBuffer[String], _status : ArrayBuffer[String], _description : ArrayBuffer[String]) {

  private var _testCaseStatus = "PASS"

  def testCaseName = _testCaseName

  def requests = _requests

  def startTime =_startTime

  def endTime = _endTime

  def status = _status

  def description = _description

  def testCaseStatus = _testCaseStatus

  def addRequest(request : String): Unit ={
    _requests += request
  }

  def addStartTime (startTime : String) : Unit = {
    _startTime += startTime
  }

  def addEndTime (endTime : String): Unit = {
    _endTime += endTime
  }

  def addStatus (status : String) : Unit = {
    _status += status
  }

  def addDescription (description : String): Unit = {
    _description += description
  }

  def updateStatus (): Unit ={
    var failedCount = 0
    for(counter <- _status.indices){
      if(_status(counter).equals("KO")) {
        failedCount += 1
      }
    }
    if(failedCount != 0)
      _testCaseStatus = "FAIL"
  }

}
