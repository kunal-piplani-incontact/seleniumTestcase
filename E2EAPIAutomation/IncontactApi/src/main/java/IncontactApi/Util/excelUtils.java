package IncontactApi.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelUtils {
	static Workbook workbook;
	Sheet sheet;

	public excelUtils(){}
	
	//Constructor 
	public excelUtils(String path) {
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			workbook = new XSSFWorkbook(inputStream);
		} catch (Exception e) {
			// System.out.println("There was a prolem in reading excel file");
			e.printStackTrace();
		}
	}

	/**
	 * Get All the active test cases which has been marked as Yes in the TestDriveExcelSheet
	 *
	 * @param sheetName
	 *            Name of Sheet
	 * @return activeTestCases
	 *            List Contining all the active TestCaseID
	 *            
	 */
	public List<String> getActiveTestCases(String sheetName) {
		List<String> activeTestCases = new ArrayList<String>();
		Sheet driverSheet = workbook.getSheet(sheetName);
		Iterator<Row> rowitr = driverSheet.rowIterator();
		rowitr.next();
		while (rowitr.hasNext()) {
			Row row = rowitr.next();
			Cell cell = row.getCell(1);
			if (cell.getStringCellValue().equalsIgnoreCase("yes")) {
				activeTestCases.add(row.getCell(0).getStringCellValue());
			}

		}

		return activeTestCases;
	}
	/**
	 * Get all the values related to the active testcases from the Config sheet in the 
	 * TestDriverExcel Sheet
	 * 
	 * @param streamName
	 *            List of active test cases
	 * @param headers
	 *            List of headers containing in the sheet
	 *  @param sheetName
	 *           Name of the sheet          
	 */
	public Map<String, Map<String, String>> getTestCasesDetails(List<String> testCases, List<String> headers,
			String sheetName) {
		Map<String, Map<String, String>> allParams = new LinkedHashMap<String, Map<String, String>>();
		String baseUrl = getCellValue(0, 1, "Config");
		for (int i = 0; i < testCases.size(); i++) {
			Map<String, String> params = new HashMap<String, String>();
			int rowNum = getRowNumber(testCases.get(i), sheetName);
			if (rowNum == -1) {
				// System.out.println("Test Case :" + testCases.get(i) + "
				// doesn't exist on sheet: " + sh);
				return null;

			}
			for (int j = 0; j < headers.size(); j++) {
				int colNum = getCloumnNumber(headers.get(j), sheetName);
				String val = getCellValue(colNum, rowNum, sheetName);
				params.put(headers.get(j), val);
			}
			params.put("BaseUrl", baseUrl);
			allParams.put(testCases.get(i), params);
		}

		return allParams;
	}
	/**
	 * Get all the value of the sheet header wise. 
	 *
	 * @param headers
	 *            List containing all the headers in the sheet.
	 * @param sheetName
	 *           Name of the sheet.
	 * @return allParams
	 *           Map containing all the information in the sheet header wise.               
	 */
	public Map<String, String> getSheetDetails(List<String> headers, String sheetName) {
		Map<String, String> allParams = new HashMap<String, String>();
		for (int j = 0; j < headers.size(); j++) {
			int colNum = getCloumnNumber(headers.get(j), sheetName);
			String val = getCellValue(colNum, 1, sheetName);
			allParams.put(headers.get(j), val);
		}
		return allParams;
	}
	/**
	 * Get all the headers present in the sheet.
	 *
	 * @param sheetName
	 *            Name of stream
	 * @return headers
	 *            List containg all the headers present in the sheet.
	 *            
	 */
	public List<String> getHeaders(String sheetName) {
		List<String> headers = new ArrayList<String>();
		Sheet driverSheet = workbook.getSheet(sheetName);
		Row row = driverSheet.getRow(0);
		Iterator<Cell> cellitr = row.cellIterator();
		while (cellitr.hasNext()) {
			Cell cell = cellitr.next();
			headers.add(cell.getStringCellValue());
		}

		return headers;

	}
	/**
	 * Get the cloumn number of the TestCase ID in the given sheet.
	 *
	 * @param cloumnHeader
	 *            Header of the column to search.
	 * @param sheetName
	 *            Name of the sheet.
	 * @return columnNumber
	 *            If the cloumnHeader ID is found it returns the column number of the sheet else returns -1.        
	 */
	public int getCloumnNumber(String cloumnHeader, String sheetName) {
		int columnNumber = -1;
		Sheet driverSheet = workbook.getSheet(sheetName);
		Row row = driverSheet.getRow(0);
		Iterator<Cell> cellitr = row.cellIterator();
		while (cellitr.hasNext()) {
			Cell cell = cellitr.next();
			if (cell.getStringCellValue().equals(cloumnHeader)) {
				columnNumber = cell.getColumnIndex();
				break;
			}
		}

		return columnNumber;
	}
	/**
	 * Get the row number of the TestCase ID in the given sheet.
	 *
	 * @param testCaseID
	 *            TestCase Id for which the row number is to be fetched.
	 * @param sheetName
	 *            Name of the sheet.
	 * @return columnNumber
	 *            If the testcase ID is found it returns the row number of the sheet else returns -1.        
	 */
	public int getRowNumber(String testCaseID, String sheetName) {
		int rowNumber = -1;
		Sheet driverSheet = workbook.getSheet(sheetName);
		Iterator<Row> rowitr = driverSheet.rowIterator();
		while (rowitr.hasNext()) {
			Row row = rowitr.next();
			Cell cell = row.getCell(0);

			if (cell.getStringCellValue().equals(testCaseID)) {
				rowNumber = row.getRowNum();
				break;
			}

		}

		return rowNumber;
	}
	/**
	 * Get The value of a particular cell based on rownumber and column number in a particular sheet.
	 *
	 * @param columnNumber
	 *            Column number of the cell.
	 * @param rowNumber
	 *            Row number of the cell.             
	 * @param sheetName 
	 *            Name of the sheet.
	 * @return value of the cell.                      
	 */
	public String getCellValue(int columnNumber, int rowNumber, String sheetName) {
		Sheet driverSheet = workbook.getSheet(sheetName);
		Row row = driverSheet.getRow(rowNumber);
		return row.getCell(columnNumber).getStringCellValue();
	}

}
