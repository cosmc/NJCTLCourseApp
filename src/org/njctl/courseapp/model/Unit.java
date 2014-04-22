package org.njctl.courseapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by ying on 11/3/13.
 */
public class Unit implements Parcelable {
	
    private String chapterId;
    private String chapterTitle;
    private ArrayList<NJCTLDocList> contents = new ArrayList<NJCTLDocList>();

    public Unit(String id, String title, ArrayList<NJCTLDocList> cont) {
    	this.chapterId = id;
        this.chapterTitle = title;
        this.contents = cont;
    }
    
    public Unit(String id, String title) {
    	this.chapterId = id;
        this.chapterTitle = title;
    }
    
    public Unit(JSONObject json)
    {
    	
    }
    
    public void add(NJCTLDocList docList)
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
    
    public ArrayList<NJCTLDocList> getContents() {
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
    	dest.writeParcelableArray(contents.toArray(new NJCTLDocList[contents.size()]), 0);
    }

    private void readFromParcel(Parcel in) {
    	this.chapterId = in.readString();
    	this.chapterTitle = in.readString();
    	this.contents = new ArrayList<NJCTLDocList>();
        in.readList(this.contents, NJCTLDocList.class.getClassLoader());
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
