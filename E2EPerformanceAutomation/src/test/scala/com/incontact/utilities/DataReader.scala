package com.incontact.utilities

import java.io.FileNotFoundException

import com.google.gson.{JsonElement, JsonObject, JsonParser}

import scala.collection.immutable.HashMap
import scala.io.Source

/**
  * Created by amruta.deokar on 7/18/2017.
  */


object DataReader {
  def read(filePath:String):HashMap[String,String]={
    var mapData= new HashMap[String,String]
    try {
      val bufferedSource = Source.fromFile(filePath)
      for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
        if (cols.size == 1)
          mapData += (cols(0) -> "")
        else
          mapData += (cols(0) -> cols(1))
      }
    }catch{
      case e : Exception =>
        Logger.error("Error resding file " +filePath )
    }
      mapData
    }


  def readJson(nodeName : String): HashMap[String, String] ={
    val fileName = "testData\\data.json"
    var mapData = new HashMap[String, String]()
    try {
      val source: String = Source.fromFile(fileName).getLines.mkString
      val parser: JsonParser = new JsonParser()
      val root: JsonElement = parser.parse(source)
      val phases: JsonObject = root.getAsJsonObject.get(nodeName).getAsJsonObject
      val keys = phases.keySet().toArray
      for (i <- 0 to keys.length - 1) {
        mapData += (keys(i).toString -> phases.get(keys(i).toString).getAsString)
      }
    }catch {
      case ex: FileNotFoundException => Logger.error("Invalid File Path")
      case ex: Exception => Logger.error("Error while reading file")
    }
    mapData
  }

}
