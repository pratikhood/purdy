package com.perficient.baseclasses.purdy;

import com.perficient.util.*;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CommonComponents extends PageObject {
	
	ArrayList<String> purdyURLs;

	

	public CommonComponents(PageManager pm, ExcelReader excelReader) {
		super(pm, excelReader);
	}
	
	public void loadURLs(String url)
	{
		String fileName = "testdata_urls.properties";
		TestData.loadURLs(fileName);
		
		if(url.equalsIgnoreCase("purdy"))
		{
			String purdyTestLoggedIn = TestData.getURLs("purdyTestLoggedIn");
			String purdyQALoggedIn = TestData.getURLs("purdyQALoggedIn");
			String purdyDEVLoggedIn = TestData.getURLs("purdyDEVLoggedIn");
			 
			purdyURLs = new ArrayList<String>();
			purdyURLs.add(purdyTestLoggedIn);
			purdyURLs.add(purdyQALoggedIn);
			purdyURLs.add(purdyDEVLoggedIn);
		}
	
 
	}

	/**
	 * @Description: This Method opens the URL
	 * @Author: Anushree Kharwade
	 * @CreatedDate: 28/01/2022
	 * @Updated By :
	 * @Updated Date: 
	 * @param url,env
	 * @Comments
	 * 
	 */
	public void open(String url, String env) throws InterruptedException {
		
		loadURLs(url);
		
		ArrayList<String> urls = purdyURLs;
	
		
		if (env.equalsIgnoreCase("Test")) {
		pageManager.navigate(urls.get(0).toString());
		pageManager.sleep(3000);
		pageManager.waitForPageLoaded(); 
		log.info("Stage URL is selected");
		}
		else if (env.equalsIgnoreCase("QA")) {
		pageManager.navigate(urls.get(1).toString());
		pageManager.sleep(6000);
		pageManager.waitForPageLoaded();
		log.info("Stage2 URL is selected");
		}
		else if (env.equalsIgnoreCase("Dev")) {
		pageManager.navigate(urls.get(2).toString());
		pageManager.sleep(6000);
		pageManager.waitForPageLoaded();
		log.info("Stage3 URL is selected");
		}
	}
	

	





}