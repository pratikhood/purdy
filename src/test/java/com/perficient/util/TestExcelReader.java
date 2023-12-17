package com.perficient.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

public class TestExcelReader {
    private ExtentTest test;
    private ExcelReader xRead;
    private String filename = "datasheet_ExcelReaderTest.xlsx";
    private String negativeFilename = "does_Not_Exist.xlsx";
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();
    private String sheetName, expectedCellData, columnName, sheetToAdd, columnToAdd;
    private int columNumber, rowNumber, expectedRowCount, expectedColumnCount;

    /**
     * Setting up test environment for ExcelReader
     */
    @BeforeClass
    public void setUp() {
        this.test = ComplexReportFactory.getTest("TestExcelReader", "TEST", "TESTING","","");
        this.xRead = new ExcelReader(test);
        this.sheetName = "Sheet1";
        this.columNumber = 0;
        this.rowNumber = 2;
        this.expectedCellData = "AF";
        this.expectedRowCount = 10;
        this.columnName = "Parameters";
        this.sheetToAdd = "New Sheet";
        this.expectedColumnCount = 4;
        this.columnToAdd = "TEST";


    }

    /**
     * Tearing down ExcelReader Test environment
     */
    @AfterClass
    public void tearDown() {
        this.test = null;
        this.xRead = null;
    }

    /**
     * Test loading in an excel file
     */
    @Test
    public void TestLoad(){
        xRead.load(filename);
        Assert.assertNotEquals(this.consoleContent.toString(), "Excel file found " + filename + "...");
    }

//    /**
//     * Negative Test loading in an excel file
//     */
//    @Test
//    public void NegativeTestLoad(){
//        xRead.load(negativeFilename);
//        Assert.assertEquals(this.consoleContent.toString(), "Excel file not found " + negativeFilename);
//    }

//    /**
//     * Test setDefaultExcelSheet
//     */
//    @Test
//    public void TestSetDefaultExcelSheet() {
//        ExcelReader xRead2 = new ExcelReader(test);
//        xRead2.setDefaultExcelSheet();
//        Assert.assertNotEquals(this.consoleContent.toString(), "Excel file found " + filename + "...");
//    }

    /**
     * test getCellData(Sting, int, int)
     */
    @Test(dependsOnMethods = { "TestLoad" })
    public void TestGetCellData(){
        String cellData = xRead.getCellData(sheetName, columNumber, rowNumber);
        Assert.assertEquals(cellData, expectedCellData);
    }

    /**
     * test getCellData(Sting, String, int)
     */
    @Test(dependsOnMethods = { "TestLoad" })
    public void TestGetCellData2(){
        String cellData = xRead.getCellData(sheetName, columnName, rowNumber);
        Assert.assertEquals(cellData, expectedCellData);
    }

    /**
     * test getRowCount
     */
    @Test(dependsOnMethods = {"TestLoad"})
    public void TestGetRowCount(){
        int rowCount = xRead.getRowCount(sheetName);
        Assert.assertEquals(rowCount, expectedRowCount);
    }

    /**
     * test isSheetExist, so we can use it in another test
     */
    @Test(dependsOnMethods = {"TestLoad"})
    public void TestIsSheetExist() {
        boolean testResult = xRead.isSheetExist(sheetName);
        Assert.assertTrue(testResult);

    }

    /**
     * Negative test isSheetExist, so we can use it in another test
     */
    @Test(dependsOnMethods = {"TestLoad"})
    public void NegativeTestIsSheetExist() {
        boolean testResult = xRead.isSheetExist("No Sheet Here");
        Assert.assertFalse(testResult);

    }

    /**
     * test addSheet
     */
    @Test(dependsOnMethods = { "TestIsSheetExist", "NegativeTestIsSheetExist" })
    public void TestAddSheet() {
        xRead.addSheet(sheetToAdd);
        Assert.assertTrue(xRead.isSheetExist(sheetToAdd));

    }

    /**
     * Test removeSheet capability
     */
    @Test(dependsOnMethods = { "TestIsSheetExist", "NegativeTestIsSheetExist", "TestAddSheet" })
    public void TestRemoveSheet() {
        xRead.removeSheet(sheetToAdd);
        Assert.assertFalse(xRead.isSheetExist(sheetToAdd));
    }

    /**
     * Test getColumnCount
     */
    @Test(dependsOnMethods = {"TestLoad"})
    public void TestGetColumnCount () {
        int columnCount = xRead.getColumnCount(sheetName);
        Assert.assertEquals(columnCount, expectedColumnCount);
    }

    /**
     * Test addColumn
     */
    @Test(dependsOnMethods = { "TestGetColumnCount" })
    public void TestAddColumn() {
        xRead.addColumn(sheetName, columnToAdd);
        int columnCount = xRead.getColumnCount(sheetName);
        Assert.assertEquals(columnCount, expectedColumnCount+1);
    }

    /**
     * Tests addHyperlink capability
     */
    @Test(dependsOnMethods = {"TestAddColumn", "TestGetCellData" })
    public void TestAddHyperlink() {
        String hyperlink = "https://www.example.com";
        xRead.addHyperLink(sheetName, columnToAdd, expectedCellData, 0, hyperlink, hyperlink);
        String retrievedHyperlink = xRead.getCellData(sheetName, columnToAdd, 2);
        Assert.assertEquals(retrievedHyperlink, hyperlink);
    }

    /**
     * Test getCellRowNum capability
     */
    @Test(dependsOnMethods = { "TestAddHyperlink" })
    public void TestGetCellRowNum() {
        String hyperlink = "https://www.example.com";
        int cellRowNum = xRead.getCellRowNum(sheetName, columnToAdd, hyperlink);
        Assert.assertEquals(cellRowNum, 2);

    }

    /**
     * Test removeColumn Capability
     */
    @Test(dependsOnMethods = {"TestAddHyperlink"})
    public void TestRemoveColumn() {
        xRead.removeColumn(sheetName, expectedColumnCount);
        int columnCount = xRead.getColumnCount(sheetName);
        Assert.assertEquals(columnCount, expectedColumnCount);
    }

    /**
     * test getExcelData capability
     */
    @Test(dependsOnMethods = {"TestLoad"})
    public void TestGetExcelData(){
        Object[][] excelObject = xRead.getExcelData(sheetName);
        Assert.assertNotNull(excelObject);
    }



}
