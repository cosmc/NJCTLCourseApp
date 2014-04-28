package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;

public class Presentation extends Document
{
	private String topicsJsonUrl = "http://content-archive.sandbox-njctl.org/courses/science/ap-physics-b/kinematics-2/kinematics-presentation-2/kinematics-presentation-2.json";
	private ArrayList<Topic> topics = new ArrayList<Topic>();
	
	public Presentation(Parcel in)
	{
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public Presentation(JSONObject json)
	{
		try{
			name = json.getString("title");
			url = json.getString("uri");
			
			String modified = json.getString("last-modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			/*
			JSONArray topicsList = json.getJSONArray("topics");
			for(int i = 0; i < topicsList.length(); i++)
			{
				topics.add(new Topic(topicsList.getJSONObject(i)));
			}*/
			
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

}
