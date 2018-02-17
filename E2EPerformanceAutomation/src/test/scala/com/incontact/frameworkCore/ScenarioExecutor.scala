package com.incontact.frameworkCore

import com.incontact.utilities.{BackupLoader, BeforeAll, Logger, PropertyController}
import com.incontact.xmlParsers.{ConfigParser, SimulationContainer, TestCaseParser}
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.PopulationBuilder

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.concurrent.duration._

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */

/**Entry point for the execution
  * Gets executed as a part of sbt task - gatling:test
  */
class ScenarioExecutor extends Simulation{

  BackupLoader.takeBackup()
  HTTPConfig.updateBaseUrl()
  BeforeAll.execute()
  Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
  val configFiles = PropertyController.properties("config.file").split(",")
  for(configFile <- configFiles)
    ConfigParser.parseConfig("config\\"+configFile)
  Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
  val testCaseFiles = PropertyController.properties("testcase.file").split(",")
  for(testCaseFile <- testCaseFiles)
    TestCaseParser.parseTestCase("tests\\"+testCaseFile)
  Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
 // TestCaseLoader.loadTestCase()
  Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")

  var populations = ListBuffer[PopulationBuilder]()

  for(scenario <- ScenarioContainer.getScenarioContainer()){
    populations += scenario.getScenario().inject(getInjectionProfile(scenario)).protocols(HTTPConfig.httpConf)
    Logger.info(scenario.getName()+" added to execution buffer")
  }

  Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
  Logger.info("Preparing to initiate test case execution")
  if(populations.size>0) {
    setUp(populations.toList)
    Logger.info("Test case execution initiated successfully")
    Logger.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
  }
  else{
    Logger.error("No scenario has been setup for Execution")
  }

  /**
    * Prepare and return injection profile for the scenario based on simulation pattern
    * @param scenario scenario to get injection profile for
    * @return
    */
  def getInjectionProfile(scenario : Scenario) : ArrayBuffer[InjectionStep] = {
    val simulation = SimulationContainer.getSimulation(scenario.getSimulation())
    var injectonProfile : ArrayBuffer[InjectionStep] =  new ArrayBuffer[InjectionStep]()
    println(simulation)
    if(simulation.simulatioType.equalsIgnoreCase("AtOnceUser")) {
      val numberOfUser = simulation.getParameter("NumberofUser").toInt
      injectonProfile += atOnceUsers(numberOfUser)
    }
    else if(simulation.simulatioType.equalsIgnoreCase("RampUser")) {
      val numberOfUser = simulation.getParameter("NumberofUser").toInt
      val duration = simulation.getParameter("Duration").toInt
      injectonProfile += rampUsers(numberOfUser) over(duration seconds)
    }
    else if(simulation.simulatioType.equalsIgnoreCase("DelayedAtOnceUser")) {
      injectonProfile  += nothingFor(simulation.getParameter("InitialDelay").toInt seconds)
      val numberOfUser = simulation.getParameter("NumberofUser").toInt
      injectonProfile += atOnceUsers(numberOfUser)
    }
    else if(simulation.simulatioType.equalsIgnoreCase("DelayedRampUser")) {
      injectonProfile  += nothingFor(simulation.getParameter("InitialDelay").toInt seconds)
      val numberOfUser = simulation.getParameter("NumberofUser").toInt
      val duration = simulation.getParameter("Duration").toInt
      injectonProfile += rampUsers(numberOfUser) over(duration seconds)
    }
    injectonProfile
  }
}
