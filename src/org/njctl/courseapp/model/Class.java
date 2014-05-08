package org.njctl.courseapp.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.subscribe.ClassDownloader;
import org.njctl.courseapp.model.subscribe.DownloadFinishListener;
import org.njctl.courseapp.model.subscribe.Downloader;

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
	
	@ForeignCollectionField(eager = true)
    protected ForeignCollection<Unit> units;
    
    @DatabaseField
    protected Date lastUpdate;
    
    @DatabaseField(canBeNull = false, foreign = true)
    protected Subject subject;
    
    @DatabaseField
    protected boolean subscribed = false;
    
    @DatabaseField
    protected boolean downloaded = false;
    
    protected Integer downloadingUnits = 0;
    
    private static RuntimeExceptionDao<Class, Integer> dao;
    protected DownloadFinishListener<Class> downloadListener;

	public static void setDao(RuntimeExceptionDao<Class, Integer> newDao)
	{
		if (dao == null)
			dao = newDao;
	}
    
    // For ORM.
    Class()
    {
    	
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
	
	public static Class get(Subject subject, JSONObject json)
	{
		try {
			if (checkJSON(json)) {
				if (dao.idExists(json.getInt("ID"))) {
					Class content = dao.queryForId(json.getInt("ID"));
					content.setProperties(json);
					dao.update(content);
					return content;
				} else {
					Class content = new Class(subject, json);
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
    		//name = json.getString("post_name");
    		
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONArray unitList = json.getJSONObject("content").getJSONArray("units");
			Log.v("NJCTLLOG", "        Looping through " + Integer.toString(unitList.length()) + " units in Class " + title + "...");
			
			for(int i = 0; i < unitList.length(); i++)
			{
				Unit unit = Unit.get(this, unitList.getJSONObject(i));
				
				if(unit != null)
				{
					units.add(unit);
					if (subscribed)
						unit.subscribe();
				}
			}
			
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
    
    // Parcelable constructor.
    public Class(Parcel in) {
    	readFromParcel(in);
    }
    
    public String getTitle() {
    	return title;
    }
    
    public int getId() {
    	return id;
    }
    
    public ArrayList<Unit> getContents() {
    	return getUnits();
    }
    
    
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
    	dest.writeInt(id);
    	dest.writeString(title);
    	dest.writeParcelableArray(units.toArray(new Unit[units.size()]), 0);
    }
    
    private void readFromParcel(Parcel in)
    {
    	units = dao.getEmptyForeignCollection("units");
    	
    	id = in.readInt();
    	title = in.readString();
    	
    	ArrayList<Unit> theUnits = new ArrayList<Unit>();
		in.readList(theUnits, Unit.class.getClassLoader());
		
		//Fill classes
		for(Unit unit : units)
		{
			units.add(unit);
		}
    }
    
    public static final Parcelable.Creator<Class> CREATOR = new Parcelable.Creator<Class>() {
    	public Class createFromParcel(Parcel in) {
    		return new Class(in);
    	}
    	public Class[] newArray(int size) {
    		return new Class[size];
    	}
    };

}
