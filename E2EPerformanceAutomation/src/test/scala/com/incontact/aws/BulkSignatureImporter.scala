package com.incontact.aws

import java.io.{BufferedWriter, FileWriter}
import java.util

import scala.collection.JavaConversions.seqAsJavaList
import com.opencsv.CSVWriter

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by sbhattacharjee on 1/5/2018.
  */
object BulkSignatureImporter {

  def exportSignature(protocol: String, uri: String, path: String, serviceName:String, region: String, payloadFile:String, csvFile:String): Unit ={
    val csv = "testData\\"+csvFile
    val bufferedSource = Source.fromFile(csv)
    val bufferedWriter = new BufferedWriter(new FileWriter("testData\\signature_"+csvFile))
    val csvWriter = new CSVWriter(bufferedWriter)
    val lineArray = bufferedSource.getLines.toArray
    var csvToWrite = new ListBuffer[Array[String]]()
    for(counter <- 0 until lineArray.length){
      var lineFields : java.util.List[String] = null
      lineFields = seqAsJavaList(lineArray(counter).split(",").toList)
      var lineFields1 : java.util.List[String] = new util.ArrayList[String]
      for(counter <- 0 until lineFields.size()) {
        lineFields1.add(lineFields.get(counter))
      }
      if(counter == 0){
        lineFields1.add("timestamp")
        lineFields1.add("authorization")
      }
      else {
        val updatedPath = DataConverter.updateParameter(path, counter, csv)
        var updatedPayload = ""
        if(!payloadFile.equals(""))
          updatedPayload = DataConverter.updateParameter(Source.fromFile("testData\\"+payloadFile).getLines().mkString, counter, csv)
        val xamzsignature = SignatureCreator.getAuthorization(protocol, uri, updatedPath, serviceName, region, updatedPayload)
        lineFields1.add(xamzsignature(0))
        lineFields1.add(xamzsignature(1))
      }
      // val lineFields1 : Array[String] = lineFields.toArray
      var lineArray1 = new Array[String](lineFields1.size())
      for(counter <- 0 until lineFields1.size())
        lineArray1(counter) = lineFields1.get(counter)
     csvToWrite += lineArray1
    }
  //  val javaList = seqAsJavaList(csvToWrite)
    csvWriter.writeAll(csvToWrite)
    bufferedWriter.close()
  }
}
