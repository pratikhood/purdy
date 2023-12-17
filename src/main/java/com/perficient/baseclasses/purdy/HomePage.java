package com.perficient.baseclasses.purdy;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import com.perficient.util.ExcelReader;
import com.perficient.util.PageManager;
import com.perficient.util.PageObject;

public class HomePage extends PageObject {

	public String manageExtrasLabel;
	float amount;

	@FindBy(xpath = "//a[normalize-space()='Sign In']")
	public WebElement SignInLink_xapth;

	public String SignInLink__xapth = "//a[normalize-space()='Sign In']";
	
	@FindBy(xpath = "//span[@id='header-myaccount-link']")
	public WebElement myAccountTab_xapth;
	
	public String myAccountTab__xapth = "//span[@id='header-myaccount-link']";
	
	@FindBy(xpath = "//button[@title='Toggling menu']")
	public WebElement hamburgerMenu_xapth;
	
	public String hamburgerMenu__xapth = "//button[@title='Toggling menu']";
	
	@FindBy(xpath = "//button[normalize-space()='Account']")
	public WebElement accOptionInHamburgerMenu_xapth;
	
	@FindBy(xpath = "//a[@title='Go to Sign In']")
	public WebElement signInOptionInHamburgerMenu_xapth;
	
	@FindBy(xpath = "//span[contains(text(),'My Account')]")
	public WebElement myAccOptionInHamburgerMenu_xapth;
	
	
	
	
	
	public HomePage(PageManager pm, ExcelReader xl) {
		super(pm, xl);
		// TODO Auto-generated constructor stub
	}
	
	public boolean verifySignInLinkDisplyed() throws Exception
	{
		pageManager.waitForSeconds(6000);
		pageManager.clickOnWebelemntBy_XY_Coordinates(0, 0);
		pageManager.waitForSeconds(1500);
		pageManager.waitElementVisible(By.xpath(SignInLink__xapth));
		
		if(!pageManager.verifyElementDisplayed(SignInLink_xapth))
		{
			pageManager.testfail("SingIn link not present on the home page");
		}
		
		boolean verifySignInLink = pageManager.verifyElementDisplayed(SignInLink_xapth);
		
		pageManager.clickByJavaScriptExecutor(SignInLink_xapth);
		
		return verifySignInLink;
		
	}
	
	public boolean verifySignInLinkDisplyedMob() throws Exception
	{
		pageManager.waitForSeconds(6000);
		pageManager.clickOnWebelemntBy_XY_Coordinates(0, 0);
		pageManager.waitForSeconds(1500);
		pageManager.waitElementVisible(By.xpath(hamburgerMenu__xapth));
		pageManager.waitForSeconds(1500);
		pageManager.clickByJavaScriptExecutor(hamburgerMenu_xapth);
		pageManager.waitForSeconds(1500);
		pageManager.clickByJavaScriptExecutor(accOptionInHamburgerMenu_xapth);
		pageManager.waitForSeconds(1500);
		
		boolean verifySignInLink = pageManager.verifyElementDisplayed(signInOptionInHamburgerMenu_xapth);
		
		pageManager.clickByJavaScriptExecutor(signInOptionInHamburgerMenu_xapth);
		pageManager.waitForSeconds(1500);
		
		return verifySignInLink;
		
	}
	
	
	
	public boolean verifyMyAccountTabDisplay() throws Exception
	{
		pageManager.waitElementVisible(By.xpath(myAccountTab__xapth));
		
		if(!pageManager.verifyElementDisplayed(myAccountTab_xapth))
		{
			pageManager.testfail("My Account not present on the home page");
		}
		
		boolean verifyMyAccountTab = pageManager.verifyElementDisplayed(myAccountTab_xapth);
		
		return verifyMyAccountTab;
	}
	
	public boolean verifyMyAccountTabDisplayMob() throws Exception
	{
		pageManager.waitForSeconds(6000);
		pageManager.clickOnWebelemntBy_XY_Coordinates(0, 0);
		pageManager.waitForSeconds(1500);
		pageManager.waitElementVisible(By.xpath(hamburgerMenu__xapth));
		pageManager.waitForSeconds(1500);
		pageManager.clickByJavaScriptExecutor(hamburgerMenu_xapth);
		pageManager.waitForSeconds(1500);
		pageManager.clickByJavaScriptExecutor(accOptionInHamburgerMenu_xapth);
		pageManager.waitForSeconds(1500);
		
		boolean verifyMyAccountTab = pageManager.verifyElementDisplayed(myAccOptionInHamburgerMenu_xapth);
	
		return verifyMyAccountTab;
	}

	
	
}
