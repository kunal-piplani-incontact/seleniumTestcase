package com.incontact.frameworkCore

/**
  * Created by sandip.bhattacharjee on 7/17/2017.
  */

import java.util
import java.util.regex.{Matcher, Pattern}


import scala.collection.immutable.HashMap

//import com.incontact.frameworkCore.Utils
import com.incontact.aws.SignatureCreator
import com.incontact.utilities.{DataReader, Logger}
import com.incontact.xmlParsers.EndpointContainer
import com.incontact.caseClasses._

import io.gatling.commons.validation.{Failure, Success, Validation}
import io.gatling.core.Predef._
import io.gatling.core.check.Validator
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import scala.io.Source

/**
  * Each test steps gets mapped to an object of HTTPRequest case
  * @param step object of Step case class
  */
class HTTPRequest(step : Step) {

  private var requestName = step.title + "_Endpoint: "+step.endpoint
  private var request = http(requestName)
  private var httpRequest : HttpRequestBuilder = null
  private var payload = ""

  /**
    * Getting the current value of httpRequest
    * @return value of httpRequest
    */
  def getHttpRequest(): HttpRequestBuilder = {
    httpRequest
  }

  step.action match {
    case "Get" => httpRequest = request.get(EndpointContainer.getEndpoint(step.endpoint))
    case "Post" => httpRequest = request.post(EndpointContainer.getEndpoint(step.endpoint))
    case "Put" => httpRequest = request.put(EndpointContainer.getEndpoint(step.endpoint))
    case "Delete" => httpRequest = request.delete(EndpointContainer.getEndpoint(step.endpoint))
    case "Head" => httpRequest = request.head(EndpointContainer.getEndpoint(step.endpoint))
  }

  for(parameter <- step.parameters){
    parameter.key match{
      case "QueryParam" => addQueryParam(parameter.value)
      case "QueryParamFile" => addQueryParam(parameter.value)
      case "Header" => addHeader(parameter.value)
      case "HeaderFile" => addHeaderFromFile(parameter.value)
      case "BasicAuth" => addBasicAuthentication(parameter.value)
      case "DigestAuth" => addDigestAuthentication(parameter.value)
      case "AWSAuth" => addAWSAuthentication(parameter.value)
      case "FormParam" => addFormParameter(parameter.value)
      case "FormParamFile" => addFormParameterFromFile(parameter.value)
      case "JSONBodyFromFile" => addJSONBodyStringFromFile(parameter.value)
      case "TextBodyFromFile" => addTextBodyStringFromFile(parameter.value)
      case "XMLBodyFromFile" => addXmlBodyStringFromFile(parameter.value)
      case "FileAsBody" => addFileAsBody(parameter.value)
      case "FileFormParam" => addFileAsFormParam(parameter.value)
      case "SaveResponse"=> saveJson(parameter.value)
      case "SendRespone"=>sendJson(parameter.value)
      case "SaveField"=>saveField(parameter.value)
      case "SaveAllField"=>saveAllField(parameter.value)
      case "SaveFieldGlobal"=>saveField(parameter.value)
      case not => println("Unexpected case : " + not.toString)
    }
  }

  for(validation <- step.validations){
    validation.key match{
      case "ResponseTime" => checkResponseTime(validation.value)
      case "Status" => checkStatus(validation.value)
      case "ResponseSubString" => checkResponseString(validation.value)
      case "CurrentUrl" => checkCurrentUrl()
      case "BodyString" => checkBody(validation.value)
      case "AssertionEquals"=>checkValue(validation.value)
      case "Header" => checkHeader(validation.value)
      case not => println("Unexpected case : " + not.toString)
    }
  }

  /**
    * Add query parameter to the request
    * @param queryParam query parameter from testcase xml file
    */
  private def addQueryParam(queryParam : String): Unit ={
    try {
      val keyValue = queryParam.split("=")
      httpRequest = httpRequest.queryParam(keyValue(0), keyValue(1))
    }catch{
      case e : Exception =>
        Logger.error("Error adding query parameter "+queryParam)
    }
  }
  /**
    * Add AWS authentication to the request
    * @param params region of the AWS service
    */
  private def addAWSAuthentication(params :String): Unit ={
    try {
      val serviceName = params.split(",")(0)
      val region = params.split(",")(1)
      var headerMap = new HashMap[String, String]()
      val protocol = step.action
      val uri = EndpointContainer.getUri(step.endpoint)
      val path = EndpointContainer.getPath(step.endpoint)
      val auths = SignatureCreator.getAuthorization(protocol, uri, path, serviceName, region, payload)
    //  headerMap += ("Content-Type" -> "application/json")
      headerMap += ("Host" -> uri)
      headerMap += ("X-Amz-Date" -> auths(0))
      headerMap += ("Authorization" -> auths(1))
      httpRequest =httpRequest.headers(headerMap)
    }catch{
      case e : Exception =>
        Logger.error("Error adding AWS authentication")
    }
  }
  /**
    * Add query parameter to the request from json file
    * @param paramFile query parameter node name from testcase xml file
    */
  private def addQueryParamFromFile(paramFile : String): Unit ={
      try {
        val queryMap = DataReader.readJson(paramFile)
        httpRequest = httpRequest.queryParamMap(queryMap)
      }catch{
        case e : Exception =>
          Logger.error("Error adding query parameter from file "+paramFile)
      }
    }

  /**
    * Add headers to the Http request
    * @param header header from test case xml file
    */
  private def addHeader(header : String): Unit ={
      try {
        var keyValue = header.split("=")
        httpRequest = httpRequest.header(keyValue(0), keyValue(1))
      }catch{
        case e : Exception =>
          Logger.error("Error adding header "+header)
      }
  }

  /**
    * Add headers to the Http request from json
    * @param paramFile header node from test case xml file
    */
  private def addHeaderFromFile(paramFile : String): Unit ={
      try {
        val headerMap = DataReader.read(paramFile)
        httpRequest = httpRequest.headers(headerMap)
      }catch{
        case e : Exception =>
          Logger.error("Error adding header from file "+paramFile)
      }
  }

  /**
    * Add basic authentication to the http request
    * @param authFile name of json file with authentication user name and password
    */
  private def addBasicAuthentication(authFile : String): Unit ={
      try {
        val authMap = DataReader.readJson("testData\\"+authFile)
        httpRequest = httpRequest.basicAuth(authMap("user"),authMap("password"))
      }catch{
        case e : Exception =>
          Logger.error("Error adding basic authentication from "+authFile)
      }
  }

  /**
    * Add digest authentication to the http request
    * @param authFile name of json file with authentication user name and password
    */
  private def addDigestAuthentication(authFile : String): Unit ={
      try {
        val authMap = DataReader.read("testData\\"+authFile)
        httpRequest = httpRequest.digestAuth(authMap("user"),authMap("password"))
      }catch{
        case e : Exception =>
          Logger.error("Error adding digest autherization from "+authFile)
      }
  }

  /**
    * Adds form parameters to the http request
    * @param formParam form parameters from test case xml file
    */
  private def addFormParameter(formParam : String): Unit ={
      try {
        val keyValue = formParam.split("=")
        httpRequest = httpRequest.formParam(keyValue(0), keyValue(1))
      }catch{
        case e : Exception =>
          Logger.error("Error adding form parameter "+formParam)
      }
  }

  /**
    * Adds form parameter from json file
    * @param nodeName name of the node containg the form parameters
    */
  private def addFormParameterFromFile(nodeName:String): Unit ={
      try {
        val dataMap = DataReader.readJson(nodeName)
        httpRequest = httpRequest.formParamMap(dataMap)
      }catch{
        case e : Exception =>
          Logger.error("Error adding form parameter from node "+nodeName)
      }
  }

  /**
    * Add JSON body string from file
    * @param path location of the json file
    */
  private def addJSONBodyStringFromFile(path : String): Unit ={
      try {
        payload = Source.fromFile("testData\\"+path).getLines().mkString
        httpRequest = httpRequest.body(StringBody(payload)).asJSON
      }catch{
        case e : Exception =>
          Logger.error("Error loading JSON body string from file "+path)
      }
  }

  private def addTextBodyStringFromFile(path : String): Unit ={
    try {
      payload = Source.fromFile("testData\\"+path).getLines().mkString
      httpRequest = httpRequest.body(StringBody(payload))
    }catch{
      case e : Exception =>
        Logger.error("Error loading Text body string from file "+path)
    }
  }

  /**
    * Add XML body string from file
    * @param path location of the xml file
    */
  private def addXmlBodyStringFromFile(path : String): Unit ={
      try {
        httpRequest = httpRequest.body(RawFileBody("testData\\"+path)).asXML
      }catch{
        case e : Exception =>
          Logger.error("Error loading XML body string from file "+path)
      }
  }

  /**
    * Add file as body to the http request
    * @param path location of the file
    */
  private def addFileAsBody(path: String): Unit ={
      try {
        httpRequest = httpRequest.bodyPart(RawFileBodyPart("testData\\"+path))
      }catch{
        case e : Exception =>
          Logger.error("Error adding file from path "+path)
      }
  }

  /**
    * Add file as form parameter to the http request
    * @param formFile location of the file
    */
  private def addFileAsFormParam(formFile : String): Unit ={
      try {
        val keyValue = formFile.split("=")
        httpRequest = httpRequest.formUpload(keyValue(0), keyValue(1))
      }catch{
        case e : Exception =>
          Logger.error("Error loading form parameter from "+formFile)
      }
  }

  /**
    * Validates the status of the http response
    * @param value value of the expected status
    */
  private def checkStatus(value : String){
      try {
        httpRequest=httpRequest.check(status.is(value.toInt))
      }catch{
        case e : Exception =>
          Logger.error("Error validating status, Expected : "+value)
      }
  }

  /**
    * Validates the response time with expect resonse time in millis
    * @param millis expected response time in millis
    */
  private def checkResponseTime(millis:String){
      try {
        httpRequest=httpRequest.check(responseTimeInMillis.lessThan(millis.toInt))
      }catch{
        case e : Exception =>
          Logger.error("Error validating response, Expected : "+millis)
      }
  }

  /**
    * Validates the current url of the response
    */
  private def checkCurrentUrl(): Unit ={
      try {
        httpRequest=httpRequest.check(currentLocation)
      }catch{
        case e : Exception =>
          Logger.error("Error validating current URL")
      }
  }

  /**
    * Validates a specific header in response
    * @param header expected header value as = separated
    */
  private def checkHeader(header : String): Unit ={
      try {
        val keyValue = header.split("=")
        httpRequest=httpRequest.check(headerRegex(keyValue(0), keyValue(1)))
      }catch{
        case e : Exception =>
          Logger.error("Error validating header. Expected : "+header)
      }
  }

  /**
    * Validates presence of a string in response
    * @param value string to validate in response
    */
  private def checkResponseString(value : String){
      try {
        httpRequest=httpRequest.check(substring(value))
      }catch{
        case e : Exception =>
          Logger.error("Error validating response string. Expected :"+value)
      }
  }

  /**
    *Validates the body of the response with expected body from file
    * @param file location of the file containing expected body
    */
  private def checkBody(file : String): Unit ={
      try {
        httpRequest=httpRequest.check(bodyString.is(ElFileBody(file)))
      }catch{
        case e : Exception =>
          Logger.error("Error validating body. Expected :"+file)
      }
  }

  /**
    * Save the JSON response to the session
    * @param value name of the variable to save in session
    */
  private def saveJson(value : String){
      try {
        httpRequest= httpRequest.check(bodyString.saveAs("value"))
      }catch{
        case e : Exception =>
          Logger.error("Error saving json" +value)
      }

  }

  /**
    * Sends saved json from session
    * @param value variable name of the stored json
    */
  private def sendJson(value : String){
      try {
        httpRequest= httpRequest.body(StringBody("${value}")).asJSON
      }catch{
        case e : Exception =>
          Logger.error("Error sending json" +value)
      }
  }

  /**
    * Save a value from json object in session to chain request
    * @param value variable name to save in session
    */
  private def saveField(value : String):Unit={
      try {
        var input = value.split(",").toArray

        var variableName = input(0)
        var variablePattern = input(1)
        httpRequest = httpRequest.check(jsonPath(variablePattern).find.saveAs(variableName))
      }catch{
        case e : Exception =>
          Logger.error("Error saving field as "+value)
      }

    }

  private def saveAllField(value : String):Unit={
    try {
      var input = value.split(",").toArray

      var variableName = input(0)
      var variablePattern = input(1)
      httpRequest = httpRequest.check(jsonPath(variablePattern).findAll.saveAs(variableName))
    }catch{
      case e : Exception =>
        Logger.error("Error saving field as "+value)
    }

  }

  /**
    * Check for null value in json response
    */
  def nullCheck[T] = new Validator[T] {
      val name = "nullCheck"
      def apply(actual : Option[T]) : Validation[Option[T]] = {
        actual match {
          case Some(null) => Success(actual)
          case _          => Failure("Response is not null")
        }
      }
    }


  /**
    * Check for not null value in json response
    */
  def notNullCheck[T] = new Validator[T] {
      val name = "notNullCheck"
      def apply(actual : Option[T]) : Validation[Option[T]] = {
        actual match {
          case Some(null) => Failure("Response is null")
          case _          => Success(actual)
        }
      }
  }


  /**
    * Check value in a node in json response
    * @param value expected value in json response node
    */
  def checkValue(value : String): Unit = {
    try {
      val input = value.split(",").toArray
      if (input != null) {
        if (input.length >= 2) {
          val par1 = input(0)
          val par2 = input(1)

          if (par2.equalsIgnoreCase("null"))
          {
            httpRequest=httpRequest.check(jsonPath(par1).validate(nullCheck[String]))
          }
          else
          if(par2.equalsIgnoreCase("notnull")) {
            httpRequest=httpRequest.check(jsonPath(par1).validate(notNullCheck[String]))

          }
          httpRequest = httpRequest.check(jsonPath(par1).is(par2))
        }
      }
    }catch{
      case e : Exception =>
        Logger.error("Error validating "+value+" in repsonse")
    }
  }


}