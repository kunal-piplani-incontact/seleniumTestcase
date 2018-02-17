package com.incontact.frameworkCore

import com.incontact.caseClasses.Step
import com.incontact.xmlParsers.EndpointContainer
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.action.async.ws.WsCloseBuilder
import io.gatling.http.request.builder.ws.WsOpenRequestBuilder

/**
  * Created by sbhattacharjee on 11/20/2017.
  */
class WSCloseRequest (step : Step) {

  private var requestName = step.title
  private var request = ws(requestName)
  private var wsRequest : WsCloseBuilder = null

  wsRequest = request.close

  def getWsRequest(): WsCloseBuilder = {
    wsRequest
  }


}
