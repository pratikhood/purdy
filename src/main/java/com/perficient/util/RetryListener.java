package com.perficient.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IAnnotationTransformer{
	
	// This retry logic will be applicable for all the failing test cases in the suite
	
	@Override
	public void transform (final ITestAnnotation annotation, final Class testClass, final Constructor testConstructor,
	final Method testMethod) {
	annotation.setRetryAnalyzer (Retry.class);
	}
}
