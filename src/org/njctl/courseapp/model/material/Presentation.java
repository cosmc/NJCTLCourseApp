package org.njctl.courseapp.model.material;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.DocumentState;
import org.njctl.courseapp.model.Unit;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.util.Log;

//TODO extends Document?!

@DatabaseTable
public class Presentation extends Document
{
	@ForeignCollectionField(eager = true)
	protected ForeignCollection<Topic> topics;
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Presentation, String> dao;

	public static void setDao(RuntimeExceptionDao<Presentation, String> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Presentation()
    {
    	
    }
    
    // Overrides default download behavior in order to be downloaded in case no topics are present,
    // and download the topics but not the whole presentation otherwise.
    public void download()
    {
    	if(hasTopics())
    	{
    		for(Topic topic : topics)
    		{
    			topic.download();
    		}
    	}
    	else
    	{
    		doDownload();
    	}
    }
    
    public Unit getUnit()
    {
    	return unit;
    }
	
	public boolean isDownloaded()
	{
		if(hasTopics())
    	{
			for(Topic topic : topics)
			{
				if(!topic.isDownloaded())
					return false;
			}
			return true;
    	}
		else
		{
			return state == DocumentState.OK;
		}
	}
	
	protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getInt("ID");
			json.getString("post_title");
			json.getString("post_name");
			json.getString("post_modified");
			
			if(json.has("chunks"))
				json.getJSONArray("chunks");
			else
				json.getString("pdf_uri");

			return true;
		}
		catch (JSONException e)
		{
			Log.w("NJCTLLOG", "                Presentation contents not found...");
			return false;
		}
	}
	
	public static Presentation get(Unit unit, JSONObject json)
	{
		try
		{
			if (checkJSON(json))
			{
				if (dao.idExists(json.getString("ID")))
				{
					Presentation content = dao.queryForId(json.getString("ID"));
					content.setProperties(json);
					
					return content;
				}
				else
				{
					Presentation content = new Presentation(unit, json);
					content.created = true;

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
		try
		{
			Date newLastUpdated = convertDate(json.getString("post_modified"));
			
			// Check if Presentation is already up to date.
			if(lastUpdatedNew == null || newLastUpdated.after(lastUpdatedNew))
			{
				lastUpdatedNew = newLastUpdated;
				name = json.getString("post_title");
				id = json.getString("ID");
				
				if(json.has("chunks"))
				{
					JSONArray topicsList = json.getJSONArray("chunks");
					Log.v("NJCTLLOG", "                Looping through " + topicsList.length() + " topics in " + name + " presentation..");
					
					for(int i = 0; i < topicsList.length(); i++)
					{
						Topic topic = Topic.get(this, topicsList.getJSONObject(i));
						if(topic.wasCreated())
						{
							topics.add(topic);
						}
						else
						{
							topics.update(topic);
						}
					}
				}
				else
				{
					url = json.getString("pdf_uri");
				}
			}
		}
		catch(SQLException e)
		{
			Log.w("SQL Presentation ERR", "                " + e.toString());
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", "                " + e.toString());
		}
	}
	
	public boolean hasTopics()
	{
		return topics.size() > 0;
	}
	
	public ArrayList<Topic> getTopics()
	{
		return new ArrayList<Topic>(topics);
	}
	
	public Presentation(Parcel in)
	{
		Presentation doc = dao.queryForId(in.readString());
		setByDocument(doc);
		unit = doc.unit;
	}
	
	protected void onDownloadFinish()
	{
		lastUpdated = lastUpdatedNew;
	}
	
	public Presentation(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
		topics = dao.getEmptyForeignCollection("topics");
		setProperties(json);
	}

	protected void notifyDownloadListener()
	{
		if(downloadListener != null)
	    	downloadListener.onDownloaded(this);
	}
}
