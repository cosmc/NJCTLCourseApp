package org.njctl.courseapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Created by Colin on 12/12/13.
 *
 */

public class NJCTLDocument implements Parcelable {
	private String _id;
	private String _title;
	private String _relativePath;
	
	public NJCTLDocument(String title) {
		this._title = title;
	}
	
	public NJCTLDocument(Parcel in) {
		this._id = in.readString();
		this._title = in.readString();
		this._relativePath = in.readString();
	}

	public String getId() {
		return this._id;
	}
	
	public String getTitle() {
		return this._title;
	}
	
	public String getRelativePath() {
		return this._relativePath;
	}
	
    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(this._id);
    	dest.writeString(this._title);
    	dest.writeString(this._relativePath);
    }
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    	public NJCTLDocument createFromParcel(Parcel in) {
    		return new NJCTLDocument(in);
    	}
    	public NJCTLDocument[] newArray(int size) {
    		return new NJCTLDocument[size];
    	}
    };
	
}
