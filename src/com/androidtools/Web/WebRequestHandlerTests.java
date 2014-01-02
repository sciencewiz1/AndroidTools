package com.androidtools.Web;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.google.gson.reflect.TypeToken;

public class WebRequestHandlerTests {

	@Test
	public void testMakeWebRequest()
	{
		String data = WebRequestHandler.makeWebRequest("http://localhost:8079/TermClasses/Reviews/index", "POST", "term_class_id=1");
		System.out.println(data);
	}
	//@Test
	public void testDeserialize()
	{
		String jsondata = WebRequestHandler.makeWebRequest("http://localhost:8079/Test/test", "GET", "");
		System.out.println(jsondata);
		Type listOfTestObject = new TypeToken<TestServerResponseObject>(){}.getType();
		TestServerResponseObject data = WebRequestHandler.<TestServerResponseObject>deserialize(jsondata,listOfTestObject);
		System.out.println(data);
	}
	//@Test
	public void testDeserialize2()
	{
		String jsondata = WebRequestHandler.makeWebRequest("http://localhost:8079/Test/testList", "GET", "");
		System.out.println(jsondata);
		Type listOfTestObject = new TypeToken<ArrayList<TestServerResponseObject>>(){}.getType();
		ArrayList<TestServerResponseObject> data = WebRequestHandler.<ArrayList<TestServerResponseObject>>deserialize(jsondata,listOfTestObject);
		System.out.println(data);
	}
}
