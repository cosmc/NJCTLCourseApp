package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;

public class Topic extends Document
{
	private String hash;
	//private Document doc;
	private Integer size;
	
	public Topic(JSONObject json)
	{
		try{
			name = json.getString("name");
			url = json.getString("uri");
			hash = json.getString("hash");
			//size = json.getString("size");
			
			String modified = json.getString("mtime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			
			
			
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", e.toString());
		}
		catch (ParseException e)
		{
			Log.w("PARSE ERR", e.toString());
		}
	}
	
	public Topic(Parcel in)
	{
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public Integer getSize()
	{
		return size;
	}
}