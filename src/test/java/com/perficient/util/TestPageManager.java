package com.perficient.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

public class TestPageManager {
    private TestCaseBase testCase = new TestCaseBase();
    private WebDriver webDriver = null;
    private ExtentTest test = null;
    private CustomAssertion customAsserter = null;
    private PageManager pageManager;
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();
    private SoftAssert softassert = null;
    protected String env; 

    /**
     * Sets up the test class for testing
     * @throws Exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        try {
            this.test = ComplexReportFactory.getTest("PageManagerTest", "TEST", "TESTING","","");
            testCase.setUpBrowser("chrome", "false", "0", "0","Prod","10.128.193.19","9200","automation-example","_doc","local","", "", "", "", "", "");
            this.webDriver = testCase.getDriver();
            this.customAsserter = new CustomAssertion(webDriver, test, softassert);
            this.pageManager = new PageManager(this.webDriver, "chrome", env, test);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Tear down the webdriver
     */
    @AfterClass
    public void tearDown() {
        this.webDriver.quit();
    }

    @Test(dependsOnMethods = {"TestNavigate"}, expectedExceptions = StaleElementReferenceException.class)
    public void TestSwitchToFrame() {
        WebElement out_of_frame = this.webDriver
                .findElement(By.xpath("//h3"));
        Assert.assertTrue(out_of_frame.isDisplayed());

        WebElement iframe = this.webDriver
                .findElement(By.xpath("//*[contains(@id, 'mce_0_ifr')]"));
        pageManager.switchToFrame(iframe);
        Assert.assertFalse(out_of_frame.isDisplayed());

    }

    /**
     * Test switching to Default content
     * capture an element out of iframe called out_of_frame
     * switch to default content
     * assert isDisplayed on out_of_frame
     */
    @Test(dependsOnMethods = {"TestSwitchToFrame"})
    public void TestSwitchToDefaultContent() {
        // element below is for verifying content view

        pageManager.switchToDefaultContent();
        WebElement out_of_frame = this.webDriver
                .findElement(By.xpath("//h3"));
        Assert.assertTrue(out_of_frame.isDisplayed());

    }

    /**
     * Test ScrollWebPage capability
     */
    @Test
    public void TestScrollWebPage(){
        pageManager.navigate("https://the-internet.herokuapp.com/infinite_scroll");
        WebElement scrollablePage = this.webDriver
                .findElement(By.xpath("//*[@id=\"content\"]/div/div/div"));
        pageManager.ScrollWebPage(scrollablePage);

    }

    /**
     * Test ScrollToTop capability
     */
    @Test(dependsOnMethods = "TestScrollWebPage")
    public void TestScrollToTop() {
        pageManager.scrollToTop();
    }


    /**
     * Test alert dismiss
     * Raise alert
     * dismiss alert
     * try to switch to alert and catch epected exception that alert is not there
     */
    @Test(expectedExceptions = NoAlertPresentException.class)
    public void TestAlertDismiss() {
        this.webDriver.get("https://the-internet.herokuapp.com/javascript_alerts");
        WebElement raiseAlert = this.webDriver.findElement(
                By.xpath("//button[@onclick='jsAlert()']")
        );
        raiseAlert.click();
        pageManager.alertDismiss();

        this.webDriver.switchTo().alert();
    }

    /**
     * Test alert dismiss
     * Raise alert
     * dismiss alert
     * try to switch to alert and catch epected exception that alert is not there
     */
    @Test
    public void TestClick() {
        this.webDriver.get("https://the-internet.herokuapp.com/javascript_alerts");
        WebElement raiseAlert = this.webDriver.findElement(
                By.xpath("//button[@onclick='jsAlert()']")
        );
        pageManager.click(raiseAlert);
        pageManager.alertDismiss();
        WebElement resultElement = this.webDriver.findElement(By.xpath("//p[@id='result']"));
        Assert.assertEquals(resultElement.getText(), "You successfuly clicked an alert");
    }

    /**
     * Test clickByJavaScriptExecutor(WebElement)
     */
    @Test
    public void TestClickByJavaScriptExecutorElement() {
        this.webDriver.get("https://the-internet.herokuapp.com/javascript_alerts");
        WebElement raiseAlert = this.webDriver.findElement(
                By.xpath("//button[@onclick='jsAlert()']")
        );
        pageManager.clickByJavaScriptExecutor(raiseAlert);
        pageManager.alertDismiss();

        WebElement resultElement = this.webDriver.findElement(By.xpath("//p[@id='result']"));
        Assert.assertEquals(resultElement.getText(), "You successfuly clicked an alert");
    }

    /**
     * Handle dropdown by child element index
     */
    @Test
    public void TestDropDownHandlingByIndex() {
        this.webDriver.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdown = this.webDriver.findElement(
                By.xpath("//select[@id='dropdown']")
        );
        WebElement selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));


        Assert.assertEquals(selectedOption.getText(), "Please select an option");
        pageManager.dropDownHandlingByIndex(dropdown, 1);
        selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));
        Assert.assertEquals(selectedOption.getText(), "Option 1");

    }

    /**
     * Test drop Down handling by child element text(s)
     */
    @Test
    public void TestDropDownHandlingByText() {
        this.webDriver.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdown = this.webDriver.findElement(
                By.xpath("//select[@id='dropdown']")
        );
        WebElement selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));


        Assert.assertEquals(selectedOption.getText(), "Please select an option");
        pageManager.dropDownHandlingByText(dropdown, "Option 2");
        selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));
        Assert.assertEquals(selectedOption.getText(), "Option 2");

    }

    /**
     * Test dropdown by using WebElement value Attribute
     */
    @Test
    public void TestDropDownHandlingByValue() {
        this.webDriver.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdown = this.webDriver.findElement(
                By.xpath("//select[@id='dropdown']")
        );
        WebElement selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));


        Assert.assertEquals(selectedOption.getText(), "Please select an option");
        pageManager.dropDownHandlingByValue(dropdown, "2");
        selectedOption = dropdown
                .findElement(By.xpath("//option[@selected='selected']"));
        Assert.assertEquals(selectedOption.getText(), "Option 2");
    }

    /**
     * Test the navigate capability
     */
    @Test
    public void TestNavigate() {
        String url = "https://the-internet.herokuapp.com/iframe";
        pageManager.navigate(url);
        Assert.assertEquals(this.webDriver.getCurrentUrl(), url);
    }

    /**
     * Test get title capability
     */
    @Test(dependsOnMethods = { "TestNavigate" })
    public void TestGetTitle() {
        Assert.assertEquals(pageManager.getTitle(), "The Internet");
    }

    /**
     * Test get driver capability
     */
    @Test
    public void TestGetDriver() {
        WebDriver driver = pageManager.getDriver();
        Assert.assertEquals(driver, this.webDriver);
    }

    /**
     * Test to see if browser we started is up
     */
    @Test
    public void TestGetBrowserFlag() {
        String browserName = pageManager.getBrowserFlag();
        Assert.assertEquals(browserName, "chrome");
    }

    /**
     * Test findBrokenImages capability
     */
    @Test
    public void TestBrokenImages() throws IOException {
        pageManager.navigate("https://the-internet.herokuapp.com/broken_images");
        WebElement imageContainer = this.webDriver.findElement(By.xpath("//div[@class='example']"));
        pageManager.findBrokenImages(imageContainer);

    }

    /**
     * Test findBrokenLinks Capability
     */
    @Test
    public void TestBrokenLinks() throws IOException {
        pageManager.navigate("https://the-internet.herokuapp.com/status_codes");
        WebElement listOfLinks = this.webDriver
                .findElement(By.xpath("//ul"));
        pageManager.findBrokenLinks(listOfLinks);
    }

    /**
     * Test getTextValue method
     */
    @Test(dependsOnMethods = { "TestNavigate" })
    public void TestGetTextValue() {
        String url = "https://the-internet.herokuapp.com/iframe";
        pageManager.navigate(url);
        WebElement heading = this.webDriver.findElement(By.xpath("//h3"));
        String heading_text = pageManager.getText(heading);
        Assert.assertEquals(heading_text, "An iFrame containing the TinyMCE WYSIWYG Editor");

    }


}
