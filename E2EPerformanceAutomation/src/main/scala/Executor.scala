import com.incontact.mailer.{EmailClient, PropertyController}
import com.incontact.metrics.{MetricsLoader, MetricsProcessor, ResultWritter}
import com.incontact.reporting.{InfluxLoader, ReportCopier, ReportGenerator}

/**
  * Created by sandip.bhattacharjee on 8/6/2017.
  */

/**
  * Executor for main sbt task
  * Covers report generation and mailing
  */
object Executor {
  /**
    * Main method to trigger report generation and emailing based on global.properties
    * @param args user defined argument. Should not be passed
    */
  def main(args : Array[String]): Unit ={
    ReportGenerator.updateResults()
    ReportGenerator.generateReport()
    ReportCopier.copyReports()
    if(PropertyController.properties("PUSH_TO_INFLUX").equals("true")){
      println("****************************************************")
      println("Analysing Reports for INFLUX database entry")
      println("****************************************************")
      try {
        InfluxLoader.uploadActiveUsers()
        InfluxLoader.uploadRequestDist()
        InfluxLoader.uploadResponseDist()
        println("****************************************************")
        println("Results pushed successfully to INFLUX")
        println("****************************************************")
      }catch{
        case e:Exception =>
          println("****************************************************")
          println("Error loading data to INFLUX")
          println("****************************************************")
      }
    }
    if(PropertyController.properties("USE_METRICS").equals("true")){
      println("Comparing Results with Metrics")
      try {
        MetricsLoader.loadMetrics()
        MetricsProcessor.processMetrics()
        ResultWritter.writeResult()
        println("****************************************************")
        println("Results processed successfully with Metrics")
        println("****************************************************")
      }catch{
        case e:Exception =>
          println("****************************************************")
          println("Error processing Metrics")
          println("****************************************************")
      }
    }
    if(!PropertyController.properties("to.mailid").equals("")) {
      println("****************************************************")
      println("Execution Completed Successfully. Mailing Report......")
      println("****************************************************")
      EmailClient.triggerMail()
    }
    else
      println("Execution Completed Successfully. No Mail Triggered.")
  }
}