package com.incontact.caseClasses

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */

/**Each simulation from apiConfig gets mapped to this class.
  * @constructor create a new simulation with name, type and parameters
  *              and path
  * @param _simulationName name of the simulation
  * @param _simulationType simulation profile (can be atonceuser, rampuser etc)
  * @param _parameters parameters used by the selected profile
  */
case class Simulation(private val _simulationName: String, private val _simulationType : String, private val _parameters : List[Parameter]) {

  /**
    * Getting private simulation name
    * @return value of private simulation name
    */
  def simulationName = _simulationName


  /**
    * Getting private simulation type
    * @return value of private simulation type
    */
  def simulatioType = _simulationType


  /**
    * Getting private parameters as HashMap
    * @return value of private parameters HashMap
    */
  def parameters = _parameters


  /**
    * Getting private parameter based on key
    * @param key key for the parameter
    * @return value of parameter based on key
    */
  def getParameter(key : String) :String = {
    var paramValue : String = null
    for (parameter <- parameters){
      println(key)
      println(parameter.key)
      println(parameter.key)
      if(parameter.key.equalsIgnoreCase(key))
        paramValue=parameter.value
    }
    paramValue
  }
}
