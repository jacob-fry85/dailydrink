package com.dailydrink.admin;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushAPI {
	private static Logger log = LoggerFactory.getLogger(PushAPI.class);
	
	public static void main(String[] args) throws Exception {
		JSONObject defaultActionObj = new JSONObject();
		defaultActionObj.put("type", "richlanding");
		defaultActionObj.put("value", "\"https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8");
		
		JSONObject androidObj = new JSONObject();
		androidObj.put("message", "Android Message");
		androidObj.put("title", "Android title");
		androidObj.put("summary", "Android Summary");
		androidObj.put("defaultAction", defaultActionObj);
		
		JSONObject payloadObj = new JSONObject();
		payloadObj.put("ANDROID", androidObj);
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("appId", "GO24BEAWF0LY1YKZN3NBGAOE_DEBUG");   
		mainObj.put("campaignName", "BackendCampaign01");    
		mainObj.put("signature", "10cf255f2ba5629283fb07ae5c5a83e7239c88bd8d81cd376056d8d7b1e79a0f");
		mainObj.put("targetAudience", "All Users"); 
		mainObj.put("requestType", "push"); 
		mainObj.put("targetPlatform", "['ANDROID']");
		mainObj.put("payload", payloadObj);
		 
		try {
			String param = mainObj.toString();
			log.info(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
		    HttpPost request = new HttpPost("https://api-01.moengage.com/v2/transaction/sendpush");
		    StringEntity params = new StringEntity(mainObj.toString());
		    request.addHeader("Content-Type", "application/json");
		    request.setEntity(params);
		    HttpResponse response;
		    
		    response = httpClient.execute(request);
		    
		    log.info("here");
		    
            System.out.println("this is the response "+response);
		// handle response here...
		} catch (Exception ex) {
		    log.info(ex.getMessage());
		} finally {
		    httpClient.close();
		}
	}
}
