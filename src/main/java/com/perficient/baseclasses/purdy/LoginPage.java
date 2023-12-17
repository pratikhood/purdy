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

public class LoginPage extends PageObject {

	public String manageExtrasLabel;
	float amount;

	@FindBy(id = "email")
	public WebElement emailField_id;

	public String emailField__id = "email";
	
	@FindBy(id = "pass")
	public WebElement passwordField_id;

	public String passwordField__id = "pass";
	
	@FindBy(xpath = "//button[contains(text(),'LOG IN')]")
	public WebElement login_xpath;
	
	
	public LoginPage(PageManager pm, ExcelReader xl) {
		super(pm, xl);
		// TODO Auto-generated constructor stub
	}
	
	public void verifyEmailFieldDisplayed() throws Exception
	{
		pageManager.waitElementVisible(By.xpath(emailField__id));
		
		if(!pageManager.verifyElementDisplayed(emailField_id))
		{
			pageManager.testfail("SingIn link not present on the home page");
		}
		
		pageManager.clickByJavaScriptExecutor(emailField_id);
		
	}
	
	public void enterEmail(String email)
	{
		pageManager.sendKeys(emailField_id, email);
		pageManager.waitForSeconds(2500);
	}
	
	public void verifyPasswordFieldDisplayed() throws Exception
	{
		pageManager.waitElementVisible(By.xpath(passwordField__id));
		
		if(!pageManager.verifyElementDisplayed(passwordField_id))
		{
			pageManager.testfail("SingIn link not present on the home page");
		}
		
		pageManager.clickByJavaScriptExecutor(passwordField_id);
		
	}
	
	public void enterPassword(String passsword)
	{
		pageManager.sendKeys(passwordField_id, passsword);
		pageManager.waitForSeconds(2500);
		pageManager.clickByJavaScriptExecutor(login_xpath);
		pageManager.waitForSeconds(2500);
	}
	

	
	
	
	
	
	
	
}
