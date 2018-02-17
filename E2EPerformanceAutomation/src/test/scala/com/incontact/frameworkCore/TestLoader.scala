package com.incontact.frameworkCore

import com.incontact.caseClasses.TestCase

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Singleton to load each test case  to scenario container
  * Uses Scenario class as a base
  */
object TestLoader {

  /**
    * Load test case to scenario container for execution
    * @param testCase test case object from case class
    */
  def loadTestCase(testCase : TestCase): Unit ={
      ScenarioContainer.addToContainer(testCase)
  }
}
