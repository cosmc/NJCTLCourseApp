package org.njctl.courseapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by ying on 11/3/13.
 */
public class Class implements Parcelable {
	
	protected int classId;
    protected String classTitle;
    protected ArrayList<Unit> units = new ArrayList<Unit>();
    protected Date lastUpdate;
    protected Subject subject;

    public Class(String name, ArrayList<Unit> unitList) {
        this.classTitle = name;
        this.units = unitList;
    }
    
    public Class(String name) {
        this.classTitle = name;
    }
    
    public static Class newInstance(Subject subject, JSONObject json)
    {
    	String classTitle = "";
    	
    	try
    	{
    		classTitle = json.getString("post_title");
    		
    		json.getJSONObject("content").getJSONArray("pages");
    		
    		return new Class(subject, json);
    	}
    	catch(JSONException e)
    	{
    		if(classTitle != "")
    		{
    			classTitle = " for class " + classTitle;
    		}
    		
    		Log.v("NJCTLLOG", "class contents" + classTitle + " not found...");
    		return null;
    	}
    }
    
    public Class(Subject subject, JSONObject json)
    {
    	this.subject = subject;
    	
    	Log.v("NJCTLLOG", json.toString());
    	
    	try {
    		classTitle = json.getString("post_title");
    		
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONArray unitList = json.getJSONObject("content").getJSONArray("pages");
			Log.v("NJCTLLOG", "Looping through " + Integer.toString(unitList.length()) + " units...");
			
			for(int i = 0; i < unitList.length(); i++)
			{
				units.add(new Unit(unitList.getJSONObject(i)));
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
    	return classTitle;
    }
    
    public int getId() {
    	return classId;
    }
    
    public ArrayList<Unit> getContents() {
    	return units;
    }
    
    
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(classId);
    	dest.writeString(classTitle);
    	dest.writeParcelableArray(units.toArray(new Unit[units.size()]), 0);
    }
    
    private void readFromParcel(Parcel in) {
    	classId = in.readInt();
    	classTitle = in.readString();
    	units = new ArrayList<Unit>();
        in.readList(units, Unit.class.getClassLoader());
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
