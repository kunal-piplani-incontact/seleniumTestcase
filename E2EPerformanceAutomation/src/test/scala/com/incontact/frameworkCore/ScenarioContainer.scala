package com.incontact.frameworkCore

import com.incontact.caseClasses.TestCase

import scala.collection.mutable.ArrayBuffer
/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Contaoins scenario once prepared from the test cases
  */
object ScenarioContainer {
  private var scenarioContainer = new ArrayBuffer[Scenario]()

  /**
    * Adds scenario to the container after preparing
    * @param testCase
    */
  def addToContainer(testCase : TestCase): Unit ={
    scenarioContainer += (new Scenario(testCase))
  }

  /**
    * Getting the entire scenario conatainer
    * @return current scenariop container
    */
  def getScenarioContainer() : ArrayBuffer[Scenario] = {
    scenarioContainer
  }

}
