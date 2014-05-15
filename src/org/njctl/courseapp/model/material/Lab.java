package org.njctl.courseapp.model.material;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.DownloadFinishListener;
import org.njctl.courseapp.model.Unit;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@DatabaseTable
public class Lab extends Document
{
	@DatabaseField(canBeNull = false, foreign = true)
	protected Unit unit;
	
	private static RuntimeExceptionDao<Lab, String> dao;
	
	// For ORM.
	Lab()
	{
		
	}

	public static void setDao(RuntimeExceptionDao<Lab, String> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	public static Lab get(Unit theUnit, JSONObject json, DownloadFinishListener<Document> listener, int newOrder)
	{
		try
		{
			if (checkJSON(json))
			{
				if (dao.idExists(json.getString("ID")))
				{
					Lab content = dao.queryForId(json.getString("ID"));
					content.downloadListener = listener;
					content.order = newOrder;
					content.setProperties(json);
					content.checkOutdated();
					
					return content;
				}
				else
				{
					Lab content = new Lab(theUnit, json);
					content.downloadListener = listener;
					content.order = newOrder;
					content.created = true;

					return content;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getString("ID");
			json.getString("post_name");
			json.getString("post_modified");
			json.getString("post_title");
    		
			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "    Lab contents not found...");
			return false;
		}
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	public Lab(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
		
		setProperties(json);
	}
	
	protected void notifyDownloadListener()
	{
		if(downloadListener != null)
	    	downloadListener.onDownloaded(this);
	}
	
	public Lab(Parcel in)
	{
		Lab doc = dao.queryForId(in.readString());
		setByDocument(doc);
		unit = doc.unit;
	}
	
	protected void onDownloadFinish()
	{
		lastUpdated = lastUpdatedNew;
		dao.update(this);
	}
	
	public static final Parcelable.Creator<Lab> CREATOR = new Parcelable.Creator<Lab>() {
    	public Lab createFromParcel(Parcel in)
    	{
    		String id = in.readString();
    		
    		return dao.queryForId(id);
    	}
    	public Lab[] newArray(int size) {
    		return new Lab[size];
    	}
    };

	public static RuntimeExceptionDao<Lab, String> getDao()
	{
		return dao;
	}
}
