package com.incontact.caseClasses

import com.incontact.frameworkCore.HTTPConfig

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */

/**Each endpoint from apiConfig gets mapped to this class.
  * @constructor create a new endpoint with name, uRi, protocol, port
  *              and path
  * @param _name name of the endpoint
  * @param _uRi uRi of the endpoint without protocol
  * @param protocol protocol to be used for the endpoint
  * @param port port in which the endpoint is accepting request
  * @param _path path of the api relative to the uRi.
  */
case  class Endpoint(private val _name : String, private val _uRi : String, private val protocol : String, private val port : String, private val _path : String) {

  /**Single endpoint created by apending all the endpoint specifications*/
  private var _endpoint=getProtocol()+"://"+getUri()+getPort()+"/"+getPath()

  /**public method to get the endpoint
    * @return endpoint
    */
  def endpoint = _endpoint


  /** public method to get the endpoint name
    * @return name of the endpoint
    */
  def name = _name
  def uRi = _uRi
  def path = _path

  /**private method to format the uRi
    *
    * @return formatted uRi to create endpoint
    */
  def getUri(): String ={
    var URI : String = null
    if(_uRi.equalsIgnoreCase("Global"))
      URI=HTTPConfig.baseUrl
    else
      URI = _uRi
    if(URI.endsWith("/"))
      URI = URI.substring(0,_uRi.length-1)
    URI
  }

  /**rivate method to format the protocol
    *
    * @return formatted protocol to create endpoint
    */
  private def getProtocol(): String ={
    protocol
  }

  /**rivate method to format the port
    *
    * @return formatted port to create endpoint
    */
  private def getPort(): String ={
    if(port.equals("null"))
      ""
    else
      ":" + port
  }

  /**rivate method to format the path
    *
    * @return formatted path to create endpoint
    */
  private def getPath(): String ={
    if(_path.startsWith("/"))
      _path.substring(1)
    _path
  }


}