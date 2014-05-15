package org.njctl.courseapp.model.material;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.Unit;
import org.njctl.courseapp.model.subscribe.DownloadFinishListener;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.util.Log;

@DatabaseTable
public class Homework extends Document
{
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Homework, String> dao;

	public static void setDao(RuntimeExceptionDao<Homework, String> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Homework()
    {
    	
    }

    public static Homework get(Unit theUnit, JSONObject json, DownloadFinishListener<Document> listener)
	{
		try {
			if (checkJSON(json)) {
				Homework content;
				if (dao.idExists(json.getString("ID"))) {
					content = dao.queryForId(json.getString("ID"));
					content.downloadListener = listener;
					content.setProperties(json);
				} else {
					content = new Homework(theUnit, json);
					content.downloadListener = listener;
					content.created = true;
				}
				return content;
			} else {
				return null;
			}
		} catch (Exception e) { // never executed..
			Log.v("NJCTLLOG", "homework exception: " + e.getMessage());
			Log.v("NJCTLLOG", Log.getStackTraceString(e));
			return null;
		}
	}
    
    protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getString("ID");
			json.getString("post_name");
			json.getString("post_modified");
			json.getString("pdf_uri");
			
			json.getString("post_title");
    		

			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "    HW contents not found...");
			return false;
		}
	}
	
	public Homework(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
		
		setProperties(json);
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	protected void onDownloadFinish()
	{
		lastUpdated = lastUpdatedNew;
		dao.update(this);
	}

	protected void notifyDownloadListener()
	{
		if(downloadListener != null)
	    	downloadListener.onDownloaded(this);
	}
	
	public Homework(Parcel in)
	{
		Homework doc = dao.queryForId(in.readString());
		setByDocument(doc);
		unit = doc.unit;
	}
	
}
