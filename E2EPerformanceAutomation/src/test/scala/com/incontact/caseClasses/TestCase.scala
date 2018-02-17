package com.incontact.caseClasses

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */

/**
  * Each test cases in testcase.xml file maps to this case class
  * @param _name name of the test case
  * @param _active active status of the test case
  * @param _simulation simulation pattern for the test case
  * @param _steps steps in current test case
  */

case class TestCase(private val _name : String, private val _active: String,  private val _simulation:String, private val _steps : List[Step]) {

  /**
    * Getting private name of the test case
    * @return value of private name
    */
  def name = _name

  /**
    * Getting private simulation pattern of the test case
    * @return value of private simulation pattern
    */
  def simulation = _simulation

  /**
    * Getting private steps of the test case as List
    * @return value of private steps as HashMap
    */
  def steps = _steps

  /**
    * Getting private active status of the test case
    * @return value of private status
    */
  def isActive(): Boolean = {
    if(_active.equalsIgnoreCase("true"))
      true
    else
      false
  }

}
