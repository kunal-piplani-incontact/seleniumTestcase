package com.incontact.caseClasses

import com.incontact.utilities.Logger


/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Each teststep from testcase gets mapped to this class.
  * @constructor create a new test step with title, loopcount, action, endpoint, paranmeters, validations
  *              and path
  * @param _title title of of the step
  * @param lc loopcount for the step
  * @param _action action to be used for the step (Get, Post etc)
  * @param _endpoint endpoint from config file to be used for the step
  * @param _parameters parameters to be passed along with the request
  * @param _validations validations to be used for the step
  */

case class Step(private val lc: String,private val _title : String, private val _action : String, private var _endpoint : String, private val _parameters : List[Parameter], private val _validations : List[Parameter]) {

  private val _loopcount=updateLoopCount(lc)

  /**
    * Getting private value of loopcount
    * @return value of private loopcount
    */
  def loopcount = _loopcount


  /**
    * Getting private value of title
    * @return value of private title
    */
  def title = _title


  /**
    * Getting private value of action
    * @return value of private action
    */
  def action = _action


  /**
    * Getting private value of endpoint
    * @return value of private endpoint
    */
  def endpoint =_endpoint


  /**
    * Getting private value of parameters HashMap
    * @return value of private parameters as HashMap
    */
  def parameters = _parameters

  /**
    * Getting private value of validations as HashMap
    * @return value of private validations as HashMap
    */
  def validations = _validations


  /**
    * Updating endpoint value based on name
    * @param endpointName name of the endpoint to change
    */
  def updateEndpoint(endpointName: String): Unit ={
    _endpoint=endpointName
  }

  /**
    * Updating loopcount for the request(Loopcount will be depricated in future)
    * @param lc loopcount value to update
    * @return updated loopcount in int format
    */
  def updateLoopCount(lc: String):Int= {
    try{
      val loopcount=lc.toInt
      if(loopcount<=0)
        throw new NumberFormatException()
      else
        loopcount
    } catch {
      case ex : NumberFormatException => Logger.warn("Invalid Argument for loopcount in "+title+". Considering single execution")
      1
    }
  }

}
