package com.perficient.util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class ReadJsonObject{
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * This method is used to read the Json Object response
	 * @param apiLink
	 */
	public void apiTesting(String apiLink ){
		try {
		URL url = new URL(apiLink);
		HttpURLConnection conn =   (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException(" HTTP error code : "
			+ conn.getResponseCode());
			}

			Scanner scan = new Scanner(url.openStream());
			String entireResponse = new String();
			
			while (scan.hasNext())
		 	entireResponse =entireResponse+ scan.nextLine();

			log.info("Response : "+entireResponse);
			scan.close();

		    JSONObject obj =new JSONObject(entireResponse);
			
			
			String responseCode = obj.getString("status");
			log.info("Status : " + responseCode);
			
			org.json.JSONArray arr = obj.getJSONArray("results");
			for (int i = 0; i < arr.length(); i++) {
			String placeid = arr.getJSONObject(i).getString("place_id");
			log.info("Place id : " + placeid);
			String formatAddress = arr.getJSONObject(i).getString(
			"formatted_address");
			log.info("Address : " + formatAddress);

			//validating Address as per the requirement
			if(formatAddress.equalsIgnoreCase("Chicago, IL, USA"))
			{
				log.info("Address is as Expected");
			}
			else
			{
				log.info("Address is not as Expected");
			}
			}

			conn.disconnect();
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
} 




		

