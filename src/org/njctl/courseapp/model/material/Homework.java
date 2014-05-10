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
	
	private static RuntimeExceptionDao<Homework, Integer> dao;

	public static void setDao(RuntimeExceptionDao<Homework, Integer> newDao)
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
				if (dao.idExists(json.getInt("ID"))) {
					Homework content = dao.queryForId(json.getInt("ID"));
					content.setProperties(json);
					dao.update(content);
					return content;
				} else {
					Homework content = new Homework(theUnit, json);
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
	

	public Homework(Parcel in)
	{
		//super(in);
		// TODO Auto-generated constructor stub
	}

}
