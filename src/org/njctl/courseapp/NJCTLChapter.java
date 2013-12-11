package org.njctl.courseapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ying on 11/3/13.
 */
public class NJCTLChapter implements Parcelable {
	
    private int chapterId;
    private String chapterTitle;
    private String lectureDocumentId;
    private String homeworkDocumentId;

    public NJCTLChapter(String title) {
        chapterTitle = title;
    }
    
    // Mandatory Parcelable constructor.
    public NJCTLChapter(Parcel in) {
    	readFromParcel(in);
    }
    
    public String getTitle() {
    	return chapterTitle;
    }
    
    public String getLecture() {
    	return "";
    }

    public String getHomework() {
    	return "";
    }
    
    
    // Mandatory Parcelable methods.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(chapterId);
    	dest.writeString(chapterTitle);
    	dest.writeString(lectureDocumentId);
    	dest.writeString(homeworkDocumentId);
    }

    private void readFromParcel(Parcel in) {
    	chapterId = in.readInt();
    	chapterTitle = in.readString();
    	lectureDocumentId = in.readString();
    	homeworkDocumentId = in.readString();
    }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    	
    	public NJCTLChapter createFromParcel(Parcel in) {
    		return new NJCTLChapter(in);
    	}
    	
    	public NJCTLChapter[] newArray(int size) {
    		return new NJCTLChapter[size];
    	}
    	
    };
    
}
