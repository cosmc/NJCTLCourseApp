package org.njctl.courseapp.model.material;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.Unit;

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

    public static Homework get(Unit theUnit, JSONObject json)
	{
		try {
			if (checkJSON(json)) {
				Homework content;
				if (dao.idExists(json.getString("ID"))) {
					content = dao.queryForId(json.getString("ID"));
					content.setProperties(json);
				} else {
					content = new Homework(theUnit, json);
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
	
	protected void setProperties(JSONObject json)
	{
		try{
			name = json.getString("post_title");
			id = json.getString("ID");
			
			if(json.has("pdf_uri"))
			{
				url = json.getString("pdf_uri");
			}
			else
			{
				Log.w("NJCTLLOG", "                pdf_uri not found for homework " + name);
			}
			
			
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			
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
	
	public Homework(Unit theUnit, JSONObject json)
	{
		unit = theUnit;
		
		setProperties(json);
	}
	
	public Unit getUnit()
	{
		return unit;
	}

	protected void notifyListener()
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
