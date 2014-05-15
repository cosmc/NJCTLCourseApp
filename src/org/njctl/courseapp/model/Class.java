package org.njctl.courseapp.model;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@DatabaseTable
public class Class implements Parcelable, DownloadFinishListener<Unit>
{
	@DatabaseField(id = true)
	protected int id;
	
	@DatabaseField
    protected String title;
	
	@ForeignCollectionField(eager = true, orderColumnName = "order")
    protected ForeignCollection<Unit> units;
    
    @DatabaseField
    protected Date lastUpdate;
    
    @DatabaseField(canBeNull = false, foreign = true)
    protected Subject subject;
    
    @DatabaseField
    protected boolean subscribed = false;
    
    @DatabaseField
    protected boolean downloaded = false;
    
    @DatabaseField
    protected Date lastOpened;
    
    @DatabaseField
    protected Integer order;
    
    protected boolean created = false;
    
    protected Integer downloadingUnits = 0;
    
    private static RuntimeExceptionDao<Class, Integer> dao;
    protected DownloadFinishListener<Class> downloadListener;

	public static void setDao(RuntimeExceptionDao<Class, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
	
	public static RuntimeExceptionDao<Class, Integer> getDao()
	{
		return dao;
	}
    
    // For ORM.
    Class()
    {
    	
    }
    
    public void setLastOpened()
    {
    	lastOpened = new Date();
    }
    
    public void subscribe()
    {
    	subscribed = true;
    	
    	for(Unit unit : units)
    	{
    		unit.subscribe();
    	}
    }
    
    public void unsubscribe()
    {
    	subscribed = false;
    	
    	for(Unit unit : units)
    	{
    		unit.unsubscribe();
    	}
    }
    
    public boolean isSubscribed()
    {
    	return subscribed;
    }
    
    public boolean isPartiallySubscribed()
    {
    	for(Unit unit : units)
    	{
    		if(unit.isSubscribed())
    			return true;
    	}
    	return false;
    }
    
    public void download(DownloadFinishListener<Class> listener)
    {
    	downloadListener = listener;
    	download();
    }
    
    public void download()
    {
    	for(Unit unit : units)
    	{
    		unit.download(this);
    		downloadingUnits++;
    	}
    }
    
    public void delete()
    {
    	for(Unit unit : units)
    	{
    		unit.delete();
    	}
    }
    
    public boolean isDownloaded()
    {
    	Log.v("NJCTLLOG", "this class has " + units.size() + " units.");
    	for(Unit unit : units)
    	{
    		if(!unit.isDownloaded())
    			return false;
    	}
    	return true;
    }
    
    public boolean isPartiallyDownloaded()
    {
    	for(Unit unit : units)
    	{
    		if(unit.isDownloaded())
    			return true;
    	}
    	return false;
    }
    
    public ArrayList<Unit> getUnits()
    {
    	return new ArrayList<Unit>(units);
    }

	public void onDownloaded(Unit unit)
	{
		downloadingUnits--;
		
		if(downloadingUnits == 0)
		{
			downloaded = true;
			
			if(downloadListener != null)
			{
				downloadListener.onDownloaded(this);
			}
		}
	}
	
	boolean wasCreated()
	{
		return created;
	}
	
	public static Class get(Subject subject, JSONObject json, DownloadFinishListener<Class> listener, int newOrder)
	{
		try
		{
			if (checkJSON(json))
			{
				Class content;
				
				if (dao.idExists(json.getInt("ID")))
				{
					content = dao.queryForId(json.getInt("ID"));
					content.downloadListener = listener;
					content.order = newOrder;
					content.created = false;
					Log.v("NJCTLLOG", "Loaded a class " + content.getId() + " for a subject....");
					content.setProperties(json);
				}
				else
				{
					content = new Class(subject, json);
					content.downloadListener = listener;
					content.order = newOrder;
					content.created = true;
					Log.v("NJCTLLOG", "Created a class " + content.getId() + " for a subject....");
				}
				
				return content;
			}
			else
			{
				Log.v("NJCTLLOG", "class json wrong..");
				return null;
			}
		}
		catch (Exception e)
		{
			Log.v("NJCTLLOG", "class exception: " + e.getMessage());
			Log.v("NJCTLLOG", Log.getStackTraceString(e));
			return null;
		}
	}
    
    protected static boolean checkJSON(JSONObject json)
	{
    	String theTitle = "";
		try {
			json.getString("ID");
			//json.getString("post_name");
			theTitle = json.getString("post_title");
			json.getString("post_modified");
			json.getJSONObject("content").getJSONArray("units");

			return true;
		} catch (JSONException e) {
			Log.w("NJCTLLOG", "    class contents not found for " + theTitle + "...");
			return false;
		}
	}
    
    public Class(Subject subject, JSONObject json)
    {
    	this.subject = subject;
    	units = dao.getEmptyForeignCollection("units");
    	
    	setProperties(json);
    }
    protected void setProperties(JSONObject json)
	{
    	try {
    		title = json.getString("post_title");
    		id = json.getInt("ID");
    		//name = json.getString("post_name");
    		
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			RuntimeExceptionDao<Unit, Integer> dao = Unit.getDao();
			List<Unit> oldContents = dao.queryForAll();
			List<Integer> newIds = new ArrayList<Integer>();
			
			JSONArray unitList = json.getJSONObject("content").getJSONArray("units");
			Log.v("NJCTLLOG", "        Looping through " + Integer.toString(unitList.length()) + " units in Class " + title + "...");
			
			for(int i = 0; i < unitList.length(); i++)
			{
				Unit unit = Unit.get(this, unitList.getJSONObject(i), i);
				
				if(unit != null)
				{
					newIds.add(unit.getId());
					
					if(unit.wasCreated())
					{
						units.add(unit);
						
						if (subscribed)
							unit.subscribe();
					}
					else
					{
						units.update(unit);
					}
				}
			}
			
			for(Unit oldContent : oldContents)
			{
				if(!newIds.contains(oldContent.getId()))
				{
					Log.v("NJCTLLOG", "unit has been deleted from json!");
					dao.delete(oldContent);
				}
			}
			
    	} catch (SQLException e) {
    		//e.printStackTrace();
			Log.w("Class SQL ERR", e.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		} catch(ParseException e)
		{
			Log.w("JSON ERR", e.toString());
		}
	}
    
    public Subject getSubject()
    {
    	return subject;
    }
    
    public void add(Unit chapter)
    {
    	this.units.add(chapter);
    }
    
    public String getTitle() {
    	return title;
    }
    
    public int getId() {
    	return id;
    }
    
    public String toString()
    {
    	return this.getTitle();
    }
    
    // Methods for Parcelable implementation.
    public int describeContents() {
    	return 0;
    }
    
    public void writeToParcel(Parcel dest, int flags)
    {
    	dest.writeInt(id);
    }
    
    public static final Parcelable.Creator<Class> CREATOR = new Parcelable.Creator<Class>() {
    	public Class createFromParcel(Parcel in)
    	{
    		Integer id = in.readInt();
    		
    		return dao.queryForId(id);
    	}
    	public Class[] newArray(int size) {
    		return new Class[size];
    	}
    };
}
