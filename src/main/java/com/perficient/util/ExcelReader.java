package com.perficient.util;
/**
 * Utility to access and perform actions on excel file.
 * @author ameya.pagore
 */

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ExcelReader {
	
	public static Xls_Reader resource=null;
	protected static ExtentTest test;
	private Log log = LogFactory.getLog(this.getClass());

	
	public ExcelReader(ExtentTest extentTest) {
		test = extentTest;
		
	}
	
	/**
	 * Loads Excel file.
	 * @param filename
	 */
	public void load(String filename) {
		try{
			resource = new Xls_Reader("./testdata/" + filename);
			log.info("Excel file found " + filename + "...");
			test.log(Status.PASS,"Read test data sheet " + filename );
		}  catch (Exception e) {
			log.info("Excel file not found " + filename);
			test.log(Status.FAIL, "Excel file not found: " + filename);
		}
	}
	
	/**
	 * Objective: set the default Excel file name 1.get the current test
	 * case class name and use this name to get a test data file name. For
	 * example, a class "TC01BingWebSearch_High" will get a datasheet file name
	 * "datasheet_TC_01_BingWebSearch" 2.load this properties file
	 */
	public void setDefaultExcelSheet() {
		String s = Thread.currentThread().getStackTrace()[3].getClassName();
		String filename = ("datasheet_" + s.split("\\.")[s.split("\\.").length - 1] + ".xlsx");
		load(filename);
	}
	
	/**
	 * This Method returns the Data inside a cell.
	 * @param sheetName
	 * @param colNum
	 * @param rowNum
	 * @return
	 */
	public String getCellData(String sheetName,int colNum,int rowNum){
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		String data=null;
		try
		{
			data = resource.getCellData(sheetName, colNum, rowNum);
			test.log(Status.PASS, "Get data from excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to get data from excel sheet.");
		}
		return data;
}
	
	/**
	 * This Method returns no. of rows in a sheet.
	 * @param sheetName
	 * @return
	 */
	public int getRowCount(String sheetName)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		int rowCount=0;
		try
		{
			rowCount = resource.getRowCount(sheetName);
			test.log(Status.PASS, "Get row count from excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to get data from excel sheet.");
		}
		return rowCount;
	}
	
	/**
	 * This Method returns the data inside a cell.
	 * @param sheetName
	 * @param colName
	 * @param rowNum
	 * @return
	 */
	public String getCellData(String sheetName,String colName,int rowNum){
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		String data=null;
		try
		{
			data = resource.getCellData(sheetName, colName, rowNum);
			test.log(Status.PASS, "Get data from excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to get data from excel sheet.");
		}
		return data;
	}
	
	/**
	 * This Method Writes data inside a cell and returns true if Data is written successfully.
	 * @param sheetName
	 * @param colName
	 * @param rowNum
	 * @param data
	 * @return
	 */
	public boolean setCellData(String sheetName,String colName,int rowNum, String data)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try
		{
			flag = resource.setCellData(sheetName, colName, rowNum, data);
			test.log(Status.PASS, "Set data in excel sheet.");
			
		} catch (Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to set data in excel sheet.");
		}
		return flag;
	}
	
	public boolean setCellData(String sheetName,String colName, String data)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try
		{
			int rowNum = getRowCount(sheetName) + 1;
			flag = resource.setCellData(sheetName, colName, rowNum, data);
			test.log(Status.PASS, "Set data in excel sheet.");
			
		} catch (Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to set data in excel sheet.");
		}
		return flag;
	}
	
	
	/**
	 * This Method adds sheet to a workbook.
	 * @param sheetname
	 * @return
	 */
	public boolean addSheet(String  sheetname)
	{	
		boolean flag=false;
		try
		{
			flag = resource.addSheet(sheetname);
			test.log(Status.PASS, "Add excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to add sheet.");
		}
		return flag;
	}
	
	/**
	 * This Method removes sheet from the workbook.
	 * @param sheetName
	 * @return
	 */
	public boolean removeSheet(String sheetName)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try
		{
			flag = resource.removeSheet(sheetName);
			test.log(Status.PASS, "Remove excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to remove sheet.");
		}
		return flag;
	}
	
	/**
	 * This method adds column to sheet.
	 * @param sheetName
	 * @param colName
	 * @return
	 */
	public boolean addColumn(String sheetName,String colName)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try
		{
			flag = resource.addColumn(sheetName, colName);
			test.log(Status.PASS, "Add column in excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to add column.");
		}
		return flag;
	}
	
	/**
	 * This method deletes column from sheet.
	 * @param sheetName
	 * @param colNum
	 * @return
	 */
	public boolean removeColumn(String sheetName, int colNum)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try
		{
			flag = resource.removeColumn(sheetName, colNum);
			test.log(Status.PASS, "Remove column from excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to remove column.");
		}
		return flag;
	}
	
	/**
	 * This method returns true if the sheet exists in a workbook.
	 * @param sheetName
	 * @return
	 */
	public boolean isSheetExist(String sheetName)
	{
		boolean flag=false;
		try
		{
			flag = resource.isSheetExist(sheetName);
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * This method returns no. of columns in a sheet.
	 * @param sheetName
	 * @return
	 */
	public int getColumnCount(String sheetName)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		int colCount=0;
		try
		{
			colCount = resource.getColumnCount(sheetName);
			test.log(Status.PASS, "Get column count of excel sheet.");
			
		} catch(Exception e)
		{
			e.printStackTrace();
			test.log(Status.FAIL, "Not able to get column count of sheet.");
		}
		return colCount;
	}
	
	/**
	 * This method creates hyperlink of the data.
	 * @param sheetName
	 * @param screenShotColName
	 * @param testCaseName
	 * @param index
	 * @param url
	 * @param message
	 * @return
	 */
	public boolean addHyperLink(String sheetName,String screenShotColName,String testCaseName,int index,String url,String message)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		boolean flag=false;
		try 
		{
			flag = resource.addHyperLink(sheetName, screenShotColName, testCaseName, index, url, message);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * This method returns Row numbers of the column.
	 * @param sheetName
	 * @param colName
	 * @param cellValue
	 * @return
	 */
	public int getCellRowNum(String sheetName,String colName,String cellValue)
	{
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);
		int num=0;
		try 
		{
			num = resource.getCellRowNum(sheetName ,colName, cellValue);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return num;
	}
	
	
	/**
	 * This Method returns Array of data from Excel sheet.
	 * @param sheetName
	 * @return
	 */
	public Object[][] getExcelData(String sheetName) {
		if (sheetName.length() > 31)
			sheetName = sheetName.substring(0, 31);

		Object[][] arrayExcelData = null;
		try {
			int totalNoOfCols = resource.getColumnCount(sheetName);
			int totalNoOfRows = resource.getRowCount(sheetName);

			arrayExcelData = new Object[totalNoOfRows - 1][totalNoOfCols];

			for (int i = 2; i < totalNoOfRows; i++) {

				for (int j = 1; j < totalNoOfCols; j++) {
					arrayExcelData[i - 1][j] = resource.getCellData(sheetName, j, i);
					log.info("Data is" + resource.getCellData(sheetName, j, i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayExcelData;
	}
	
	
	/************** To the Data for TestCase ******************/
	public static Object[][] getTestData(String DataFileName, String SheetName, String TestName) {

		ReadExcelDataFile readdata = new ReadExcelDataFile(System.getProperty("user.dir") + "//testdata//"+ "//ExcelTestDataReader//"+ DataFileName);
		String sheetName = SheetName;
		String testName = TestName;

		// Find Start Row of TestCase
		int startRowNum = 0;
		while (!readdata.getCellData(sheetName, 0, startRowNum).equalsIgnoreCase(testName)) {
			startRowNum++;
		}
		
		int startTestColumn = startRowNum + 1;
		int startTestRow = startRowNum + 2;

		// Find Number of Rows of TestCase
		int rows = 0;
		while (!readdata.getCellData(sheetName, 0, startTestRow + rows).equals("")) {
			rows++;
		}
		
		// Find Number of Columns in Test
		int colmns = 0;
		while (!readdata.getCellData(sheetName, colmns, startTestColumn).equals("")) {
			colmns++;
		}
		
		//Define Two Object Array
		Object[][] dataSet = new Object[rows][1];
		Hashtable<String, String> dataTable = null;
		int dataRowNumber=0;
		for (int rowNumber = startTestRow; rowNumber <= startTestColumn + rows; rowNumber++) {
			dataTable = new Hashtable<String, String>();
			for (int colNumber = 0; colNumber < colmns; colNumber++) {
				String key = readdata.getCellData(sheetName, colNumber, startTestColumn);
				String value = readdata.getCellData(sheetName, colNumber, rowNumber);
				dataTable.put(key, value);
				
				/*dataSet[dataRowNumber][colNumber]=readdata.getCellData(sheetName, colNumber, rowNumber);
				  00,01,02,03
				  10,11,12,13*/
			}
			dataSet[dataRowNumber][0]=dataTable;
			dataRowNumber++;
		}
	 return dataSet;
	}


}
