package org.njctl.courseapp.model.material;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.DocumentState;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.subscribe.DownloadFinishListener;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.util.Log;

@DatabaseTable
public class Topic extends Document
{
	private static RuntimeExceptionDao<Topic, String> dao;
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected Presentation presentation;
	
	@DatabaseField
	protected String hash = "";
	
	@DatabaseField
	protected String newHash = "";

	public static void setDao(RuntimeExceptionDao<Topic, String> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Topic()
    {
    	
    }
    
    public static Topic get(Presentation pres, JSONObject json)
	{
		try {
			if (checkJSON(json))
			{
				if (dao.idExists(json.getString("post_name")))
				{
					Topic content = dao.queryForId(json.getString("post_name"));
					content.setProperties(json);
					content.checkOutdated();
					
					return content;
				}
				else
				{
					Topic content = new Topic(pres, json);

					return content;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
    
    protected void checkOutdated()
    {
    	if(newHash != hash)
    	{
    		state = DocumentState.OUTDATED;
    	}
    }
    
    protected static boolean checkJSON(JSONObject json)
	{
		try
		{
			json.getString("label");
			json.getString("post_name");
			json.getString("pdf_uri");
			json.getString("pdf_md5");
    		
			return true;
		}
		catch (JSONException e)
		{
			Log.w("NJCTLLOG", "                Topic contents not found...");
			return false;
		}
	}
    
    protected void setProperties(JSONObject json)
    {
    	try{
			name = json.getString("label");
			url = json.getString("pdf_uri");
			newHash = json.getString("pdf_md5");
			id = json.getString("post_name");
			
			Log.i("NJCTLLOG", "                Topic " + name + " successfully created.");
			
		}
		catch(JSONException e)
		{
			Log.w("JSON ERR", "                " + e.toString());
		}
    }
    
	public Topic(Presentation pres, JSONObject json)
	{
		presentation = pres;
		created = true;
		setProperties(json);
		lastUpdatedNew = pres.lastUpdatedNew;
		
		try {
			hash = json.getString("pdf_md5");
		} catch (JSONException e) {
			Log.v("NJCTLLOGTOPIC", Log.getStackTraceString(e));
		}
		
	}
	
	public void download()
	{
		if(state != DocumentState.OK)
		{
			doDownload();
		}
	}
	
	protected void onDownloadFinish()
	{
		lastUpdated = new Date();
		hash = newHash;
	}
	
	public Unit getUnit()
	{
		return presentation.getUnit();
	}
	
	protected void notifyDownloadListener()
	{
		if(downloadListener != null)
	    	downloadListener.onDownloaded(this);
	}
	
	public Topic(Parcel in)
	{
		Topic doc = dao.queryForId(in.readString());
		setByDocument(doc);
		hash = doc.hash;
		presentation = doc.presentation;
	}
}