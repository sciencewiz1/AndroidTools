package com.androidtools.Web;

public class TestServerResponseObject extends ServerResponseObject {

	public String displayName;
	
	public String otherData;
	
	@Override
	public String toString()
	{
		return displayName+","+otherData;
		
	}
}
