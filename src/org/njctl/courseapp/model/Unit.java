package org.njctl.courseapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.njctl.courseapp.model.material.Document;
import org.njctl.courseapp.model.material.Homework;

/**
 * Created by ying on 11/3/13.
 */
public class Unit implements Parcelable {
	
    private String chapterId;
    private String chapterTitle;
    private ArrayList<Document> contents = new ArrayList<Document>();
    private ArrayList<Homework> homeworks = new ArrayList<Homework>();
    private ArrayList<Presentation> presentations = new ArrayList<Presentation>();
    private Date lastUpdate;

    public Unit(String id, String title, ArrayList<Document> cont) {
    	this.chapterId = id;
        this.chapterTitle = title;
        this.contents = cont;
    }
    
    public Unit(String id, String title) {
    	this.chapterId = id;
        this.chapterTitle = title;
    }
    
    public static Unit newInstance(JSONObject json)
    {
    	try
    	{
    		json.getJSONObject("content").getJSONArray("presentations");
    		
    		return new Unit(json);
    	}
    	catch(JSONException e)
    	{
    		Log.v("NJCTLLOG", "unit contents not found...");
    		return null;
    	}
    }
    
    public Unit(JSONObject json)
    {
    	try {
			chapterTitle = json.getString("post_title");
			
			String modified = json.getString("post_modified");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			lastUpdate = df.parse(modified);
			
			JSONObject content = json.getJSONObject("content");
			
			if(content.has("homework"))
			{
				JSONArray homeworkList = content.getJSONArray("homework");
				Log.v("NJCTLLOG", "Looping through " + Integer.toString(homeworkList.length()) + " homeworks...");
				
				for(int i = 0; i < homeworkList.length(); i++)
				{
					homeworks.add(new Homework(homeworkList.getJSONObject(i)));
				}
			}
			
			JSONArray presentationList = content.getJSONArray("presentations");
			Log.v("NJCTLLOG", "Looping through " + Integer.toString(presentationList.length()) + " presentations...");
			
			for(int i = 0; i < presentationList.length(); i++)
			{
				Presentation presentation = Presentation.newInstance(presentationList.getJSONObject(i));
				
				if(presentation != null)
				{
					presentations.add(presentation);
				}
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		} catch (ParseException e)
		{
			e.printStackTrace();
			Log.w("JSON ERR", e.toString());
		}
    }
    
    public void add(Document docList)
    {
    	this.contents.add(docList);
    }
    
    // Mandatory Parcelable constructor.
    public Unit(Parcel in) {
    	readFromParcel(in);
    }
    
	public String getId() {
    	return chapterId;
    }
    
    public String getTitle() {
    	return chapterTitle;
    }
    
    public ArrayList<Document> getContents() {
    	return contents;
    }
    
    
    // Mandatory Parcelable methods.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(chapterId);
    	dest.writeString(chapterTitle);
    	dest.writeParcelableArray(homeworks.toArray(new Homework[homeworks.size()]), 0);
    	dest.writeParcelableArray(presentations.toArray(new Presentation[presentations.size()]), 0);
    	//dest.writeParcelableArray(contents.toArray(new NJCTLDocList[contents.size()]), 0);
    }

    private void readFromParcel(Parcel in) {
    	this.chapterId = in.readString();
    	this.chapterTitle = in.readString();
    	this.homeworks = new ArrayList<Homework>();
    	in.readList(this.homeworks, Homework.class.getClassLoader());
    	this.presentations = new ArrayList<Presentation>();
        in.readList(this.presentations, Presentation.class.getClassLoader());
    }
    
    public static final Parcelable.Creator<Unit> CREATOR = new Parcelable.Creator<Unit>() {
    	
    	public Unit createFromParcel(Parcel in) {
    		return new Unit(in);
    	}
    	
    	public Unit[] newArray(int size) {
    		return new Unit[size];
    	}
    	
    };
    
}
