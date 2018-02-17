package IncontactApi.Util;

import java.text.SimpleDateFormat;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
public class ExtentReportsDemo {

       static String reportLocation;
       static ExtentReports extent;
       ExtentTest extentTest; 

       public ExtentReportsDemo(String projectPath)
       {
           java.util.Date now = new java.util.Date();
           java.sql.Timestamp current = new java.sql.Timestamp(now.getTime());      
           String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);
           reportLocation = projectPath+"\\Reports\\APIAutomationReport_"+timeStamp+".html";
           extent = new ExtentReports(reportLocation, false);
           
       }
    
       public static void setReportPath()
       {      
              java.util.Date now = new java.util.Date();
              java.sql.Timestamp current = new java.sql.Timestamp(now.getTime());      
              String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(current);
              String reportLocation = "C:\\Tools\\Incontact_API"+timeStamp+".html";
       }

       public void startTestCaseReport(String TestCaseId,String TestDescription)
       {
              extentTest = extent.startTest(TestCaseId, TestDescription);   
       }
       
       public void AddDataToTestReport(String desc, boolean Flag )
       {
              if(Flag==true){
                     extentTest.log(LogStatus.PASS, desc);
              }
              else{
                     extentTest.log(LogStatus.FAIL, desc);
                     extent.endTest(extentTest);
                     extent.flush();
                     }    
       }
       
       public void EndTestCaseReport()
       {
              extent.endTest(extentTest);
       }
       
       public void WriteResult()
       {
     	   extent.flush();
       }
    @SuppressWarnings("deprecation")
       public static void GenrateReport (Boolean Flag) {             
              // writing everything to document.
              System.out.println("Execution complete");

    }
    
    /*public static void main (String args[])
    {
       ExtentReportsDemo T1=new ExtentReportsDemo("C:\\Tools\\Incontact_API");
       //T1.setReportPath();
       T1.startTestCaseReport("Test1","Billing");
       T1.AddDataToTestReport("Test1",true);
       T1.EndTestCaseReport();
              T1.startTestCaseReport("Test2","Billing");
       T1.AddDataToTestReport("Test2",false);
       T1.EndTestCaseReport();
       
    }*/
}
