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
public class Subject implements Parcelable {
	
    private int subjectId;
    private String subjectTitle;
    private int colorBarResource = 0;
    private ArrayList<Class> classes = new ArrayList<Class>();
    private Date lastUpdate;

    public Subject(String name, ArrayList<Class> classList) {
        this.subjectTitle = name;
        this.classes = classList;
    }
    
    public Subject(String name) {
        this.subjectTitle = name;
    }
    
    public Subject(JSONObject json)
    // TODO: This constructor will probably need to pull different stuff out of the JSON.
    {
    	try {
    		subjectTitle = json.getString("post_title");
    		
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONArray classList = json.getJSONArray("pages");
			
			for(int i = 0; i < classList.length(); i++)
			{
				classes.add(new Class(this, classList.getJSONObject(i)));
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
    
    public void add(Class aClass)
    {
    	this.classes.add(aClass);
    }
    
    // Parcelable constructor.
    public Subject(Parcel in) {
    	readFromParcel(in);
    }
    
    public String getTitle() {
    	return subjectTitle;
    }
    
    public int getId() {
    	return subjectId;
    }
    
    public int getColorBarResource() {
    	return colorBarResource;
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
    	dest.writeString(subjectTitle);
    	dest.writeParcelableArray(classes.toArray(new Class[classes.size()]), 0);
    }
    
    private void readFromParcel(Parcel in) {
    	subjectId = in.readInt();
    	subjectTitle = in.readString();
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
