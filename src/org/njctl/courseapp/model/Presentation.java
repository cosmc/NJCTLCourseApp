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
	
	public static Presentation newInstance(JSONObject json)
	{
		String presentationTitle = "";
		
		try{
			presentationTitle = json.getString("title");
			json.getString("date");
			json.getJSONArray("chunks");
			
			return new Presentation(json);
		}
		catch(JSONException e)
		{
			if(presentationTitle != "")
    		{
				presentationTitle = " for " + presentationTitle;
    		}
			
			Log.v("NJCTLLOG", "presentation contents not found" + presentationTitle);
			return null;
		}
	}
	
	public Presentation(JSONObject json)
	{
		try{
			name = json.getString("title");
			
			if(json.has("pdf_uri"))
			{
				url = json.getString("pdf_uri");
			}
			else
			{
				Log.v("NJCTLLOG", "pdf_uri not found for presentation " + name);
			}
			
			
			String modified = json.getString("date");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			
			JSONArray topicsList = json.getJSONArray("chunks");
			Log.v("NJCTLLOG", "Looping through " + topicsList.length() + " topics in " + name + " presentation..");
			
			for(int i = 0; i < topicsList.length(); i++)
			{
				topics.add(new Topic(topicsList.getJSONObject(i)));
			}
			
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
