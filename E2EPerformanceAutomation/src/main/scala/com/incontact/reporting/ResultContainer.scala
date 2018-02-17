package com.incontact.reporting

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sandip.bhattacharjee on 8/21/2017.
  */

/**Singleton to hold results in Hashmap
  *
  */
object ResultContainer {

  private var resultContainer = new ArrayBuffer[TestCaseResult]()


  /**
    * Adds result to container
    * @param testCaseResult Test Case result as object generated from log file
    */
  def addTestCaseResult(testCaseResult : TestCaseResult): Unit ={
    resultContainer += testCaseResult
  }

  /**
    * To get the current result container
    * @return current instance of result container
    */
  def getResultContainer() : ArrayBuffer[TestCaseResult] = {
    resultContainer
  }

}
