package com.incontact.xmlParsers

import com.incontact.caseClasses.Endpoint

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 7/31/2017.
  */
/**Singleton to store all parsed endpoints from config files
  */
object EndpointContainer{
  private var configContainer = new HashMap[String, Endpoint]

  /**
    * Add endpoint to the container
    * @param endpoint endpoint to add to the container
    */
  def addToContainer(endpoint: Endpoint): Unit ={
    configContainer += (endpoint.name -> endpoint)
  }

  /**
    * Get endpoint from the container
    * @param key name of the endpoint as key
    */
  def getEndpoint(key : String) : String = {
    configContainer(key).endpoint
  }

  /**
    * Get uri value for the endpoint (implemented for AWS authentication)
    * @param key name of the endpoint as key
    * @return
    */
  def getUri(key : String) : String = {
    configContainer(key).uRi
  }

  /**
    * Get path value for the endpoint (implemented for AWS authentication)
    * @param key name of the endpoint as key
    * @return
    */
  def getPath(key : String) : String = {
    configContainer(key).path
  }

}
