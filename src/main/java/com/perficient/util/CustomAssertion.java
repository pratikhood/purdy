package com.perficient.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.ScreenCapture;

public class CustomAssertion {
	private Log log = LogFactory.getLog(this.getClass());
	private WebDriver driver;
	private static ExtentTest test;
	private SoftAssert softAssert;
	String currentPath = ".\\test-output\\errorImages";
	static String returnPath = ".\\errorImages";

	public CustomAssertion(WebDriver d, ExtentTest extentTest, SoftAssert softAssert) {
		driver = d;
		test = extentTest;
		this.softAssert = softAssert;
	}

	public enum ErrorType {
		ELEMENT_NOTFOUND("Element was not found, "), ELEMENT_STALE(
				"Element was no longer located in the DOM and has become stale, "), WAIT_TIMEOUT(
						"Wait timeout occured, "), ASSERTED("Assertion failed, ");

		public String errorMsg;

		private ErrorType(String errorMsg) {
			this.errorMsg = errorMsg;
		}
	}

	
	public boolean assertEquals(String actual, String expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
			test.log(Status.PASS, message + " actual: " + actual + " expected: " + expected + "...are equal");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "actual: " + actual + " expected: " + expected + "...are not equal");
			String path = snapshot();
			printError(e, message, path);}
			softAssert.assertEquals(actual, expected, message);
			return false;
	}

	public boolean assertEquals(String actual, String expected) {
		try {
			Assert.assertEquals(actual, expected);
			test.log(Status.PASS, "actual: " + actual + " expected: " + expected);
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "actual: " + actual + " expected: " + expected + "...are not equal");
			String path = snapshot();
			printError(e, path);}
			softAssert.assertEquals(actual, expected);
			return false;
	}

	public boolean assertNotNull(Object obj) {
		try {
			Assert.assertNotNull(obj);
			test.log(Status.PASS,  "Object " + obj.toString() + " is not null.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Object " + obj.toString() + " is null.");
			String path = snapshot();
			printError(e, path);
			}
			softAssert.assertNotNull(obj);
			return false;
	}
	
	public boolean assertNotNull(Object obj, String message) {
		try {
			Assert.assertNotNull(obj, message);
			test.log(Status.PASS, message + "Object " + obj.toString() + " is not null.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Object " + obj.toString() + " is null.");
			String path = snapshot();
			printError(e, message, path);
			}
			softAssert.assertNotNull(obj, message);
			return false;
	}


	public boolean assertTrue(boolean expression, String message) {
		try {
			Assert.assertTrue(expression, message);
			test.log(Status.PASS, message + " Expression " + expression + " is true.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion failed : " + message);
			String path = snapshot();
			printError(e, message, path);
			softAssert.assertTrue(expression, message);
			return false;
		}
	}

	public boolean assertTrue(boolean expression) {
		try {
			Assert.assertTrue(expression);
			test.log(Status.PASS, "Expression " + expression + " is true.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion failed");
			String path = snapshot();
			printError(e, path);
			softAssert.assertTrue(expression);
			return false;
		}
	}
	
	public boolean assertTrue(boolean expression,String PassingMsg, String FailingMsg) {
		try {
			Assert.assertTrue(expression, FailingMsg);
			test.log(Status.PASS, PassingMsg);
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, FailingMsg);
			String path = snapshot();
			printError(e, FailingMsg, path);}
			softAssert.assertTrue(expression, FailingMsg);
			return false;
	}

	public boolean assertFalse(boolean expression) {
		try {
			Assert.assertFalse(expression);
			test.log(Status.PASS, "Expression " + expression + " is false.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion failed");
			String path = snapshot();
			printError(e, path);}
			softAssert.assertTrue(expression);
			return false;
	}

	public boolean assertFalse(boolean expression, String message) {
		try {
			Assert.assertFalse(expression, message);
			test.log(Status.PASS, message + "Expression " + expression + " is false.");
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, message);
			String path = snapshot();
			printError(e, message, path);}
			softAssert.assertTrue(expression, message);
			return false;
	}
	
	
	public boolean assertNotEquals(String actual, String unexpected, String PassingMsg, String FailingMsg) {
		try {
			Assert.assertNotEquals(actual, unexpected);
			test.log(Status.PASS, "actual: " + actual + " Unexpected: " + unexpected + PassingMsg);
			return true;
		} catch (AssertionError e) {
			test.log(Status.FAIL, "actual: " + actual + " Unexpected: " + unexpected + FailingMsg);
			String path = snapshot();
			printError(e, path, FailingMsg);}
			softAssert.assertNotEquals(actual, unexpected, FailingMsg);
			return false;
	}
	
	public boolean assertContains(String actual, String expected) {
		try {			
			log.info(actual);
			log.info(expected);
			Assert.assertTrue(actual.contains(expected));
			test.log(Status.PASS, "actual: " + actual + " expected: " + expected );
			log.info("Pass");
			return true;
			}
		catch (AssertionError e) {
			test.log(Status.FAIL, "actual: " + actual + " expected: " + expected);
			String path = snapshot();
			printError(e, path);}
			return false;
	}
	
	
	public SoftAssert getSoftAssert() {
		return softAssert;
	}
	
	
	/*
	 * public String snapshot(TakesScreenshot drivername) { File scrFile =
	 * drivername.getScreenshotAs(OutputType.FILE); String dt = getDatetime(); try {
	 * log.info("save snapshot path is:" + currentPath + "\\" + dt + ".png");
	 * FileUtils.copyFile(scrFile, new File(currentPath + "\\" + dt + ".png"));
	 * FileUtils.copyFile(scrFile, new File(returnPath + "\\" + dt + ".png")); }
	 * catch (IOException e) { log.error("Can't save screenshot"); return ""; }
	 * finally { log.info("screen shot finished, it's in " + currentPath +
	 * " folder"); return returnPath + "\\" + dt + ".png"; } }
	 */
	/*
	 * public String getDatetime() { SimpleDateFormat date = new
	 * SimpleDateFormat("yyyymmdd_hhmmss"); return date.format(new Date()); }
	 */

	/*
	 * public void printError(AssertionError e, String message, String path) {
	 * log.info(path); String[] paths = path.split("/"); String imageName =
	 * paths[paths.length - 1]; try { test.log(Status.FAIL, message + e +
	 * "Screencast below: "); ScreenCapture screenCapture = new ScreenCapture();
	 * test.addScreenCaptureFromPath(path); test.log(Status.FAIL, message + e); }
	 * catch (IOException e1) { e1.printStackTrace(); } }
	 * 
	 * public void printError(AssertionError e, String path) { try {
	 * test.log(Status.FAIL, "\n" + e + "\n Screencast below: " +
	 * test.addScreenCaptureFromPath(path)); } catch (IOException e1) {
	 * e1.printStackTrace(); } }
	 */
	
	public String snapshot() {
		
		TakesScreenshot drivername =((TakesScreenshot)driver);
		String execScript = "return document.documentElement.scrollHeight>document.documentElement.clientHeight;";
		JavascriptExecutor scrollBarPresent = (JavascriptExecutor) driver;
		Boolean test = (Boolean) (scrollBarPresent.executeScript(execScript));
		//scrollBarPresent.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		System.out.println("isScrollable value is "+test);
		String separator = "\\";
		String userPath = System.getProperty("user.dir");
		if (userPath.startsWith("/")) {
			separator = "/";
			currentPath = currentPath.replace("\\", "/");
			returnPath = returnPath.replace("\\", "/");
		}
		if (test)
		//{
			//String name = getDatetime();
			/*try {
			scrollBarPresent.executeScript("window.scrollBy(0,-document.body.scrollHeight)");
			File scrFile = drivername.getScreenshotAs(OutputType.FILE);
			
				System.out.println("save snapshot path is:" + currentPath + separator + name + ".png");
				FileUtils.copyFile(scrFile, new File(currentPath + separator + name + ".png"));
				FileUtils.copyFile(scrFile, new File(returnPath + separator + name + ".png"));
				scrollBarPresent.executeScript("window.scrollBy(0,document.body.scrollHeight)");
				File scrFile2 = drivername.getScreenshotAs(OutputType.FILE);
				System.out.println("save snapshot path is:" + currentPath + separator + name + "2.png");
				FileUtils.copyFile(scrFile2, new File(currentPath + separator + name + "2.png"));
			FileUtils.copyFile(scrFile2, new File(returnPath + separator + name + "2.png"));
			} catch (IOException exception) {
				System.out.println("Can't save screenshot");
				return "";
			} finally {
				System.out.println("screen shot finished, it's in " + currentPath + " folder");
				String path = returnPath + separator + name + ".png" + "@@@" + returnPath + separator + name + "2.png";
				return path;
			}
			
		}*/
		//else
		{
			File scrFile = drivername.getScreenshotAs(OutputType.FILE);
			String name2 = getDatetime();
			/*try {
				System.out.println("save snapshot path is:" + currentPath + "\\" + name2 + ".png");
				FileUtils.copyFile(scrFile, new File(currentPath + "\\" + name2 + ".png"));
				FileUtils.copyFile(scrFile, new File(returnPath + "\\" + name2 + ".png"));
			} catch (IOException exception) {
				System.out.println("Can't save screenshot");
				return "";
			} finally {
				System.out.println("screen shot finished, it's in " + currentPath + " folder");
				return returnPath + "\\" + name2 + ".png";
			}*/
		}
		return userPath;
	}

	

	public String getDatetime() {

		SimpleDateFormat date = new SimpleDateFormat("yyyymmdd_hhmmss");
		System.out.println("date value is =====> " + date);

		return date.format(new Date());
	}

	public void printError(AssertionError e, String message, String path) {
		
		if(path.contains("@@@"))
		{
			
			String[] arrOfStr = path.split("@@@");
				try {
					log.info(arrOfStr[0]+" "+arrOfStr[1]);
					test.log(Status.FAIL, message + e + "Screencast below: ");
					test.addScreenCaptureFromPath(arrOfStr[0]);
					test.addScreenCaptureFromPath(arrOfStr[1]);
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
		}
		else {
			try {
				log.info(path);
				test.log(Status.FAIL, message + e + "Screencast below: ");
				test.addScreenCaptureFromPath(path);
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
		}
	}

	public void printError(AssertionError e, String path) {
		if(path.contains("@@@"))
		{
			String[] arrOfStr = path.split("@@@");
			System.out.println(arrOfStr[0]+"  ::: 1234 :::   "+arrOfStr[1]);
			String addr = System.getProperty("user.dir");
			System.out.println("curent dir :" +addr);
				try {
					test.log(Status.FAIL, "\n" + e + "\n Screencast below: " + test.addScreenCaptureFromPath(arrOfStr[0])+ " "+test.addScreenCaptureFromPath(arrOfStr[1]));
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
		}
		else {
			try {
				test.log(Status.FAIL, "\n" + e + "\n Screencast below: " + test.addScreenCaptureFromPath(path));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	public boolean assertEquals(float actual, float expected) throws IOException {
		try {
			Assert.assertEquals(actual, expected);
			test.log(Status.PASS, "actual: " + actual + " expected: " + expected);
			return true;
		} catch (AssertionError e) {
			String path = snapshot();
			printError(e, path);
			return false;
		}
	}
	
	public boolean assertEquals(int actual, int expected) throws IOException {
		try {
			Assert.assertEquals(actual, expected);
			test.log(Status.PASS, "actual: " + actual + " expected: " + expected);
			return true;
		} catch (AssertionError e) {
			String path = snapshot();
			printError(e, path);
			return false;
		}
	}
	
	public boolean assertNotEquals(String actual, String expected) throws IOException {
		try {
			Assert.assertNotEquals(actual, expected);
			test.log(Status.PASS, "actual: " + actual + " expected: " + expected);
			return true;
		} catch (AssertionError e) {
			String path = snapshot();
			printError(e, path);
			return false;
		}
	}
	
	/**
	 * @Description: Given the names and location of different image files, method verifies if the images are same or not.
	 * If the images are the same and the locations / names are different, return an empty string ("").
	 * Else, return error text back to caller.
	 * @Author: Bryan Cox
	 * @CreatedDate: 31-05-2023
	 * @Updated By : Bryan Cox
	 * @Updated Date: 24-10-2023
	 * @param
	 * @throws
	 * @Comments Updated so that it can compare the actual image with a list of expected images.
	 *
	 */

	

	/**
	 * @Description: Given webElement, method gets webElement's color and verifies that it's red, green, and blue values
	 * matches the passed in red, green, blue values.
	 * @Author: Bryan Cox
	 * @CreatedDate: 10-10-2023
	 * @Updated By :
	 * @Updated Date:
	 * @param
	 * @throws
	 * @Comments
	 *
	 */
	public static String assertColor (WebElement item, String expectedRed, String expectedGreen, String expectedBlue)
			throws Exception {
		String returnString = "";
		String cssColorString = item.getCssValue("color");

		String[] rgb = cssColorString.replace("rgb(", "").replace(")", "").split(", ");
		String red = rgb[0];
		String green = rgb[1];
		String blue = rgb[2];

		if(!red.equals(expectedRed) || !green.equals(expectedGreen) || !blue.equals(expectedBlue)) {
			returnString = "Expected color rgb values (" + expectedRed + "," + expectedGreen + "," + expectedBlue;
			returnString = returnString + ") did not match actual rgb values (" + red + "," + green + "," + blue + ")";
		}

		return returnString;
	}

}
