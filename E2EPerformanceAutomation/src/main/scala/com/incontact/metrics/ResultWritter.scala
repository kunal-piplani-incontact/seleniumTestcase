package com.incontact.metrics

import java.io.{BufferedWriter, File, FileWriter}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sbhattacharjee on 11/17/2017.
  */
object ResultWritter {
  val testCase = new ArrayBuffer[String]()
  val endpoint = new ArrayBuffer[String]()
  val responseStatus = new ArrayBuffer[String]()
  val passStatus = new ArrayBuffer[String]()
  val responseResult = new ArrayBuffer[String]()
  val passResult = new ArrayBuffer[String]()

  def updateResult(_testCase : String, _endpoint:String, _responseResult:String, _responseStaus:String, _passResult:String, _passStatus:String): Unit = {
    testCase += _testCase
    endpoint += _endpoint
    responseResult += _responseResult
    responseStatus += _responseStaus
    passResult += _passResult
    passStatus += _passStatus
  }

  def writeResult(): Unit ={
    val resultFile = new File("target\\result.log")
    val bw = new BufferedWriter(new FileWriter(resultFile))
    for(counter <- 0 until testCase.size){
      bw.write("**********************************************\n")
      bw.write(testCase(counter)+"\n")
      bw.write(endpoint(counter)+"\n")
      bw.write("Response Time Result :: "+responseResult(counter)+"\n")
      bw.write(responseStatus(counter)+"\n")
      bw.write("Response Check Result :: "+passResult(counter)+"\n")
      bw.write(passStatus(counter)+"\n")
      bw.write("**********************************************\n")
    }
    bw.close()
  }

}
