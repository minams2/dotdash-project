package com.qa.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import com.dotdash.testbase.TestBase;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

//This class has 2 methods. ClosableHttpResponse will return responseString from api url, getValueByJpath this return the value for parameter from json object

public class RestClient extends TestBase {
	JSONObject responseJson;
	CloseableHttpResponse closableHttpResponse;
	RestClient restClient;
	JSONArray json;
	ArrayList taskName;
	
	//1. Get Method
	public  CloseableHttpResponse get(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new  HttpGet(url);
		CloseableHttpResponse closableHttpResponse= httpClient.execute(httpget);
		return closableHttpResponse;
	
	}
	
	
	public String getValueByJPath(JSONObject responsejson, String jpath) {
		Object obj = responsejson;
		for(String s: jpath.split("/"))
			if(!s.isEmpty())
				if(!s.contains("[") || s.contains("]"))
					obj =((JSONObject)obj).get(s);		
				else if(s.contains("[") || s.contains("]"))
					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0])).get(Integer.parseInt(s.split("\\[")[1].replace("]", "")));
		return obj.toString();
	}

	
}
