package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.material.Document;
import org.njctl.courseapp.model.material.Topic;

import android.os.Parcel;
import android.util.Log;

public class Presentation
{
	protected String name;
	protected Date lastUpdated;
	protected String id;
	protected ArrayList<Topic> topics = new ArrayList<Topic>();
	
	public Presentation(Parcel in)
	{

	}
	
	public boolean isDownloaded()
	{
		return true;
	}
	
	public static Presentation newInstance(JSONObject json)
	{
		String presentationTitle = "";
		String date = "";
		String reason = "";
		
		try{
			presentationTitle = json.getString("post_title");
			date = json.getString("post_modified");
			json.getString("ID");
			json.getJSONArray("chunks");
			
			return new Presentation(json);
		}
		catch(JSONException e)
		{
			if(presentationTitle != "")
    		{
				presentationTitle = " for " + presentationTitle;
    		}
			if(date != "" && presentationTitle != "")
				reason = " because no chunks (topics) were found.";
			
			Log.w("NJCTLLOG", "                presentation contents not found" + presentationTitle + reason);
			return null;
		}
	}
	
	public Presentation(JSONObject json)
	{
		try{
			name = json.getString("post_title");
			/*
			if(json.has("pdf_uri"))
			{
				url = json.getString("pdf_uri");
			}
			else
			{
				Log.w("NJCTLLOG", "pdf_uri not found for presentation " + name);
			}*/
			
			
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			
			id = json.getString("ID");
			
			JSONArray topicsList = json.getJSONArray("chunks");
			Log.v("NJCTLLOG", "                Looping through " + topicsList.length() + " topics in " + name + " presentation..");
			
			for(int i = 0; i < topicsList.length(); i++)
			{
				topics.add(new Topic(this, id, topicsList.getJSONObject(i)));
			}
			
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", "                " + e.toString());
		}
		catch (ParseException e)
		{
			Log.w("PARSE ERR", "                " + e.toString());
		}
	}

}
