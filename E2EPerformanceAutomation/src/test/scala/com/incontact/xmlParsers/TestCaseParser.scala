package com.incontact.xmlParsers

import com.incontact.caseClasses.{Parameter, TestCase, Step}
import com.incontact.frameworkCore.TestLoader
import com.incontact.utilities.{Logger, SchemaValidator}
import com.incontact.utilities.SchemaValidator

import scala.xml.{Node, XML}

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Parsers for test case files
  */
object TestCaseParser {
  /**
    * Parse key value pair from xml node
    * @param node node to parse as key value pair
    * @return key value as Parameter object
    */
  def keyValueFromNode(node : Node): Parameter ={
    Parameter((node \ "@type").text, node.text)
  }

  /**
    * Parse step node from xml file
    * @param node node containing test step definition
    * @return parsed node as step object
    */
  def stepFromFromNode(node:Node) : Step={
    val loopcount = (node \ "@loopcount").text
    val title = (node \ "Title").text
    val action = (node \ "Action").text
    val endpoint = (node \ "TargetEndpoint").text
    val parameters = (node \ "Parameter").map(keyValueFromNode).toList
    val validations = (node \ "Validation").map(keyValueFromNode).toList
    Step(loopcount, title, action, endpoint, parameters, validations)
  }

  /**
    * Parse test case node from xml file
    * @param node node containing test test case definition
    * @return parsed node as testcase object
    */
  def testCaseFromNode(node:Node) : TestCase = {
    var testCase : TestCase =null
    try {
      val name = (node \ "@name").text
      val isEnabled = (node \ "@enable").text
      val simulation = (node \ "@simulation").text
      val steps = (node \ "TestStep").map(stepFromFromNode).toList
      testCase = TestCase(name, isEnabled, simulation, steps)
    }catch {
      case ex: Exception =>
        Logger.error("Parsing failed for test case " +(node \ "@name").text)
    }
    testCase
  }

  /**
    * Parse test case file and loads for execution
    * @param filePath path of the test case file
    */
  def parseTestCase(filePath : String): Unit ={
    if(SchemaValidator.validateTestCase(filePath)) {
      Logger.info("Parsing started for Test Cases")
      val xmlData = XML.loadFile(filePath)
      val testcases = (xmlData \ "TestCase").map(testCaseFromNode).toArray
      for(testCase <- testcases){
        if(testCase !=null && testCase.isActive()) {
          TestLoader.loadTestCase(testCase)
          Logger.info(testCase.name+" paresed and added to container")
        }
      }
    }
  }
}