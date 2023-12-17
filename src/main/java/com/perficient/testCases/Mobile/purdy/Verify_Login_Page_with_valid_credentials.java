package com.perficient.testCases.Mobile.purdy;

import java.util.Hashtable;
import com.perficient.util.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.perficient.baseclasses.purdy.CommonComponents;
import com.perficient.baseclasses.purdy.HomePage;
import com.perficient.baseclasses.purdy.LoginPage;
import com.perficient.util.ComplexReportFactory;
import com.perficient.util.TestCaseBase;
import com.aventstack.extentreports.Status;

public class Verify_Login_Page_with_valid_credentials extends TestCaseBase {


	@DataProvider
	public Object[][] get_Data_Verify_Login_Page_with_valid_credentials() 
	{
		return ExcelReader.getTestData("Purdy.xlsx", "LoginUser","Verify_Login_Page_with_valid_credentials");
	}

	@Test(groups= {"regression", "smokes","mobile"},priority=0, dataProvider = "get_Data_Verify_Login_Page_with_valid_credentials")
	public void customerLogin(Hashtable<String, String> dataTable) throws Exception 
	{
		CommonComponents comp = new CommonComponents(pageManager, excelReader);
		
		String env = environment;

		comp.open("purdy", env);

		pageManager.logToReport("Verify the home page title");
		customAssertion.assertEquals(dataTable.get("HomePageTitle"), pageManager.getTitle());

		Assert.assertEquals(ComplexReportFactory.getTest(testName).getStatus(), Status.PASS);
	}


	@Test(groups= {"regression", "smokes","mobile"},priority=1, dataProvider = "get_Data_Verify_Login_Page_with_valid_credentials")
	public void redirectToLoginPage(Hashtable<String, String> dataTable) throws Exception
	{
		HomePage homePage = new HomePage(pageManager, excelReader);

		pageManager.logToReport("Click on the SignIn link");
		customAssertion.assertTrue(homePage.verifySignInLinkDisplyedMob());
		
		pageManager.logToReport("Verify the login page title");
		customAssertion.assertEquals(dataTable.get("LoginPageTitle"), pageManager.getTitle());

		Assert.assertEquals(ComplexReportFactory.getTest(testName).getStatus(), Status.PASS);
	}
	
	@Test(groups= {"regression", "smokes","mobile"},priority=2, dataProvider = "get_Data_Verify_Login_Page_with_valid_credentials")
	public void homePageValidationAfterLogin(Hashtable<String, String> dataTable) throws Exception
	{
		HomePage homePage = new HomePage(pageManager, excelReader);
		LoginPage login = new LoginPage(pageManager, excelReader);
		
		pageManager.logToReport("Enter email in the email field");
		login.enterEmail(dataTable.get("EmailID"));
		
		pageManager.logToReport("Enter password in the password field");
		login.enterPassword(dataTable.get("Password"));
		
		pageManager.logToReport("Verify the user login successfully");
		customAssertion.assertTrue(homePage.verifyMyAccountTabDisplayMob());

		Assert.assertEquals(ComplexReportFactory.getTest(testName).getStatus(), Status.PASS);
	}


}
