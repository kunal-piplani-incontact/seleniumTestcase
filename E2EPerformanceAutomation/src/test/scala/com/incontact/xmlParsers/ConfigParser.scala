package com.incontact.xmlParsers

import com.incontact.caseClasses.{Endpoint, Parameter, Simulation}
import com.incontact.utilities.{Logger, SchemaValidator}


import scala.xml.{Node, XML}

/**
  * Created by sandip.bhattacharjee on 8/2/2017.
  */
/**Parsers for config files
  */
object ConfigParser {
  /**
    * Parse key value pair from xml node
    * @param node node to parse as key value pair
    * @return key value as Parameter object
    */
  def keyValueFromNode(node : Node): Parameter ={
    Parameter((node \ "@type").text, node.text)
  }

  /**
    * Parse endpoint node from xml file
    * @param node node containing the endpoint definition
    * @return endpoint as Endpoint object
    */
  def endPointFromNode(node:Node) : Endpoint={
    var endpoint : Endpoint =null
    try {
      val name = (node \ "@name").text
      val uRi = (node \ "Uri").text
      val protocol = (node \ "Protocol").text
      val port = (node \ "Port").text
      val path = (node \ "Path").text
      endpoint = Endpoint(name, uRi, protocol, port, path)
    }catch{
      case ex : Exception => Logger.error("Parsing failed for endpoint <"+(node \ "@name").text+">")
    }
    endpoint
  }

  /**
    * Parse simulation node from xml file
    * @param node node containing the simulation definition
    * @return simulation as Endpoint object
    */
  def simulationFromNode(node : Node) : Simulation={
    var simulation:Simulation = null
    try{
      val name = (node \ "@name").text
      val simulationType = (node \ "@profile").text
      val parameter = (node \ "Parameter").map(keyValueFromNode).toList
      simulation = Simulation(name, simulationType, parameter)
    }catch{
      case ex : Exception => Logger.error("Parsing Failed For Simulation "+(node \ "@name").text)
    }
    simulation
  }

  /**
    * Parse config file from xml
    * @param filePath path of the confoig file
    */
  def parseConfig(filePath : String): Unit ={
    if(SchemaValidator.validateConfigFile(filePath)) {
      val xmlData = XML.loadFile(filePath)
      val endpoints = (xmlData \ "EndPoint").map(endPointFromNode).toArray
      println((xmlData \ "Simulation").size)
      val simulations = (xmlData \ "Simulation").map(simulationFromNode).toArray
      for(endpoint <- endpoints){
        if(endpoint!=null){
          EndpointContainer.addToContainer(endpoint)
          Logger.info(endpoint.name+" parsed and added to container")

        }
      }
      println(simulations.size)
      for(simulation <- simulations){
        if(simulation!=null){
          SimulationContainer.addToContainer(simulation)
          Logger.info(simulation.simulationName+" parsed and added to container")
        }
      }
    }
  }
}