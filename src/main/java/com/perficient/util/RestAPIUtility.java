package com.perficient.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 * @Description : This is utility to post the JSON object .
 * @author Anushree Kharwade
 * @Created Date : 29/03/2022
 */

public class RestAPIUtility {
	
	protected Log log = LogFactory.getLog(this.getClass());


	public int getResponseCode(String url) {
		
		int getresponsecode = RestAssured.given().when().get(url).getStatusCode();
		log.info("The response status from getResponseBodyRest is "+getresponsecode);
		return getresponsecode;
	}
	
	public int updatePatchResponseCode(String url, String jsonBody){
	
		int updateresponsecode = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).when().patch(url).getStatusCode();
		log.info("The response status from updatePatchResponseCode is "+updateresponsecode);
		return updateresponsecode;
	}

	public int postResponseCode(String url, String jsonBody){
		
		int postResponsecode = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).when().post(url).getStatusCode();
		log.info("The response status from postResponseCode is "+postResponsecode);
		return postResponsecode;
	}
	
	public int putResponseCode(String url, String jsonBody) {
	
		int putResponseCode = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).when().put(url).getStatusCode();
		log.info("The response status from putResponseCode is "+putResponseCode);
		return putResponseCode;
	}	
	
	public int deleteResponseCode(String url, String jsonBody) {
	  
		int deleteResponseCode = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).when().delete(url).getStatusCode();
		log.info("The response status from deleteResponseCode is "+deleteResponseCode);
		return deleteResponseCode; 
	 }
	 
	  
}
