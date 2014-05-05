package org.njctl.courseapp.model.material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.Class;
import org.njctl.courseapp.model.Presentation;
import org.njctl.courseapp.model.Unit;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.support.v4.util.ArrayMap;
import android.util.Log;

@DatabaseTable
public class Topic extends Document
{
	private static RuntimeExceptionDao<Topic, Integer> dao;
	
	@DatabaseField
	private String hash;
	
	private Integer size;
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected Presentation presentation;

	public static void setDao(RuntimeExceptionDao<Topic, Integer> newDao)
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
				ArrayMap<String, Object> conditions = new ArrayMap<String, Object>();
				conditions.put("presentation", pres);
				List<Topic> results = dao.queryForFieldValues(conditions);
				ArrayList<Topic> topics = new ArrayList<Topic>(results);
				
				if (topics.size() == 1) {
					Topic content = topics.get(0);
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
		} catch (Exception e) { // never executed..
			return null;
		}
	}
    
    protected static boolean checkJSON(JSONObject json)
	{
		try {
			json.getString("label");
			json.getString("pdf_uri");
			json.getString("pdf_md5");
    		

			return true;
		} catch (JSONException e) {
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
			
			//size = json.getString("size");
			/*
			String modified = json.getString("mtime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdated = df.parse(modified);
			*/
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
	
	public Topic(Parcel in)
	{
		//super(in);
		// TODO Auto-generated constructor stub
	}
	
	public Integer getSize()
	{
		return size;
	}
}