package org.njctl.courseapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import android.util.Log;

/**
 * Created by ying on 11/3/13.
 */
public class NJCTLChapter implements Parcelable {
	
    private String chapterId;
    private String chapterTitle;
    private ArrayList<NJCTLDocList> contents;

    public NJCTLChapter(String id, String title, ArrayList<NJCTLDocList> cont) {
    	this.chapterId = id;
        this.chapterTitle = title;
        this.contents = cont;
    }
    
    // Mandatory Parcelable constructor.
    public NJCTLChapter(Parcel in) {
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
    
    public static final Parcelable.Creator<NJCTLChapter> CREATOR = new Parcelable.Creator<NJCTLChapter>() {
    	
    	public NJCTLChapter createFromParcel(Parcel in) {
    		return new NJCTLChapter(in);
    	}
    	
    	public NJCTLChapter[] newArray(int size) {
    		return new NJCTLChapter[size];
    	}
    	
    };
    
}
