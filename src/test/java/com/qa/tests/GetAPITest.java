package com.qa.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.dotdash.testbase.TestBase;
import com.qa.client.RestClient;

//This class read api url from properties file in set up method. 
//It has test for GetAPITest will validate status code and put the header information in hashmap
//it has test for GetAPITotalTaskTest which will do the following
//a. create json array from api url and for each json object it will call getValueByJPath for each parameter like id, category, taskname, due date
//b. it print out total number of tasks and task 
//c. it will validate if the category is empty and get the task for unassigned category and i will print values of tasks
//d. it will get duedate which are not empty and will print  task based on descending order of duedates 
public class GetAPITest extends TestBase {
	RestClient restClient = new RestClient();
	String url;
	CloseableHttpResponse closableHttpResponse;
	JSONArray json;
	JSONObject responseJson;
	int taskCount;
	
	
	@BeforeTest
	public void setUp() throws ClientProtocolException, IOException {
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\com\\dotdash\\config\\config.properties");
		prop = new Properties();
		prop.load(fis);
		url = prop.getProperty("apiurl");
		
	}

	@Test(priority=1)
	public void getAPITest() throws ClientProtocolException, IOException {
		 restClient = new RestClient();
		 closableHttpResponse = restClient.get(url);
		 
		//status code
			int statusCode=closableHttpResponse.getStatusLine().getStatusCode();
			System.out.println("Status Repsonse Code---->" +statusCode);
			Assert.assertEquals(REPSONSE_STATUS_CODE_200, statusCode);
			
		
			// All headers
			Header headersArray[]=closableHttpResponse.getAllHeaders();
			
		    HashMap<String, String> allHeaders = new HashMap<String, String>();
			for(Header header: headersArray) {
			allHeaders.put(header.getName(), header.getValue());
			}
			System.out.println("Headers Array--->" +allHeaders);		
	}
	
	@Test(priority=2)
	public void GetAPITotalTaskTest() throws ParseException, IOException {
		Boolean flag = false;
		String notEmptyCategory = null;
		ArrayList<String> taskName = new ArrayList<String>();
		String taskname = null, id;
		String duedate,category;
		int newdate = 0;
		int count = 0;
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		Map<Integer, String> hm = new TreeMap<Integer, String>().descendingMap();
		int dateCount = 0;
		restClient = new RestClient();
		closableHttpResponse = restClient.get(url);
		String responseString = EntityUtils.toString(closableHttpResponse.getEntity(), "UTF-8");
		json = new JSONArray(responseString);
		
		for (int i = 0; i < json.length(); i++) {
		   responseJson = json.getJSONObject(i);
	        System.out.println("Response JSON from API--->"+responseJson);
		    id =restClient.getValueByJPath(responseJson, "/id");
    		category =restClient.getValueByJPath(responseJson, "/category");
		    duedate =restClient.getValueByJPath(responseJson, "/due date");
		    taskname =restClient.getValueByJPath(responseJson, "/task name");
		    taskName.add(taskname);
		
		if((duedate.startsWith("\r\n",0) )|| (duedate.equals(""))){
			dateCount = 0;
		}
			else
			{	
			dateCount= dateCount+1;
			if(duedate.endsWith("\r\n"))
				duedate=duedate.substring(0,duedate.length()-2);
			newdate= Integer.parseInt(duedate);
			hm.put(newdate, taskname);
			
		}
			
		if(category.equals(""))
		{
		     count=count+1;  
		      hashmap.put(count,taskname);
		}
  }
			System.out.println("================================================");
		     System.out.println(" The Total Number of Task is :" +taskName.size());
		     
			for(int j=0;j<taskName.size();j++)
	    	   System.out.println("Task-Name:"+taskName.get(j));
			System.out.println("================================================");
			
			if(taskName.size()!=0)
				flag = true;
			Assert.assertTrue(flag);
		
			System.out.println("Task does not have category assigned"); 
			//This is will print count of empty category as key and task will be value
			for(Map.Entry m:hashmap.entrySet()) {
				System.out.println(m.getKey()+" "+m.getValue());
			}
			System.out.println("================================================");
			System.out.println("Due Dates in descending orders and Task Name");
			Set set = hm.entrySet();
			Iterator it = set.iterator();
			while(it.hasNext()) {
				Map.Entry me =(Map.Entry)it.next();
				System.out.println(me.getKey()+":"+me.getValue());
			}
	 
}
}
