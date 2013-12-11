package org.njctl.courseapp;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ying on 11/3/13.
 */
public class NJCTLClass implements Parcelable {
	
    private int classId;
    private String className;
    private ArrayList<NJCTLChapter> contents;

    public NJCTLClass(String name, ArrayList<NJCTLChapter> chapters) {
        this.className = name;
        this.contents = chapters;
    }
    
    // Parcelable constructor.
    public NJCTLClass(Parcel in) {
    	readFromParcel(in);
    }
    
    public String getName() {
    	return className;
    }
    
    public int getId() {
    	return classId;
    }
    
    public ArrayList<NJCTLChapter> getContents() {
    	return contents;
    }
    
    
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(classId);
    	dest.writeString(className);
    	dest.writeParcelableArray((NJCTLChapter[]) contents.toArray(), flags);
    }
    
    private void readFromParcel(Parcel in) {
    	classId = in.readInt();
    	className = in.readString();
    	contents = new ArrayList<NJCTLChapter>();
        in.readList(contents, NJCTLChapter.class.getClassLoader());
    }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    	public NJCTLClass createFromParcel(Parcel in) {
    		return new NJCTLClass(in);
    	}
    	public NJCTLClass[] newArray(int size) {
    		return new NJCTLClass[size];
    	}
    };
    
}
