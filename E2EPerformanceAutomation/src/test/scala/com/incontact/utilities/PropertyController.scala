package com.incontact.utilities

import java.io.{File, FileInputStream}
import java.util.Properties

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 7/13/2017.
  */
/**Loads property value from global.properties file based on key
  */
object PropertyController {
  private var propertyMap = new HashMap[String, String]

  /**
    * Getting property value from global.properties based on key
    * @param key key to search in global.properties
    * @return value of the property
    */
  def properties(key:String): String={
    try {
      if (!propertyMap.contains(key)) {
        val prop = new Properties()
        prop.load(new FileInputStream(new File("global.properties")))
        val value = prop.getProperty(key)
        propertyMap += (key -> value)
      }
      propertyMap(key)
    }catch {
      case e: Exception =>
        Logger.error("")
        ""
    }
  }
}