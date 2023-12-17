package com.perficient.util;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

public class TestCustomAssertion {
    private TestCaseBase testCase = new TestCaseBase();
    private WebDriver webDriver = null;
    private ExtentTest test = null;
    private CustomAssertion customAsserter = null;
    private SoftAssert softassert = null;

    /**
     * Sets up the test class for testing
     * @throws Exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        try {
            this.test = ComplexReportFactory.getTest("CustomAssertTest", "TEST", "TESTING","","");
            testCase.setUpBrowser("chrome", "false", "0", "0","Prod","10.128.193.19","9200","automation-example","_doc","local","", "", "", "", "", "");
            this.webDriver = testCase.getDriver();
            this.customAsserter = new CustomAssertion(webDriver, test, softassert);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Destroys the test class data after it has run this is to kill the browser
     */
    @AfterClass
    public void tearDown() {
        this.webDriver.quit();
        this.testCase = null;
        this.test = null;
        this.customAsserter = null;
    }

    /**
     * Test Custom assertEquals
     * @throws IOException
     */
    @Test
    public void TestAssertEquals() throws IOException {

        boolean testResult = customAsserter.assertEquals("1", "1");
        Assert.assertEquals(testResult, true);
    }

    /**
     * Negative test custom assertEquals
     * @throws IOException
     */
    @Test
    public void NegativeTestAssertEquals() throws IOException {

        boolean testResult = customAsserter.assertEquals("1", "2");
        Assert.assertEquals(testResult, false);
    }

    /**
     * Test custom assertNotNull
     */
    @Test
    public void TestAssertNotNull() {
        Object testObject = new Object();
        boolean testResult = customAsserter.assertNotNull(testObject);
        Assert.assertTrue(testResult);

    }

    /**
     * Negative test custom assertNotNull
     */
    @Test
    public void NegativeTestAssertNotNull() {
        Object testObject = null;
        boolean testResult = customAsserter.assertNotNull(testObject);
        Assert.assertFalse(testResult);
    }

    /**
     * Test custom assertTrue
     */
    @Test
    public void TestAssertTrue() {
        boolean testResult = customAsserter.assertTrue(true, null);
        Assert.assertTrue(testResult);
    }

    /**
     * Negative test custom assertTrue
     */
    @Test
    public void NegativeTestAssertTrue() {
        boolean testResult = customAsserter.assertTrue(false, null);
        Assert.assertFalse(testResult);
    }

    /**
     * Test custom assertNotNull(Object, String)
     */
    @Test
    public void TestAssertNotNull2(){
        Object testObject = "testObject String";
        String message = "Test Message";
        boolean testResult = customAsserter.assertNotNull(testObject, message);
        Assert.assertTrue(testResult);
    }

    /**
     * Negative test custom assertNotNull(Object, String)
     */
    @Test
    public void NegativeTestAssertNotNull2(){
        Object testObject = null;
        String message = "Test Message";
        boolean testResult = customAsserter.assertNotNull(testObject, message);
        Assert.assertFalse(testResult);
    }

    /**
     * Test custom snapshot util
     */
    @Test
    public void TestSnapshot() {
        String filePath = customAsserter.snapshot();
        File screenshot = new File(filePath);
        Assert.assertTrue(screenshot.exists());
    }

}
