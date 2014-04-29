package org.njctl.courseapp.model.material;

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
			name = json.getString("label");
			url = json.getString("pdf_uri");
			hash = json.getString("pdf_md5");
			//size = json.getString("size");
			/*
			String modified = json.getString("mtime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			*/
			Log.i("NJCTLLOG", "                Topic " + name + " successfully created.");
			
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", "                " + e.toString());
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