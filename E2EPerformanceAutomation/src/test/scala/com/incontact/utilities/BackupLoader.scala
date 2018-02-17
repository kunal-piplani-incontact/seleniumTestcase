package com.incontact.utilities

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.apache.commons.io.FileUtils





/**
  * Created by sandip.bhattacharjee on 8/22/2017.
  */
/**Takign backups before execution
  * Extent report, gatling report is in scope for backup
  */
object BackupLoader {

  /**
    * Takes backup of existing extent report and gatling report
    */
  def takeBackup(): Unit ={
    try {
      val backup = new File("backups")
      val gatlingReport = new File("target\\gatling")
      val executionReport = new File("target\\executionreport.html")
      val logFile = new File("target\\execution.log")
      if (!backup.exists)
        backup.mkdir
      val date = new Date
      val sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a")
      val formattedDate = sdf.format(date)
      val backUpFolder = formattedDate.replaceAll("/", ".").replaceAll(":", ".")
      if (gatlingReport.exists)
        FileUtils.moveDirectory(gatlingReport, new File("backups\\" + backUpFolder + "\\Gatling Report"))
      if (executionReport.exists)
        FileUtils.moveFileToDirectory(executionReport, new File("backups\\" + backUpFolder), true)
      val reportDir = new File("target\\reports");
      if (reportDir.exists())
        FileUtils.deleteDirectory(reportDir)
    }catch{
      case e : Exception =>
        Logger.error("")
    }
  }

}
