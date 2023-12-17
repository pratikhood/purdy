package com.perficient.util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kong.unirest.Unirest;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : This is utility to post the JSON object .
 * @author sagar.ghotey
 * @Created Date : 22/10/2021
 */

public class PostJsonObject {


	static String Apigeeresponse = null;
	static String BearerToken = null;
	String bearerauth=null;
	static String apigeeurl = "https://test.api.dishcloud.io/generic/platform/v1/oauth2/token";
	static String retailAgentCreateTokenurl = "https://test.api.dishcloud.io/wireless/retail/v1/token-service/retailToken";

	public String postRequest(String token) throws IOException {

		String quotes="\"";
		bearerauth= quotes+"Bearer "+token+quotes;
		kong.unirest.HttpResponse<String> response = Unirest.post(retailAgentCreateTokenurl)
				.header("Content-Type", "application/json")
				.header("Authorization", bearerauth)
				.body("{\"loggedInAgentId\": \"Automation\", \"dealerNumber\": \"565656\", \"source\": \"GSF\"}")
				.asString();
		String s= response.getBody().toString();
		JSONObject jsonObj = new JSONObject(s);
		System.out.println("jsonObj is "+jsonObj);
		String name = jsonObj.getString("token");
		return name;
	}

	public static String PostApigeeToken() throws IOException {

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
		Request request = new Request.Builder().url(apigeeurl).method("POST", body)
				.addHeader("Authorization",
						"Basic NVF2Vm1QaXdhR0ZVTVJzbGRodDR6N0FrTGp3VUFlZ3I6d3Zsd1U2Y0EyVVlPc0JKQg==")
				.addHeader("Content-Type", "application/x-www-form-urlencoded").build();
		Response apigeeresponse = client.newCall(request).execute();
		Apigeeresponse=apigeeresponse.body().string();
		BearerToken= Apigeeresponse.substring(61,89);
		return BearerToken;
	}



	public JSONArray getRequestApiArray(String url, String token) throws IOException {

		String quote="\"";
		bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);
		kong.unirest.HttpResponse<String> response = Unirest.get(url).header("Authorization", bearerauth).header("X-Correlation-Id", "AUTOMATIONXXXXXXXXXXXXXXXXXXXXXXXXXX").header("X-Message-Id", "1000").header("X-Client-Id", "RW-BOOST").header("X-TimeStamp", "2021-11-09T21:48:06.000+0000").header("X-ForwardedBy", "RW-BOOST").asString();
		String stringResponse= response.getBody().toString();
		int intResponse = response.getStatus();
		System.out.println("intResponse " + intResponse);
		JSONArray arrObj = new JSONArray(stringResponse);
		return arrObj;

	}

	public static Map<String, String> apiHeaders(String bearerauth)
	{
		Map<String,String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", bearerauth);
		requestHeaders.put("X-Correlation-Id", "3594c9f8-9d88-4ea6-b2a8-3623f4acefcd");
		requestHeaders.put("X-Message-Id", "1000");
		requestHeaders.put("X-Client-Id", "RW-BOOST");
		requestHeaders.put("X-TimeStamp", "2022-01-01T00:00:00.000+0000");
		requestHeaders.put("X-ForwardedBy", "RW-BOOST");
		
		return requestHeaders;
	}

	public static int getResponseCode(String url) throws IOException{
		//RestAssured.given().when().get("https://run.mocky.io/v3/9529877f-2c30-4520-ab2c-109f4e16b506").then().log().all();
		//System.out.println("The response status is "+statusCode);
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);
		
		int code1 = RestAssured.given().headers(apiHeaders(bearerauth)).when().get(url).getStatusCode();
		System.out.println("The response status from getResponseBodyRest is "+code1);
		return code1;
	}
	
	public static int updatePatchResponseCode(String url, String jsonBody) throws IOException{
	
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);
		int code1 = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).headers(apiHeaders(bearerauth)).when().patch(url).getStatusCode();
		System.out.println("The response status from updatePatchResponseCode is "+code1);
		return code1;
	}

	public static int postResponseCode(String url, String jsonBody) throws IOException{
		
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);		
		int code1 = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).headers(apiHeaders(bearerauth)).when().post(url).getStatusCode();
		System.out.println("The response status from postResponseCode is "+code1);
		return code1;
	}
	
public static String postResponseBody(String url, String jsonBody) throws IOException{
		
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);		
		String accountRoles = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).headers(apiHeaders(bearerauth)).when().post(url).getBody().asString();
		System.out.println("The response status from postResponseCode is "+accountRoles);
		return accountRoles;
	}

	public static int putResponseCode(String url, String jsonBody) throws IOException{
	
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);
		int code1 = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).headers(apiHeaders(bearerauth)).when().put(url).getStatusCode();
		System.out.println("The response status from putResponseCode is "+code1);
		return code1;
	}	
	
	  public static int deleteResponseCode(String url, String jsonBody) throws
	  IOException{
	  
	  String token = PostApigeeToken(); String quote="\""; String bearerauth=
	  quote+"Bearer "+token+quote; System.out.println("bearerauth :" + bearerauth);
	  int code1 = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).headers(apiHeaders(bearerauth)).when().delete(url).getStatusCode();
	  System.out.println("The response status from deleteResponseCode is "+code1);
	  return code1; 
	  }
	 
	  public static int emailVerificationCode(String url, String jsonBody)throws IOException
	  {
		int code1 = RestAssured.given().contentType(ContentType.JSON).body(jsonBody).header("Authorization", "ApiKey: b1caf743-9c48-4cf4-a0f5-4d1fce8fe0ba").when().post(url).getStatusCode();
		return code1;		  
	  }
	  
	  public static int smartyAddressCode(String url)throws IOException
	  {
		int code1 = RestAssured.given().header("Referer", "https://commerce-qa.dish.com/shop/").when().get(url).getStatusCode();
		return code1;		  
	  }
	  
	  public String getAccessToken(String code) throws Exception {
			
			
			String response =  RestAssured.given()
			.header("Content-Type", "application/x-www-form-urlencoded")
			.header("Authorization", "Basic cmVzdHBjdmRpc2g6Mkt2bERvSmxlWjhYdGpxVFU5S0tqUFh4bWprWnZVTmtOQzFhRHNOcnBwZHFVa094aEZPRTY4d1lVN1E3UnJMWQ==")
			.formParam("code", code)
			.formParam("grant_type", "authorization_code")
			.formParam("redirect_uri", "boostone://auth_callback")
			.when().post("https://cust-auth-fed-np.dish.com/as/token.oauth2")
			.getBody().asString();
			
			return response;
	  }

	public String getResponseBody(String url) throws IOException{
		//RestAssured.given().when().get("https://run.mocky.io/v3/9529877f-2c30-4520-ab2c-109f4e16b506").then().log().all();
		//System.out.println("The response status is "+statusCode);
		String token = PostApigeeToken();
		String quote="\"";
		String bearerauth= quote+"Bearer "+token+quote;
		System.out.println("bearerauth :" + bearerauth);

		String returnBody = RestAssured.given().headers(apiHeaders(bearerauth)).when().get(url).getBody().asPrettyString();
		System.out.println("The response body from getResponseBodyRest is "+ returnBody);
		return returnBody;
	}
}
