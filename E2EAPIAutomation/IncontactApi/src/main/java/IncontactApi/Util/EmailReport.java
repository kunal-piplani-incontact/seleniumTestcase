package IncontactApi.Util;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This utility class provides a functionality to send an HTML e-mail message
 * with embedded images.
 * 
 * @author www.codejava.net
 *
 */

public class EmailReport {

	/**
	     * Sends an HTML e-mail with inline images.
	     * 
	     * @param host
	     *            SMTP host
	     * @param port
	     *            SMTP port
	     * @param userName
	     *            e-mail address of the sender's account
	     * @param password
	     *            password of the sender's account
	     * @param toAddress
	     *            e-mail address of the recipient
	     * @param subject
	     *            e-mail subject
	     * @param htmlBody
	     *            e-mail content with HTML tags
	     * @param mapInlineImages
	     *            key: Content-ID value: path of the image file
	     * @throws MessagingException 
	     * @throws FileNotFoundException 
	     * @throws Exception
	     */

		public  void sendReport(Map<String,String> emailConfig,String htmlPath) throws MessagingException, FileNotFoundException{
			

	        // sets SMTP server properties
	        Properties properties = new Properties();
	        properties.put("mail.transport.protocol", "smtp");
	        properties.put("mail.smtp.host", "co1cas10arr.na.nice.com");//"co1cas10arr.na.nice.com"
	        properties.put("mail.smtp.auth", "false");
	        Session session = Session.getInstance(properties);
	        Transport transport = session.getTransport("smtp");
	        transport.connect();
	        try {
	        // creates a new e-mail message
	        Message msg = new MimeMessage(session);
	 
	        msg.setFrom(new InternetAddress(emailConfig.get("From")));
	        String[] emailAddress = emailConfig.get("TO").split(",");
	        InternetAddress[] toAddresses = new InternetAddress[emailAddress.length];
	        for(int i=0;i<emailAddress.length;i++)
	        {
	        	toAddresses[i] = new InternetAddress(emailAddress[i]);
	        }
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject(emailConfig.get("ProjectName"));
	        msg.setSentDate(new Date());	       
	        // creates message part
	         MimeBodyPart messageBodyPart = new MimeBodyPart();
	         BodyPart messageBody  = new MimeBodyPart();
	         StringBuffer emailBody = new StringBuffer();
	         emailBody.append("<html>"+
             "<body>"+
             "<table style=\"width:1000px;height:250px\" border=\"1\">"+
	         "<tr>"+
		     "<th id=\"c1\" style=\"width:40px;height:25px;color:white;background-color:#151B54\">API Testing Report</th>"+
             "</tr>"+
	         "<tr>"+
		     "<td headers=\"c1\" style=\"font:bold\" >"+
		     "<p>Hi All</p>"+
		     "<p>Please Find the API automation test report attached.</p>"+
		     "<p>&nbsp</p>"+
		     "<p>Regards</p>"+
		     "<p>API Automation</p>"+
		     "</td>"+
	         "</tr>"+
	         "<tr>"+
		     "<td headers=\"c1\" style=\"width:40px;height:15px;color:white;background-color:#151B54\">Incontact</td>"+

	         "</tr>"+
             "</table>"+

            "</body>"+
            "</html>"	);
	         messageBody.setContent(emailBody.toString(),"text/html");
	         DataSource source = new FileDataSource(htmlPath); // ex : "C:\\test.pdf"
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName("ApiReport.html"); // ex : "test.pdf"
	         Multipart multipart = new MimeMultipart();
	         multipart.addBodyPart(messageBodyPart);
	         multipart.addBodyPart(messageBody);
	         msg.setContent(multipart); 
	        transport.sendMessage(msg,msg.getAllRecipients());
	            System.out.println("Automation report sent successfully to the recipients!!!");

	        } catch (MessagingException mex) {
	            mex.printStackTrace();
	        }
	    }

//	public static void main(String[] args) throws FileNotFoundException, MessagingException {
//		excelUtils util = new excelUtils(
//				"C:\\Users\\mgupta\\IncontactNew\\QAAPIAutomation\\SoapUi\\NewProject\\TestDriverExcel\\TestDriverExcel.xlsx");
//		(new EmailReport()).sendReport(util.getSheetDetails(util.getHeaders("EmailConfig"), "EmailConfig"),
//				"C:\\Users\\mgupta\\IncontactNew\\QAAPIAutomation\\SoapUi\\NewProject\\Reports\\APIAutomationReport_2017.08.18.14.28.54.html");
//	}
}
