package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;

public class Homework extends Document
{
	public Homework(JSONObject json)
	{
		try{
			name = json.getString("title");
			url = json.getString("pdf_uri");
			
			String modified = json.getString("date");
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
	

	public Homework(Parcel in)
	{
		super(in);
		// TODO Auto-generated constructor stub
	}

}
