package org.njctl.courseapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * 
 * Created by Colin on 12/12/13.
 *
 */

public class NJCTLDocList implements Parcelable {
	private String _id;
	private String _title;
	private ArrayList<NJCTLDocument> _docs  = new ArrayList<NJCTLDocument>();
	
	public NJCTLDocList(String id, String title, ArrayList<NJCTLDocument> docs) {
		this._id = id;
		this._title = title;
		this._docs = docs;
	}
	
	public NJCTLDocList(String id, String title) {
		this._id = id;
		this._title = title;
	}
	
	public void add(NJCTLDocument doc)
	{
		this._docs.add(doc);
	}
	
	public NJCTLDocList(Parcel in) {
		readFromParcel(in);
	}
	
	public String getId() {
		return _id;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public ArrayList<NJCTLDocument> getContents() {
		return _docs;
	}

    // Methods for Parcelable implementation.
    
    @Override
    public int describeContents() {
    	return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(_id);
    	dest.writeString(_title);
    	dest.writeParcelableArray(_docs.toArray(new NJCTLDocument[_docs.size()]), 0);
    }
    
    private void readFromParcel(Parcel in) {
    	_id = in.readString();
    	_title = in.readString();
    	_docs = new ArrayList<NJCTLDocument>();
        in.readList(_docs, NJCTLDocument.class.getClassLoader());
    }
    
    public static final Parcelable.Creator<NJCTLDocList> CREATOR = new Parcelable.Creator<NJCTLDocList>() {
    	public NJCTLDocList createFromParcel(Parcel in) {
    		return new NJCTLDocList(in);
    	}
    	public NJCTLDocList[] newArray(int size) {
    		return new NJCTLDocList[size];
    	}
    };
	
}
