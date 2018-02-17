package com.incontact.xmlParsers

import com.incontact.caseClasses.Simulation
import com.incontact.utilities.Logger

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */
/**Sngleton to store all parsed simulations from config files
  */
object SimulationContainer {
  var simulationContainer = new HashMap[String, Simulation]

  /**
    * Adds simulation to the container
    * @param simulation simulation to add
    */
  def addToContainer(simulation: Simulation) = {
    simulationContainer += (simulation.simulationName -> simulation)
  }

  /**
    * Get simulation from container
    * @param simulationName name of the simulation to get
    * @return simulation pattern from the container
    */
  def getSimulation(simulationName : String) :Simulation = {
    if(simulationContainer.contains(simulationName))
      simulationContainer(simulationName)
    else{
      Logger.warn("No Simulation exists with name <"+simulationName+"> executing with default simulation")
      simulationContainer("default")
    }
  }

}
