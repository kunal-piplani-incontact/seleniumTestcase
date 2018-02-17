package com.incontact.metrics

import com.incontact.reporting.ResultContainer

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sbhattacharjee on 11/17/2017.
  */
object MetricsProcessor {
  def processMetrics(): Unit ={
    for(testCaseResult <- ResultContainer.getResultContainer()){
      val endpoints = new ArrayBuffer[String]()
      for(request <- testCaseResult.requests){
        if(!endpoints.contains(request.split("_Endpoint: ")(1)))
          endpoints += request.split("_Endpoint: ")(1)
      }
      for(endpoint <- endpoints){
        val metrics = MetricsContainer.getMetrics(endpoint)
        val responseTime = metrics.responseTime
        val responsePercentage = metrics.responsePercentage
        val passPercentage = metrics.passPercentage
        var responseBase = 0
        var responsePassed = 0
        var totalResponse = 0
        var passedResponse = 0
        for(counter <- 0 until testCaseResult.requests.size){
          if(testCaseResult.requests(counter).contains(endpoint)){
            totalResponse += 1
            if(testCaseResult.status(counter)=="OK"||(testCaseResult.status(counter)=="KO"&&(testCaseResult.description(counter).contains("responseTime")))){
              passedResponse += 1
              responseBase += 1
              if((testCaseResult.endTime(counter).toLong - testCaseResult.startTime(counter).toLong)<=responseTime)
                responsePassed += 1
            }
          }
        }
        var ignorePassPercentage = false
        var ignoreResponsePercentage = false
        if(responseBase==0) {
          responseBase = 1
          ignoreResponsePercentage = true
        }
        if(totalResponse==0){
          totalResponse = 1
          ignorePassPercentage = true
        }
        val actualPassPercentage = passedResponse*100/totalResponse
        val actualResponsePercentage = responsePassed*100/responseBase
        var responseResult = "Failed"
        var passResult = "Failed"
        if(actualResponsePercentage>=responsePercentage)
          responseResult="Passed"
        if(actualPassPercentage>=passPercentage)
          passResult = "Passed"
        ResultWritter.updateResult("Test Case : "+testCaseResult.testCaseName, "Endpoint : "+ endpoint, responseResult,  "Expected Percentage : "+responsePercentage+", Actual Percentage : "+(if(ignoreResponsePercentage) "NA" else actualResponsePercentage),passResult, "Expected Percentage : "+passPercentage+", Actual Percentage : "+(if(ignorePassPercentage) "NA" else actualPassPercentage))
      }
    }
  }
}
