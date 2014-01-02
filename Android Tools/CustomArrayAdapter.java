package com.example.ratemit.adpaters;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ratemit.R;
import com.example.ratemit.models.Category;
import com.example.ratemit.resources.FilterCallable;
import com.example.ratemit.resources.GenericCallable;
import com.example.ratemit.resources.WebRequestHandler;
import com.google.gson.reflect.TypeToken;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomArrayAdapter<T> extends ArrayAdapter<T>{
	
	private HashMap<View,T> viewToCategory;
	
	private GenericCallable<View> buttonCallback;
	
	private ListView listView;
	
	private Type t;
	
	private int templateResourceID;
	
	public CustomArrayAdapter(FragmentActivity fragment, int resource, List<T> objects, ListView v, Type t) {
		super(fragment, resource, objects);
		// TODO Auto-generated constructor stub
		this.viewToCategory = new HashMap<View,T>();
		this.templateResourceID = resource;
		this.listView = v;
		this.t = t;
	}
	public CustomArrayAdapter(FragmentActivity fragment, int resource, List<T> objects, ListView v, Type t, GenericCallable<View> callback) 
	{
		super(fragment, resource, objects);
		// TODO Auto-generated constructor stub
		this.viewToCategory = new HashMap<View,T>();
		this.templateResourceID = resource;
		this.buttonCallback = callback;
		this.listView = v;
		this.t = t;
	}
	public CustomArrayAdapter(Context context, int resource, List<T> objects, ListView v, Type t) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.viewToCategory = new HashMap<View,T>();
		this.templateResourceID = resource;
		this.listView = v;
		this.t = t;
	}
	public CustomArrayAdapter(Context context, int resource, List<T> objects, ListView v, Type t, GenericCallable<View> callback) 
	{
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.viewToCategory = new HashMap<View,T>();
		this.templateResourceID = resource;
		this.buttonCallback = callback;
		this.listView = v;
		this.t = t;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View v = convertView;
	    System.out.println("in get view");
	    if (v == null) {

	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(templateResourceID, null);

	    }
	    //get currently stored element
	    T ce = this.getItem(position);
	    
	    //try to serialize object and then deserialize into hashmap where 
	    //keys can be extracted
	    HashMap<String,Object> data = null;
	    try{
	    	String serialized = WebRequestHandler.serialize(ce, this.t);
	    	//data = WebRequestHandler.deserialize(serialized, new TypeToken<HashMap<String,Object>>(){}.getType());
	    	data = dataToHashMap(serialized, 2);
	    }
	    catch(Exception e)
	    {
	    	//e.printStackTrace();
	    }
	    System.out.println("made it to hashmap! %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	    //ensure that the value and that hashmap conversion worked
	    if(ce != null && data!=null){ 
		    System.out.println(data.toString());
		    for(String key: data.keySet())
		    {
		    	//find the corresponding view element and put its text in there
		    	View correspondingView = v.findViewWithTag(key);
		    	if(correspondingView != null && correspondingView instanceof TextView)
		    	{
		    		TextView textView = (TextView) correspondingView;
		    		textView.setText(data.get(key).toString());
		    	}
		    }
		    //link button callback
	        if(this.buttonCallback != null)
	        {
	        	v.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							buttonCallback.call(arg0);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
	        	});
	        }
		    //add to hashmap
	        this.viewToCategory.put(v, ce);
	    }
	    
	    return v;

	}
	private HashMap<String,Object> dataToHashMap(String serialized, int levels){
		HashMap<String,Object> data = new HashMap<String,Object>();
		if(levels == 0){
			return data;
		}
		HashMap<String,Object> dataTemp = new HashMap<String,Object>();
		System.out.println("Serialized!%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(serialized);
		try{
			dataTemp = WebRequestHandler.deserialize(serialized, new TypeToken<HashMap<String,Object>>(){}.getType());
		}catch(Exception e){
			return data;
		}
		if(dataTemp != null){
			for(String key: dataTemp.keySet()){
				boolean isString  = true;
				try{
					String val = (String)dataTemp.get(key);
				}catch(Exception e){
					isString = false;
				}
				if(!isString){
					String newSerialized = null;
					try{
						newSerialized = WebRequestHandler.serialize(dataTemp.get(key), new TypeToken<Object>(){}.getType());
					}catch(Exception e){
						newSerialized = null;
					}
					HashMap<String,Object> data1 = dataToHashMap(newSerialized, levels-1);
					if(data1.size() == 0){
						data.put(key, dataTemp.get(key));
					}
					for(String key1: data1.keySet()){
						Object finalValue = data1.get(key1);
						data.put(key+"_"+key1, finalValue);
					}
				}else{
					data.put(key, dataTemp.get(key));
				}
			}
		}
		System.out.println("data%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(data.toString());
		return data;
	}
	//TODO: fix this
	public void sortBy(Field f, boolean ascend)
	{
		//add stuff here
		try{
			this.t.getClass().getField(f.getName());
		}catch(Exception e)
		{
			
		}
	}
	//TODO: fix this
	public void filterBy(FilterCallable<T> f)
	{
		//add stuff here 
	}
	public void activateAdapter()
	{
		this.listView.setAdapter(this);
	}
	public T getDataFromView(View v)
	{
		T c = null;
		if(this.viewToCategory.containsKey(v))
		{
			c = this.viewToCategory.get(v);
		}
		return c;
	}
	public void updateAndNotify(ArrayList<T> data)
	{
		System.out.println("in update and notify");
		this.clear();
		this.addAll(data);
		this.notifyDataSetChanged();
		System.out.println("after notify");
		System.out.println(this.getCount());
	}
}
