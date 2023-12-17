package com.perficient.util;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TestComplexReportFactory {
    private ComplexReportFactory reportFactory = new ComplexReportFactory();
    private TestCaseBase testCaseBase = new TestCaseBase();

    /**
     * Set up Complex Report Factory testing environment
     */
    @BeforeClass
    public void setUp() {
    }

    /**
     * Tear down Complex Report Factory testing environment
     */
    @AfterClass
    public void tearDown() {
        reportFactory = null;
    }

}
