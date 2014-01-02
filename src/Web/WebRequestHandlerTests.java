package com.example.ratemit.resources;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.example.ratemit.models.Category;
import com.example.ratemit.models.Term;
import com.example.ratemit.resources.*;
import com.example.ratemit.serverreqobjects.TestServerResponseObject;
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
	//@Test
	public void testDeserialize3()
	{
		ArrayList<Category> categories = new ArrayList<Category>();
		Category c1 = new Category();
		c1.id = 1;
		c1.name = "test1";
		c1.description = "testy test";
		Category c2 = new Category();
		c2.id = 2;
		c2.name = "test2";
		c2.description = "row my boat";
		categories.add(c1);
		categories.add(c2);
		String jsondata = WebRequestHandler.serialize(categories, new TypeToken<ArrayList<Category>>(){}.getType());
	    ArrayList<HashMap<String,Object>> data = WebRequestHandler.deserialize(jsondata, new TypeToken<ArrayList<HashMap<String,Object>>>(){}.getType());
	    System.out.println(data.toString());
		
	}
	//@Test 
	public void testDeserializeCategoryList()
	{
		String jsondata = WebRequestHandler.makeWebRequest("http://localhost:8079/Test/getCategories", "GET", "");
		System.out.println(jsondata);
		Type listOfTestObject = new TypeToken<ArrayList<Category>>(){}.getType();
		ArrayList<Category> data = WebRequestHandler.<ArrayList<Category>>deserialize(jsondata,listOfTestObject);
		System.out.println(data);
	}
	//@Test
	public void testPOST()
	{
		String jsondata = WebRequestHandler.makeWebRequest("http://localhost:8079/Test/getCategoryElements", "POST", "category=Classes");
		System.out.println(jsondata);
		Type listOfTestObject = new TypeToken<ArrayList<Term>>(){}.getType();
		ArrayList<Term> data = WebRequestHandler.<ArrayList<Term>>deserialize(jsondata,listOfTestObject);
		System.out.println(data);
	}
}
