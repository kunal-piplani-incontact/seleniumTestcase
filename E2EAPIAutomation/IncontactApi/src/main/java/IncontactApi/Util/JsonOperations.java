package IncontactApi.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JsonOperations {
	static String responseFilePath;// = AssertionsUtil.responseFilePath;
	static String expectedFilePath;// = AssertionsUtil.expectedFilesPath;
	static String projectPath;
	static ExtentReportsDemo reportObj;
	public JsonOperations(ExtentReportsDemo obj){
		expectedFilePath = AssertionsUtil.expectedFilesPath;
		projectPath = AssertionsUtil.projPath;
		reportObj = obj;
	}
	
	/**
	 * Function to read the file content at given path
	 * @param filePath - the path of the file
	 * @return - string containing the file content
	 */
	public String readJsonResponse(String filePath){
		BufferedReader br = null;
		String jsonString="";
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line="";
			while((line = br.readLine()) != null){
				jsonString += line +"\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportObj.AddDataToTestReport("Exception in readJsonResponse. "+e.getMessage(), false);
		}finally {
			try {
				if(br != null){
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonString;
	}
	/**
	 * function to form a path in the format required by JsonPath api  
	 * @param path - the path given by user in driver excel sheet separated by "/"
	 * @return - path in valid readable format
	 */
	public String jsonPathBuilder(String path){
		String validJsonPath="$";
		String[] pathArray = path.split("/");
		for(int i=0; i<pathArray.length; i++){
			validJsonPath = validJsonPath + "." + pathArray[i];
		}
		return validJsonPath;
	}
	/*public String jsonOps(String keyPath){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonString = readJsonResponse(responseFilePath);
		String[] pathArray = keyPath.split("/");
		String jsonPathReader="$", actualValue="";
		if(pathArray.length == 1){
			jsonPathReader = jsonPathReader + "." + pathArray[0];
			try{
				jsonString = JsonPath.read(jsonString, "$.content.text");
				System.out.println(jsonString);
				actualValue = JsonPath.read(jsonString, jsonPathReader);
				System.out.println(actualValue);
				return actualValue;
			}
			catch(PathNotFoundException e){
			//	System.out.println("No such path exists in response!");
				reportObj.AddDataToTestReport("Value Compare Assertion: No such element exists on given path: "+keyPath, false);
			}
		}
		else{
			for(int i=0; i<pathArray.length; i++){
				jsonPathReader = jsonPathReader + "." +pathArray[i];
			}
			System.out.println(jsonPathReader);
			try {
				jsonString = JsonPath.read(jsonString, "$.content.text");
				actualValue = JsonPath.read(jsonString, jsonPathReader);
				System.out.println(actualValue);
				return actualValue;
			} catch (PathNotFoundException e) {
				e.printStackTrace();
				reportObj.AddDataToTestReport("Value Compare Assertion: No such path exists!", false);
			}
		}
		return actualValue;
	}*/
	/**
	 * To validate the value of any given field in json response
	 * @param tagPath - the path required to navigate to required field in json response
	 * @param tagValue - the expected value at the given tagPath
	 */
	public void valCompare(String tagPath, String tagValue){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonString = readJsonResponse(responseFilePath);
		String[] pathArray = tagPath.split("/");
		String jsonPathReader="$", actualValue="";
		boolean assertPass=false;
		if(pathArray.length == 1){
			jsonPathReader = jsonPathReader + "." + pathArray[0];
		}
		else{
			for(int i=0; i<pathArray.length; i++){
				jsonPathReader = jsonPathReader + "." +pathArray[i];
			}
		}
		try{
			jsonString = JsonPath.read(jsonString, "$.content.text");
			actualValue = JsonPath.read(jsonString, jsonPathReader);
			if(tagValue.equals(actualValue)){
				reportObj.AddDataToTestReport("Value Compare Assertion: Expected Value: \""+tagValue+"\" matches with Actual Value: \""+actualValue+ "\" at "+ tagPath, true);
			}
			else{
				reportObj.AddDataToTestReport("Value Compare Assertion: Value found at: \""+tagPath+"\" is : \""+actualValue, false);
			}
		}
		catch(PathNotFoundException e){
			reportObj.AddDataToTestReport("Value Compare Assertion: No such element exists on given path: "+tagPath, false);
		}
	}
	public void verifyTagCount(String tagPath, int expectedTagCount){
		try {
			responseFilePath = AssertionsUtil.responseFilePath;
			String jsonResponseString = readJsonResponse(responseFilePath);
			jsonResponseString = JsonPath.read(jsonResponseString, "$.content.text");
			tagPath = "$.."+tagPath+".length()";
			List a =  JsonPath.read(jsonResponseString, tagPath);
			if(a.size() == 0){
				reportObj.AddDataToTestReport("Count match assertion: "+tagPath+" element not found on given path", false);
			}
			if(a.contains(null)){
				if(a.size() == expectedTagCount){
					reportObj.AddDataToTestReport("Count Match Assertion: For \"" +tagPath+"\" Expected count: "+expectedTagCount+" Actual Count: "+a.size(), true);
				}
				else{
					reportObj.AddDataToTestReport("Count Match Assertion: For \"" +tagPath+"\" Expected count: "+expectedTagCount+" Actual Count: "+a.size(), false);
				}
			}
			else{
				if(a.get(0).equals(expectedTagCount)){
					reportObj.AddDataToTestReport("CountMatch assertion. Expected key count "+expectedTagCount+ " Actual key count "+a.get(0)+ " in response.", true);
				}
				else{
					reportObj.AddDataToTestReport("Pass: Expected key count "+expectedTagCount+ " does not match with actual count "+a.get(0)+ " in response.", false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportObj.AddDataToTestReport("Exception in VerifyTagCount assertion! "+e.getMessage(), false);
		}
	}
	public void verifyExistence(String tagPath){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonResponseString = readJsonResponse(responseFilePath);
		String validJsonPath = jsonPathBuilder(tagPath);
		validJsonPath = validJsonPath.substring(0, 2) + "." + validJsonPath.substring(2, validJsonPath.length());
		List result=null;
		try {
			jsonResponseString = JsonPath.read(jsonResponseString, "$.content.text");
			result =  JsonPath.read(jsonResponseString, validJsonPath);
			if(result.size() == 0){
				reportObj.AddDataToTestReport("VerifyExistence Assertion: \""+tagPath+"\" element not found on given path", false);
			}
			else{
				reportObj.AddDataToTestReport("VerifyExistence Assertion: \""+tagPath+"\" element found on given path", true);
			}
		} catch (PathNotFoundException e) {
			reportObj.AddDataToTestReport("VerifyKeyExistence Assertion: Path not found exception. No such path exists in reponse: "+tagPath, false);
			e.printStackTrace();
		}
		catch(InvalidPathException e){
			reportObj.AddDataToTestReport("VerifyKeyExistence Assertion: Invalid path exception while reading "+tagPath, false);
			e.printStackTrace();
			System.out.println("Invalid path given in input");
		}
		catch (Exception e) {
			reportObj.AddDataToTestReport("VerifyKeyExistence Assertion: Exception "+e.getMessage(), false);
		}
	}
	/**
	 * CheckIfNull validates that the given field has value null
	 * @param elementPath : it's the field path in response whose value it to be checked as null
	 * @return true if value is null else false
	 */
	public boolean CheckIfNull(String elementPath){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonResponseString = readJsonResponse(responseFilePath);
		String[] pathArray = elementPath.split("/");
		String validJsonPath = jsonPathBuilder(elementPath);
		validJsonPath = validJsonPath.substring(0, 2) + "." + validJsonPath.substring(2, validJsonPath.length());
		try {
			jsonResponseString = JsonPath.read(jsonResponseString, "$.content.text");
			List a = JsonPath.read(jsonResponseString, validJsonPath);
			if(a.contains(null)){
				return true;
			}
			else{
				return false;
			}
			
		} catch (InvalidPathException e) {
			reportObj.AddDataToTestReport("Exception in CheckNull Assertion. "+e.getMessage(), false);
		}
		catch (Exception e) {
			reportObj.AddDataToTestReport("Exception in CheckNull Assertion. "+e.getMessage(), false);
		}
		return false;
	}
	/**
	 * fileCompare function validates the response text(content/text) against the expected response file saved under ExpectedResponse
	 * folder in project structure.
	 * @return the 
	 */
	public String fileCompare(){
		responseFilePath = AssertionsUtil.responseFilePath;
		expectedFilePath = AssertionsUtil.expectedFilesPath;
		String expectedJson = readJsonResponse(expectedFilePath);
		String responseJson = readJsonResponse(responseFilePath);
		try {
			responseJson = JsonPath.read(responseJson, "$.content.text");
			JSONCompareResult result = JSONCompare.compareJSON(expectedJson, responseJson, JSONCompareMode.STRICT);
			if(result.failed()){
				reportObj.AddDataToTestReport("File Compare Assertion: Failed. Following are the differences between expected and actual reponse: "+result.toString(), false);
				return result.toString();
			}
			else{
				reportObj.AddDataToTestReport("File Compare Assertion: Expected and Actual reponse are same", true);
				return "Response File matches with the expected repsonse file";
			}
		} catch (InvalidJsonException e) {
			reportObj.AddDataToTestReport("FileCompare Assertion: Invalid JSON in response file : "+e.getMessage(), false);
			return "Invalid JSON format received";
		}
		catch (Exception e) {
			reportObj.AddDataToTestReport("FileCompare Assertion: Exception : "+e.getMessage(), false);
			return "Invalid JSON format received";
		}
	}
	/**
	 * This function validates the header data given a specific Header type and it expected value.
	 * @param headerName: it's the type of header that needs to be validated from the response header
	 * @param headerValue: it's the expected value of that header that will be validated against the actual response
	 */
	public void headerValidation(String headerName, String headerValue){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonResponseString = readJsonResponse(responseFilePath);
		try {
			List headers = JsonPath.read(jsonResponseString, "$.headers");
			boolean isHeaderFlag=false, isValueFlag=false;
			String actualValue="";
			for(int i=0; i<headers.size(); i++){
				if(JsonPath.read(jsonResponseString, "$.headers["+i+"].name").equals(headerName)){
					isHeaderFlag = true;
					actualValue = JsonPath.read(jsonResponseString, "$.headers["+i+"].value");
					if(actualValue.equals(headerValue)){
						isValueFlag = true;
						reportObj.AddDataToTestReport("HeaderValidation Assertion: Expected value: \""+ headerValue +"\" for Header: \""+ headerName+"\""+" matches with actual value \""+ actualValue +"\"", true);
						break;
					}
				}
			}
			if(!isHeaderFlag){
				reportObj.AddDataToTestReport("HeaderValidation Assertion: No such Header Type:\""+ headerName +"\" exists in reponse header", false);
			}
			if(!isValueFlag && isHeaderFlag){
				reportObj.AddDataToTestReport("HeaderValidation Assertion: Expected value: \""+ headerValue +"\" for Header: \""+ headerName+"\""+" does not match with actual value \""+ actualValue +"\"", false);
			}
		} catch (Exception e) {
			reportObj.AddDataToTestReport("Exception in HeaderValidation Assertion: "+e.getMessage(), false);
		}
		 
	}
	/**
	 * Function to validate the response status
	 * @param expectedValue - expected status 
	 * @return - result string
	 */
	public String StatusCodeValidation(String expectedValue){
		responseFilePath = AssertionsUtil.responseFilePath;
		String jsonResponseString = readJsonResponse(responseFilePath);
		try {
			List statusCode = JsonPath.read(jsonResponseString, "$..status");
			int statusCodeValue = JsonPath.read(jsonResponseString, "$.status");
			System.out.println(statusCodeValue);
			if(statusCode.size() == 0){
				System.out.println("No such element found");
				reportObj.AddDataToTestReport("StatusCodeValidation assertion. No Status field found in reponse ", false);
				return "No such element found";
			}
			if(statusCode.get(0).toString().equals(expectedValue)){
				reportObj.AddDataToTestReport("StatusCodeValidation Assertion: Expected status code \"" + expectedValue + "\" matches with actual code \""+ statusCode.get(0).toString()+"\"", true);
				return "StatusCodeValidation Assertion: Expected status code \"" + expectedValue + " matches with actual code \""+ statusCode.get(0);
			}
			else{
				reportObj.AddDataToTestReport("StatusCodeValidation Assertion: Expected status code \"" + expectedValue + " does not match with actual code \""+ statusCode.get(0).toString()+"\"", false);
				return "StatusCodeValidation Assertion: Expected status code \"" + expectedValue + " does not match with actual code \""+ statusCode.get(0).toString()+"\"";
			}
		} catch (Exception e) {
			System.out.println(e);
			reportObj.AddDataToTestReport("Exception in StatusCodeValidation assertion. "+e.getMessage(), false);
			return "Exception in StatusCodeValidation assertion. "+e.getMessage();
		}
	}
	public void responseStructureValidation(){
		responseFilePath = AssertionsUtil.responseFilePath;
		expectedFilePath = AssertionsUtil.expectedFilesPath;
		String expectedJson = readJsonResponse(expectedFilePath);
		String responseJson = readJsonResponse(responseFilePath);
		String missingFields="", unexpectedFields="";
		try {
			responseJson = JsonPath.read(responseJson, "$.content.text");
			JSONCompareResult result = JSONCompare.compareJSON(expectedJson, responseJson, JSONCompareMode.STRICT);
			if(result.isMissingOnField()){
				System.out.println("Following fields are missing in response:");
				for(int i=0; i<result.getFieldMissing().size(); i++ ){
					missingFields = missingFields +" "+ result.getFieldMissing().get(i).getExpected();
				}
				reportObj.AddDataToTestReport("ResponseStructreValidation: Following fields are missing in response: " + missingFields, false);
				System.out.println(missingFields);
			}
			if(result.isUnexpectedOnField()){
				System.out.println("Following fields are extra and unexpected in received response:");
				for(int i=0; i<result.getFieldUnexpected().size(); i++ ){
					unexpectedFields = unexpectedFields + " " + result.getFieldUnexpected().get(i).getActual();
				}
				reportObj.AddDataToTestReport("ResponseStructreValidation: Following fields are unexpected in response that are not in expected reponse: " + unexpectedFields, false);
				System.out.println(unexpectedFields);
			}
			else{
				System.out.println("Response json structure matches with expected json structure");
				reportObj.AddDataToTestReport("ResponseStructreValidation: Response json structure matches with expected json structure", true);
			}
		} catch (Exception e) {
			reportObj.AddDataToTestReport("Exception in responseStructureValidation: "+e.getMessage(), false);
			System.out.println("Exception in responseStructureValidation: "+e.getMessage());
		}
		
		
	}
	/*public static void main(String args[]){
		responseFilePath = "C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework\\ResponseFiles\\temp.json";
		expectedFilePath = "C:\\Users\\cagrawal\\Documents\\SmartBear\\Framework\\ExpectedResponse\\TestCase2_Response.json";
		JsonOperations obj = new JsonOperations(reportObj);
//		obj.StatusCodeValidation("403");
//		obj.fileCompare();
//		obj.responseStructureValidation();
		//obj.valCompare("message", "MissingAuthenticationToken");
		//obj.headerValidation("Connection", "keep-alive");
		//obj.headerValidation("Content", "keep-alive");
		//obj.headerValidation("Content-Type", "application/json");
		//obj.headerValidation("Content-Type", "application/text");
	}*/
}