package com.incontact.aws

/**
  * Created by sbhattacharjee on 1/5/2018.
  */

import java.util.regex.{Pattern,Matcher}

import scala.io.Source

object DataConverter {

    def updateParameter(data:String, lineNumber:Int, csv:String): String ={
      val pattern: Pattern = Pattern.compile("\\$\\{(.*?)\\}")
      val matcher: Matcher = pattern.matcher(data)
      var updatedData : String =data
      while(matcher.find()){
        val toReplace = matcher.group()
        val param = toReplace.substring(2, toReplace.length - 1)
        val replaceWith: String = getData(param, lineNumber, csv)
        updatedData = updatedData.replace(toReplace, replaceWith)
      }
      println(updatedData)
      updatedData
    }

    def getData(key : String, lineNumber: Int, csv:String):String={
      val bufferedSource = Source.fromFile(csv)
      val lineArray = bufferedSource.getLines.toArray
      val headers = lineArray(0).split(",")
      val column = headers.indexOf(key)
      val currentLine = lineArray(lineNumber).split(",")
      bufferedSource.close
      return currentLine(column)
    }
}
