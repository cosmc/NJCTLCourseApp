package org.njctl.courseapp.model.material;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.Presentation;
import org.njctl.courseapp.model.Unit;

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
					dao.update(content);
					return content;
				}
				else
				{
					Topic content = new Topic(pres, json);
					dao.create(content);

					return content;
				}
			} else {
				return null;
			}
		} catch (Exception e) { // never executed if checkJSON works correctly.
			return null;
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
			hash = json.getString("pdf_md5");
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
	}
	
	public Unit getUnit()
	{
		return presentation.getUnit();
	}
	
	public Topic(Parcel in)
	{
		Topic doc = dao.queryForId(in.readString());
		setByDocument(doc);
		presentation = doc.presentation;
	}
}