package com.incontact.aws

import java.io.{BufferedReader, FileReader}
import java.math.BigInteger
import java.util.Date

import com.incontact.utilities.DataReader

import scala.io.Source

/**
  * Created by sandip.bhattacharjee on 9/17/2017.
  */

/**Singleton to generate AWS V4 signature for IAM authenticated APIs.
  */
object SignatureCreator {

  import javax.crypto.Mac
  import javax.crypto.spec.SecretKeySpec
  import java.io.UnsupportedEncodingException
  import java.security.MessageDigest
  import java.security.NoSuchAlgorithmException
  import java.text.DateFormat
  import java.text.SimpleDateFormat
  import java.util.Locale
  import java.util.TimeZone

  /**
    * Geberates byte array based on SHA-256 algorithm
    * @param data data to convert to byte array
    * @param key key to convert
    * @return byte array converted from string
    */
  @throws[Exception]
  def HmacSHA256(data: String, key: Array[Byte]): Array[Byte] = {
    val algorithm = "HmacSHA256"
    val mac = Mac.getInstance(algorithm)
    mac.init(new SecretKeySpec(key, algorithm))
    mac.doFinal(data.getBytes("UTF8"))
  }

  /**
    * Generates hex code from passed string
    * @param data String to convert to hex code
    * @return Hex code generated from the argument
    */
  private def generateHex(data: String) : String ={
    var messageDigest : MessageDigest = null
    var hex = ""
    try {
      messageDigest = MessageDigest.getInstance("SHA-256")
      messageDigest.update(data.getBytes("UTF-8"))
      val digest = messageDigest.digest
      hex = String.format("%064x", new BigInteger(1, digest))
    } catch {
      case e@(_: NoSuchAlgorithmException | _: UnsupportedEncodingException) =>
        e.printStackTrace()
    }
    hex
  }

  /**
    * Generate signature key for AWS API
    * @param key AWS secret key
    * @param dateStamp current zoned date stamp in GMT
    * @param regionName region of AWS account
    * @param serviceName service name for the AWS service. by default execute-api
    * @return signature key for the request
    */
  @throws[Exception]
  def getSignatureKey(key: String, dateStamp: String, regionName: String, serviceName: String): Array[Byte] = {
    val kSecret = ("AWS4" + key).getBytes("UTF8")
    val kDate = HmacSHA256(dateStamp, kSecret)
    val kRegion = HmacSHA256(regionName, kDate)
    val kService = HmacSHA256(serviceName, kRegion)
    val kSigning = HmacSHA256("aws4_request", kService)
    kSigning
  }

  /**
    * Generate hex number in String format from byte array
    * @param data byte array to convert
    * @return hex number in String format
    */
  def toHexNumber(data: Array[Byte]): String = {
    val sb = new StringBuilder(data.length * 2)
    var i = 0
    while ( {
      i < data.length
    }) {
      var hex = Integer.toHexString(data(i))
      if (hex.length == 1) sb.append("0")
      else if (hex.length == 8) hex = hex.substring(6)
      sb.append(hex)

      {
        i += 1; i - 1
      }
    }
    sb.toString.toLowerCase(Locale.getDefault)
  }

  /**
    * Getting current zoned timestam[p in GMT
    * @return current zoned timestamp in GMT
    */
  private def getCurrentTimeStamp():String = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    dateFormat.format(new Date())
  }
  /**
    * Getting current zoned datestam[p in GMT
    * @return current zoned timestamp in GMT
    */
  private def getCurrentDate():String = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    dateFormat.format(new Date())
  }

  /**
    * Geberating the signature from specified parameters for API
    * @param protocol protocol for the request
    * @param uri uri to send request
    * @param path path under uri to send request
    * @param secretKey AWS secret key
    * @param region region used for the service
    * @param serviceName service name. default execute-api
    * @return signature generated based on SHA-256
    */
  private def generateSignature(protocol: String, uri: String, path: String, secretKey: String, region: String, serviceName: String, payload:String)():Array[String] = {

    val timeStampSignature = new Array[String](2)
    try {
      val timedate = getCurrentTimeStamp()
      val date = getCurrentDate()
      val signingKey = getSignatureKey(secretKey, date, region, serviceName)
      println(payload)
      val payloadhash = generateHex(payload)
      val canonicalURI = protocol.toUpperCase + "\n" + path + "/\n\nhost:" + uri + "\nx-amz-date:" + timedate + "\n\nhost;x-amz-date\n" + payloadhash
      println(canonicalURI)
      val stringtosign = "AWS4-HMAC-SHA256\n" + timedate + "\n" + date + "/" + region + "/" + serviceName + "/aws4_request\n" + generateHex(canonicalURI)
      val signature = HmacSHA256(stringtosign, signingKey)
      timeStampSignature(0) = timedate
      timeStampSignature(1) = toHexNumber(signature)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    timeStampSignature
  }

  /**
    * Geberating authorization token
    * @param protocol protocol for the request
    * @param uri uri to send request
    * @param path path under uri to send request
    * @param region region used for the service
    * @return authorization token for the request
    */
  def getAuthorization(protocol: String, uri: String, path: String, serviceName:String, region: String, payload:String) : Array[String]= {
    val timeStampAuth = new Array[String](2)
    try {
      var formattedUri = ""
      var formattedPath = ""
      if (uri.endsWith("/"))
        formattedUri = uri.substring(0, uri.length - 1)
      else
        formattedUri = uri
      if(path.equals(""))
        formattedPath=path
      else {
        if (!path.startsWith("/"))
          formattedPath = "/" + path
        else
          formattedPath = path
        if (path.endsWith("/"))
          formattedPath = formattedPath.substring(0, formattedPath.length - 1)
      }
      var accessKey: String = null
      var secretKey: String = null
      val bufferedReader = new BufferedReader(new FileReader("c:\\accessKeys.csv"))
      while (bufferedReader.readLine() != null) {
        val keys = bufferedReader.readLine().split(",")
        accessKey = keys(0)
        secretKey = keys(1)
      }
      val timeSign = generateSignature(protocol.toUpperCase(), formattedUri, formattedPath, secretKey, region, serviceName, payload)
      val timestamp = timeSign(0)
      val signature = timeSign(1)
      val authorization = "AWS4-HMAC-SHA256 Credential=" + accessKey + "/" + timestamp.substring(0, 8) + "/" + region + "/"+serviceName+"/aws4_request,SignedHeaders=host;x-amz-date,Signature=" + signature

      timeStampAuth(0) = timestamp
      timeStampAuth(1) = authorization
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    timeStampAuth
  }
}
