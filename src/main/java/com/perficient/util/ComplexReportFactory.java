package com.perficient.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;


public class ComplexReportFactory {
	
	public static ExtentReports reporter;
	public static ExtentXReporter extentxReporter;
	public static ExtentHtmlReporter htmlReporter;
	public static Map<Long, String> threadToExtentTestMap = new HashMap<Long, String>();
	public static Map<String, ExtentTest> nameToTestMap = new HashMap<String, ExtentTest>();
	static String filePath;
	static String returnPath = "./test-output";
	
	

	private synchronized static ExtentReports getExtentReport(String sampleSuite, String environment) {
		/*
		 * if (reporter == null) {
		 * 
		 * String dt = getDatetime(); String extentReportName = "ExtentReport-"+
		 * categoryName +"-" + dt; filePath = "./test-output/extentReports/" +
		 * extentReportName + ".html"; htmlReporter = new ExtentHtmlReporter(filePath);
		 * htmlReporter.config().setReportName(extentReportName);
		 * htmlReporter.config().setProtocol(Protocol.HTTPS); reporter = new
		 * ExtentReports(); reporter.attachReporter(htmlReporter);
		 * 
		 * }
		 */
		if (reporter == null) {
			String dt = getDatetime();
			if(environment.equalsIgnoreCase("qa")) {
				filePath = "./test-output/Report-QA-" +sampleSuite+ "-"+ dt + ".html";
			}else if (environment.equalsIgnoreCase("test")) {
				filePath = "./test-output/Report-Stage-" +sampleSuite+ "-"+ dt + ".html";
				}
			else if (environment.equalsIgnoreCase("dev")) {
				filePath = "./test-output/Report-Dev-" +sampleSuite+ "-"+ dt + ".html";
				}	
			htmlReporter = new ExtentHtmlReporter(filePath);
			if(environment.equalsIgnoreCase("qa")) {
				htmlReporter.config().setReportName("Report-QA-" +sampleSuite+"-"+ dt);
			}else if (environment.equalsIgnoreCase("test")) {
				htmlReporter.config().setReportName("Report-Stage-" +sampleSuite+"-"+ dt);
			}
			else if (environment.equalsIgnoreCase("dev")) {
				htmlReporter.config().setReportName("Report-Dev-" +sampleSuite+"-"+ dt);
			}
			
			htmlReporter.config().setProtocol(Protocol.HTTPS);
			reporter = new ExtentReports();
			reporter.attachReporter(htmlReporter);
			
		}
		return reporter;
	}

	/*
	 * public synchronized static ExtentTest getTest(String testName, String
	 * categoryName, String testDescription) {
	 * 
	 * // if this test has already been created return if
	 * (!nameToTestMap.containsKey(testName)) { Long threadID =
	 * Thread.currentThread().getId(); ExtentTest test =
	 * getExtentReport(categoryName).createTest(testName, testDescription);
	 * test.assignCategory(categoryName); nameToTestMap.put(testName, test);
	 * threadToExtentTestMap.put(threadID, testName); } return
	 * nameToTestMap.get(testName); }
	 */

	public synchronized static ExtentTest getTest(String testName, String categoryName, String testDescription, String sampleSuite, String environment) {

		// if this test has already been created return
		if (!nameToTestMap.containsKey(testName)) {
			Long threadID = Thread.currentThread().getId();
			ExtentTest test = getExtentReport(sampleSuite, environment).createTest(testName, testDescription);
			test.assignCategory(categoryName);
			nameToTestMap.put(testName, test);
			threadToExtentTestMap.put(threadID, testName);
		}
		return nameToTestMap.get(testName);
	}
	public synchronized static ExtentTest getTest(String testName) {
		return getTest(testName, "", "","","");
	}

	public synchronized static ExtentTest getTest() {
		Long threadID = Thread.currentThread().getId();

		if (threadToExtentTestMap.containsKey(threadID)) {
			String testName = threadToExtentTestMap.get(threadID);
			return nameToTestMap.get(testName);
		}
		// system log, this shouldnt happen but in this crazy times if it did
		// happen log it.
		return null;
	}

	public synchronized static void closeTest(String testName) {

		if (!testName.isEmpty()) {
			ExtentTest test = getTest(testName);
		}
	}

	public synchronized static void closeTest(ExtentTest test) {
		if (test != null) {
		}
	}

	public synchronized static void closeTest() {
		ExtentTest test = getTest();
		closeTest(test);
	}

	public synchronized static void closeReport() {
		if (reporter != null) {
			reporter.flush();
			reporter.close();
		}
	}

	public static String getDatetime() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd_hh.mm.ss");
		return date.format(new Date());
	}
	
	}