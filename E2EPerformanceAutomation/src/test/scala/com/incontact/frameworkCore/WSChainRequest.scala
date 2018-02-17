package com.incontact.frameworkCore

import akka.io.Tcp.Message
import com.incontact.caseClasses.Step
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.action.async.ws.{WsCloseBuilder, WsSendBuilder}

import scala.io.Source

/**
  * Created by sbhattacharjee on 11/20/2017.
  */
class WSChainRequest (step : Step){
  private var requestName = step.title
  private var request = ws(requestName)
  private var wsRequest : WsSendBuilder = null

  for(parameter <- step.parameters){
    parameter.key match{
      case "SendText" => sendText(parameter.value)
      case "SendTextFromFile" => sendTextFile(parameter.value)
      case not => println("Unexpected case : " + not.toString)
    }
  }

  for(validation <- step.validations){
    validation.key match{
      case "CheckCount" => checkCount(validation.value)
      case "CheckMessage" => checkMessage(validation.value)
      case not => println("Unexpected case : " + not.toString)
    }
  }

  def sendText(text : String): Unit ={
    if(wsRequest==null)
      wsRequest = request.sendText(text)
    else
      wsRequest = request.sendText(text)
  }

  def sendTextFile(path : String): Unit ={
    if(wsRequest==null)
      wsRequest = request.sendText(Source.fromFile("testData\\"+path).getLines().mkString)
    else
      wsRequest = request.sendText(Source.fromFile("testData\\"+path).getLines().mkString)
  }

  def checkCount(text : String): Unit ={
    val timeout = text.split(",")(0).toInt
    val count = text.split(",")(1).toInt
    wsRequest = wsRequest.check(wsAwait.within(timeout).until(count))
  }

  def checkMessage(text : String): Unit ={
    val timeout = text.split(",")(0).toInt
    val count = text.split(",")(1).toInt
    val message = text.split(",")(2)
    wsRequest = wsRequest.check(wsAwait.within(timeout).until(count).regex(message))
  }

  def getWsRequest(): WsSendBuilder = {
    wsRequest
  }

}
