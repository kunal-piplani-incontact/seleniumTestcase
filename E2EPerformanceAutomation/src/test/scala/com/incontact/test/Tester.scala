package com.incontact.test

import java.io.{BufferedReader, File, FileInputStream, FileReader}
import javax.activation.{DataHandler, FileDataSource}
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import java.util.zip
import java.util.zip.ZipFile

import org.zeroturnaround.zip.ZipUtil

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.util.Date

import com.incontact.aws.SignatureCreator
import com.incontact.frameworkCore.HTTPConfig

import scala.collection.immutable.HashMap

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */
object Tester{
  def main(args : Array[String]) {

   // ReportGenerator.updateResults
   // ReportGenerator.generateReport()
   // println(ResultContainer.getResultContainer().size)
    /*for(i <- results.indices){
      for(j <- results(i).indices){
        print(results(i)(j)+"   ")
      }
      println()
    }*/
 /*   while (br.readLine != null){
      println(br.readLine());
    }*/
 /*   val lines = Source.fromFile(("target\\gatling\\scenarioexecutor-1502711103633\\simulation.log")).toList;
    println(lines(0));
    println(lines(2))
    println(lines(3))
    println(lines(4))*/
    import java.text.DateFormat
 //   ReportGenerator.updateResults()
 //   ReportGenerator.generateReport()
   /* val bufferedReader = new BufferedReader(new FileReader("c:\\accessKeys.csv"))
    while (bufferedReader.readLine() != null){
      val keys = bufferedReader.readLine().split(",")
      println(keys(0) + "     " + keys(1))
    }*/
  /*  val auths = SignatureCreator.getAuthorization("GET", "2epm7cn73b.execute-api.us-west-2.amazonaws.com/", "dev/egressregistration/hutch/bpl/", "us-west-2")
    println(auths(0))
    println(auths(1))*/
  //  println(scala.io.Source.fromFile("testData\\data.json").getLines().mkString)
    var globalFileds = new HashMap[String, String]

    def addToGlobalField(key:String, value:String): Unit ={
      globalFileds += (key -> value)
    }

    def updateGlobalFiled(data:String): String ={
      val hashIndex = data.indexOf("#")
      val endIndex = data.indexOf("}",hashIndex)
      println(data.substring(hashIndex,endIndex+1))
      println(data.substring(hashIndex+2, endIndex))
      var updatedData = data.replace(data.substring(hashIndex,endIndex+1), globalFileds(data.substring(hashIndex+2, endIndex)))
      updatedData
    }
    addToGlobalField("token","12345")
    println(updateGlobalFiled("Authorization = #{token}"))
  }
}