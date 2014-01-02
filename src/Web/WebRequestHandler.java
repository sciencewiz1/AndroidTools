package com.example.ratemit.resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;

import android.os.Build;

import com.google.gson.*;

public class WebRequestHandler {

	public static String generatePostData(HashMap<String,String> data)
	{
		String out = "";
		for(String key:data.keySet())
		{
			String value = data.get(key);
			if(out == "")
			{
				out+=key+"="+value;
			}else{
				out+="&"+key+"="+value;
			}
		}
		return out;
	}
	public static String makeWebRequest(String URL, String method, String data)
	{
		String out = "";
		URL url;
		try {
			//convert input to byte array
		    byte[] dataBytes = data.getBytes();
			
			//create connection
			url = new URL(URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedOutputStream httpOutStream = null;
			//urlConnection.setReadTimeout(100000);
			urlConnection.setDoInput(true);
			/*if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13)
			{ 
				urlConnection.setRequestProperty("Connection", "close");//disables connection reuse
			}*/
			urlConnection.setRequestProperty("Connection", "close");//disables connection reuse
			if(method == "POST")
		    {
		    	//by default the HttpURlConneciton does a GET Request
		    	//these parameters need to be specified to do a POST 
				urlConnection.setDoOutput(true);     
			    //urlConnection.setFixedLengthStreamingMode(dataBytes.length);
				urlConnection.setChunkedStreamingMode(dataBytes.length);
			    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			    //send the data to the server
			    httpOutStream = new BufferedOutputStream(urlConnection.getOutputStream());     
			    httpOutStream.write(dataBytes);
			    httpOutStream.flush();
		    }
		    //get response stream
		    BufferedInputStream httpInputStream = new BufferedInputStream(urlConnection.getInputStream());
		    
		    //convert bytes to string
		    int numBytes = httpInputStream.available();
		    System.out.println("IN NETWORK REQUEST, NUM BYTES AVAILABLE:>>>>>>>>>>>>>>>>>>>>>>>");
		    System.out.println("response code" + urlConnection.getResponseCode());
		    System.out.println("Bytes"+numBytes);
		    byte[] responseBytes = new byte[numBytes];
		    httpInputStream.read(responseBytes, 0, numBytes);
		    String decoded = new String(responseBytes, "UTF-8");
		    out = decoded;
		    
		    //clean up
		    httpInputStream.close();
		    urlConnection.disconnect();
		    if(httpOutStream != null)
		    {
		    	httpOutStream.close();
		    }
			System.out.println("IN NETWORK REQUEST, GOT:>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println("decoded: "+decoded);
		} catch (Exception e) {
			System.out.println("GOT EXCEPTION WITH>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(URL);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	public static <T> T deserialize(String json, Type t)
	{
		T data = null;
		try
		{
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			data = gson.fromJson(json, t);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	public static <T> String serialize(T object, Type t)
	{
		String out = "";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		out = gson.toJson(object, t);
		return out;
	}
}
