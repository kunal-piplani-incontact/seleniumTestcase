package com.incontact.frameworkCore

import com.incontact.caseClasses.Step
import com.incontact.utilities.{DataReader, Logger}
import com.incontact.xmlParsers.EndpointContainer
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.action.async.ws.WsSendBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import io.gatling.http.request.builder.ws.WsOpenRequestBuilder
import io.gatling.jdbc.Predef._

/**
  * Created by sbhattacharjee on 11/20/2017.
  */
class WSOpenRequest(step : Step){

  private var requestName = step.title + "_Endpoint: "+step.endpoint;
  private var request = ws(requestName)
  private var wsRequest : WsOpenRequestBuilder = null

  wsRequest = request.open(EndpointContainer.getEndpoint(step.endpoint))

  def getWsRequest(): WsOpenRequestBuilder = {
    wsRequest
  }




}
