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

import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@DatabaseTable(tableName = "subjects")
public class Subject implements Parcelable {
	
    private int subjectId;
    
    private String title;
    
    private int bottomColorBarResource = 0;
    private int bigSideColorBarResource = 0;
    private int smallSideColorBarResource = 0;
    
    private ArrayList<Class> classes = new ArrayList<Class>();
    private Date lastUpdate;

    public Subject(String name, ArrayList<Class> classList) {
        this.title = name;
        this.classes = classList;
    }
    
    public Subject(String name) {
        this.title = name;
    }
    
    public static Subject newInstance(JSONObject json)
    {
    	try
    	{
    		json.getJSONObject("content").getJSONArray("pages");
    		
    		return new Subject(json);
    	}
    	catch(JSONException e)
    	{
    		Log.w("NJCTLLOG", "    subject contents not found...");
    		return null;
    	}
    }
    
    public Subject(JSONObject json)
    {
    	try {
    		title = json.getString("post_title");
    		
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONArray classList = json.getJSONObject("content").getJSONArray("pages");
			Log.v("NJCTLLOG", "    Looping through " + Integer.toString(classList.length()) + " classes...");
			
			for(int i = 0; i < classList.length(); i++)
			{
				Class theClass = Class.newInstance(this, classList.getJSONObject(i));
				
				if(theClass != null)
				{
					classes.add(theClass);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("JSON ERR", "JSON ERR in Subject: " + e.toString());
		} catch(ParseException e)
		{
			Log.w("JSON ERR", e.toString());
		}
    }
    
    public ArrayList<Class> getClassesDownloaded()
    {
    	ArrayList<Class> classList = new ArrayList<Class>();
    	
    	for(int i = 0; i < classes.size(); i++)
    	{
    		if(classes.get(i).isDownloaded())
    		{
    			classList.add(classes.get(i));
    		}
    	}
    	
    	return classList;
    }
    
    public void add(Class aClass)
    {
    	this.classes.add(aClass);
    }
    
    // Parcelable constructor.
    public Subject(Parcel in) {
    	readFromParcel(in);
    }
    
    public String getTitle() {
    	return title;
    }
    
    public int getId() {
    	return subjectId;
    }
    
    public int getBottomColorBarResource() {
    	return bottomColorBarResource;
    }
    
    public int getBigSideColorBarResource() {
    	return bigSideColorBarResource;
    }
    
    public int getSmallSideColorBarResource() {
    	return smallSideColorBarResource;
    }

    public ArrayList<Class> getContents() {
    	return classes;
    }
    
    
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(subjectId);
    	dest.writeString(title);
    	dest.writeParcelableArray(classes.toArray(new Class[classes.size()]), 0);
    }
    
    private void readFromParcel(Parcel in) {
    	subjectId = in.readInt();
    	title = in.readString();
    	classes = new ArrayList<Class>();
        in.readList(classes, Class.class.getClassLoader());
    }
    
    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
    	public Subject createFromParcel(Parcel in) {
    		return new Subject(in);
    	}
    	public Subject[] newArray(int size) {
    		return new Subject[size];
    	}
    };
    
}
