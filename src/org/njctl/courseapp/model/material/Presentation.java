package org.njctl.courseapp.model.material;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.support.v4.util.ArrayMap;
import android.util.Log;

//TODO extends Document?!

@DatabaseTable
public class Presentation extends Document implements DownloadFinishListener<Document>
{
	@ForeignCollectionField(eager = true, orderColumnName = "order")
	protected ForeignCollection<Topic> topics;
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Presentation, String> dao;
	
	protected Integer downloadingTopics = 0;

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
    			downloadingTopics++;
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
	
	public static Presentation get(Unit unit, JSONObject json, DownloadFinishListener<Document> listener, int newOrder)
	{
		try
		{
			if (checkJSON(json))
			{
				if (dao.idExists(json.getString("ID")))
				{
					Presentation content = dao.queryForId(json.getString("ID"));
					content.downloadListener = listener;
					content.order = newOrder;
					content.setProperties(json);
					content.checkOutdated();
					
					return content;
				}
				else
				{
					Presentation content = new Presentation(unit, json);
					content.downloadListener = listener;
					content.order = newOrder;
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
				title = json.getString("post_title");
				id = json.getString("ID");
				checkOutdated();
				
				if(json.has("chunks"))
				{
					JSONArray topicsList = json.getJSONArray("chunks");
					RuntimeExceptionDao<Topic, String> dao = Topic.getDao();
					Map<String, Object> condition = new ArrayMap<String, Object>(1);
					condition.put("presentation_id", this.getId());
					List<Topic> oldContents = dao.queryForFieldValues(condition);
					List<String> newIds = new ArrayList<String>();
					
					Log.v("NJCTLLOG", "                Looping through " + topicsList.length() + " topics in " + title + " presentation..");
					
					for(int i = 0; i < topicsList.length(); i++)
					{
						Topic topic = Topic.get(this, topicsList.getJSONObject(i), this, i);
						
						if(topic != null)
						{
							newIds.add(topic.getId());
							
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
					
					for(Topic oldContent : oldContents)
					{
						if(!newIds.contains(oldContent.getId()))
						{
							Log.v("NJCTLLOG", "topic has been deleted from json!");
							dao.delete(oldContent);
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
		dao.update(this);
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

	public void onDownloaded(Document content)
	{
		downloadingTopics--;
		
		if(downloadingTopics == 0)
			notifyDownloadListener();
	}

	public static RuntimeExceptionDao<Presentation, String> getDao()
	{
		return dao;
	}
}
