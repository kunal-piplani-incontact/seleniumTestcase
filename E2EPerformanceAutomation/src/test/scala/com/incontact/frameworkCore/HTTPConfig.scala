package com.incontact.frameworkCore

import com.incontact.utilities.PropertyController
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 7/12/2017.
  */
/**Contains the common protocols for the framework
  * Expandable as new protocols are included in apis
  */
object HTTPConfig {

  var baseUrl = ""

  val httpConf = http
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  def updateBaseUrl()={
    val url = PropertyController.properties("BASE_URL")
    if(!url.equals("")){
      baseUrl = url
    }
  }

  private var globalFileds = new HashMap[String, String]

  def addToGlobalField(key:String, value:String): Unit ={
    globalFileds += (key -> value)
    println(key +"="+value)
  }

  def getGlobalFields():HashMap[String,String]={
    globalFileds
  }
}
