package org.njctl.courseapp.model.material;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.util.Log;

@DatabaseTable
public class Handout extends Document
{
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Handout, String> dao;

	public static void setDao(RuntimeExceptionDao<Handout, String> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	// For ORM.
    Handout()
    {
    	
    }
	
	public static Handout get(Unit unit, JSONObject json, DownloadFinishListener<Document> listener, int newOrder)
	{
		try {
			if (checkJSON(json))
			{
				if (dao.idExists(json.getString("ID")))
				{
					Handout content = dao.queryForId(json.getString("ID"));
					content.downloadListener = listener;
					content.order = newOrder;
					content.setProperties(json);
					content.checkOutdated();
					
					return content;
				}
				else
				{
					Handout content = new Handout(unit, json);
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
			Log.w("NJCTLLOG", "    Handout contents not found...");
			return false;
		}
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	public Handout(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
		
		setProperties(json);
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
	
	public Handout(Parcel in)
	{
		Handout doc = dao.queryForId(in.readString());
		setByDocument(doc);
		unit = doc.unit;
	}

	public static RuntimeExceptionDao<Handout, String> getDao()
	{
		return dao;
	}
}
