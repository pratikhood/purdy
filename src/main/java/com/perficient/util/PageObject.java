package com.perficient.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.support.PageFactory;


public class PageObject {

	protected Log log = LogFactory.getLog(this.getClass());
	protected PageManager pageManager;
	public ExcelReader excelReader;
	protected static CustomAssertion customAssertion;
	public RestAPIUtility restApiutility;
	
	public PageObject(PageManager pm, ExcelReader xl) {
		pageManager = pm;
		excelReader=xl;
		PageFactory.initElements(pageManager.getDriver(), this);
	}
	
	public PageObject(PageManager pm ,CustomAssertion ca) {
		pageManager = pm;
		PageFactory.initElements(pageManager.getDriver(), this);
		customAssertion = ca;
	}
	
	public PageObject(PageManager pm, ExcelReader xl, CustomAssertion ca) {
		pageManager = pm;
		excelReader=xl;
		customAssertion= ca;
		PageFactory.initElements(pageManager.getDriver(), this);
	}
	
	public PageObject(PageManager pm, ExcelReader xl, CustomAssertion ca, RestAPIUtility api) {
		pageManager = pm;
		excelReader=xl;
		customAssertion= ca;
		restApiutility = api;
		PageFactory.initElements(pageManager.getDriver(), this);
	}
	
}



