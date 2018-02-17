package com.incontact.caseClasses

/**
  * Created by sandip.bhattacharjee on 7/31/2017.
  */

/**Each parameter from config and testcase file gets mapped to this class.
  * @constructor create a new parameter using key value pairs
  *              and path
  * @param _key key of the parameter
  * @param _value value of the parameter
  */
case class Parameter(private val _key : String, private val _value : String){

  /**
    * Getting private key of the parameter
    * @return value of the private key
    */
  def key = _key

  /**
    * Getting private value of the parameter
    * @return value of the private value
    */
  def value =_value

}
