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
import org.njctl.courseapp.model.material.Handout;
import org.njctl.courseapp.model.material.Topic;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.util.Log;

@DatabaseTable
public class Presentation
{
	protected String name;
	protected Date lastUpdated;
	protected String id;
	protected ArrayList<Topic> topics = new ArrayList<Topic>();
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Presentation, Integer> dao;

	public static void setDao(RuntimeExceptionDao<Presentation, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Presentation()
    {
    	
    }
	
	public boolean isDownloaded()
	{
		return true;
	}
	
	protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getString("ID");
			json.getString("post_name");
			json.getString("post_modified");
			json.getString("post_title");
    		
			json.getJSONArray("chunks");

			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "                Presentation contents not found...");
			return false;
		}
	}
	
	public static Presentation get(Unit unit, JSONObject json)
	{
		try {
			if (checkJSON(json)) {
				if (dao.idExists(json.getInt("ID"))) {
					Presentation content = dao.queryForId(json.getInt("ID"));
					content.setProperties(json);
					dao.update(content);
					return content;
				} else {
					Presentation content = new Presentation(unit, json);
					dao.create(content);

					return content;
				}
			} else {
				return null;
			}
		} catch (Exception e) { // never executed..
			return null;
		}
	}
	
	protected void setProperties(JSONObject json)
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
				Topic topic = Topic.get(this, topicsList.getJSONObject(i));
				topics.add(topic);
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
	
	public Presentation(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
	}

}
