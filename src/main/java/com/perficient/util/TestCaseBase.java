package com.perficient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.Status;
import com.itextpdf.text.DocumentException;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lingala.zip4j.exception.ZipException;

@Listeners({ TestMethodListener.class })
public class TestCaseBase {
	private com.aventstack.extentreports.ExtentTest test;
	private String browserName;
	protected String testName;
	protected String className;
	protected String userStoryName;
	protected String buildNumber;
	protected PageManager pageManager;
	protected ExcelReader excelReader;
	protected RestAPIUtility restApiutility;
    private WebDriver driver_original;
	protected String browserFlag;
	private String onGrid;
	private String host;
	private String port;
	private static int ieCountCurrent = 0;
	private static int firefoxCountCurrent = 0;
	private static int chromeCountCurrent = 0;
	private static int safariCountCurrent = 0;
	private LogWebdriverEventListener eventListener;
	protected Log log = LogFactory.getLog(this.getClass());
	protected EventFiringWebDriver driver;
	private String clientEmail;
	private String actualResult;
	private String testActualResult;
	private HashMap<String, String> expected;
	protected CustomAssertion customAssertion;
	//protected static String errorImagesPath="./target/errorImages"; 
	protected static String errorImagesPath="./errorImages";
	protected File errorImageFolder;
	protected String[] mailIds;
	protected String mailAddress; 
	protected String xmlFilesPath=System.getProperty("user.dir") + "/testdata/xmlfiles";
	protected File xmlFileFolder;
	protected File extentReportFileFolder;
	public Createpdfdocument pdf_new;
	protected String testPackageName;
	protected SoftAssert softAssert;
	public String environment;
	protected String elasticLocation;
	protected String elasticIp;
	protected String elasticPort;
	protected String elasticIndex;
	protected String elasticContentType;
	protected String projectCode;
	private String sampleSuite;
	public String newStatus;
	public BrowserMobProxy proxy;
	private String rPlatformName;
	private String rPlatformVersion;
	private String rBrowserVersion;
	private String rResolution;
	private String rLocation;

	@BeforeSuite(alwaysRun = true)
	public void generateBuildNumber() throws IOException, DocumentException {
		pdf_new = new Createpdfdocument();
		//pdf_new.openDoc();
		errorImageFolder = new File(errorImagesPath);
		if (!errorImageFolder.exists()) {
			errorImageFolder.mkdir();
		}
		FileUtils.cleanDirectory(errorImageFolder);
		xmlFileFolder = new File(xmlFilesPath);
		if (!xmlFileFolder.exists()) {
			xmlFileFolder.mkdir();
		}
		FileUtils.cleanDirectory(xmlFileFolder);

//		extentReportFileFolder = new File(ComplexReportFactory.returnPath);
//		if (extentReportFileFolder.exists()) {
//			FileUtils.cleanDirectory(extentReportFileFolder);
//		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd_HH.mm");
		buildNumber = df.format(new Date());//
	}


	@Parameters({"browserUsed", "runningOnGrid", "hubHost", "hubPort", "environment", "elasticIp",
			"elasticPort", "elasticIndex", "elasticContentType", "projectCode", "suiteReportName", "platformName",
			"platformVersion", "browserVersion", "resolution", "location"})
	@BeforeClass(alwaysRun = true)
	public void setUpBrowser( @Optional("chromeDesktop") String browserUsed, @Optional("false") String runningOnGrid,
			@Optional("localhost") String hubHost, @Optional("4444") String hubPort, @Optional("test") String environment,
			@Optional("10.128.190.5") String elasticIp, @Optional("9200") String elasticPort,
			@Optional("ngpautomation") String elasticIndex, @Optional("_doc") String elasticContentType,
			@Optional("testing") String projectCode, @Optional("AutomationExecution") String suiteReportName,
							  @Optional("Mac") String platformName, @Optional("macOS Catalina") String platformVersion,
							  @Optional("117") String browserVersion, @Optional("3840x216") String resolution,
							  @Optional("US East") String location) throws Exception {
	
		newStatus = null;
		log.info("Running setupBrowser for Test Class: " + this.getClass().getName());
		
		initParams(browserUsed, runningOnGrid, hubHost, hubPort, environment, elasticIp, elasticPort,
				elasticIndex, elasticContentType, projectCode,suiteReportName, platformName, platformVersion,
				browserVersion, resolution, location);

		if (hubHost.equals("setUpSelRemoteHost")) {
			setUpSelRemoteHost();
		}
		else{
			if (hubHost.equals("remoteHost")) {
				setUpRemoteHost();
			} else {
				selectBrowser();
			}
		}

		eventListener = new LogWebdriverEventListener();
		driver = new EventFiringWebDriver(driver_original);
		driver.register(eventListener);
		
		
		if (browserFlag.equals("ie") || browserFlag.equals("chrome") || browserFlag.equals("firefox") || browserFlag.equals("safari") || browserFlag.equals("edge")) 
//			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
			//pdf_new.defaultContent(sampleSuite, environment);
			setDefaultTestData();
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method caller) {
		
		String[] classes = caller.getDeclaringClass().getName().split("\\.");
		className = classes[classes.length - 1];
		userStoryName = classes[classes.length - 2];
		testName = browserFlag + "-" + className + "-" + caller.getName();
		final String description = "The Extent Report for:"+className;
		test = ComplexReportFactory.getTest(testName, className, description, sampleSuite, environment);
		test.log(Status.INFO, "Test Started!");
		softAssert = new SoftAssert();
		customAssertion = new CustomAssertion(driver, test, softAssert);
		pageManager = new PageManager(driver, browserFlag, environment, test);
		excelReader = new ExcelReader(test);
		restApiutility = new RestAPIUtility();
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method caller) {
		ComplexReportFactory.closeTest(testName);
		log.info(caller.getName() + " : " + test.getStatus());
		//Assert.assertSame(ComplexReportFactory.getTest(testName).getRunStatus(),  Status.PASS);  
		//Assert.assertEquals(ComplexReportFactory.getTest(testName).getStatus(), Status.PASS);
		if(newStatus != "fail")
		{
			newStatus = test.getStatus().toString();
		}
		Assert.assertEquals(ComplexReportFactory.getTest(testName).getStatus(), Status.PASS);
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws ZipException, DocumentException {
		TestData.load("mailreport.properties");
		clientEmail=TestData.get("recepientEmailIds");
		mailAddress= TestData.get("recepientEmailIds");
		ComplexReportFactory.closeReport();
		//pdf_new.closeDoc();
		zipAndMail();
	}
	


	private void selectBrowser() throws Exception {
		if (browserFlag.equals("ie")) {
			setUpIEWin64(onGrid);
		}else if (browserFlag.equals("firefoxDesktop"))
		{
			setUpFirefoxWithDefaultProfileDesktop(onGrid);
		} 
		else if (browserFlag.equals("firefoxMobile"))
		{
			setUpFirefoxWithDefaultProfileMobile(onGrid);
		} 
		else if(browserFlag.equals("edgeDesktop"))
		{
			setUpEdgeWin32Desktop(onGrid);
		}
		else if(browserFlag.equals("edgeMobile"))
		{
			setUpEdgeWin32DMobile(onGrid);
		}
		else if (browserFlag.equals("chromeDesktop"))
		{
			setUpChromeWin32Desktop(onGrid);
		} else if (browserFlag.equals("chromeMobile")) {
			setUpChromeWin32Mobile(onGrid);
		}
		else if (browserFlag.equals("chromeDesktopLoggedIn")) 
		{
			setUpChromeWin32DesktopLoggedIn(onGrid);
		} else if (browserFlag.equals("chromeMobileLoggedIn")) {
			setUpChromeWin32MobileLoggedIn(onGrid);
		}
		else if (browserFlag.equals("safariDesktop")) {
			setUpSafari(onGrid);
		}else if (browserFlag.equals("safariMobile")) {
			setUpSafariMob(onGrid);
		} else if (browserFlag.equals("random")) {
			setUpRandomBrowserPerCaseDesktop(onGrid);
		} else if (browserFlag.equals("random")) {
			setUpRandomBrowserPerCaseMobile(onGrid);
		} else if (browserFlag.equals("percentage_specified")) {
			setupBrowserPerPercentageDesktop();
		} else if (browserFlag.equals("percentage_specified")) {
			setupBrowserPerPercentageMobile();
		} else if (browserFlag.equals("mobileByCBT")) {
			setMobileByCBT();
		} else if (browserFlag.equals("mobileByBrowserStack")) {
			setMobileByBrowserStack();
		}
		else if (browserFlag.equals("appium")) {
			setUpAppium();
		}
		else if (browserFlag.equals("chromeHeadless")) {
			setUpChromeHeadless(onGrid);
		}
		else if (browserFlag.equals("firefoxHeadless")) {
			setUpFirefoxHeadless(onGrid);
		}
		else if (browserFlag.equals("edgeHeadless")) {
			setUpEdgeHeadless(onGrid);
		}
		else if (browserFlag.equals("chromeWebInapp")) {
			setUpChromeWebInApp(onGrid);
		}
	}

	private void setupBrowserPerPercentageDesktop() throws Exception {
		Properties PROPERTIES_RESOURCES = SystemUtil.loadPropertiesResources("/browser-percentage.properties");
		String ie = PROPERTIES_RESOURCES.getProperty("ie.percentage");
		String firefox = PROPERTIES_RESOURCES.getProperty("firefox.percentage");
		String chrome = PROPERTIES_RESOURCES.getProperty("googlechrome.percentage");
		int testcaseCount = Integer.parseInt(PROPERTIES_RESOURCES.getProperty("testcase.count"));
		newBrowserPerPercentageDesktop(ie, firefox, chrome, testcaseCount, onGrid);
	}
	
	private void setupBrowserPerPercentageMobile() throws Exception {
		Properties PROPERTIES_RESOURCES = SystemUtil.loadPropertiesResources("/browser-percentage.properties");
		String ie = PROPERTIES_RESOURCES.getProperty("ie.percentage");
		String firefox = PROPERTIES_RESOURCES.getProperty("firefox.percentage");
		String chrome = PROPERTIES_RESOURCES.getProperty("googlechrome.percentage");
		int testcaseCount = Integer.parseInt(PROPERTIES_RESOURCES.getProperty("testcase.count"));
		newBrowserPerPercentageMobile(ie, firefox, chrome, testcaseCount, onGrid);
	}

	private void initParams(String browserUsed, String runningOnGrid, String hubHost, String hubPort, String env, String elasticIp, String elasticPort, String elasticIndex,
			String elasticContentType, String projectCode,String sampleSuitename, String platformName,
							String platformVersion, String browserVersion, String resolution, String location) {
		
		browserFlag = browserUsed;
		onGrid = runningOnGrid;
		host = hubHost;
		port = hubPort;
		environment = env;
		sampleSuite = sampleSuitename;
		rPlatformName = platformName;
		rPlatformVersion = platformVersion;
		rBrowserVersion = browserVersion;
		rResolution = resolution;
		rLocation = location;
		this.projectCode = System.getProperty("projectCode") != null ? System.getProperty("projectCode") : projectCode;
		this.elasticIp = System.getProperty("elasticIp") != null ? System.getProperty("elasticIp") : elasticIp;
		this.elasticPort = System.getProperty("elasticPort") != null ? System.getProperty("elasticPort") : elasticPort;
		this.elasticIndex = System.getProperty("elasticIndex") != null ? System.getProperty("elasticIndex")
				: elasticIndex;
		this.elasticContentType = System.getProperty("elasticContentType") != null
				? System.getProperty("elasticContentType")
				: elasticContentType;
		this.elasticLocation = "http://" + this.elasticIp + ":" + this.elasticPort + "/" + this.elasticIndex + "/"
				+ this.elasticContentType + "/";
		actualResult = null;
		expected = new HashMap<String, String>();

		log.info("onGrid=" + runningOnGrid);
		log.info("environment=" + environment);
		log.info("browserFlag=" + browserFlag);
		log.info("elasticLocation=" + elasticLocation);
		log.info("projectCode=" + projectCode);
		
		if (!onGrid.equals("false")) {
			log.info("hubHost=" + hubHost);
			log.info("hubPort=" + hubPort);
		}
	}

	/**
	 * Objective: Randomize the browser for each test case
	 *
	 * @param onGrid
	 * @throws Exception
	 *             Updated by colin @2013-10-24, now this method will not be
	 *             directly used it only be called withthin setUpBrowser when
	 *             browserFlag in testNG.xml is set to 'random'
	 */
	private void setUpRandomBrowserPerCaseDesktop(String onGrid) throws Exception {
		log.info("Setting up random browser...");
		Random rndObj = new Random();
		int rndBrowserIndex = rndObj.nextInt(3);
		if (rndBrowserIndex == 0) {
			setUpIEWin64(onGrid);
			browserFlag = "ie";
		} else if (rndBrowserIndex == 1) {
			setUpFirefoxWithDefaultProfile(onGrid);
			browserFlag = "firefox";
		} else if (rndBrowserIndex == 2) {
			setUpChromeWin32Desktop(onGrid);
			browserFlag = "chrome";
		} else {
			log.error("Random select browser fails");
			throw new Exception("No browser is specified for the random number: " + rndBrowserIndex + ".");
		}
	}
	
	/**
	 * Objective: Randomize the browser for each test case
	 *
	 * @param onGrid
	 * @throws Exception
	 *             Updated by colin @2013-10-24, now this method will not be
	 *             directly used it only be called withthin setUpBrowser when
	 *             browserFlag in testNG.xml is set to 'random'
	 */
	private void setUpRandomBrowserPerCaseMobile(String onGrid) throws Exception {
		log.info("Setting up random browser...");
		Random rndObj = new Random();
		int rndBrowserIndex = rndObj.nextInt(3);
		if (rndBrowserIndex == 0) {
			setUpIEWin64(onGrid);
			browserFlag = "ie";
		} else if (rndBrowserIndex == 1) {
			setUpFirefoxWithDefaultProfile(onGrid);
			browserFlag = "firefox";
		} else if (rndBrowserIndex == 2) {
			setUpChromeWin32Mobile(onGrid);
			browserFlag = "chrome";
		} else {
			log.error("Random select browser fails");
			throw new Exception("No browser is specified for the random number: " + rndBrowserIndex + ".");
		}
	}

	/**
	 * Objective: Set up the browser per percentage by different browsers
	 *
	 * @param iePercentage
	 *            : The percentage of test cases which to be executed in IE
	 * @param firefoxPercentage
	 *            :The percentage of test cases which to be executed in Firefox
	 * @param chromePercentage
	 *            : The percentage of test cases which to be executed in chrome
	 * @param testCaseCount
	 *            : Total count of test cases
	 * @param onGrid
	 * @throws Exception
	 */
	private void newBrowserPerPercentageDesktop(String iePercentage, String firefoxPercentage, String chromePercentage,
			int testCaseCount, String onGrid) throws Exception {
		log.info("Setting up browser per percentage: ie=" + iePercentage + " firefox=" + firefoxPercentage + " chrome="
				+ chromePercentage + " test case count=" + testCaseCount);
		// Convert the percentage to float
		float iePercent = new Float(iePercentage.substring(0, iePercentage.indexOf("%"))) / 100;
		float firefoxPercent = new Float(firefoxPercentage.substring(0, firefoxPercentage.indexOf("%"))) / 100;
		// Get the rounded ieMaxCount, if ieMaxCount<1, plus 1
		int ieMaxCount = Math.round(iePercent * testCaseCount);
		if (ieMaxCount < 1) {
			ieMaxCount = 1;
		}
		// Get the rounded firefoxMaxCount, if ieMaxCount<1, plus 1
		int firefoxMaxCount = Math.round(firefoxPercent * testCaseCount);
		if (firefoxMaxCount < 1) {
			firefoxMaxCount = 1;
		}
		// Get the chromeMaxCount by math
		int chromeMaxCount = testCaseCount - ieMaxCount - firefoxMaxCount;
		// set up the browser by the specified percentage
		if (ieCountCurrent < ieMaxCount) {
			setUpIEWin64(onGrid);
			browserFlag = "ie";
			ieCountCurrent++;
		} else if (ieCountCurrent == ieMaxCount && firefoxCountCurrent < firefoxMaxCount) {
			setUpFirefoxWithDefaultProfileDesktop(onGrid);
			browserFlag = "firefoxDesktop";
			firefoxCountCurrent++;
		} 
		else if (ieCountCurrent == ieMaxCount && firefoxCountCurrent < firefoxMaxCount) {
			setUpFirefoxWithDefaultProfileMobile(onGrid);
			browserFlag = "firefoxMobile";
			firefoxCountCurrent++;
		}else if (ieCountCurrent == ieMaxCount && firefoxCountCurrent == firefoxMaxCount
				&& chromeCountCurrent < chromeMaxCount) {
			setUpChromeWin32Desktop(onGrid);
			browserFlag = "chrome";
			chromeCountCurrent++;
		} else {
			throw new Exception("The current ieCount:" + ieCountCurrent + ", firefoxCount:" + firefoxCountCurrent
					+ "and chromeCount: " + chromeCountCurrent + " doesn't fit the conditions");
		}
		log.info("ie Count Current=" + ieCountCurrent);
		log.info("ie Count Max=" + ieMaxCount);
		log.info("firefox Count Current=" + firefoxCountCurrent);
		log.info("firefox Count Max=" + firefoxMaxCount);
		log.info("googlechrome Count Current=" + chromeCountCurrent);
		log.info("googlechrome Count Max=" + chromeMaxCount);
	}
	
	
	/**
	 * Objective: Set up the browser per percentage by different browsers
	 *
	 * @param iePercentage
	 *            : The percentage of test cases which to be executed in IE
	 * @param firefoxPercentage
	 *            :The percentage of test cases which to be executed in Firefox
	 * @param chromePercentage
	 *            : The percentage of test cases which to be executed in chrome
	 * @param testCaseCount
	 *            : Total count of test cases
	 * @param onGrid
	 * @throws Exception
	 */
	private void newBrowserPerPercentageMobile(String iePercentage, String firefoxPercentage, String chromePercentage,
			int testCaseCount, String onGrid) throws Exception {
		log.info("Setting up browser per percentage: ie=" + iePercentage + " firefox=" + firefoxPercentage + " chrome="
				+ chromePercentage + " test case count=" + testCaseCount);
		// Convert the percentage to float
		float iePercent = new Float(iePercentage.substring(0, iePercentage.indexOf("%"))) / 100;
		float firefoxPercent = new Float(firefoxPercentage.substring(0, firefoxPercentage.indexOf("%"))) / 100;
		// Get the rounded ieMaxCount, if ieMaxCount<1, plus 1
		int ieMaxCount = Math.round(iePercent * testCaseCount);
		if (ieMaxCount < 1) {
			ieMaxCount = 1;
		}
		// Get the rounded firefoxMaxCount, if ieMaxCount<1, plus 1
		int firefoxMaxCount = Math.round(firefoxPercent * testCaseCount);
		if (firefoxMaxCount < 1) {
			firefoxMaxCount = 1;
		}
		// Get the chromeMaxCount by math
		int chromeMaxCount = testCaseCount - ieMaxCount - firefoxMaxCount;
		// set up the browser by the specified percentage
		if (ieCountCurrent < ieMaxCount) {
			setUpIEWin64(onGrid);
			browserFlag = "ie";
			ieCountCurrent++;
		} else if (ieCountCurrent == ieMaxCount && firefoxCountCurrent < firefoxMaxCount) {
			setUpFirefoxWithDefaultProfile(onGrid);
			browserFlag = "firefox";
			firefoxCountCurrent++;
		} else if (ieCountCurrent == ieMaxCount && firefoxCountCurrent == firefoxMaxCount
				&& chromeCountCurrent < chromeMaxCount) {
			setUpChromeWin32Mobile(onGrid);
			browserFlag = "chrome";
			chromeCountCurrent++;
		} else {
			throw new Exception("The current ieCount:" + ieCountCurrent + ", firefoxCount:" + firefoxCountCurrent
					+ "and chromeCount: " + chromeCountCurrent + " doesn't fit the conditions");
		}
		log.info("ie Count Current=" + ieCountCurrent);
		log.info("ie Count Max=" + ieMaxCount);
		log.info("firefox Count Current=" + firefoxCountCurrent);
		log.info("firefox Count Max=" + firefoxMaxCount);
		log.info("googlechrome Count Current=" + chromeCountCurrent);
		log.info("googlechrome Count Max=" + chromeMaxCount);
	}

	/**
	 * Objective: Close the opened browser which was opened by WebDriver
	 */
	/*@AfterClass(alwaysRun = true)
	public void tearDown(ITestContext context) throws Exception {
		String[] property = System.getProperty("user.dir").split("\\\\");
		String projectName = property[property.length - 1];
		String testCaseName = className.substring(className.lastIndexOf(".") + 1);
		Set<ITestResult> result = context.getFailedTests().getAllResults();
		if (result.isEmpty()) {
			actualResult = "PASS";
		} else {
			actualResult = "FAIL";
		}
		log.info(buildNumber + " " + projectName + " " + userStoryName + " " + testCaseName + " " + browserFlag + " "
				+ actualResult);
		// Thread.sleep(4000);
		driver.quit();
		pdf_new = new Createpdfdocument();
		//pdf_new.iTextPDF(className, newStatus);
		// if (OS.isFamilyWindows())
		// Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");

	}*/

	private void setUpFirefoxWithDefaultProfile(String onGrid) throws Exception {
		FirefoxOptions options = new FirefoxOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
		
		if (onGrid.equals("false")) {
			/* Use below traditional way to set browser binary if there is any download restriction on execution machine
			 * And comment out "WebDriverManager" command
			 * 
			 * File file = new File("./lib/geckodriver.exe");
			 * System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
			 */
			
			options.merge(capability);
			
			options.addArguments("--headless=new"); //Set it to true to use the firefox as headless browsers
			driver_original = new FirefoxDriver(options);
			} else {
			options.merge(capability);
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}

	}

	// Below code copied from boost
//	private void setUpFirefoxWithDefaultProfile(String onGrid) throws Exception {
//		if (onGrid.equals("false")) {

//			FirefoxOptions options = new FirefoxOptions();
//			options.setHeadless(true);
//			// System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
//			// "firefoxLog");
//			// java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
//			// WebDriverManager.firefoxdriver().version("73").setup(); Use this if you want
//			// to use any specific browser version
//			driver_original = new FirefoxDriver(options);
//		} 
//		else 
//		{
//			System.out.println("in Else Block");
////			DesiredCapabilities capability = DesiredCapabilities.firefox();
////			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
//		}
//	}
	
	private void setUpFirefoxHeadless(String onGrid) throws Exception {
		FirefoxOptions options = new FirefoxOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
		Boolean FirefoxOptionHeadless = true;
		if (onGrid.equals("false")) {
		
			
			if (FirefoxOptionHeadless) {
				// Set Firefox Headless mode as TRUE			
				options.addArguments("--headless=new");
			}
			driver_original = new FirefoxDriver(options);

		} else {
			
			  driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
	}

	private void setUpSafari(String onGrid) throws Exception {
		SafariOptions options = new SafariOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		if (onGrid.equals("false")) {
			driver_original = new SafariDriver(options);
		} else {
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
		driver_original.manage().window().maximize();
	}

	private void setUpSafariMob(String onGrid) throws Exception {
		SafariOptions options = new SafariOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		if (onGrid.equals("false")) {
			driver_original = new SafariDriver(options);
		} else {
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
		Dimension dem = new Dimension(390,844);
		driver_original.manage().window().setSize(dem);
	}

	private void setUpIEWin64(String onGrid) throws Exception {
		InternetExplorerOptions options = new InternetExplorerOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		if (onGrid.equals("false")) {
			/* Use below traditional way to set browser binary if there is any download restriction on execution machine
			 * And comment out "WebDriverManager" command
			 * 
			 * File file = new File("./lib/IEDriverServer.exe");
			 * System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			 */
			
			//Below command launch browser driver using WebDriverManager. So no need to manage driver related settings with manually
			//WebDriverManager.iedriver().browserInDocker().enableVnc().enableRecording();
			//WebDriverManager.iedriver().version("10").setup(); Use this if you want to use any specific browser version
			
			driver_original = new InternetExplorerDriver(options);
		} else {
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
	}
	
	private void setUpEdgeWin32Desktop(String onGrid) throws Exception {
		EdgeOptions options = new EdgeOptions();
		//options.addArguments("--headless");
		options.addArguments("--remote-allow-origins=*");
		if (onGrid.equals("false")) {
			
			try {
				driver_original = new EdgeDriver(options);
			}
			catch (Exception e) {
				String path = System.getProperty("user.dir");
				if (path.startsWith("/")) {
					System.setProperty("webdriver.edge.driver", path + "/lib/msedgedriver.exe");
				} else {
					System.setProperty("webdriver.edge.driver", path + "\\lib\\msedgedriver.exe");
				}
				//System.out.println("Starting Edge");
				driver_original = new EdgeDriver(options);
			}
		} else {
			//driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
		driver_original.manage().window().maximize();
		
	}
	
	private void setUpEdgeWin32DMobile(String onGrid) throws Exception {
		
		EdgeOptions options = new EdgeOptions();
        //options.addArguments("--headless");
		options.addArguments("--remote-allow-origins=*");
		Dimension dem = new Dimension(390,844);
		try {
			driver_original = new EdgeDriver(options);
		}
		catch (Exception e) {
			String path = System.getProperty("user.dir");
			if (path.startsWith("/")) {
				System.setProperty("webdriver.edge.driver", path + "/lib/msedgedriver.exe");
			} else {
				System.setProperty("webdriver.edge.driver", path + "\\lib\\msedgedriver.exe");
			}
			//System.out.println("Starting Edge");
			driver_original = new EdgeDriver(options);
		}
		driver_original.manage().window().setSize(dem);
		
	}
	
	private void setUpEdgeHeadless(String onGrid) throws Exception{
		EdgeOptions options = new EdgeOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(EdgeOptions.CAPABILITY, options);
		Boolean edgeOptionHeadless = true; 
		if (onGrid.equals("false")) {
	
		// Below command launch browser driver using WebDriverManager. So no need to
		// manage driver related settings with manually
		
		//ChromeOptions options = new ChromeOptions();
		
		  if (edgeOptionHeadless) { 
			 // options.addArguments("--whitelisted-ips");
			  options.addArguments("no-sandbox");
			  options.addArguments("disable-dev-shm-usage");
			  options.addArguments("headless"); 
			  options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36"); 
			  options.addArguments("--headless=new"); 
			  options.addArguments("window-size=1280,800");
			  options.addArguments("start-maximized"); 
			  options.addArguments("ignore-certificate-errors"); //newly added
		 }

		  	options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.merge(capability);
		driver_original = new EdgeDriver(options);
		} 
		else {
			options.merge(capability);
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
	}
	
//	private void setUpChromeWin32(String onGrid) throws Exception {

//		ChromeOptions options = new ChromeOptions();
//		DesiredCapabilities capability = new DesiredCapabilities();
//		capability.setCapability(ChromeOptions.CAPABILITY, options);
//		options.merge(capability);
//
//		if (onGrid.equals("false")) {
//			/* Use below traditional way to set browser binary if there is any download restriction on execution machine
//			 * And comment out "WebDriverManager" command
//			 * 
//			 * File file = new File("./lib/chromedriver.exe");
//			 * System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//			 */
//			
//			//Below command launch browser driver using WebDriverManager. So no need to manage driver related settings with manually
//			
//			//WebDriverManager.chromedriver().version("78.0.3904.105").setup(); Use this if you want to use any specific browser version 
//			//driver_original = new ChromeDriver();
//			
//			options.setAcceptInsecureCerts(true);
//			options.merge(capability);
//			
//			options.setHeadless(true); //Set it to true to use the chrome as headless browsers
//
//			driver_original = new ChromeDriver(options);
//		} else {
//			options.merge(capability);
//			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability)//;
//		}
//	}
	
	
	
	
	
	

	private void setUpChromeWin32Desktop(String onGrid) throws Exception {
		ChromeOptions options = new ChromeOptions();

		//options.addArguments("--verbose");
		//options.addArguments("--whitelisted-ips");
		//options.setBinary("/opt/google/chrome/chrome");
        //options.addArguments("--headless");
        options.addArguments("--no-sandbox");
		options.addArguments("--window-size=1920x1080");
        options.addArguments("--start-maximized");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
		options.setAcceptInsecureCerts(true);
		driver_original = new ChromeDriver(options);
		driver_original.manage().window().maximize();	
	}
	
	private void setUpChromeWin32DesktopLoggedIn(String onGrid) throws Exception {
		
		ChromeOptions options = new ChromeOptions();
		
		//options.addArguments("--verbose");
		//options.addArguments("--whitelisted-ips");
		//options.setBinary("/opt/google/chrome/chrome");
        //options.addArguments("--headless");
        options.addArguments("--no-sandbox");
		options.addArguments("--window-size=1920x1080");
        options.addArguments("--start-maximized");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--remote-allow-origins=*");
		options.setAcceptInsecureCerts(true);
		driver_original = new ChromeDriver(options);
		driver_original.manage().window().maximize();
		
		
	}
	private void setUpChromeWin32MobileLoggedIn(String onGrid) throws Exception {
		
		
		ChromeOptions options = new ChromeOptions();
		
		//options.addArguments("--verbose");
		//options.add_argument("--remote-debugging-address=0.0.0.0");
		//options.addArguments("--whitelisted-ips");
		//options.setBinary("/opt/google/chrome/chrome");
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		Dimension dem = new Dimension(390, 844);
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--remote-allow-origins=*");
		driver_original = new ChromeDriver(options);
		driver_original.manage().window().setSize(dem);
	}
	private void setUpChromeWebInApp(String onGrid) throws Exception {
		
		
		proxy = new BrowserMobProxyServer();
	 	proxy.start(0);
	 	Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
	 	
		ChromeOptions options = new ChromeOptions();
		
		//options.addArguments("--verbose");
		options.addArguments("--whitelisted-ips");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
		options.addArguments("--window-size=1920x1080");
        options.addArguments("--start-maximized");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
		options.setAcceptInsecureCerts(true);
		options.setCapability(CapabilityType.PROXY, seleniumProxy);
		driver_original = new ChromeDriver(options);
		driver_original.manage().window().maximize();
		
	}

	
	private void setUpChromeWin32Mobile(String onGrid) throws Exception {

		ChromeOptions options = new ChromeOptions();
		
		//options.addArguments("--verbose");
		//options.add_argument("--remote-debugging-address=0.0.0.0");
		//options.addArguments("--whitelisted-ips");
		//options.setBinary("/opt/google/chrome/chrome");
		//options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		Dimension dem = new Dimension(390, 844);
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
		driver_original = new ChromeDriver(options);
		driver_original.manage().window().setSize(dem);
	}
	
	
//Below code is for the setting up the browser for the mobile view
//	private void setUpChromeWin32(String onGrid) throws Exception {
//		ChromeOptions options = new ChromeOptions();
//		//options.addArguments("--headless");
//		options.addArguments("--no-sandbox");
//		//options.addArguments("--window-size=1920x1080");
//		Dimension dem = new Dimension(390,844);
//		//pageManager.getDriver().manage().window().setSize(dem);
//		options.addArguments("--disable-dev-shm-usage");
//		options.addArguments("--disable-popup-blocking");
//		options.addArguments("--remote-allow-origins=*");
//		driver_original = new ChromeDriver(options);
//		driver_original.manage().window().setSize(dem);
//		//driver_original.manage().window().maximize();
//		}


	private void setUpChromeHeadless(String onGrid) throws Exception{
		ChromeOptions options = new ChromeOptions();
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability(ChromeOptions.CAPABILITY, options);
		Boolean chromeOptionHeadless = true; 
		if (onGrid.equals("false")) {
	
		// Below command launch browser driver using WebDriverManager. So no need to
		// manage driver related settings with manually
		
		//ChromeOptions options = new ChromeOptions();
		
		  if (chromeOptionHeadless) { 
			 
			 //options.addArguments("--verbose");
			 // options.addArguments("--whitelisted-ips");
			  options.addArguments("no-sandbox");
			  options.addArguments("disable-dev-shm-usage");
			  options.addArguments("headless"); 
			  options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36"); 
			  options.addArguments("--headless=new");
			  options.addArguments("window-size=1280,800");
			  options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
			  options.addArguments("start-maximized"); 
			  options.addArguments("ignore-certificate-errors"); //newly added
		 }

		  	options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.merge(capability);
		driver_original = new ChromeDriver(options);
		} 
		else {
			options.merge(capability);
			driver_original = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capability);
		}
	}
	
	
	private void setMobileByCBT() throws MalformedURLException {
		String username = "";// Your username
		String authkey = "";// Your authkey
		String URL = "http://" + username + ":" + authkey + "@hub.crossbrowsertesting.com:80/wd/hub";
		
		// Created object of DesiredCapabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("name", "Mobile Device Testing");
		caps.setCapability("browserName", "Safari");
		caps.setCapability("deviceName", "iPhone X Simulator");
		caps.setCapability("platformVersion", "11.0");
		caps.setCapability("platformName", "iOS");
		caps.setCapability("deviceOrientation", "portrait");
		caps.setCapability("record_video", "true");

		driver_original = new RemoteWebDriver(new URL(URL), caps);
	}
	
	private void setMobileByBrowserStack() throws MalformedURLException {
		String USERNAME = "";// Your username
		String AUTOMATE_KEY = "";// Your authkey
		String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
		
		// Created object of DesiredCapabilities
		DesiredCapabilities caps = new DesiredCapabilities();
	    caps.setCapability("browserName", "iPhone");
	    caps.setCapability("device", "iPhone 11 Pro");
	    caps.setCapability("realMobile", "true");
	    caps.setCapability("os_version", "13");
	    caps.setCapability("name", "Browser Stack Sample Test");
	    caps.setCapability("build", "v2");
	    caps.setCapability("project", "Browser Stack Example");
	    caps.setCapability("acceptSslCerts", "true");

	    driver_original = new RemoteWebDriver(new URL(URL), caps);
	}
	
	private void setUpAppium() throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("BROWSER_NAME", "Android");
		capabilities.setCapability("VERSION", "1.0.41"); 
		capabilities.setCapability("deviceName","37d5e2e1");
		capabilities.setCapability("platformName","Android");
		// This package name of your app
		capabilities.setCapability("appPackage", "com.android.calculator2");
		// This is Launcher activity of your app
		capabilities.setCapability("appActivity","com.android.calculator2.Calculator");
		Thread.sleep(300);
		driver_original = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);	
		Thread.sleep(3000);		
	}
	

	/**
	 * Objective: set the default test data file name 1.get the current test
	 * case class name and use this name to get a test data file name. For
	 * example, a class "TC01BingWebSearch_High" will get a testdata file name
	 * "testdata_TC_01_BingWebSearch" 2.load this properties file
	 */
	private void setDefaultTestData() {
		String s = this.getClass().getName();
		String filename = ("testdata_" + s.split("\\.")[s.split("\\.").length - 1] + ".properties");
		log.debug("Setting TestData file = " + filename);
		TestData.load(filename);
	}
	
	private void zipAndMail() throws ZipException
	{
		//Convert the report to zip.
		ZipAndMail.convertToZip();
		mailIds = splitMailAddress(mailAddress);
		//Send the zip file of report in email. 
		for (String clientEmail : mailIds) {
			clientEmail.trim();
			//ZipAndMail.sendMail(clientEmail);
		}
	}
	
	protected WebDriver getDriver() {
		return driver;
	}
	
	public WebDriver getRemoteDriver() {
			return driver_original;
	}
	
	public String getElasticLocation() {
		return elasticLocation;
	}

	public String getProjectCode() {
		return projectCode;
	}
	
	private String[] splitMailAddress(String mailAddress){
		mailIds = mailAddress.split(",");
		return mailIds;
	}
	
	private void setUpFirefoxWithDefaultProfileDesktop(String onGrid) throws Exception 
	{
		
			//Set Firefox Headless mode as TRUE
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--whitelisted-ips");
			//options.addArguments("--headless");
			options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
			//System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "firefoxLog"); java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

			//WebDriverManager.firefoxdriver().version("73").setup(); Use this if you want to use any specific browser version
			options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
			driver_original = new FirefoxDriver(options);
			driver_original.manage().window().maximize();

		 
	}


	private void setUpFirefoxWithDefaultProfileMobile(String onGrid) throws Exception 
	{	
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--whitelisted-ips");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
		Dimension dem = new Dimension(390,844);
		
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("user-agent=test-automation-96cbdf45-2232-48cd-be0c-ceb7942126b0");
		driver_original = new FirefoxDriver(options);
		
		driver_original.manage().window().setSize(dem);	
	}

	// method makes connection to remote server (using user's securityToken), starts up a machine (using platformName,
// platformVersion, location, and resolution), and opens a web browser (using browserName, browserVersion).
	private void setUpRemoteHost() throws Exception {
		String url = "https://dish-public.perfectomobile.com/nexperience/perfectomobile/wd/hub";
		Map<String, Object> remoteOptions = new HashMap<>();

		// determine and save the browserName
		String browserName = browserFlag;
		browserName = browserName.replace("Desktop", "").replace("desktop", "");
		browserName = browserName.replace("Mobile", "").replace("mobile", "");
		browserName = browserName.substring(0, 1).toUpperCase() + browserName.substring(1);
		remoteOptions.put("browserName", browserName);

		// set securityToken
		String securityToken = "";
		// determine if on Windows or Mac and set separator either \ for Windows or / for Mac
		String path = System.getProperty("user.dir");
		String separator = "\\";
		if (path.startsWith("/"))
			separator = "/";
		// open securityToken.txt file that is in the testdata folder and set 1st line contents to be securityToken
		String securityTokenLocation = path + separator + "testdata" + separator + "securityToken.txt";
		File securityTokenFile = new File(securityTokenLocation);
		FileReader fileReader = new FileReader(securityTokenFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		securityToken = bufferedReader.readLine();
		// set securityToken
		remoteOptions.put("securityToken", securityToken);

		remoteOptions.put("platformVersion", rPlatformVersion);
		remoteOptions.put("location", rLocation);
		remoteOptions.put("resolution", rResolution);

		boolean validBrowser = false;
		if (browserName.equals("Chrome")) {
			ChromeOptions browserOptions = new ChromeOptions();
			browserOptions.setPlatformName(rPlatformName);
			browserOptions.setBrowserVersion(rBrowserVersion);
			browserOptions.setCapability("perfecto:options", remoteOptions);
			driver_original = new RemoteWebDriver(new URL(url), browserOptions);
			validBrowser = true;
		}
		if (browserName.equals("Firefox")) {
			FirefoxOptions browserOptions = new FirefoxOptions();
			browserOptions.setPlatformName(rPlatformName);
			browserOptions.setBrowserVersion(rBrowserVersion);
			browserOptions.setCapability("perfecto:options", remoteOptions);
			driver_original = new RemoteWebDriver(new URL(url), browserOptions);
			validBrowser = true;
		}
		if (browserName.equals("Safari")) {
			SafariOptions browserOptions = new SafariOptions();
			browserOptions.setPlatformName(rPlatformName);
			browserOptions.setBrowserVersion(rBrowserVersion);
			browserOptions.setCapability("perfecto:options", remoteOptions);
			driver_original = new RemoteWebDriver(new URL(url), browserOptions);
			validBrowser = true;
		}
		if (browserName.equals("Edge")) {
			EdgeOptions browserOptions = new EdgeOptions();
			browserOptions.setPlatformName(rPlatformName);
			browserOptions.setBrowserVersion(rBrowserVersion);
			browserOptions.setCapability("perfecto:options", remoteOptions);
			driver_original = new RemoteWebDriver(new URL(url), browserOptions);
			validBrowser = true;
		}

		if (!validBrowser) {
			pageManager.testfail(browserFlag + " is not a valid browser.");
		}
	}

	private void setUpSelRemoteHost() throws Exception {
		Map<String, Object> perfectoOptions = new HashMap<>();

		ChromeOptions browserOptions = new ChromeOptions();
		browserOptions.setPlatformName("Mac");
		browserOptions.setBrowserVersion("119");

		perfectoOptions.put("platformVersion", "macOS Ventura");
		perfectoOptions.put("browserName", "Chrome");
		perfectoOptions.put("location", "NA-US-BOS");
		perfectoOptions.put("resolution", "1440x900");
		perfectoOptions.put("scriptName", "MAC-Monterey-Webtest-S4-Chrome");

		String myToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5OGQzMWI0OC1mODUyLTQ4ZTItOTgwMy03MmRhM2UyZDc3N2EifQ.eyJpYXQiOjE3MDE4NzY2OTEsImp0aSI6IjllY2YzZjY1LTQxZTctNDBhYS1hZGU1LTVjOTNjODE2OGYyMCIsImlzcyI6Imh0dHBzOi8vYXV0aDgucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL2Rpc2gtcHVibGljLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDgucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL2Rpc2gtcHVibGljLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6IjZlM2NiNDc4LWIzMTktNGZlZS05N2ZjLTcxYTU0NjE1YTk5MiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiMTc4Zjc1NWYtNDRmYS00MzUzLTgyMWYtYTVkYzc4MTViNWNmIiwic2Vzc2lvbl9zdGF0ZSI6ImYyZTZiOGU0LTgzOWYtNDlhYS1iNzA0LTc3MTYyY2ZkOTAzMyIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgb2ZmbGluZV9hY2Nlc3MgZW1haWwifQ.sC0zrBxBCmDAAiKMzniyjxk8m1_UnQ89-_qxohCW9Fs";
		perfectoOptions.put("securityToken", myToken);
		browserOptions.setCapability("perfecto:options", perfectoOptions);
		/*
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);

		// set platformName, and platformVersion
		capabilities.setCapability("platformName", "Mac");
		capabilities.setCapability("perfecto:platformVersion", "macOS Ventura");
		capabilities.setCapability("browserName", "Safari");
		capabilities.setCapability("browserVersion", "16");
		capabilities.setCapability("perfecto:location", "NA-US-BOS");
		capabilities.setCapability("perfecto:resolution", "800x600");
		capabilities.setCapability("perfecto:securityToken", "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI5OGQzMWI0OC1mODUyLTQ4ZTItOTgwMy03MmRhM2UyZDc3N2EifQ.eyJpYXQiOjE3MDAyMjY5MDEsImp0aSI6IjQwODRjYzBlLWQ5ZjQtNGVlMC05NzlmLWRiMWQwZmE4NDNiMiIsImlzcyI6Imh0dHBzOi8vYXV0aDgucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL2Rpc2gtcHVibGljLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDgucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL2Rpc2gtcHVibGljLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6IjZlM2NiNDc4LWIzMTktNGZlZS05N2ZjLTcxYTU0NjE1YTk5MiIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiNDIwYTcxM2MtMjk0My00NDY3LThlZmMtMTMyOWJkYzg3YTQxIiwic2Vzc2lvbl9zdGF0ZSI6Ijg4NTM4MmFmLWNlYjYtNDJmYy1iZTNjLTI2ZGM5MjZhYmQzYSIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgb2ZmbGluZV9hY2Nlc3MgZW1haWwifQ.ItELD77rkH-ecq9PLAsEIUQsP9YaCU2vIngakcRhXPg");
*/
		driver_original = new RemoteWebDriver(new URL("https://dish-public.perfectomobile.com/nexperience/perfectomobile/wd/hub"), browserOptions);
	}

}

