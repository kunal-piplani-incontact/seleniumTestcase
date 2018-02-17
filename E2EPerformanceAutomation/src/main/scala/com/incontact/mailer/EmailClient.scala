package com.incontact.mailer

import java.io.File
import javax.activation.{DataHandler, FileDataSource}
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import javax.mail.{Message, Session, Transport}
import javax.mail.Address

import org.apache.commons.io.FileUtils
import org.zeroturnaround.zip.ZipUtil

import scala.io.Source

/**
  * Created by sandip.bhattacharjee on 8/10/2017.
  */

/**Singleton to send email post execution.
  */
object EmailClient {

  /**Trigger mail to toList specified in global.properties
    *
    */
  def triggerMail(): Unit ={
    ZipUtil.pack(new File(PropertyController.properties("gatling.reportdir")+"\\"),new File("target\\gatling.zip"))
    val to = PropertyController.properties("to.mailid").split(",")
    val address = new Array[Address](to.length)
    for(index <- to.indices){
      address(index) = new InternetAddress(to(index))
    }
    val from = PropertyController.properties("from.mailid")
    val host = "tlvmail.nice.com"
    val properties = System.getProperties
    properties.setProperty("mail.smtp.host", host)
    val session = Session.getDefaultInstance(properties)
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(from))
      message.addRecipients(Message.RecipientType.TO, address)
      message.setSubject("Performance Test Execution Report")
      val messageBodyPart = new MimeBodyPart()
      val text =
        """
          |**************************************************************************************
          |******Performance Test Execution Completed. Report and Log File Attached In The Mail*****
          |**************************************************************************************
        """.stripMargin
      messageBodyPart.setText(text)
      val messageReportPart = new MimeBodyPart()
      val report = "target\\gatling.zip"
      val reportFile = new FileDataSource(report)
      messageReportPart.setDataHandler(new DataHandler(reportFile))
      messageReportPart.setFileName("performace_report.zip")
      val messageLogPart = new MimeBodyPart()
      val log = PropertyController.properties("gatling.log")
      val logFile = new FileDataSource(log)
      messageLogPart.setDataHandler(new DataHandler(logFile))
      messageLogPart.setFileName("execution.log")
      val messageExReportPart = new MimeBodyPart()
      val messageExReport = "target\\executionreport.html"
      val exReport = new FileDataSource(messageExReport)
      messageExReportPart.setDataHandler(new DataHandler(exReport))
      messageExReportPart.setFileName("executionreport.html")
      val messageResultPart = new MimeBodyPart()
      if(PropertyController.properties("USE_METRICS").equals("true")) {
        val resultFile = new FileDataSource("target\\result.log")
        messageResultPart.setDataHandler(new DataHandler(resultFile))
        messageResultPart.setFileName("result.log")
      }
      val multipart = new MimeMultipart()
      multipart.addBodyPart(messageBodyPart)
      multipart.addBodyPart(messageReportPart)
      multipart.addBodyPart(messageExReportPart)
      multipart.addBodyPart(messageLogPart)
      if(PropertyController.properties("USE_METRICS").equals("true"))
        multipart.addBodyPart(messageResultPart)
      message.setContent(multipart)
      Transport.send(message)
      println("****************************************************")
      println("Report sent successfully...")
      println("****************************************************")
      val zipFile = new File("target\\gatling.zip")
      zipFile.delete()
    } catch {
      case mex: Exception => {
        println("****************************************************")
        println("Error sending mail to recipients")
        println("****************************************************")
      }
    }
  }
}