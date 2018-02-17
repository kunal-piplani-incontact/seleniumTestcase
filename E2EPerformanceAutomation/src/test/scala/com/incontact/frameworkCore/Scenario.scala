package com.incontact.frameworkCore
import com.incontact.aws.DataConverter
import com.incontact.caseClasses.TestCase
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.core.structure.ScenarioBuilder

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Scenario prepared from each test cases
  * @constructor create a new new scenario from the test case
  * @param testCase test case passed from the parsed test case file
  */

class Scenario(testCase : TestCase) {
  val name = testCase.name
  val simulation = testCase.simulation
  var scn = scenario(name)

  scn = scn.exec{session =>
    val globalFields = HTTPConfig.getGlobalFields()
    println("Global Size is : "+globalFields.size)
    var newSession:Session = session
    for(key <- globalFields.keys){
      println(key +"is :"+globalFields(key))
      newSession = newSession.set(key, globalFields(key))
    }
    println(newSession)
    newSession
  }

  for (step <- testCase.steps) {
    for (parameter <- step.parameters) {
      if(parameter.key=="Pause")
        scn = scn.pause(10)
      if (parameter.key == "FeedCSV")
        scn = scn.feed(csv("testData\\" + parameter.value))
      if (parameter.key == "Rendezvous")
        scn = scn.rendezVous(parameter.value.toInt)
    }
    step.action match {
      case "connect" => scn = scn.exec(new WSOpenRequest(step).getWsRequest())
      case "send" => scn = scn.exec(new WSChainRequest(step).getWsRequest())
      case "close" => scn = scn.exec(new WSCloseRequest(step).getWsRequest())
      case not => scn = scn.exec(new HTTPRequest(step).getHttpRequest())
    }
    for (parameter <- step.parameters) {
      if (parameter.key == "SaveFieldGlobal")
        scn = scn.exec{session =>
          println("tokenfound : " + session.attributes(parameter.value.split(",")(0)))
          HTTPConfig.addToGlobalField(parameter.value.split(",")(0), session.attributes(parameter.value.split(",")(0)).toString)
          session
        }
    }
  }


  /**
    * Get name of the scenario
    * @return name of the scenario
    */
  def getName() : String ={
    name
  }

  /**
    * Get simulatiuon pattern of the scenario
    * @return simulation pattern for the scenario
    */
  def getSimulation() : String ={
    simulation
  }

  /**
    * Get current value of the scenario
    * @return current value of the scenario
    */
  def getScenario() : ScenarioBuilder = {
    scn
  }


}


