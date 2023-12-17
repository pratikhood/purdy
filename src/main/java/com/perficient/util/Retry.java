package com.perficient.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class Retry implements IRetryAnalyzer {
	
	/* if retryCount is 0 and maxRetryCount 3 the it will retry the failing step of the class for 3 times */
	
	/* User need to mention 'retryAnalyzer=Retry.class' at the @Test annotation level like @Test(priority =1, groups= {"desktop"}, retryAnalyzer=Retry.class)*/

	private int retryCount = 0;
	private static final int maxRetryCount = 1;

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {

			retryCount++;
			return true;
		}
		return false;
	}
}
