package com.incontact.mailer

import java.io.{File, FileInputStream}
import java.util.Properties

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 8/10/2017.
  */

/**Property controller for main from global.properties
  */
object PropertyController {

  private var propertyMap = new HashMap[String, String]

  /**Reads property from global.config based on specified key
  * @param key Key to search in global.properties
   */
  def properties(key:String): String={
      if(!propertyMap.contains(key)){
        val prop = new Properties()
        prop.load(new FileInputStream(new File("global.properties")))
        val value = prop.getProperty(key)
        propertyMap += (key -> value)
      }
      propertyMap(key)
  }
}