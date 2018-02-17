package IncontactApi.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import IncontactApi.Util.ParseXml;

import IncontactApi.Util.excelUtils;

public class AssertionsUtil {
	static excelUtils excelUtility;
	static String responseFilePath, responseType, sheetName="Assertions", inputSheetPath, expectedFilesPath, projPath;
	int testCaseIdCol, assertTypeCol, pathCol, expectedValueCol, expectedCountcol;
	String xmlPathValue;
	ParseXml xmlParserObj = new ParseXml();
	ExtentReportsDemo reportObj;
	JsonOperations jsonParser;
	public AssertionsUtil(String projectPath){
		projPath = projectPath;
		excelUtility = new excelUtils(projectPath + "\\TestDriverExcel\\TestDriverExcel.xlsx");
		inputSheetPath = projectPath + "\\TestDriverExcel\\TestDriverExcel.xlsx";
		//responseFilePath = projectPath+"\\ResponseFiles\\TestCase1_Response.json";
		reportObj = new ExtentReportsDemo(projectPath);
		jsonParser = new JsonOperations(reportObj);
	}
	
	/**
	 * 
	 * @param testCaseId - the id of current test cased being executed in groovy script
	 * @param projectPath - the project folder path to form response files and expected file folder path
	 * @return - assertion result string
	 */
	public String validateTest(String testCaseId, String projectPath){
		String assertionResult = "";
		boolean result=false;
		try {
			reportObj.startTestCaseReport(testCaseId,"inContact Billing API Report");
			FileInputStream inputStream = new FileInputStream(new File(inputSheetPath));
			Workbook wb = new XSSFWorkbook(inputStream);
			Sheet sheet = wb.getSheet("Assertions");
			testCaseIdCol = excelUtility.getCloumnNumber("TestCaseID", sheetName);
			assertTypeCol = excelUtility.getCloumnNumber("Type", sheetName);
			pathCol = excelUtility.getCloumnNumber("Path", sheetName);
			expectedCountcol = excelUtility.getCloumnNumber("ExpectedCount", sheetName);
			expectedValueCol = excelUtility.getCloumnNumber("ExpectedValue", sheetName);
			String responseTypeFile = excelUtility.getCellValue(excelUtility.getCloumnNumber("ResponseType", "APIDToCallDetails"), excelUtility.getRowNumber(testCaseId,"APIDToCallDetails"), "APIDToCallDetails");
			responseFilePath = projectPath+"\\ResponseFiles\\"+testCaseId+"_Response."+responseTypeFile;
			expectedFilesPath = projectPath+"\\ExpectedResponse\\"+testCaseId+"_Response."+responseTypeFile;
			
			for(int i=1; i <= sheet.getLastRowNum(); i++){
				Row row = sheet.getRow(i);
				String newRowCaseId = row.getCell(testCaseIdCol).getStringCellValue();
				String tagPath="", tagValue="";
				int tagCount=0;
				if(newRowCaseId.equals(testCaseId)){
					String responseType = excelUtility.getCellValue(excelUtility.getCloumnNumber("ResponseType", "APIDToCallDetails"), excelUtility.getRowNumber(testCaseId,"APIDToCallDetails"), "APIDToCallDetails");
					String assertionType = row.getCell(assertTypeCol).getStringCellValue();
					switch(assertionType){
					case "ValCompare":
						tagPath = row.getCell(pathCol).getStringCellValue();
						if(row.getCell(expectedValueCol).getCellType() == 1){
							tagValue = row.getCell(expectedValueCol).getStringCellValue().toString();
						}
						else{
							tagValue = String.valueOf(row.getCell(expectedValueCol).getNumericCellValue());
						}
						if(responseType.equals("json")){
							jsonParser.valCompare(tagPath, tagValue);
						}
						else if(responseType.equals("xml")){
							xmlParserObj.getNodeValue(responseFilePath, tagPath);
							if(xmlPathValue.equals(tagValue)){
								assertionResult = assertionResult + "\nValueCompare Assertion: Expected value "+tagValue+" matched with "+xmlPathValue ;
							}
							else{
								assertionResult = assertionResult + "\nValueCompare Assertion: Expected value "+tagValue+" does matched with "+xmlPathValue ;
								System.out.println("Fail xml");
							}
						}
						break;
					
					case "CountMatch":
						int xmlTagCount=0;
						tagPath = row.getCell(pathCol).getStringCellValue();
						if(row.getCell(expectedCountcol).getCellType() == 1){
							tagCount = Integer.parseInt(row.getCell(expectedCountcol).getStringCellValue());
						}
						else{
							tagCount = (int)row.getCell(expectedCountcol).getNumericCellValue();
						}
						if(responseType.equals("json")){
							jsonParser.verifyTagCount(tagPath, tagCount);
						}
						else if(responseType.equals("xml")){
							xmlTagCount = xmlParserObj.getNodeCount(responseFilePath, tagPath);
							if(xmlTagCount == tagCount){
								assertionResult = assertionResult + "\nCountMatch Assertion: Expected count "+tagCount+" matched with "+xmlTagCount ;
								reportObj.AddDataToTestReport(tagPath+" Element found on given path", true);
							}
							else{ 
								assertionResult = assertionResult + "\nCountMatch Assertion: Expected value "+tagCount+" does matched with "+ xmlTagCount ;
								reportObj.AddDataToTestReport("CountMatch Assertion: Expected value "+tagCount+" does matched with "+ xmlTagCount, false);
								System.out.println("Fail xml");
							}
						}
						break;
					case "VerifyKeyExistence":
						tagPath = row.getCell(pathCol).getStringCellValue();
						if(responseType.equals("json")){
							jsonParser.verifyExistence(tagPath);
						}
						else if(responseType.equals("xml")){
							boolean existenceResult=false;
							existenceResult = xmlParserObj.nodeExists(responseFilePath, tagPath);
							if(!existenceResult){
								assertionResult = assertionResult + "\nKeyExistence Assertion: Path " + tagPath + " does not exits" ;
								reportObj.AddDataToTestReport("KeyExistence Assertion: Path " + tagPath + " does not exit in response", false);
								System.out.println("Fail:");
							}
							else{
								assertionResult = assertionResult + "\nKeyExistence Assertion: Path " + tagPath + " exits in response." ;
								reportObj.AddDataToTestReport("KeyExistence Assertion: Path " + tagPath + " exits in response", true);
								System.out.println("Pass: Node exists in the response fule");
							}
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					case "CheckNull":
						tagPath = row.getCell(pathCol).getStringCellValue();
						if(responseType.equals("json")){
							result = jsonParser.CheckIfNull(tagPath);
							if(result){
								reportObj.AddDataToTestReport("CheckNull Assertion: Null found at \""+tagPath+"\"", true);
							}
							else{
								reportObj.AddDataToTestReport("CheckNull Assertion: NOT Null at given "+tagPath, false);
							}
						}
						else if(responseType.equals("xml")){
							result = xmlParserObj.checkNodeValueNull(responseFilePath, tagPath);
							if(result){
								reportObj.AddDataToTestReport("CheckNull Assertion: Null at given "+tagPath, true);
							}
							else{
								reportObj.AddDataToTestReport("CheckNull Assertion: NOT Null at given "+tagPath, false);
							}
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					case "CheckNotNull":
						tagPath = row.getCell(pathCol).getStringCellValue();
						result = jsonParser.CheckIfNull(tagPath);
						if(responseType.equals("json")){
							if(result){
								reportObj.AddDataToTestReport("CheckNotNull Assertion: Null found at "+tagPath, false);
							}
							else{
								reportObj.AddDataToTestReport("CheckNotNull Assertion: Value is NOT Null at "+tagPath, true);
							}
						}
						else if(responseType.equals("xml")){
							result = xmlParserObj.checkNodeValueNull(responseFilePath, tagPath);
							if(result){
								reportObj.AddDataToTestReport("CheckNotNull Assertion: Null at given "+tagPath, false);
							}
							else{
								reportObj.AddDataToTestReport("CheckNotNull Assertion: NOT Null at given "+tagPath, true);
							}
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					case "FileCompare":
						if(responseType.equals("json")){
							jsonParser.fileCompare();
						}
						else if(responseType.equals("xml")){
							//TODO: xml validation code
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					case "StatusCodeValidation":
						if(row.getCell(expectedValueCol).getCellType() == 1){
							tagValue = row.getCell(expectedValueCol).getStringCellValue();
						}
						else{
							tagValue = String.valueOf((int)row.getCell(expectedValueCol).getNumericCellValue());
						}
						if(responseType.equals("json")){
							jsonParser.StatusCodeValidation(tagValue);
						}
						else if(responseType.equals("xml")){
							//TODO: xml validation code
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					case "HeaderValidation":
						tagPath = row.getCell(pathCol).getStringCellValue();
						tagValue = row.getCell(expectedValueCol).getStringCellValue().toString();
						if(responseType.equals("json")){
								jsonParser.headerValidation(tagPath, tagValue);
						}
						else if(responseType.equals("xml")){
							if(xmlPathValue.equals(tagValue)){
								assertionResult = assertionResult + "HeaderValidation Assertion: Expected value "+tagValue+" matched with "+xmlPathValue ;
							}
							else{
								assertionResult = assertionResult + "HeaderValidation Assertion: Expected value "+tagValue+" does matched with "+xmlPathValue ;
								System.out.println("Fail xml");
							}
						}
						break;
					case "ResponseStrutureValidation":
						if(responseType.equals("json")){
							jsonParser.responseStructureValidation();
						}
						else if(responseType.equals("xml")){
							//TODO: xml validation code
						}
						else{
							reportObj.AddDataToTestReport("Error in ResponseType column in TestDriver sheet. No such responseType: "+responseType, false);
						}
						break;
					default:
						System.out.println("Assertion type error. No such assertion exists: "+ assertionType);
						reportObj.AddDataToTestReport("Assertion type error. No such assertion exists: "+ assertionType, false);
					}
				}
			}
			return assertionResult;
		} catch (IOException e) {
			//e.printStackTrace();
			reportObj.AddDataToTestReport("IO Exception in ValidateTest function. Check if all required files are present in respective folders.", false);
		}
		catch (Exception e) {
			reportObj.AddDataToTestReport("Exception in ValidateTest function: "+e.getMessage(), false);
		}
		finally {
			reportObj.EndTestCaseReport();
			reportObj.WriteResult();
		}
		return "Empty String";
	}
	
	/*public static void main(String[] args){
		AssertionsUtil obj = new AssertionsUtil("C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework");
	
		//obj.validateTest("TestCase3","C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework");
		obj.validateTest("TestCase3","C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework");
		obj.validateTest("TestCase2","C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework");
		
	}*/
}
