package com.dailydrink.admin;


import org.json.JSONArray;
import org.json.JSONObject;

public class TestJson {
	public static String getEdward(JSONArray json) throws Exception {
		for(int i = 0; i < json.length(); i++) {
	    	JSONObject obj = json.getJSONObject(i);
	    	 String name = obj.getString("personName");
	    	 if(name.contains("Edward"))  {
//	    		 System.out.println("isEdward = " + name);
	    		 return name;
	    	 }
	    }
		return null;
	}
	
	public static void main(String[] argStrings ) {
		try {
		    JSONObject jo2 = new JSONObject();
		    jo2.put("personName", "John Doe");
		    jo2.put("Age", "55");
		
		    JSONObject jo3 = new JSONObject();
		    jo3.put("personName", "Mary Rose");
		    jo3.put("Age", "22");
		
		    JSONObject jo4 = new JSONObject();
		    jo4.put("personName", "Edward");
		    jo4.put("Age", "19");
		
		    JSONArray ja1 = new JSONArray();
		    ja1.put(jo2);
		    ja1.put(jo3);
		    ja1.put(jo4);
		
		    JSONObject jo5 = new JSONObject();
		    jo5.put("young", "19");
		    jo5.put("middleage", "30");
		    jo5.put("old", "60");
		
		    JSONObject mObj = new JSONObject();
		    mObj.put("status", "ok");
		    mObj.put("person", ja1);
		    mObj.put("category", jo5);
		    
//		    System.out.println(mObj.length());
		    
		    if(mObj.has("person") ) {
		    	String personString = mObj.getString("person");
	    	    JSONArray json = new JSONArray(personString);
			    String result = getEdward(json); 	
			    System.out.println("Result = " + result);
		    }			   
		} catch(Exception e){
			
		}
	
	}
}
